import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.regex.Pattern;


public class WebCrawler {
	private ConcurrentInvertedIndex index;
	private TreeSet<URI> visited;
	private WorkQueue queue;
	private int numThreads;
	private URI seed;
	private Pattern delimiter;
	private int pagesCrawled;
	
	public WebCrawler(URI seed, boolean digitDelimiter, int numThreads){
		this.seed = seed;
		this.numThreads = numThreads;
		this.pagesCrawled = 0;
		if(digitDelimiter)
			this.delimiter = Pattern.compile("[^a-zA-Z]+");
		else
			this.delimiter = Pattern.compile("[^a-zA-Z0-9]+");
	}
	
	public ConcurrentInvertedIndex crawl(){
		index = new ConcurrentInvertedIndex();
		queue = new WorkQueue(numThreads);
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
	}
	
	private class CrawlWorker implements Runnable{
		private URI page;
		
		public CrawlWorker(URI page){
			this.page = page;
		}
		
		public void run(){
			String data = HTTPFetcher.download(page.getHost(), page.getPath());
			ArrayList<String> links = HTMLLinkParser.listLinks(data);
			for(String str: links){
				URI link = new URI(str).resolve(page);
				synchronized(visited){
					if(!visited.contains(link) && visited.size() < 50){
						visited.add(link);
						queue.execute(new CrawlWorker(link));
					}
				}
			}
			data = HTMLCleaner.cleanHTML(data);
			IndexInputBatch batch = new IndexInputBatch(page.toString());
			Scanner stringScanner = new Scanner(data).useDelimiter(delimiter);
			while(stringScanner.hasNext()){
				batch.add(stringScanner.next().toLowerCase());
			}
			index.addBatch(batch);
			stringScanner.close();
			}
		}
	}
	
}
