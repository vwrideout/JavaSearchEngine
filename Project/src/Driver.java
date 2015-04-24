import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**Main method for project 2. Reads from "config.json" into a Configuration object,
 * parses an input file into an InvertedIndex, then writes the contents of the index into 
 * an output file. If search path in given, reads in queries from another input file, then
 * writes the results of the searches to another output file.
 * @author Vincent Rideout
 *
 */

public class Driver {

	public static void main(String[] args) throws Exception {
		boolean built = false;
		boolean inputIsURL = true;
		ConcurrentInvertedIndex index = null;
		Path path = FileSystems.getDefault().getPath("config.json");
		Configuration config = new Configuration(path);
		try{
			config.init();
		}catch (InitializationException ie){
			System.out.println(ie.getMessage());
			return;
		}
		Path inputDir = FileSystems.getDefault().getPath(config.getInputPath());
		PrintWriter pw;
		URL seed = new URL(inputDir.toString());
		try{
			seed.toURI();
		}catch(URISyntaxException e){
			inputIsURL = false;
		}
		if(inputIsURL){
			WebCrawler crawler = new WebCrawler(seed.toURI(), config.useDigitDelimiter(), config.getNumberThreads());
			index = crawler.crawl();
			built = true;
		}
		else if(inputDir.toFile().isDirectory()){	
			InvertedIndexBuilder builder = new InvertedIndexBuilder(inputDir, config.useDigitDelimiter(), config.getNumberThreads());
			index = builder.build();
			built = true;
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
		}
	}
	
}
