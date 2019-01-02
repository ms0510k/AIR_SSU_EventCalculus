import java.util.ArrayList;
import java.util.StringTokenizer;


public class CheckUtil {
	private int baseTime = 0; 
	
	public int calTimeVolum(String str) {
		int time = 0;
		
		StringTokenizer stk = new StringTokenizer(str, "_"); 
		stk.nextToken();stk.nextToken();stk.nextToken();
		time = Integer.parseInt(stk.nextToken()) * 60 + Integer.parseInt(stk.nextToken());
		
		return time;
	}
	
	public boolean isIntention(ArrayList<String> intentions, String line) {
		for (String intention : intentions) {
			if (line.equals(intention))
				return true;
		}
		
		return false;
	}
}
