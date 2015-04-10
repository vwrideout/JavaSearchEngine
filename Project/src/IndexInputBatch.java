/**
 * Data storage class for batch processing of text files into an Inverted Index.
 * @author Vincent Rideout
 *
 */
public class IndexInputBatch {
	private String docName;
	private int numWords;
	private String[] words;
	
	/**
	 * Constructor to instantiate a new IndexInputBatch.
	 * @param docName - Name of the document being processed into this batch.
	 */
	public IndexInputBatch(String docName){
		this.docName = docName;
		this.numWords = 0;
		this.words = new String[100];
	}
	
	public String getDocName(){
		return docName;
	}
	
	public int getNumWords(){
		return numWords;
	}
	
	public String getWord(int i){
		return words[i];
	}
	
	/**
	 * Add a word to the batch. The file location of the word is tracked by its place in the array. 
	 * @param word
	 */
	public void add(String word){
		if(numWords > (words.length -1)){
			String temp[] = new String[words.length * 2];
			for(int i = 0; i < numWords; i++){
				temp[i] = words[i];
			}
			words = temp;
		}
		words[numWords] = word;
		numWords++;
	}
}
