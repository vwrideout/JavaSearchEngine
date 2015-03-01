import java.util.Iterator;
import java.util.TreeSet;



public class DocumentResultList extends TreeSet<DocumentResult>{

	private static final long serialVersionUID = 1L;
	private String query;
	
	public DocumentResultList(String query){
		super();
		this.query = query;
	}
	
	public DocumentResult contains(String fileName){
		for(DocumentResult dr: this){
			if(fileName == dr.getName()){
				return dr;
			}
		}
		return null;
	}
	
	public String toString(){
		Iterator<DocumentResult> iter = this.descendingIterator();
		StringBuffer s = new StringBuffer();
		s.append(query + "\n");
		while(iter.hasNext()){
			s.append(iter.next().getName() + "\n");
		}
		s.append("\n");
		return s.toString();
	}
	
	public String getQuery(){
		return query;
	}
	
}
