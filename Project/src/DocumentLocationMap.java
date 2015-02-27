import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Stores a mapping from a document (a file name) to a set of integers that represent the
 * locations where a word appears in the document.
 * @author Vincent Rideout
 *
 */
public class DocumentLocationMap {

	private TreeMap<String, TreeSet<Integer>> map;
	private String word;
	
	/**
	 * Intitialize an empty map.
	 * @param word - the word that this mapping represents included
	 * 					for convenience
	 */
	public DocumentLocationMap(String word) {
		this.map = new TreeMap<String, TreeSet<Integer>>();
		this.word = word;
	}
	
	
	/**
	 * Add a new location for a given file. The first word in a file is at location 1, the second
	 * word at location 2, and so on.
	 * @param fileName - the name of the file where the word appears.
	 * @param location - the location in the file where the word appears.
	 */
	public void addLocation(String fileName, int location) {	
		if(!map.containsKey(fileName)){
			map.put(fileName, new TreeSet<Integer>());
		}
		map.get(fileName).add(location);
	}
	
	/**
	 * Returns true if there is already a mapping from the given fileName to the location 
	 * specified and false otherwise. This method is used for convenience and sanity checking
	 * to ensure that the same location is not added multiple times.
	 * @param fileName
	 * @param location
	 * @return
	 */
	public boolean contains(String fileName, int location) {
		return(map.containsKey(fileName) && map.get(fileName).contains(location));
	}
	
	/**
	 * Helper method for other toString method. 
	 * @param fileName
	 * @return - string representation of one file and the words mapped to it.
	 */
	private String toString(String fileName) {
		StringBuffer s = new StringBuffer("\"" + fileName + "\"");
		if(map.containsKey(fileName)){
			for(Integer i: map.get(fileName))
				s.append(", " + i);
		}
		s.append("\n");
		return s.toString();
	}
	
	/**
	 * Return a string representation of this mapping. Keep in mind that concatenating immutable String objects
	 * is extremely inefficient! Hint: my main toString method iterated over the keys in the mapping and called
	 * a helper method to build each line of the result. The format of the return value will be as follows:
	 * "filename1", location1, location2, location3
	 * "filename2", location1, location2, location3
	 * Example: 
	 * "input/gutenberg/11.txt", 1, 3, 15, 19
	 * "input/gutenberg/1322.txt", 5, 18, 19
	 * @param fileName
	 * @return
	 */
	public String toString() {
		StringBuffer s = new StringBuffer();
		for(String fileName: map.keySet()){
			s.append(toString(fileName));
		}
		return s.toString();
	}
	
	public static void main(String[] args) {
	
		DocumentLocationMap map = new DocumentLocationMap("sample");
		//map.addLocation("test1", 1);
		//map.addLocation("test1", 5);
		map.addLocation("test2", 12);
		map.addLocation("test1", 2);
		//map.addLocation("test2", 1);
		map.addLocation("test3", 19);
		System.out.println(map);
	}
	
}
