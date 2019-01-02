import java.util.ArrayList;
import java.util.Vector;


public class PerceptCount {
	private Vector<String> MealVector;
	private Vector<String> WatchingTVVector;
	private Vector<String> CommunicationVector;
	private Vector<String> ReadingVector;
	private Vector<String> CleaningVector;
	
	public PerceptCount(){
		MealVector           = new Vector<String>();
		WatchingTVVector     = new Vector<String>();
		CommunicationVector  = new Vector<String>();
		ReadingVector        = new Vector<String>();
		CleaningVector       = new Vector<String>();
	}
	
	public void count(PerceptCount pc, ArrayList<PerceptSequence> history){
		int mindex=0, windex=0, cindex=0, rindex=0, clindex=0;
		String countV;
		
		for(PerceptSequence ps : history){
			if(ps.getOngoing_intention().contains("Meal"))
				mindex = vectorCotroll(true, pc.getMealVector(), mindex);
			else
				mindex = vectorCotroll(false, pc.getMealVector(), mindex);
			if(ps.getOngoing_intention().contains("WatchingTV"))
				windex = vectorCotroll(true, pc.getWatchingTVVector(), windex);
			else
				windex = vectorCotroll(false, pc.getWatchingTVVector(), windex);
			if(ps.getOngoing_intention().contains("Communication"))
				cindex = vectorCotroll(true, pc.getCommunicationVector(), cindex);
			else
				cindex = vectorCotroll(false, pc.getCommunicationVector(), cindex);
			if(ps.getOngoing_intention().contains("Reading"))
				rindex = vectorCotroll(true, pc.getReadingVector(), rindex);
			else
				rindex = vectorCotroll(false, pc.getReadingVector(), rindex);
			if(ps.getOngoing_intention().contains("Cleaning"))
				clindex = vectorCotroll(true, pc.getCleaningVector(), clindex);
			else
				clindex = vectorCotroll(false, pc.getCleaningVector(), clindex);
		}
	}
	
	public int vectorCotroll(boolean b, Vector<String> cv, int c){
		String countV;
		if(b==true){
			if(cv.size() > c){
				countV = cv.get(c);
				countV = Integer.toString(Integer.parseInt(countV)+1);
				cv.setElementAt(countV, c);
			}else{
				countV = Integer.toString(1);
				cv.add(countV);
			}
			c++;
		}else{
			if(cv.size()<=c){
				countV = Integer.toString(0);
				cv.add(countV);
			}
			c++;
		}
		return c;
	}
	
	public Vector<String> getMealVector() {
		return MealVector;
	}
	public void setMealVector(Vector<String> mealVector) {
		MealVector = mealVector;
	}

	public Vector<String> getWatchingTVVector() {
		return WatchingTVVector;
	}

	public void setWatchingTVVector(Vector<String> watchingTVVector) {
		WatchingTVVector = watchingTVVector;
	}

	public Vector<String> getCommunicationVector() {
		return CommunicationVector;
	}

	public void setCommunicationVector(Vector<String> communicationVector) {
		CommunicationVector = communicationVector;
	}

	public Vector<String> getReadingVector() {
		return ReadingVector;
	}

	public void setReadingVector(Vector<String> readingVector) {
		ReadingVector = readingVector;
	}

	public Vector<String> getCleaningVector() {
		return CleaningVector;
	}

	public void setCleaningVector(Vector<String> cleaningVector) {
		CleaningVector = cleaningVector;
	}

}
