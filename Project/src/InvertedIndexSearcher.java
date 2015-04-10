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
	private WorkQueue queue;
	private DocumentResultList[] results;
	private ReadWriteLock lock;
	private int numThreads;
	
	/**
	 * Constructor to instantiate a new InvertedIndexSearcher.
	 * @param searchPath - Path to an input file containing queries.
	 * @param index - The InvertedIndex object on which to run queries.
	 */
	public InvertedIndexSearcher(Path searchPath, ConcurrentInvertedIndex index, int numThreads){
		this.searchPath = searchPath;
		this.index = index;
		this.numThreads = numThreads;
		this.lock = new ReadWriteLock();
	}
	
	/**
	 * Executes all queries in the input file.
	 * @return - ArrayList of results in the same order the queries were input.
	 */
	public void search(){
		int i = 0;
		results = new DocumentResultList[100];
		Scanner fileScanner;
		queue = new WorkQueue(numThreads);
		try{
			fileScanner = new Scanner(searchPath);
			while(fileScanner.hasNextLine()){
				if(i >= results.length){
					DocumentResultList[] tmp = new DocumentResultList[results.length * 2];
					for(int j = 0; j < i; j++){
						tmp[j] = results[j];
					}
					results = tmp;
				}
				queue.execute(new IndexSearcher(i++, fileScanner.nextLine()));
			}
			fileScanner.close();
		}catch(IOException ioe){
			System.out.println("Bad query path");
		}
		queue.shutdown();
		queue.awaitTermination();
	}
	
	/**
	 * Executes all queries and returns the results in a String.
	 */
	public String toString(){
		this.search();
		StringBuffer s = new StringBuffer();
		for(int i = 0; results[i] != null; i++){
			s.append(results[i].toString());
		}
		return s.toString();
	}
	
	/**
	 * Helper class to run searches in threaded fashion.
	 * @author Vincent
	 *
	 */
	private class IndexSearcher implements Runnable{
		private int resultindex;
		private String query;
		
		public IndexSearcher(int resultindex, String query){
			this.resultindex = resultindex;
			this.query = query;
		}
		
		public void run(){
			DocumentResultList drl = index.search(query);
			lock.lockWrite();
			results[resultindex] = drl;
			lock.unlockWrite();
		}
	}
}
