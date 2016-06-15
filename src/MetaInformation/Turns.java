package MetaInformation;

import java.util.ArrayList;

import Mechanisms.Appraisal.Controllability.CONTROLLABILITY;
import Mechanisms.Appraisal.Desirability.DESIRABILITY;
import Mechanisms.Appraisal.Expectedness.EXPECTEDNESS;
import Mechanisms.Appraisal.Relevance.RELEVANCE;
import MentalState.Goal;
import MetaInformation.AppraisalVector.APPRAISAL_TYPE;
import MetaInformation.AppraisalVector.EMOTION_INSTANCE;

public class Turns {
	
	public enum WHOSE_TURN {USER, AGENT, AUTO};
	
	private static ArrayList<AppraisalVector> appraisalVectors = new ArrayList<AppraisalVector>();
	
	private static Turns turn = new Turns();
	
	private static int count;
	
	private Turns() {
		count = 1;
	}
	
	public static Turns getInstance() {
		return turn;
	}

	public static void setTurnAppraisals(MentalProcesses mentalProcesses, Goal eventGoal, APPRAISAL_TYPE appraisalType, RELEVANCE relevance, DESIRABILITY desirability, CONTROLLABILITY controllability, EXPECTEDNESS expectedness) {
		
		appraisalVectors.add(new AppraisalVector(mentalProcesses, eventGoal, getTurnNumber(), appraisalType, relevance, desirability, expectedness, controllability));
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
	
	public EMOTION_INSTANCE getTurnHumanEmotion(Goal goal) {
		
		for(AppraisalVector vector : appraisalVectors) {
			if (vector.getGoal().getLabel().equals(goal.getLabel()))
				if (vector.getAppraisalType().equals(APPRAISAL_TYPE.REVERSE_APPRAISAL))
					if (vector.getTurnNumber() == Turns.getInstance().getTurnNumber())
						return vector.getEmotionInstance();
		}
		return null;
	}
	
	public AppraisalVector getAppraisalVector(Goal goal) {
		
		for(AppraisalVector vector : appraisalVectors) {
			if (vector.getGoal().getLabel().equals(goal.getLabel()))
				if (!vector.getAppraisalType().equals(APPRAISAL_TYPE.REVERSE_APPRAISAL))
					if (vector.getTurnNumber() == Turns.getInstance().getTurnNumber())
						return vector;
		}
		return null;
	}
	
	public ArrayList<AppraisalVector> getTurnAppraisalVectors(int turnNumber) {
		
		ArrayList<AppraisalVector> turnAppraisalVectors = new ArrayList<AppraisalVector>();
		
		for(AppraisalVector vector : appraisalVectors)
			if (vector.getTurnNumber() == turnNumber)
				if (!vector.getAppraisalType().equals(APPRAISAL_TYPE.REVERSE_APPRAISAL))
					turnAppraisalVectors.add(vector);
		
		return (turnAppraisalVectors.size() == 0) ? null : turnAppraisalVectors;
	}
	
	public ArrayList<AppraisalVector> getCurrentAppraisalVectors() {
		
		ArrayList<AppraisalVector> currentAppraisalVectors = new ArrayList<AppraisalVector>();
		
		for(AppraisalVector vector : appraisalVectors) {
			if (vector.getTurnNumber() == getTurnNumber())
				currentAppraisalVectors.add(vector);
		}
		
		return currentAppraisalVectors;
	}
	
	public double getTurnOverallDesirabilityValue(ArrayList<AppraisalVector> turnAppraisalVectors) {
		
		double overallDesirabilityValue = 0.0;
		
		for (int i = 0 ; i < turnAppraisalVectors.size() ; i++) {
			overallDesirabilityValue += getDesirabilityValue(turnAppraisalVectors.get(i).getDesirabilitySymbolicValue());
		}
		
		if (turnAppraisalVectors.size() == 0) 
			return 0.0; // This might bias agent's behavior.
		else {
			overallDesirabilityValue = ((double)overallDesirabilityValue)/turnAppraisalVectors.size();
			
			if (overallDesirabilityValue >= 0.6)
				return 1.0;
			else if ((overallDesirabilityValue >= 0.2) && (overallDesirabilityValue < 0.6))
				return 0.5;
			else if ((overallDesirabilityValue > -0.2) && (overallDesirabilityValue < 0.2))
				return 0.0;
			else if ((overallDesirabilityValue <= -0.2) && (overallDesirabilityValue > -0.6))
				return -0.5;
			else if (overallDesirabilityValue <= -0.6)
				return -1.0;
			else
				throw new IllegalArgumentException("Illegal Overall Desirability Value: " + overallDesirabilityValue);
		}
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
				throw new IllegalArgumentException("Illegal Desirability Symbolic Value: " + desirabilitySymbolicValue);	
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
				throw new IllegalArgumentException("Illegal Controllability Symbolic Value: " + controllabilitySymbolicValue);
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
				throw new IllegalArgumentException("Illegal Expectedness Symbolic Value: " + expectednessSymbolicValue);	
		}
	}
}
