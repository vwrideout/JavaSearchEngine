import java.io.File;
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
		//open json file
		Path path = FileSystems.getDefault().getPath("config.json");
		Configuration config = new Configuration(path);
		boolean goodinit = true;
		//read json file, aborting program if exceptions are thrown
		try{
			config.init();
		}catch (InitializationException ie){
			goodinit = false;
		}
		if(goodinit){
			//read the input file into an InvertedIndex
			File inputDir = new File(config.getInputPath());
			if(inputDir.isDirectory()){	
				InvertedIndexBuilder builder = new InvertedIndexBuilder(inputDir, config.useDigitDelimiter());
				InvertedIndex index = builder.build();
				//if output path received, print the toString of the index to it.
				if(config.getOutputPath() != null){
					PrintWriter pw = new PrintWriter(config.getOutputPath());
					pw.print(index.toString());
					pw.close();
				}
			}
		}
	}
	
}
