import java.util.LinkedList;

/**
 * An adaptation of the IBM implementation of WorkQueue.
 * @author Vincent Rideout
 *
 */
public class WorkQueue {
	private final int nThreads;
	private final PoolWorker[] threads;
	private final LinkedList queue;
	private volatile boolean running;
	
	/**
	 * Constructor to instantiate a new WorkQueue.
	 * @param nThreads - Number of threads desired.
	 */
	public WorkQueue(int nThreads){
		running = true;
		this.nThreads = nThreads;
		queue = new LinkedList();
		threads = new PoolWorker[nThreads];
		for(int i = 0; i < nThreads; i++){
			threads[i] = new PoolWorker();
			threads[i].start();
		}
	}
	
	/**
	 * Insert a job into the WorkQueue.
	 * @param r
	 */
	public void execute(Runnable r){
		if(running){
			synchronized(queue){
				queue.addLast(r);
				queue.notifyAll();
			}
		}
		else{
			System.out.println("WorkQueue has been shut down.");
		}
	}
	
	/**
	 * Stop the WorkQueue from accepting new jobs.
	 */
	public void shutdown(){
		running = false;
		synchronized(queue){
			queue.notifyAll();
		}
	}
	
	/**
	 * Blocking method to wait for all jobs in the queue to finish.
	 * To be called after shutdown.
	 */
	public void awaitTermination(){
		for(int i = 0; i < nThreads; i++){
			try{
				threads[i].join();
			}catch (InterruptedException ie){
				System.out.println(ie.getMessage());
			}
		}
	}
	
	/**
	 * Helper class to populate the WorkQueue with jobs.
	 * @author Vincent
	 *
	 */
	private class PoolWorker extends Thread{
		
		public void run(){
			Runnable r;
			while(true){
				synchronized(queue){
					while(running && queue.isEmpty()){
						try{
							queue.wait();
						}catch (InterruptedException ie){
							System.out.println(ie.getMessage());
						}
					}
					if(!running && queue.isEmpty()){
						break;
					}
					r = (Runnable) queue.removeFirst();
				}
				try{
					r.run();
				}catch(RuntimeException re){
					System.out.println(re.getMessage());
				}
			}
		}
	}
}
