import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * Another class to build an InvertedIndex. This one builds the index by traversing web pages.
 * It will parse the seed page and all pages linked to on the seed page, until it reads 50 pages
 * or runs out of links to follow. 
 * @author Vincent Rideout
 *
 */
public class WebCrawler {
	private ConcurrentInvertedIndex index;
	private TreeSet<URI> visited;
	private WorkQueue queue;
	private int numThreads;
	private URI seed;
	private Pattern delimiter;
	private int jobsPending;
	
	/**
	 * Constructor to instantiate a new WebCrawler.
	 * @param seed - URI object containing the URL of the first page to crawl.
	 * @param digitDelimiter
	 * @param numThreads
	 */
	public WebCrawler(URI seed, boolean digitDelimiter, int numThreads){
		this.seed = seed;
		this.numThreads = numThreads;
		this.visited = new TreeSet<URI>();
		if(digitDelimiter)
			this.delimiter = Pattern.compile("[^a-zA-Z]+");
		else
			this.delimiter = Pattern.compile("[^a-zA-Z0-9]+");
	}
	
	/**
	 * Builds a ConcurrentInvertedIndex from the seed web page.
	 * @return - the new index.
	 */
	public ConcurrentInvertedIndex crawl(){
		index = new ConcurrentInvertedIndex();
		queue = new WorkQueue(numThreads);
		jobsPending = 1;
		visited.add(seed);
		queue.execute(new CrawlWorker(seed));
		synchronized(visited){
			try{
				visited.wait();
			}catch(InterruptedException ie){
				System.out.println(ie.getMessage());
			}
		}
		queue.shutdown();
		queue.awaitTermination();
		return index;
	}
	
	private synchronized void incrementJobs(){
		jobsPending++;
	}
	
	private synchronized void decrementJobs(){
		if(--jobsPending < 1){
			synchronized(visited){
				visited.notifyAll();
			}
		}
	}
	
	/**
	 * Helper class to build the index in threaded fashion.
	 * @author Vincent Rideout
	 *
	 */
	private class CrawlWorker implements Runnable{
		private URI page;
		
		public CrawlWorker(URI page){
			this.page = page;
		}
		
		public void run(){
			String data = HTTPFetcher.download(page.getHost(), page.getPath());
			ArrayList<String> links = HTMLLinkParser.listLinks(data);
			for(String str: links){
				try{
					URI link = page.resolve(new URI(str));
					link = new URI(link.getScheme(), link.getSchemeSpecificPart(), null);
					synchronized(visited){
						if(!visited.contains(link) && visited.size() < 50){
							visited.add(link);
							incrementJobs();
							queue.execute(new CrawlWorker(link));
						}
					}
				}catch(URISyntaxException e){}
			}
			data = HTMLCleaner.cleanHTML(data);
			IndexInputBatch batch = new IndexInputBatch(page.toString());
			Scanner stringScanner = new Scanner(data).useDelimiter(delimiter);
			while(stringScanner.hasNext()){
				batch.add(stringScanner.next().toLowerCase());
			}
			index.addBatch(batch);
			stringScanner.close();
			decrementJobs();
		}
	}
	
}
