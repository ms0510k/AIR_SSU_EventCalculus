import java.io.*;
import java.util.ArrayList;


public class MainDriver {

	public static void main(String[] args) throws IOException {
		double minimumSupport = 0.01;
		// Reasoner와 FileUtil 생성
		FileUtil fileUtil = new FileUtil();  
		//CheckUtil checkUtil = new CheckUtil();
		Reasoner reasoner = new Reasoner();
		// Noise Reduction에서 Fluent History를 최근에서 얼마만큼의 사이즈로 확인할 것인가를 결정
		int windowSize = 0;
		int count = 0;

		// Fluent_info => 각 Fluent 마다 Reasoning 할 수 있는 Action, Pose, Object들을 명시해놓은 파일
		ArrayList<Fluent> fluents = fileUtil.getFluentsWithInformation("Scenario/fluent_info.txt");
		//Event Calculus를 구동할 시나리오들을 명시해놓은 파일
		ArrayList<ArrayList<PerceptSequence>> scenarios = fileUtil.readFilesScenario("Scenario/Tag 데이터 분단위 정렬/2018_03_이숙재/이숙재_day/");
//		ArrayList<ArrayList<PerceptSequence>> scenarios = fileUtil.readScenario("Scenario/Tag 데이터 분단위 정렬/2018_04_김차순/Tag_김차순_cam0_2018_04_30.txt");
		// 각 Fluent 마다 동시에 발생할 수 있는 Fluent들을 명시해놓은 파일
		ArrayList<ConflictGraph> conflictGraphs = fileUtil.readConflictGraph("Scenario/conflict_graph.txt"); 

//		for (ArrayList<PerceptSequence> h : scenarios) {
//			for (PerceptSequence ps : h) {
//				System.out.println("StartTime : " + ps.getStartTime());
//			}
//		}

		ArrayList<PerceptSequence> history = null;
		ArrayList<ArrayList<PerceptSequence>> histories = new ArrayList<ArrayList<PerceptSequence>>();
		PerceptCount pc = new PerceptCount();
		boolean isFirst = false;

		// 하나의 시나리오 씩 받아옴
		for(ArrayList<PerceptSequence> scenario : scenarios){
			isFirst = true;
			history = new ArrayList<PerceptSequence>();

			// 하나의 시나리오 내에서 각 t 시점마다의 인지된 Activity, Pose, Object 정보를 받아옴
			for(PerceptSequence ps : scenario){
				// Reasoning...
				String intention = reasoner.reasoning(ps, fluents);

				// 추론된 Fluent Set
				ps.setIntention(intention);
				
				if(isFirst) {
					ps.setOngoing_intention(reasoner.resoluteConflict2(ps, ps, isFirst));
					//System.out.println(ps.getStartTime()+"::"+reasoner.getIntentionDelay());
				}else {
					ps.setOngoing_intention(reasoner.resoluteConflict2(history.get(history.size()-1), ps, isFirst));
					//System.out.println(ps.getStartTime()+"::"+reasoner.getIntentionDelay());
				}
				// 처음일 경우에는 전 시점의 Fluent가 없으므로 자기 자신을 2번 넣음
				//if (isFirst)
					//ps.setOngoing_intention(reasoner.resoluteConflict(ps, ps, conflictGraphs));
				//else 
					//ps.setOngoing_intention(reasoner.resoluteConflict(history.get(history.size()-1), ps, conflictGraphs));

				//Fluent History의 Size가 3보다 크거나 같을 경우 Noise Reduction 진행
				if(history.size() >= 2)
					reasoner.noiseReduction2(history.get(history.size()-2), history.get(history.size()-1), ps);

				if (ps.getTerminatedFluent().size() != 0) {
					ps.getCandidateTerminatableFluent().clear();
				}

				history.add(ps);
				isFirst = false;
			}
			pc.count(pc, history);
			histories.add(history);
		}
		
		//---> 결과 출력: 파일로 수정
		for (ArrayList<PerceptSequence> h : histories) {
			for (PerceptSequence ps : h) {
//				System.out.println("StartTime : " + ps.getStartTime());
//				System.out.println("finishTime : " + ps.getFinishTime());
//				System.out.println("Current Activity : " + ps.getActivity());
//				System.out.println("Current Pose : " + ps.getPose());
//				//System.out.println("Current Object : " + ps.getObj());
//				System.out.println("Intention Reasoning : " + ps.getIntention());
//				System.out.println("Fluent : " + ps.getOngoing_intention());
//				System.out.println("Terminated Fluent : " + ps.getTerminatedFluent());
//				//System.out.println("noisy Fluent : " + ps.getNoisyFluent());
//				//System.out.println("Candidate Terminatable Fluent : " + ps.getCandidateTerminatableFluent());
//				System.out.println("-------------------------------");
				count++;
			}
//			System.out.println("========Next Scenario=========");
		}
		
//		//Apriori 구현, 빈도 구하기
//		ComplexIntention ci = new ComplexIntention();
//		ci.composeSource();
//		ci.Apriori(histories, minimumSupport,count);
//		ci.writeComplexIntentions(histories, "Scenario/output/");
//
//
//		fileUtil.writeFluentCount(pc, "Scenario/fluent_count.txt");

		//Apriori 구현, 빈도 구하기 <---- 여기부터
		ComplexIntention ci = new ComplexIntention();
		ci.composeSource();
		ci.composeCounterHash();
		ci.daysCount(histories);
		ci.writeDaysCount("Scenario/output/daysCounts.txt");
		ci.Apriori(histories, minimumSupport,count);
		ci.writeComplexIntentions(histories, "Scenario/output/");
		//System.out.println(":::::");
		// <----여기까지
	}

	// @jaeseung : func for py4j
	public String run_engine(String option) throws IOException {

		// @jaeseung : redirect stdout to string
		String data= "";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//System.setOut(new PrintStream(baos));

		double minimumSupport = 0.01;
		// Reasoner와 FileUtil 생성
		FileUtil fileUtil = new FileUtil();
		//CheckUtil checkUtil = new CheckUtil();
		Reasoner reasoner = new Reasoner();
		// Noise Reduction에서 Fluent History를 최근에서 얼마만큼의 사이즈로 확인할 것인가를 결정
		int windowSize = 0;
		int count = 0;

		// Fluent_info => 각 Fluent 마다 Reasoning 할 수 있는 Action, Pose, Object들을 명시해놓은 파일
		ArrayList<Fluent> fluents = fileUtil.getFluentsWithInformation("Scenario/fluent_info.txt");
		//Event Calculus를 구동할 시나리오들을 명시해놓은 파일
		//ArrayList<ArrayList<PerceptSequence>> scenarios = fileUtil.readFilesScenario("Scenario/Tag 데이터 분단위 정렬/2018_03_이숙재/");
		// 각 Fluent 마다 동시에 발생할 수 있는 Fluent들을 명시해놓은 파일
		ArrayList<ConflictGraph> conflictGraphs = fileUtil.readConflictGraph("Scenario/conflict_graph.txt");

		// @jaeseung : neeed min-parsed data
		ArrayList<ArrayList<PerceptSequence>> scenarios;
		if (option.equals("final_demo_sec1.txt"))
			scenarios = fileUtil.readScenario("Scenario/Tag_초단위/final_demo_sec1.txt");
		else if (option.equals("final_demo_sec2.txt"))
			scenarios = fileUtil.readScenario("Scenario/Tag_초단위/final_demo_sec2.txt");
		else
			scenarios = fileUtil.readScenario("Scenario/Tag_초단위/final_demo_sec3.txt");


//		for (ArrayList<PerceptSequence> h : scenarios) {
//			for (PerceptSequence ps : h) {
//				System.out.println("StartTime : " + ps.getStartTime());
//			}
//		}

		ArrayList<PerceptSequence> history = null;
		ArrayList<ArrayList<PerceptSequence>> histories = new ArrayList<ArrayList<PerceptSequence>>();
		PerceptCount pc = new PerceptCount();
		boolean isFirst = false;

		// 하나의 시나리오 씩 받아옴
		for(ArrayList<PerceptSequence> scenario : scenarios){
			isFirst = true;
			history = new ArrayList<PerceptSequence>();

			// 하나의 시나리오 내에서 각 t 시점마다의 인지된 Activity, Pose, Object 정보를 받아옴
			for(PerceptSequence ps : scenario){
				// Reasoning...
				String intention = reasoner.reasoning(ps, fluents);

				// 추론된 Fluent Set
				ps.setIntention(intention);

				if(isFirst) {
					ps.setOngoing_intention(reasoner.resoluteConflict2(ps, ps, isFirst));
					//System.out.println(ps.getStartTime()+"::"+reasoner.getIntentionDelay());
				}else {
					ps.setOngoing_intention(reasoner.resoluteConflict2(history.get(history.size()-1), ps, isFirst));
					//System.out.println(ps.getStartTime()+"::"+reasoner.getIntentionDelay());
				}

				// 처음일 경우에는 전 시점의 Fluent가 없으므로 자기 자신을 2번 넣음
				//if (isFirst)
				//ps.setOngoing_intention(reasoner.resoluteConflict(ps, ps, conflictGraphs));
				//else
				//ps.setOngoing_intention(reasoner.resoluteConflict(history.get(history.size()-1), ps, conflictGraphs));

				//Fluent History의 Size가 3보다 크거나 같을 경우 Noise Reduction 진행
				if(history.size() >= 2)
					reasoner.noiseReduction2(history.get(history.size()-2), history.get(history.size()-1), ps);

				if (ps.getTerminatedFluent().size() != 0) {
					ps.getCandidateTerminatableFluent().clear();
				}

				history.add(ps);
				isFirst = false;
			}
			pc.count(pc, history);
			histories.add(history);
		}

		//---> 결과 출력: 파일로 수정
		for (ArrayList<PerceptSequence> h : histories) {
			for (PerceptSequence ps : h) {
//				System.out.println("StartTime : " + ps.getStartTime());
//				System.out.println("finishTime : " + ps.getFinishTime());
//				System.out.println("Current Activity : " + ps.getActivity());
//				System.out.println("Current Pose : " + ps.getPose());
//				//System.out.println("Current Object : " + ps.getObj());
//				System.out.println("Intention Reasoning : " + ps.getIntention());
//				System.out.println("Fluent : " + ps.getOngoing_intention());
//				System.out.println("Terminated Fluent : " + ps.getTerminatedFluent());
//				//System.out.println("noisy Fluent : " + ps.getNoisyFluent());
//				//System.out.println("Candidate Terminatable Fluent : " + ps.getCandidateTerminatableFluent());
//				System.out.println("-------------------------------");
				count++;
			}
//			System.out.println("========Next Scenario=========");
		}

		//Apriori 구현, 빈도 구하기
		ComplexIntention ci = new ComplexIntention();
		ci.composeSource();
		ci.Apriori(histories, minimumSupport,count);
		ci.writeComplexIntentions(histories, "Scenario/output/");


		fileUtil.writeFluentCount(pc, "Scenario/fluent_count.txt");

		return baos.toString();
	}

}
