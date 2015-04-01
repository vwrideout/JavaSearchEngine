
public class ConcurrentInvertedIndex extends InvertedIndex {
	
	private ReadWriteLock lock;
	
	public ConcurrentInvertedIndex() {
		super();
		lock = new ReadWriteLock();
	}
	
	public void addWordcount(String fileName, int num) {
		lock.lockWrite();
		super.addWordcount(fileName, num);
		lock.unlockWrite();
	}
	
	public void add(String word, String fileName, int location) {
		lock.lockWrite();
		super.add(word,  fileName, location);
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
