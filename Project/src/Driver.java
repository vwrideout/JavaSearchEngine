import java.io.PrintWriter;
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
		if(inputDir.toFile().isDirectory()){	
			InvertedIndexBuilder builder = new InvertedIndexBuilder(inputDir, config.useDigitDelimiter(), config.getNumberThreads());
			ConcurrentInvertedIndex index = builder.build();
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
