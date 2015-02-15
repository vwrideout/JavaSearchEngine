import java.io.File;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;


public class Driver {

	public static void main(String[] args) throws Exception {
		Path path = FileSystems.getDefault().getPath("config.json");
		Configuration config = new Configuration(path);
		boolean goodinit = true;
		try{
			config.init();
		}catch (InitializationException ie){
			goodinit = false;
			System.out.println(ie.getMessage());
		}
		if(goodinit){
			System.out.println(config.toString());
			File inputDir = new File(config.getInputPath());
			if(inputDir.isDirectory()){	
				InvertedIndexBuilder builder = new InvertedIndexBuilder(inputDir, config.useDigitDelimiter());
				InvertedIndex index = builder.build();
				if(config.getOutputPath() != null){
					PrintWriter pw = new PrintWriter(config.getOutputPath());
					pw.print(index.toString());
					pw.close();
				}
				else{
					System.out.println("no output file");
				}
			}
			else{
				System.out.println("bad input file");
			}
		}
	}
	
}
