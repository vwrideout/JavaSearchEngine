import java.io.File;
import java.io.FileNotFoundException;
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
	private File directory;
	private Pattern delimiter;
	private InvertedIndex index;
	
	public InvertedIndexBuilder(File directory, boolean digitDelimiter){
		this.directory = directory;	
		String exp;
		if(digitDelimiter)
			exp = "[^a-zA-Z]+";
		else
			exp = "[^a-zA-Z0-9]+";
		this.delimiter = Pattern.compile(exp);		
	}
	
	public InvertedIndex build(){
		System.out.println(directory);
		index = new InvertedIndex();
		processDir(directory);
		return index;
	}
	
	private void processDir(File dir){
		for(File f: dir.listFiles()){
			if(f.isDirectory())
				processDir(f);
			else if(f.getName().toLowerCase().endsWith(".txt"))
				processFile(f, f.getPath());
		}
	}
	
	private void processFile(File file, String fileName){
		Scanner fileScanner;
		int count = 1;
		try {
			fileScanner = new Scanner(file).useDelimiter(delimiter);
			while(fileScanner.hasNext())
				index.add(fileScanner.next().toLowerCase(), fileName, count++);
			fileScanner.close();
		} catch (FileNotFoundException ffe) {
			System.out.println("Unable to open file.");
		}		
	}
}
