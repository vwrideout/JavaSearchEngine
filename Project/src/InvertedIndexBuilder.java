import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.DirectoryStream;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Class that builds the InvertedIndex.
 * 
 * @author Vincent Rideout
 *
 */
public class InvertedIndexBuilder {

	private Path directory;
	private Pattern delimiter;
	private InvertedIndex index;
	
	/**
	 * Constructor to instantiate a new InvertedIndexBuilder
	 * @param directory 
	 * @param digitDelimiter
	 */
	public InvertedIndexBuilder(Path directory, boolean digitDelimiter){
		this.directory = directory;	
		if(digitDelimiter)
			this.delimiter = Pattern.compile("[^a-zA-Z]+");
		else
			this.delimiter = Pattern.compile("[^a-zA-Z0-9]+");
	}
	
	public InvertedIndex build(){
		index = new InvertedIndex();
		processDir(directory);
		return index;
	}
	
	//recursively traverses all .txt files and subdirectories
	
	private void processDir(Path dir){
		try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)){
			for(Path file: stream){
				if(file.toFile().isDirectory()){
					processDir(file);
				}
				else if(file.toString().toLowerCase().endsWith(".txt")){
					processFile(file, file.toString());
				}
			}
		} catch (IOException ioe) {
			System.out.println("Error processing directory.");
		}
	}
	
	/**reads the contents of a .txt file, adding them to the InvertedIndex at location 
	 * denoted by the running wordcount. No exceptions thrown in case of I/O errors.
	 */
	
	private void processFile(Path file, String fileName){
		Scanner fileScanner;
		int count = 1;
		int totalwords = 1;
		try {
			fileScanner = new Scanner(file).useDelimiter(delimiter);
			while(fileScanner.hasNext()){
				index.add(fileScanner.next().toLowerCase(), fileName, count++);
				totalwords++;
			}
			index.addWordcount(fileName, totalwords);
			fileScanner.close();
		} catch (IOException ioe) {
			System.out.println("Unable to open file.");
		}		
	}
}
