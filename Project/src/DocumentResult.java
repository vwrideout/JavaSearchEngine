/**
 * Class for storing results of a TF-IDF comparison.
 * Comparable implemented by comparing the TF-IDF scores.
 * @author Vincent Rideout
 *
 */

public class DocumentResult implements Comparable<DocumentResult>{
	/**
	 * Private variables to store the name of the document and the TF-IDF score of the search on that document.
	 */
	private String doc;
	private Double score;
	
	/**
	 * Constructor to instantiate a new DocumentResult.
	 * @param doc - Name of the file.
	 * @param score - TF-IDF score of the search on that file.
	 */
	public DocumentResult(String doc, double score){
		this.doc = doc;
		this.score = score;
	}
	
	/**
	 * Compares by document name in the case of TF-IDF ties.
	 */
	public int compareTo(DocumentResult dr){
		if(this.score.compareTo(dr.getScore()) == 0){
			return this.doc.compareTo(dr.getName());
		}
		return(this.score.compareTo(dr.getScore()));
	}
	
	public String getName(){
		return doc;
	}
	
	public void setName(String doc){
		this.doc = doc;
	}
	
	public double getScore(){
		return score;
	}
	
	public void setScore(double score){
		this.score = score;
	}
}
