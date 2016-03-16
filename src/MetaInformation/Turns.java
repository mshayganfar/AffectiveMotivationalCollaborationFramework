package MetaInformation;

public class Turns {
	
	private int count;
	
	public Turns() {
		count = 1;
	}
	
	public void updateTurn() {
		count++;
	}
	
	public String toString() {
		return ("turn:" + count);
	}
	
	public int getCurrentTurn() {
		return count;
	}
	
	public int value(String strTurn) {
		return Integer.valueOf(strTurn.substring(5));
	}
	
	public String getLastTurn() {
		return ("\"turn:" + count + "\"");
	}
	
	public String getPreviousTurn() {
		return (count == 1) ? ("\"turn:" + count + "\"") : ("\"turn:" + (count-1) + "\"");
	}
}
