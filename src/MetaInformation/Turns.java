package MetaInformation;

import java.util.ArrayList;

import Mechanisms.Appraisal.Controllability.CONTROLLABILITY;
import Mechanisms.Appraisal.Desirability.DESIRABILITY;
import Mechanisms.Appraisal.Expectedness.EXPECTEDNESS;
import Mechanisms.Appraisal.Relevance.RELEVANCE;
import MentalState.Goal;
import MetaInformation.AppraisalVector.WHOSE_APPRAISAL;

public class Turns {
	
	private static ArrayList<AppraisalVector> appraisalVectors = new ArrayList<AppraisalVector>();
	
	private static Turns turn = new Turns();
	
	private static int count;
	
	private Turns() {
		count = 1;
	}
	
	public static Turns getInstance() {
		return turn;
	}

	public static void setTurnAppraisals(MentalProcesses mentalProcesses, Goal eventGoal, WHOSE_APPRAISAL whoseAppraisal, RELEVANCE relevance, DESIRABILITY desirability, CONTROLLABILITY controllability, EXPECTEDNESS expectedness) {
		
		appraisalVectors.add(new AppraisalVector(mentalProcesses, eventGoal, getTurnNumber(), whoseAppraisal, relevance, desirability, expectedness, controllability));
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
	
	public ArrayList<AppraisalVector> getAppraisalVectors() {
		return this.appraisalVectors;
	}
	
	public void setDesirabilityValue(Goal goal, DESIRABILITY desirability) {
		
		for(int i = 0 ; i< appraisalVectors.size() ; i++) {
			if (appraisalVectors.get(i).getGoal().getLabel().equals(goal.getLabel())) {
				appraisalVectors.get(i).setDesirabilityValue(desirability);
				return;
			}
		}
	}
	
	public AppraisalVector getAppraisalVector(Goal goal) {
		
		for(AppraisalVector vector : appraisalVectors) {
			if (vector.getGoal().getLabel().equals(goal.getLabel()))
				return vector;
		}
		
		return null;
	}
	
	public ArrayList<AppraisalVector> getCurrentAppraisalVectors() {
		
		ArrayList<AppraisalVector> currentAppraisalVectors = new ArrayList<AppraisalVector>();
		
		for(AppraisalVector vector : appraisalVectors) {
			if (vector.getTurnNumber() == getTurnNumber())
				currentAppraisalVectors.add(vector);
		}
		
		return currentAppraisalVectors;
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
			case HIGH_CONTROLLABLE:
				return 1.0;
			case LOW_CONTROLLABLE:
				return 0.5;
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
