import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;


public class Driver {

	public static void main(String[] args) throws Exception {
		Path path = FileSystems.getDefault().getPath("config.json");
		Configuration config = new Configuration(path);
		config.init();
		System.out.println(config.toString());
		InvertedIndexBuilder builder = new InvertedIndexBuilder(config.getInputPath(), config.useDigitDelimiter());
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
	
}
