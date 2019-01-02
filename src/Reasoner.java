import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;


public class Reasoner {
	private int startTime;
	private int finishTime;
	private int timeP;
	private int preTime;
	private int processTime;
	private Hashtable<String, String> intentionDelay;  
	private Vector<String> shortTimeIntention;

	private ArrayList<String> demo_intention;
	/**
	 * 인지된 Activity, Pose, Object들이 해당 Fluent의 Activity, Pose, Object List에 있는지 파악하여
	 * 있을 경우에는 해당 Fluent를 Return하고 아닐 경우에는 null Return
	 *
	 * @param ps Activity, Pose, Object
	 * @param fluents Fluent List
	 * @return
	 */
	public Reasoner() {
		timeP = 0;
		preTime = 0;
		intentionDelay = new Hashtable<String, String>();
		shortTimeIntention = new Vector<String>();
		shortTimeIntention.add("Drink");

		intentionDelay.put("PrepareMeal", "0");
		intentionDelay.put("Meal", "0");
		intentionDelay.put("Refreshment", "0");
		intentionDelay.put("WatchingTV", "0");
		intentionDelay.put("CommunicationWithPerson", "0");
		intentionDelay.put("Communication", "0");
		intentionDelay.put("Reading", "0");
		intentionDelay.put("Cleaning", "0");
		intentionDelay.put("ArrangeThing", "0");
		intentionDelay.put("Smoking", "0");
		intentionDelay.put("HealthCare", "0");
		intentionDelay.put("Drink", "0");
		intentionDelay.put("Clothing", "0");
	}
	
	public Hashtable<String, String> getIntentionDelay(){
		return intentionDelay;
	}
	
	public String reasoning(PerceptSequence ps, ArrayList<Fluent> fluents) {
		String activity = ps.getActivity();
		String pose = ps.getPose();
		String object = ps.getObj();
		
		for (Fluent f : fluents) {
			if (f.getActivity().contains(activity)) {
				if (f.getPose().contains(pose)) {
					//if (f.getObj().contains(object)) { -->일단 object 비처리
						return f.getIntention();
					//}
				}
			}
		}
		return null;
	}

	/**
	 * 바로 전 시점의 Fluent와 현 시점의 Fluent의 충돌 발생 여부를 검사하여,
	 * 충돌이 일어날 경우에는 현 시점의 Fluent로 교체하고, 아닐 경우에는 같이 가도록 설정
	 *
	 * @param past
	 * @param present
	 * @param cgs Conflict Graph List
	 * @return
	 */
	public HashSet<String> resoluteConflict(PerceptSequence past, PerceptSequence present, ArrayList<ConflictGraph> cgs) {
		// 현재 True인 Fluent가 없는 경우에는 과거 시점의 Fluent를 바로 넣음
		if(past.getOngoing_intention().size() == 0){
			present.getOngoing_intention().add(past.getIntention());
		}
		else{
			// 아닐 경우에는 현재 True인 Fluent들을 하나씩 꺼내서 Conflict Resolution 진행
			// 같이 갈 수 있는 Fluent일 경우 Ongoing_intention에 넣고
			// 아닐 경우에는 기존에 있던 Fluent를 종료 후보 Fluent List에 넣음
			for(String pastFluent : past.getOngoing_intention()){
				for(ConflictGraph cf : cgs){
					if(cf.getSourceFluent().equals(present.getIntention())){
						if(cf.getNonConflictFluent().contains(pastFluent)){
							present.getOngoing_intention().add(pastFluent);
						}
						else{
							present.getCandidateTerminatableFluent().add(pastFluent);
						}
					}
				}

				present.getOngoing_intention().add(present.getIntention());
			}
		}
		
		if(past.getIntention()!=null && present.getIntention()!=null && past.getIntention().equals(present.getIntention())){
			if(!past.getCandidateTerminatableFluent().isEmpty())
				present.setTerminatedFluent((ArrayList<String>)past.getCandidateTerminatableFluent().clone());
		}
		
		return present.getOngoing_intention();
	}
	
	public HashSet<String> resoluteConflict2(PerceptSequence past, PerceptSequence present, boolean first){
		HashSet<String> ongoing_intentions = new HashSet<String>();
		Iterator i;
		String ContinuedIntention = null;
		int remainedTime;
		CheckUtil checkUtil = new CheckUtil();
		
		startTime = checkUtil.calTimeVolum(present.getStartTime());
		finishTime = checkUtil.calTimeVolum(present.getFinishTime());
		timeP = finishTime - startTime;
		processTime = startTime - preTime; 
		
		//System.out.println("StartTime : " + present.getStartTime());
//		System.out.println("DEBUG 1111 : ");

		if(first) {
			if(present.getIntention() != null) {
				ongoing_intentions.add(present.getIntention());
//				System.out.println("DEBUG 0 : " + present.getIntention());
				intentionDelay.put(present.getIntention(), "2");
//				System.out.println("DEBUG 1 : " + intentionDelay);
			}
		}else {
			i = past.getOngoing_intention().iterator();
			while(i.hasNext()) {
				ContinuedIntention = i.next().toString();
				remainedTime = Integer.parseInt(intentionDelay.get(ContinuedIntention));
				if(remainedTime > processTime) {
					//System.out.println("ContinuedIntention : " + ContinuedIntention);
					ongoing_intentions.add(ContinuedIntention);
					intentionDelay.replace(ContinuedIntention, Integer.toString(remainedTime - (processTime + timeP)));
					//시간빼기->intentionDelay 저장
//					System.out.println("DEBUG 2 : " + intentionDelay);
				}else {
					intentionDelay.replace(ContinuedIntention, "0");
					//시간초기화->0
				}
			}
			//추론된 인텐션 추가->intentionDelay 초기화
			if(present.getIntention() != null) {
				ongoing_intentions.add(present.getIntention());
				if(intentionDelay.containsKey(present.getIntention()))
					intentionDelay.replace(present.getIntention(), "2");
				else {
					intentionDelay.put(present.getIntention(), "2");
//					System.out.println("DEBUG 01 : " +  intentionDelay);
				}
			}
		}
		preTime = finishTime; 
		return ongoing_intentions;
	}
	
	 
	
	public void noiseReduction(ArrayList<PerceptSequence> history, int windowSize, PerceptSequence present){
		boolean isAllEq = false;
		PerceptSequence prev = history.get(history.size()-1);
		
		if(present.getIntention()!=null && prev.getIntention()!=null && !present.getIntention().equals(prev.getIntention())){
			for(int in = (history.size()-2); in <= (history.size()-windowSize); in++){
				if(history.get(in).getIntention()!=null){
					isAllEq = false;
					break;
				}
				
				if(present.getIntention().equals(history.get(in).getIntention()))
					isAllEq = true;
				else{
					isAllEq = false;
					break;
				}
			}
			
			if(isAllEq == true){
				present.setNoisyFluent(prev.getIntention());
				present.setOngoing_intention(history.get(history.size()-windowSize).getOngoing_intention());
				//if(!present.getCandidateTerminatableFluent().contains(present.getNoisyFluent()))
				//	present.getCandidateTerminatableFluent().add(present.getNoisyFluent());
				if(present.getCandidateTerminatableFluent().contains(present.getNoisyFluent())){
					present.getCandidateTerminatableFluent().remove(present.getNoisyFluent());	
				}
			}
			else {
				present.setOngoing_intention(history.get(history.size()-1).getOngoing_intention());
			}
				
		}
	}
	
	public void noiseReduction2(PerceptSequence past, PerceptSequence present, PerceptSequence future){
		if(present.getIntention() != null && past.getIntention() != null && future.getIntention() != null) {
			if(!shortTimeIntention.contains(present.getIntention())&& !present.getIntention().equals(past.getIntention()) && !present.getIntention().equals(future.getIntention()) && past.getIntention().equals(future.getIntention())) {
				if(!past.getOngoing_intention().contains(present.getIntention())) {
					present.getOngoing_intention().remove(present.getIntention());				
					future.getOngoing_intention().remove(present.getIntention());
					present.setIntention(past.getIntention());
				}
			}
		}
		//meal과 drink 예외상황 설정
		if(present.getIntention() != null && ((present.getIntention().equals("Drink")&&present.getOngoing_intention().contains("Meal"))||(present.getIntention().equals("Meal")&&present.getOngoing_intention().contains("Drink")))) {
			present.getOngoing_intention().remove("Drink");
			present.setIntention("Meal");
		}else if(present.getOngoing_intention().contains("Meal")&&present.getOngoing_intention().contains("Drink")) {
			present.getOngoing_intention().remove("Drink");
		}
		//meal과 refreshment 구분 설정
		//...
	}
}
