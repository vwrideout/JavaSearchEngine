import java.util.ArrayList;

/**
 * Custom list object for storing DocumentResult objects.
 * @author Vincent Rideout
 *
 */
public class DocumentResultList extends ArrayList<DocumentResult>{

	private static final long serialVersionUID = 1L;
	private String query;
	
	/**
	 * Constructor to instantiate a new DocumentResultList
	 * @param query - The search query associated with this object.
	 */
	public DocumentResultList(String query){
		super();
		this.query = query;
	}

	public boolean contains(String fileName){
		for(DocumentResult dr: this){
			if(fileName == dr.getName()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Changes the score of a DocumentResult stored in the list.
	 * @param fileName - Name of the file being updated.
	 * @param delta - Desired change in TF-IDF score.
	 */
	public void updateScore(String fileName, double delta){
		for(DocumentResult dr: this){
			if(fileName == dr.getName()){
				dr.setScore(dr.getScore() + delta);
				return;
			}
		}
	}
	
	/**
	 * Returns as a string: The query, a blank line, then each file name stored.
	 * Collections.sort should be called on the list before printing.
	 */
	public String toString(){
		StringBuffer s = new StringBuffer();
		s.append(query + "\n");
		for(DocumentResult dr: this){
			s.append(dr.getName() + "\n");
		}
		s.append("\n");
		return s.toString();
	}
	
	public String getQuery(){
		return query;
	}
	
}
