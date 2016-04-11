package MetaInformation;

import java.util.ArrayList;

import Mechanisms.Appraisal.Desirability.DESIRABILITY;

public class Turns {
	
	private static ArrayList<DESIRABILITY> desirabilities = new ArrayList<DESIRABILITY>(); 
	
	private static Turns turn = new Turns();
	
	private static int count;
	
	private Turns() {
		count = 1;
	}
	
	public static Turns getInstance() {
		return turn;
	}
	
	public static void updateTurnDesirability(DESIRABILITY desirability) {
		desirabilities.add(desirability);
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
	
	public ArrayList<DESIRABILITY> getDesirabilities() {
		return this.desirabilities;
	}
	
	public double getDesirabilityValue(DESIRABILITY desirabilitySymbolicValue) {
		
		switch (desirabilitySymbolicValue) {
			case HIGH_DESIRABLE:
				return 1.0;
			case DESIRABLE:
				return 0.5;
			case NEUTRAL:
				return 0.0;
			case UNDESIRABLE:
				return -0.5;
			case HIGH_UNDESIRABLE:
				return -1.0;
			default:
				return 0.0;	
		}
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
