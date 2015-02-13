import java.nio.file.FileSystems;


public class Driver {

	public static void main(String[] args) throws Exception {
		Configuration config = new Configuration(path);
		InvertedIndexBuilder builder = new InvertedIndexBuilder(config.getInputPath(), config.useDigitDelimiter());
		InvertedIndex index = builder.build();
		tostring index into config.getOutputPath()
	}
	
}
