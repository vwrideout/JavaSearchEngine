import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;


public class InvertedIndexSearcher {
	
	private Path searchPath;
	private InvertedIndex index;
	
	public InvertedIndexSearcher(Path searchPath, InvertedIndex index){
		this.searchPath = searchPath;
		this.index = index;
	}
	
	public ArrayList<DocumentResultList> search(){
		ArrayList<DocumentResultList> output = new ArrayList<DocumentResultList>();
		Scanner fileScanner;
		try{
			fileScanner = new Scanner(searchPath);
			while(fileScanner.hasNextLine()){
				output.add(index.search(fileScanner.nextLine()));
			}
			fileScanner.close();
		}catch(IOException ioe){
			System.out.println("Bad query path");
		}
		return output;
	}
	
	public String toString(){
		ArrayList<DocumentResultList> output = this.search();
		StringBuffer s = new StringBuffer();
		for(DocumentResultList drl: output){
			s.append(drl.toString());
		}
		return s.toString();
	}
}
