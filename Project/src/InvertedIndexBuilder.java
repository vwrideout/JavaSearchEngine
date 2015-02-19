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
 * @author Vincent Rideout
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
		//different regex's depending on whether digits should be delimiters
		String exp;
		if(digitDelimiter)
			exp = "[^a-zA-Z]+";
		else
			exp = "[^a-zA-Z0-9]+";
		this.delimiter = Pattern.compile(exp);		
	}
	
	public InvertedIndex build(){
		index = new InvertedIndex();
		processDir(directory);
		return index;
	}
	
	//recursively traverses all .txt files and subdirectories
	
	private void processDir(File dir){
		
		/* 
		I recommend using Path rather than File to be consistent with 
		use of Java 8.
		see: http://docs.oracle.com/javase/tutorial/essential/io/dirs.html
		*/
		
		for(File f: dir.listFiles()){
			if(f.isDirectory())
				processDir(f);
			else if(f.getName().toLowerCase().endsWith(".txt"))
				processFile(f, f.getPath());
		}
	}
	
	/**reads the contents of a .txt file, adding them to the InvertedIndex at location 
	 * denoted by the running wordcount. No exceptions thrown in case of I/O errors.
	 */
	
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
