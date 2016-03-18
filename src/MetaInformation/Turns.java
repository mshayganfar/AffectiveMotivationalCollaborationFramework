package MetaInformation;

public class Turns {
	
	private static Turns turn = new Turns();
	
	private static int count;
	
	private Turns() {
		count = 1;
	}
	
	public static Turns getInstance() {
		return turn;
	}
	
	public static void updateTurn() {
		count++;
	}
	
	public static int getTurnNumber() {
		return count;
	}
	
	public String toString() {
		return ("turn:" + count);
	}
	
//	public int value(String strTurn) {
//		return Integer.valueOf(strTurn.substring(5));
//	}
	
//	public String getLastTurn() {
//		return ("\"turn:" + count + "\"");
//	}
	
//	public String getPreviousTurn() {
//		return (count == 1) ? ("\"turn:" + count + "\"") : ("\"turn:" + (count-1) + "\"");
//	}
}
