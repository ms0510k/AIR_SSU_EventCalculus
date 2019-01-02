import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public class ComplexIntention {
	private Vector<String> source;
	private Hashtable<String, int[]> counterHash;
	private Hashtable<HashSet<String>, Integer> countTable;
	private Hashtable<HashSet<String>, Double> supportTable;
	private HashSet<HashSet<String>> target;
	private Vector<HashSet<String>> totalSupportSet;

	public ComplexIntention() {
		source = new Vector<String>();
		counterHash = new Hashtable<String, int[]>();
		countTable = new Hashtable<HashSet<String>, Integer>();
		target = new HashSet<HashSet<String>>();
		supportTable = new Hashtable<HashSet<String>, Double>();
		totalSupportSet = new Vector<HashSet<String>>();
	}

	public void composeSource() throws IOException {
		FileReader fr = new FileReader("Scenario/intentions.txt");
		BufferedReader br = new BufferedReader(fr);

		String line;
		while((line = br.readLine())!= null) {
			source.add(line);
		}
		br.close(); fr.close();

		//System.out.println(source);
	}

	public void composeCounterHash() {
		for(String s:source) {
			int[] i = new int[24];
			counterHash.put(s, i);
		}
		//System.out.println(counterHash);
	}

	public void composeTarget() {

		if(target.size()==0) {
			for(String s:source) {
				HashSet<String> subTarget = new HashSet<String>();
				subTarget.add(s);
				target.add(subTarget);
			}
		}else {
			HashSet<HashSet<String>> tempTarget = new HashSet<HashSet<String>>();
			for(String s:source) {
				Iterator<HashSet<String>> subT = target.iterator();
				while(subT.hasNext()) {
					HashSet<String> subTarget = (HashSet<String>)subT.next().clone();
					if(!subTarget.contains(s)) {
						subTarget.add(s);
						tempTarget.add(subTarget);
					}
				}
			}
			//target.addAll(tempTarget);
			target = tempTarget;
			//System.out.println(target);
		}
	}

	public void composeCountTable() {
		countTable.clear();
		Iterator i = target.iterator();
		while(i.hasNext()) {
			countTable.put((HashSet<String>)i.next(), Integer.valueOf(0));
		}
		//System.out.println(countTable);
	}

	public void scan(ArrayList<ArrayList<PerceptSequence>> histories) {
		int count;

		for (ArrayList<PerceptSequence> h : histories) {
			for (PerceptSequence ps : h) {
				for( HashSet<String> subt : target) {
					if(ps.getOngoing_intention().containsAll(subt)) {
						count = countTable.get(subt).intValue()+1;
						countTable.replace(subt, Integer.valueOf(count));
					}
				}
			}
		}
		//System.out.println(countTable);
	}

	public void composeSupportTable(double ms, int total) {
		double support;
		String ts;
		HashSet<String> temp;
		Enumeration<HashSet<String>> e = countTable.keys();

		supportTable.clear();
		while(e.hasMoreElements()) {
			temp = e.nextElement();
			support = countTable.get(temp).intValue() / (double)total;
			if(support >= ms) {
				supportTable.put(temp, Double.valueOf(support));
				totalSupportSet.add(temp);
				System.out.println(temp+"의 최소지지도 통과: "+support);
			}else {
				Iterator<String> i = temp.iterator();
				while(i.hasNext()) {
					ts = i.next();
					if(source.contains(ts))
						source.remove(ts);
				}
				//target 삭재
				target.remove(temp);
			}
		}
		//System.out.println(source);
	}

	public void writeSupportInfo(BufferedWriter bw, int start) throws IOException {
		bw.append("최소지지도를 만족하면서 복합적으로 발생하는 "+start+"개의 intention(s)정보: \n");

		HashSet<String> temp;
		Enumeration<HashSet<String>> e = supportTable.keys();
		while(e.hasMoreElements()) {
			temp = e.nextElement();
			Iterator<String> i = temp.iterator();
			while(i.hasNext()) {
				bw.append(i.next());
				if(i.hasNext())
					bw.append(" and ");
			}
			bw.append(":: "+supportTable.get(temp).toString()+"\n");
		}
		bw.append("\n");
	}

	public void daysCount(ArrayList<ArrayList<PerceptSequence>> histories) {
		String key;
		String time;
		int index, count;
		for (ArrayList<PerceptSequence> h : histories) {
			for (PerceptSequence ps : h) {
				time = ps.getStartTime().substring(11, 13);
				index = Integer.parseInt(time);
				Iterator<String> i = ps.getOngoing_intention().iterator();
				while(i.hasNext()) {
					key = i.next();
					count = counterHash.get(key)[index];
					count++;
					counterHash.get(key)[index] = count;
				}
			}
		}
		//System.out.println(counterHash);
	}

	public void writeDaysCount(String file) throws IOException{
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);

		String intention;
		Iterator<String> i = counterHash.keys().asIterator();
		while(i.hasNext()) {
			intention = i.next();
			bw.write(intention+"\n");
			for(int j=0; j<24; j++) {
				bw.write(j+"::"+counterHash.get(intention)[j]+"\n");
			}
			bw.write("\n");
		}
		bw.close();
		fw.close();
	}

	public void Apriori(ArrayList<ArrayList<PerceptSequence>> histories, double minimumSupport, int transaction) throws IOException {
		int start = 1;
		int presize = 0;
		int postsize = 1;

		FileWriter fw = new FileWriter("Scenario/output/support_info.txt");
		BufferedWriter bw = new BufferedWriter(fw);

		while(true) {
			composeTarget();
			composeCountTable();
			scan(histories);
			composeSupportTable(minimumSupport, transaction);
			writeSupportInfo(bw, start);
			start++;

			if(supportTable.size()==0)
				break;
		}

		bw.close();
		fw.close();
	}

	public Vector<HashSet<String>> getTotalSupportSet(){
		return totalSupportSet;
	}

	public void writeIntentionTime(ArrayList<ArrayList<PerceptSequence>> histories, BufferedWriter bw, HashSet<String> intentions) throws IOException {
		for (ArrayList<PerceptSequence> h : histories) {
			for (PerceptSequence ps : h) {
				if(ps.getOngoing_intention().containsAll(intentions)) {
					Iterator<String> i = intentions.iterator();
					while(i.hasNext()) {
						bw.append(i.next());
						if(i.hasNext())
							bw.append(" and ");
					}
					bw.append("\t"+ps.getStartTime()+"\t"+ps.getFinishTime()+"\n");
				}
			}
		}
	}

	public void writeComplexIntentions(ArrayList<ArrayList<PerceptSequence>> histories, String dir) throws IOException {
		FileWriter fw = new FileWriter(dir + "intention(s).txt");
		BufferedWriter bw = new BufferedWriter(fw);

		//System.out.println(totalSupportSet);
		for(HashSet<String> intentions: totalSupportSet) {
			writeIntentionTime(histories, bw, intentions);
		}

		bw.close();
		fw.close();
	}
}
