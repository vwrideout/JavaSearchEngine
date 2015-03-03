import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Helper class to run searches on an InvertedIndex object.
 * @author Vincent Rideout
 */
public class InvertedIndexSearcher {
	
	private Path searchPath;
	private InvertedIndex index;
	
	/**
	 * Constructor to instantiate a new InvertedIndexSearcher.
	 * @param searchPath - Path to an input file containing queries.
	 * @param index - The InvertedIndex object on which to run queries.
	 */
	public InvertedIndexSearcher(Path searchPath, InvertedIndex index){
		this.searchPath = searchPath;
		this.index = index;
	}
	
	/**
	 * Executes all queries in the input file.
	 * @return - ArrayList of results in the same order the queries were input.
	 */
	public ArrayList<DocumentResultList> search(){
		ArrayList<DocumentResultList> output = new ArrayList<DocumentResultList>();
		Scanner fileScanner;
		try{
			fileScanner = new Scanner(searchPath);
			while(fileScanner.hasNextLine()){
				output.add(index.search(fileScanner.nextLine()));
			}
			fileScanner.close();
		}catch(IOException ioe){
			System.out.println("Bad query path");
		}
		return output;
	}
	
	/**
	 * Executes all queries and returns the results in a String.
	 */
	public String toString(){
		ArrayList<DocumentResultList> output = this.search();
		StringBuffer s = new StringBuffer();
		for(DocumentResultList drl: output){
			s.append(drl.toString());
		}
		return s.toString();
	}
}
