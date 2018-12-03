import java.util.ArrayList;


public class Fluent {
	private String intention;
	private ArrayList<String> activity = new ArrayList<String>();
	private ArrayList<String> pose = new ArrayList<String>();
	private ArrayList<String> obj = new ArrayList<String>();
	
	public String getIntention() {
		return intention;
	}
	public void setIntention(String intention) {
		this.intention = intention;
	}		
	public ArrayList<String> getActivity() {
		return activity;
	}
	public void setActivity(ArrayList<String> activity) {
		this.activity = activity;
	}
	public ArrayList<String> getPose() {
		return pose;
	}
	public void setPose(ArrayList<String> pose) {
		this.pose = pose;
	}
	public ArrayList<String> getObj() {
		return obj;
	}
	public void setObj(ArrayList<String> obj) {
		this.obj = obj;
	}
	
}
