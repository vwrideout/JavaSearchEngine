import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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
	private ConcurrentInvertedIndex index;
	private WorkQueue queue;
	
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
	
	/**
	 * Build an inverted index from all text files in the given directory.
	 * @return - InvertedIndex object containing data from the text files.
	 */
	public ConcurrentInvertedIndex build(){
		index = new ConcurrentInvertedIndex();
		queue = new WorkQueue(100);
		processDir(directory);
		queue.shutdown();
		queue.awaitTermination();
		return index;
	}
	
	/**
	 * Recursively traverses all subdirectories, calling processFile on all .txt files.
	 * @param dir - The parent directory Path object to traverse.
	 */
	
	private void processDir(Path dir){
		try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)){
			for(Path file: stream){
				if(file.toFile().isDirectory()){
					processDir(file);
				}
				else if(file.toString().toLowerCase().endsWith(".txt")){
					queue.execute(new FileProcessor(file));
				}
			}
		} catch (IOException ioe) {
			System.out.println("Error processing directory.");
		}
	}
	
	/**
	 * Process a .txt file into the InvertedIndex. Also adds a total word count to the index for files with wordcount > 0.
	 * @param file - Path object representing the .txt file to be processed.
	 */
	private void processFile(Path file){
		Scanner fileScanner;
		int count = 1;
		int totalwords = 0;
		try {
			fileScanner = new Scanner(file).useDelimiter(delimiter);
			while(fileScanner.hasNext()){
				index.add(fileScanner.next().toLowerCase(), file.toString(), count++);
				totalwords++;
			}
			if(totalwords > 0){
				index.addWordcount(file.toString(), totalwords);
			}
			fileScanner.close();
		} catch (IOException ioe) {
			System.out.println("Unable to open file.");
		}		
	}
	
	private class FileProcessor implements Runnable{
		private Path file;
		
		public FileProcessor(Path file){
			this.file = file;
		}
		
		public void run(){
			Scanner fileScanner;
			int count = 1;
			int totalwords = 0;
			try {
				fileScanner = new Scanner(file).useDelimiter(delimiter);
				while(fileScanner.hasNext()){
					index.add(fileScanner.next().toLowerCase(), file.toString(), count++);
					totalwords++;
				}
				if(totalwords > 0){
					index.addWordcount(file.toString(), totalwords);
				}
				fileScanner.close();
			} catch (IOException ioe) {
				System.out.println("Unable to open file.");
			}
		}
	}
}
