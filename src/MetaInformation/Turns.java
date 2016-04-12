package MetaInformation;

import java.util.ArrayList;

import Mechanisms.Appraisal.Controllability.CONTROLLABILITY;
import Mechanisms.Appraisal.Desirability.DESIRABILITY;
import Mechanisms.Appraisal.Expectedness.EXPECTEDNESS;

public class Turns {
	
	private static ArrayList<DESIRABILITY> desirabilities       = new ArrayList<DESIRABILITY>();
	private static ArrayList<CONTROLLABILITY> controllabilities = new ArrayList<CONTROLLABILITY>();
	private static ArrayList<EXPECTEDNESS> expectednesses       = new ArrayList<EXPECTEDNESS>();
	
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
	
	public ArrayList<CONTROLLABILITY> getControllabilities() {
		return this.controllabilities;
	}
	
	public ArrayList<EXPECTEDNESS> getExpectednesses() {
		return this.expectednesses;
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
	
	public double getControllabilityValue(CONTROLLABILITY controllabilitySymbolicValue) {
		
		switch (controllabilitySymbolicValue) {
			case CONTROLLABLE:
				return 1.0;
			case UNCONTROLLABLE:
				return 0.0;
			default:
				return -0.1;	
		}
	}
	
	public double getExpectednessValue(EXPECTEDNESS expectednessSymbolicValue) {
		
		switch (expectednessSymbolicValue) {
			case MOST_EXPECTED:
				return 1.0;
			case EXPECTED:
				return 0.8;
			case LESS_EXPECTED:
				return 0.6;
			case LESS_UNEXPECTED:
				return 0.4;
			case UNEXPECTED:
				return 0.2;
			case MOST_UNEXPECTED:
				return 0.0;
			default:
				return -0.1;	
		}
	}
}
