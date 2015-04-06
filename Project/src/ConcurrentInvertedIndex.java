
public class ConcurrentInvertedIndex extends InvertedIndex {
	
	private ReadWriteLock lock;
	
	public ConcurrentInvertedIndex() {
		super();
		lock = new ReadWriteLock();
	}
	
	/**public void addBatch(InvertedIndex batch){
		lock.lockWrite();
		for(String word: batch.words()){
			super.add(batch.getDLM(word));
		}
		for(String docName: batch.docNames()){
			super.addWordcount(docName, batch.getWordCount(docName));
		}
		lock.unlockWrite();
	}*/
	
	public void add(String word, String filename, int location){
		lock.lockWrite();
		super.add(word, filename, location);
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
