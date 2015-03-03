import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;



public class DocumentResultList extends ArrayList<DocumentResult>{

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
