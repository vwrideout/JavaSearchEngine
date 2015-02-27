import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**Main method for project 1. Reads from "config.json" into a Configuration object,
 * parses an input file into an InvertedIndex, then writes the contents of the index into 
 * an output file. 
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
			return;
		}
		Path inputDir = FileSystems.getDefault().getPath(config.getInputPath());
		if(inputDir.toFile().isDirectory()){	
			InvertedIndexBuilder builder = new InvertedIndexBuilder(inputDir, config.useDigitDelimiter());
			InvertedIndex index = builder.build();
			if(config.getOutputPath() != null){
				PrintWriter pw = new PrintWriter(config.getOutputPath());
				pw.print(index.toString());
				pw.close();
			}
		}
	}
	
}
