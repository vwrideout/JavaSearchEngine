
public class DocumentResult implements Comparable<DocumentResult>{
	private String doc;
	private Double score;
	
	public DocumentResult(String doc, double score){
		this.doc = doc;
		this.score = score;
	}
	
	public int compareTo(DocumentResult dr){
		return this.score.compareTo(dr.getScore());
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
