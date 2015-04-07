import java.util.LinkedList;


public class WorkQueue {
	private final int nThreads;
	private final PoolWorker[] threads;
	private final LinkedList queue;
	private volatile boolean running;
	
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
		
		//TODO: this for loop is not necessary.
		for(int i = 0; i < nThreads; i++){
			threads[i].shutdown();
		}
		running = false;
		synchronized(queue){
			queue.notifyAll();
		}
	}
	
	public void awaitTermination(){
		for(int i = 0; i < nThreads; i++){
			try{
				System.out.println("trying to join thread " + i);
				threads[i].join();
				System.out.println("Thread " + i + " joined.");
			}catch (InterruptedException ie){
				System.out.println(ie.getMessage());
			}
		}
	}
	
	private class PoolWorker extends Thread{
		private volatile boolean workerRunning;
		
		public void run(){
			Runnable r;
			workerRunning = true;
			while(true){
				synchronized(queue){
					//TODO: this if is not necessary.
					if(!workerRunning && queue.isEmpty()){
						queue.notifyAll();
						return; 
					}
					
					//TODO: this condition should ensure that the queue is empty AND
					//the WQ is still running.
					while(queue.isEmpty()){
						try{
							queue.wait();
						}catch (InterruptedException ie){
							System.out.println(ie.getMessage());
						}
					}

					if(!workerRunning && queue.isEmpty()){
						//TODO: no need to notify, just break out of the while.
						queue.notifyAll();
						return;
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
		
		//TODO: unnecessary
		public void shutdown(){
			workerRunning = false;
		}
	}
}
