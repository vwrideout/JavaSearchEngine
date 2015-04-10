/**
 * Threadsafe extension of the InvertedIndex class.
 * Uses a ReadWriteLock to ensure thread safety.
 * Most methods simply acquire a lock and call their super method.
 * @author Vincent
 *
 */
public class ConcurrentInvertedIndex extends InvertedIndex {
	
	private ReadWriteLock lock;
	private int batches;
	
	/**
	 * Constructor to instantiate a new ConcurrentInvertedIndex.
	 */
	public ConcurrentInvertedIndex() {
		super();
		batches = 0;
		lock = new ReadWriteLock();
	}

	public void add(String word, String filename, int location){
		lock.lockWrite();
		super.add(word, filename, location);
		lock.unlockWrite();
	}
	
	/**
	 * Add a batch of word/location pairs to the index.
	 * Meant to minimize the time spent acquiring/releasing the write lock. 
	 * @param batch
	 */
	public void addBatch(IndexInputBatch batch){
		lock.lockWrite();
		if(batch.getNumWords() > 0){
			super.addWordcount(batch.getDocName(), batch.getNumWords());
		}
		for(int i = 0; i < batch.getNumWords(); i++){
			super.add(batch.getWord(i), batch.getDocName(), i+1);
		}
		lock.unlockWrite();
	}
	
	public void addWordcount(String filename, int num){
		lock.lockWrite();
		super.addWordcount(filename, num);
		lock.unlockWrite();
	}
	
	public DocumentResultList search(String query) {
		lock.lockRead();
		try{
			return super.search(query);
		} finally{
			lock.unlockRead();
		}
	}
	
	public String toString() {
		lock.lockRead();
		try{
			return super.toString();
		} finally{
			lock.unlockRead();
		}
	}
}
