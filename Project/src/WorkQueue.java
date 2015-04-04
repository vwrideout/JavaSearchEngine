import java.util.LinkedList;


public class WorkQueue {
	private final int nThreads;
	private final PoolWorker[] threads;
	private final LinkedList queue;
	private boolean running;
	
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
	
	public void shutdown(){
		for(int i = 0; i < nThreads; i++){
			threads[i].running = false;
		}
	}
	
	public void awaitTermination(){
		for(int i = 0; i < nThreads; i++){
			try{
				threads[i].join();
			}catch (InterruptedException ie){
				System.out.println(ie.getMessage());
			}
		}
	}
	
	private class PoolWorker extends Thread{
		private boolean running;
		
		public void run(){
			Runnable r;
			running = true;
			while(running || !queue.isEmpty()){
				synchronized(queue){
					while(queue.isEmpty()){
						try{
							queue.wait();
						}catch (InterruptedException ie){
							System.out.println(ie.getMessage());
						}
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
