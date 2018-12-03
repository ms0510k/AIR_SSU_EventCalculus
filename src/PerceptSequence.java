import java.util.ArrayList;
import java.util.HashSet;


public class PerceptSequence {
	//private String time="";
	private String startTime="";
	private String finishTime="";
	private String user="";
	private String activity=""; 
	private String pose=""; 
	private String obj=""; 
	private String intention=""; 
	private String noisyFluent = "";
	private HashSet<String> ongoing_intention = new HashSet<String>();
	private ArrayList<String> TerminatedFluent = new ArrayList<String>();
	private ArrayList<String> CandidateTerminatableFluent = new ArrayList<String>();
	
	public PerceptSequence(){
		this.startTime="";
		this.finishTime="";
		this.user="";
		this.activity="";
		this.pose="";
		this.obj = "";
		this.intention="";
	}
	
	public PerceptSequence(String stime, String ftime, String user, String activity, String pose, String obj){
		this.startTime=stime;
		this.finishTime= ftime;
		this.user = user;
		this.activity=activity;
		this.pose=pose;
		this.obj=obj;
	}
	
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String fTime) {
		this.finishTime = fTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String stime) {
		this.startTime = stime;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPose() {
		return pose;
	}
	public void setPose(String pose) {
		this.pose = pose;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getObj() {
		return obj;
	}
	public void setObj(String obj) {
		this.obj = obj;
	}
	public String getIntention() {
		return intention;
	}
	public void setIntention(String intention) {
		this.intention = intention;
	}

	public HashSet<String> getOngoing_intention() {
		return ongoing_intention;
	}

	public void setOngoing_intention(HashSet<String> ongoing_intention) {
		this.ongoing_intention = ongoing_intention;
	}

	public String getNoisyFluent() {
		return noisyFluent;
	}

	public void setNoisyFluent(String noisyFluent) {
		this.noisyFluent = noisyFluent;
	}

	public ArrayList<String> getTerminatedFluent() {
		return TerminatedFluent;
	}

	public void setTerminatedFluent(ArrayList<String> terminatedFluent) {
		TerminatedFluent = terminatedFluent;
	}

	public ArrayList<String> getCandidateTerminatableFluent() {
		return CandidateTerminatableFluent;
	}

	public void setCandidateTerminatableFluent(
			ArrayList<String> candidateTerminatableFluent) {
		CandidateTerminatableFluent = candidateTerminatableFluent;
	}
}
