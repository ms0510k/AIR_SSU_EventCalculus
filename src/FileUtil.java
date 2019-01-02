import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;


public class FileUtil {
	private CheckUtil checkUtil;
	
	public FileUtil() {
		checkUtil = new CheckUtil();
	}
	
	public ArrayList<ConflictGraph> readConflictGraph(String fileName){
		ArrayList<ConflictGraph> cgs = new ArrayList<ConflictGraph>();
		ConflictGraph cg = new ConflictGraph();
		File conflictGraphFile = new File(fileName);
		
		try {
			BufferedReader b = new BufferedReader(new FileReader(conflictGraphFile));
			StringTokenizer stk1;
			StringTokenizer stk2;
			String single_line="";
			try {
				while((single_line = b.readLine()) != null){					
					stk1 = new StringTokenizer(single_line, ":");
					while(stk1.hasMoreTokens()){
						cg = new ConflictGraph();
						cg.setSourceFluent(stk1.nextToken());
						String nonConflicts = stk1.nextToken();
						stk2 = new StringTokenizer(nonConflicts, ",");
						
						while(stk2.hasMoreTokens()){
							cg.getNonConflictFluent().add(stk2.nextToken());
						}
						
						cgs.add(cg);
					}
					
				}
			//b.close(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cgs;
		
	}
	
	public ArrayList<ArrayList<PerceptSequence>> readFilesScenario(String dir) throws IOException{
		String temp = null;
		String single_line = null;
		ArrayList<ArrayList<PerceptSequence>> scenarios = new ArrayList<ArrayList<PerceptSequence>>();
		ArrayList<PerceptSequence> scenario = new ArrayList<PerceptSequence>();
		PerceptSequence ps = new PerceptSequence();
		
		File file = new File(dir);
		String[] list = file.list();
		//system.out.println(list[0]);
		
		for(String fileName : list)
		{
			String sFolderName = dir+fileName;
			//System.out.println(sFolderName);
			FileReader fr = new FileReader(sFolderName); 
			BufferedReader br = new BufferedReader(fr);
			
			while((single_line = br.readLine()) != null){
				if(single_line.equals(""))
					continue;
				StringTokenizer tokenizer = new StringTokenizer(single_line, "\t");
				while(tokenizer.hasMoreTokens()){
					ps = new PerceptSequence();
					temp = tokenizer.nextToken();
					ps.setStartTime(temp);
					ps.setFinishTime(temp);
					//ps.setStartTime(tokenizer.nextToken());
					//ps.setFinishTime(tokenizer.nextToken());
					//ps.setUser(tokenizer.nextToken());
					temp = tokenizer.nextToken();
					if(temp.charAt(0)==' ')
						ps.setPose(temp.substring(1));
					else
						ps.setPose(temp);
					if(tokenizer.hasMoreTokens())
						ps.setActivity(tokenizer.nextToken());
					if(tokenizer.hasMoreTokens())
						break;
				}
				scenario.add(ps);
			}
			System.out.println(sFolderName+" 퍼셉트 파일 인식.");
			scenarios.add(scenario);
			scenario = new ArrayList<PerceptSequence>();
			
			br.close();
			fr.close();
		}
		
		return scenarios;
	}
	
	public ArrayList<ArrayList<PerceptSequence>> readScenario(String fileName){
		String temp = null;
		String single_line = null;
		ArrayList<ArrayList<PerceptSequence>> scenarios = new ArrayList<ArrayList<PerceptSequence>>();
		ArrayList<PerceptSequence> scenario = new ArrayList<PerceptSequence>();
		PerceptSequence ps = new PerceptSequence();

		File scenarioFile = new File(fileName);

			BufferedReader b = null;
			try {
				b = new BufferedReader(new FileReader(scenarioFile));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				while((single_line = b.readLine()) != null){
					if(!single_line.equals("")){
						StringTokenizer tokenizer = new StringTokenizer(single_line, "\t");
						while(tokenizer.hasMoreTokens()){
							ps = new PerceptSequence();
							temp = tokenizer.nextToken();
							ps.setStartTime(temp);
							ps.setFinishTime(temp);
							//ps.setStartTime(tokenizer.nextToken());
							//ps.setFinishTime(tokenizer.nextToken());
							//ps.setUser(tokenizer.nextToken());
							temp = tokenizer.nextToken();
							if(temp.charAt(0)==' ')
								ps.setPose(temp.substring(1));
							else
								ps.setPose(temp);
							//ps.setPose(tokenizer.nextToken().indexOf(0));
							if(tokenizer.hasMoreTokens())
								ps.setActivity(tokenizer.nextToken());
							if(tokenizer.hasMoreTokens())
								break;
							//ps.setObj(tokenizer.nextToken());
						}
						scenario.add(ps);
					}
					//else {
					//	System.out.println(sc+"번 시나리오 저장.");
					//	sc++;
					//	scenarios.add(scenario);
					//	scenario = new ArrayList<PerceptSequence>();
					//}
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
			scenarios.add(scenario);

		return scenarios;
	}
	
/*	public ArrayList<ArrayList<PerceptSequence>> readScenario(String fileName){
		int sc = 1;
		String single_line = null;
		ArrayList<ArrayList<PerceptSequence>> scenarios = new ArrayList<ArrayList<PerceptSequence>>();
		ArrayList<PerceptSequence> scenario = new ArrayList<PerceptSequence>();
		PerceptSequence ps = new PerceptSequence();

		File scenarioFile = new File(fileName);

			BufferedReader b = null;
			try {
				b = new BufferedReader(new FileReader(scenarioFile));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				while((single_line = b.readLine()) != null){
					if(!single_line.equals("")){
						StringTokenizer tokenizer = new StringTokenizer(single_line, ",");
						while(tokenizer.hasMoreTokens()){
							ps = new PerceptSequence();
							ps.setTime(tokenizer.nextToken());
							ps.setActivity(tokenizer.nextToken());
							ps.setPose(tokenizer.nextToken());
							//ps.setObj(tokenizer.nextToken());
						}
						scenario.add(ps);
					}
					else {
						System.out.println(sc+"번 시나리오 저장.");
						sc++;
						scenarios.add(scenario);
						scenario = new ArrayList<PerceptSequence>();
					}
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
			scenarios.add(scenario);

		return scenarios;
	}*/
	
	public ArrayList<Fluent> getFluentsWithInformation(String fileName) {
		
		File fluentFile = new File(fileName);
		
		ArrayList<Fluent> fluents = new ArrayList<Fluent>();
		ArrayList<String> intentions = getIntentions();
		
		try {
			BufferedReader fluentLines = new BufferedReader(new FileReader(fluentFile));
			String line = null;
			
			StringTokenizer st1, st2;
			
			Fluent fluent = new Fluent();
			ArrayList<String> activities = new ArrayList<String>();
			ArrayList<String> poses = new ArrayList<String>();
			ArrayList<String> objects = new ArrayList<String>();
			
			boolean isFirst = true;
			
			while((line = fluentLines.readLine()) != null){
				if (checkUtil.isIntention(intentions, line)) {
					if (isFirst) {
						fluent.setIntention(line);
						isFirst = false;
					}
					else {
						fluent.setActivity(activities);
						fluent.setPose(poses);
						fluent.setObj(objects);
						
						fluents.add(fluent);
						
						fluent = new Fluent();
						activities = new ArrayList<String>();
						poses = new ArrayList<String>();
						objects = new ArrayList<String>();
						
						fluent.setIntention(line);
					}
				}
				// Activities, Poses, Objects
				else {
					st1 = new StringTokenizer(line, ":");
					while(st1.hasMoreTokens()){
						String key = st1.nextToken();
						String values = st1.nextToken();
						
						st2 = new StringTokenizer(values, ",");
						
						while(st2.hasMoreTokens()) {
							if (key.equals("Activities")) {
								activities.add(st2.nextToken());
							}
							else if (key.equals("Poses")) {
								poses.add(st2.nextToken());
							}
							else {
								objects.add(st2.nextToken());
							}
						}
					}
				}
			}
			
			fluent.setActivity(activities);
			fluent.setPose(poses);
			fluent.setObj(objects);
			
			fluents.add(fluent);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return fluents;
	}
	
	//public ArrayList
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getIntentions() {
		ArrayList<String> intentions = new ArrayList<String>();
		
		File intentionFile = new File("./Scenario/intentions.txt");
		
		try {
			BufferedReader lines = new BufferedReader(new FileReader(intentionFile));
			
			String line = "";
			while((line = lines.readLine()) != null){
				intentions.add(line);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return intentions;
	}

	public void writeFluentCount(PerceptCount pc, String fileName) {
		FileWriter fluentFile;
		try {
			fluentFile = new FileWriter(fileName);
			BufferedWriter bfr = new BufferedWriter(fluentFile);
			
			bfr.write("Meal::\n");
			for(String s: pc.getMealVector()){
				bfr.write(s+'\n');
			}
			
			bfr.append("WatchingTV::\n");
			for(String s: pc.getWatchingTVVector()){
				bfr.write(s+'\n');
			}
			
			bfr.append("Communication::\n");
			for(String s: pc.getCommunicationVector()){
				bfr.write(s+'\n');
			}
			
			bfr.append("Reading::\n");
			for(String s: pc.getReadingVector()){
				bfr.write(s+'\n'); 
			}
			
			bfr.append("Cleaning::\n");
			for(String s: pc.getCleaningVector()){
				bfr.write(s+'\n'); 
			} 
			
			bfr.close(); fluentFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*String data;
		FileOutputStream output;
		try {
			output = new FileOutputStream(fileName);
			
			data = "Meal";
			output.write(data.getBytes());
			for(int i=0; i <pc.getMealVector().size(); i++) {
	            data = pc.getMealVector().elementAt(i)+"\n";
	            output.write(data.getBytes());
	        }
	        output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
        
	}

	/*private String makeCountSeq(Vector<String> cv) {
		String e = "";
		for(String s: cv){
			e += s + " "; 
		}
		return e;
	}*/
}
