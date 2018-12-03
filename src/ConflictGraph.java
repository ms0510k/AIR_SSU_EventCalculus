import java.util.ArrayList;


public class ConflictGraph {

	/**
	 * @param args
	 */
	private String sourceFluent=""; 
	
	private ArrayList<String> nonConflictFluent = new ArrayList<String>(); 
	
	public String getSourceFluent() {
		return sourceFluent;
	}
	public void setSourceFluent(String sourceFluent) {
		this.sourceFluent = sourceFluent;
	}
	public ArrayList<String> getNonConflictFluent() {
		return nonConflictFluent;
	}
	public void setNonConflictFluent(ArrayList<String> conflictFluent) {
		this.nonConflictFluent = conflictFluent;
	}

}
