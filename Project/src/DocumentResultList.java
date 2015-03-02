import java.util.Iterator;
import java.util.TreeSet;



public class DocumentResultList extends TreeSet<DocumentResult>{

	private static final long serialVersionUID = 1L;
	private String query;
	
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
	
	public void updateScore(String fileName, double delta){
		for(DocumentResult dr: this){
			if(fileName == dr.getName()){
				dr.setScore(dr.getScore() + delta);
				return;
			}
		}
	}
	
	public String toString(){
		Iterator<DocumentResult> iter = this.descendingIterator();
		StringBuffer s = new StringBuffer();
		s.append(query + "\n");
		while(iter.hasNext()){
			DocumentResult dr = iter.next();
			s.append(dr.getName() + ": " + dr.getScore() + "\n");
		}
		s.append("\n");
		return s.toString();
	}
	
	public String getQuery(){
		return query;
	}
	
}
