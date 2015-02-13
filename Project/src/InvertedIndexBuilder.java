import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Class that builds the InvertedIndex.
 * 
 * @author srollins
 *
 */
public class InvertedIndexBuilder {

	/**
	
	It is up to you to design this class. 
	This class will need to recursively traverse an input directory and any time it finds a file
	that has the .txt extension (case insensitive!), it will process the file and add all words to 
	the InvertedIndex.
	
	
	**/
	private String directory;
	private InvertedIndex index;
	private boolean digitDelimiter;
	
	public InvertedIndexBuilder(String directory, boolean digitDelimiter){
		this.directory = directory;
		this.digitDelimiter = digitDelimiter;
	}
	
	public InvertedIndex build(){
		index = new InvertedIndex();
		String exp;
		if(digitDelimiter)
			exp = "[^a-zA-Z]+";
		else
			exp = "[^a-zA-Z0-9]+";
		Pattern pattern = Pattern.compile(exp);
		processDir(directory, pattern);
		return index;
	}
	
	private void processDir(File dir, Pattern pattern){
		for(File f: dir.listFiles()){
			if(f.isDirectory())
				processDir(f, pattern);
			else if(f.getName().toLowerCase().endsWith(".txt"))
				processFile(f, pattern);
		}
	}
	
	private void processFile(File file, Pattern pattern){
		blah
	}
}
