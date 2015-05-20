import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**Main method for project 4. Reads from "config.json" into a Configuration object,
 * parses an input file or website into a ConcurrentInvertedIndex, then writes the contents of 
 * the index into an output file. If search path in given, reads in queries from another input file, then
 * writes the results of the searches to another output file.
 * @author Vincent Rideout
 *
 */

public class Driver {

	public static void main(String[] args) throws Exception {
		boolean built = false;
		ConcurrentInvertedIndex index = null;
		Path path = FileSystems.getDefault().getPath("config.json");
		Configuration config = new Configuration(path);
		try{
			config.init();
		}catch (InitializationException ie){
			System.out.println(ie.getMessage());
			return;
		}
		PrintWriter pw;
		if(config.inputPathIsURL()){
			WebCrawler crawler = new WebCrawler(new URI(config.getInputPath()), config.useDigitDelimiter(), config.getNumberThreads());
			index = crawler.crawl();
			built = true;
		}
		else {
			Path inputDir = FileSystems.getDefault().getPath(config.getInputPath());
			if(inputDir.toFile().isDirectory()){	
				InvertedIndexBuilder builder = new InvertedIndexBuilder(inputDir, config.useDigitDelimiter(), config.getNumberThreads());
				index = builder.build();
				built = true;
			}
		}
		if(built){
			if(config.getOutputPath() != null){
				pw = new PrintWriter(config.getOutputPath());
				pw.print(index.toString());
				pw.close();
			}
			if(config.getSearchPath() != null){
				Path searchPath = FileSystems.getDefault().getPath(config.getSearchPath());
				InvertedIndexSearcher searcher = new InvertedIndexSearcher(searchPath, index, config.getNumberThreads());
				searcher.search();
				String outputFilename = "results/default.txt";
				if(config.getSearchOutputPath() != null){
					outputFilename = config.getSearchOutputPath();
				}
				pw = new PrintWriter(outputFilename);
				pw.print(searcher.toString());
				pw.close();
			}
			Server server = new Server(8080);
			ServletHandler handler = new ServletHandler();
			server.setHandler(handler);
			ServletHolder searchHolder = new ServletHolder(new SearchServlet(index));
			handler.addServletWithMapping(searchHolder, "/search");
			handler.addServletWithMapping(LoginServlet.class, "/login");
			server.start();
			server.join();
		}
	}
	
}
