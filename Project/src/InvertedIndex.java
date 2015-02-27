import java.util.TreeMap;

/**
 * Maintains a mapping from a word to a list of documents and positions in those documents where the word was found.
 * See the project description for more information: https://github.com/CS212-S15/projects/blob/master/specifications/project1.md
 * @author Vincent Rideout
 *
 */
public class InvertedIndex {

	private TreeMap<String, DocumentLocationMap> map;
	private TreeMap<String, Integer> wordsInDoc;
	
	/**
	 * Constructor to instantiate a new InvertedIndex
	 */
	public InvertedIndex() {
		this.map = new TreeMap<String, DocumentLocationMap>();
		this.wordsInDoc = new TreeMap<String, Integer>();
	}
	
	/**
	 * Store the total number of words contained in a document.
	 * @param fileName - name of the document
	 * @param num - total number of words in the document
	 */
	public void addWordsInDoc(String fileName, int num){
		wordsInDoc.put(fileName, num);
	}
	/**
	 * Adds a new word to the index. If the word is already in the index, the method simply adds a new document/position.
	 * 
	 * @param word - the word to be added
	 * @param fileName - the name of the document where the word is found
	 * @param location - the position in the document where the word is found.
	 */
	public void add(String word, String fileName, int location) {
		if(!map.containsKey(word)){
			map.put(word, new DocumentLocationMap(word));
		}
		map.get(word).addLocation(fileName, location);
	}

	/**
	 * Returns a string representation of the index. 
	 * See the project specification for the required format of the String representation
	 * of the index. Your output must match exactly to pass all tests.
	 */
	public String toString() {		
		StringBuffer s = new StringBuffer();
		for(String word: map.keySet()){
			s.append(word + "\n" + map.get(word).toString() + "\n");
		}
		return s.toString();
	}	

	public static void main(String[] args) {	
		InvertedIndex ii = new InvertedIndex();
		ii.add("hello", "test1", 1);
		ii.add("hello", "test1", 10);
		ii.add("animal", "test1", 4);
		ii.add("hello", "test2", 8);
		ii.add("animal", "test2", 1);
		ii.add("hello", "test2", 4);
		ii.add("bob", "test3", 4);
		System.out.println(ii);
		
	}
	
}
