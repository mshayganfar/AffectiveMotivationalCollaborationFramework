package Mechanisms.Appraisal;

import edu.wpi.cetask.Plan;
import edu.wpi.cetask.TaskClass.Input;

import Mechanisms.Collaboration.Collaboration.FOCUS_TYPE;
import Mechanisms.Collaboration.Collaboration.GOAL_STATUS;
import MentalState.Goal;
import MentalState.Goal.DIFFICULTY;
import MentalState.MentalState;
import MentalState.Motive.MOTIVE_TYPE;
import MetaInformation.MentalProcesses;

public class Controllability extends AppraisalProcesses{

	public enum CONTROLLABILITY {CONTROLLABLE, UNCONTROLLABLE, UNKNOWN};
	
	public Controllability(MentalProcesses mentalProcesses) {
		this.collaboration = mentalProcesses.getCollaborationMechanism();
	}

	public CONTROLLABILITY isEventControllable(Goal eventGoal) {
		
		double dblAgency       			= getAgencyValue(eventGoal);
		double dblAutonomy     			= getAutonomyValue(eventGoal);
		double dblPredecessors 			= checkSucceededPredecessorsRatio(eventGoal);
		double dblInputs       			= checkAvailableInputRatio(eventGoal);
		double dblOverallGoalDifficulty = getOverallDifficultyValue(eventGoal);
		
		double controllabilityValue = (double)((dblAgency * getAgencyWeight()) + (dblAutonomy * getAutonomyWeight()) + 
						(dblPredecessors * getPredecessorRatioWeight()) + (dblInputs * getInputRatioWeight()) +
						(dblOverallGoalDifficulty * getGoalDifficultyWeight()))
						/(getAgencyWeight() + getAutonomyWeight() + getPredecessorRatioWeight() + getInputRatioWeight() + getGoalDifficultyWeight());
		
		if(controllabilityValue >= getHumanEmotionalThreshold())
			return CONTROLLABILITY.CONTROLLABLE;
		else
			return CONTROLLABILITY.UNCONTROLLABLE;
	}
	
	// Agency: The capacity, condition, or state of acting or of exerting power.
	private double getAgencyValue(Goal eventGoal) {
		
		if (collaboration.getGoalType(eventGoal).equals(FOCUS_TYPE.PRIMITIVE)) {
			if (!eventGoal.getActiveMotive().getMotiveType().equals(MOTIVE_TYPE.EXTERNAL))
				return 1.0;
			else
				return 0.0;
		}
		else {
			double motiveTypeSum = 0.0;
			int countMotives = 0;
			for (Plan plan : eventGoal.getPlan().getChildren()) {
				for (Goal goal : MentalState.getInstance().getGoals())
					if (goal.getPlan().getType().equals(plan.getType())) {
						if (!goal.getActiveMotive().getMotiveType().equals(MOTIVE_TYPE.EXTERNAL))
							motiveTypeSum++;
						countMotives++;
					}
			}
			return (double)motiveTypeSum/((countMotives == 0) ? 1 : countMotives);
		}
	}
	
	// Autonomy: The quality or state of being self-governing. Self-directing freedom or self-governing state.
	private double getAutonomyValue(Goal eventGoal) {

		double countSelfResponsible = 0;
		
		if (collaboration.getGoalType(eventGoal).equals(FOCUS_TYPE.PRIMITIVE)) {
			if (collaboration.getResponsibleAgent(eventGoal.getPlan()).equals(AGENT.SELF))
				return 1.0;
			else if (collaboration.getResponsibleAgent(eventGoal.getPlan()).equals(AGENT.OTHER))
				return -0.5;
			else  if (collaboration.getResponsibleAgent(eventGoal.getPlan()).equals(AGENT.UNKNOWN))
				return -1.0;
			else // Should never happen
				return 0.0;
		}
		else {
			collaboration.clearChildrenResponsibility();
			collaboration.getResponsibleAgent(eventGoal.getPlan());
			
			for (AGENT agent : collaboration.getChildrenResponsibility()) {
				if (agent.equals(AGENT.SELF))
					countSelfResponsible++;
				else if (agent.equals(AGENT.BOTH))
					countSelfResponsible += 0.5;
				else if (agent.equals(AGENT.OTHER))
					countSelfResponsible -= 0.5;
				else if (agent.equals(AGENT.UNKNOWN))
					countSelfResponsible -= 1.0;
			}
			
			return ((double)countSelfResponsible/((collaboration.getChildrenResponsibility().size() == 0) ? 1 : collaboration.getChildrenResponsibility().size()));
		}
	}
	
	private double checkSucceededPredecessorsRatio(Goal eventGoal) {
		
		double dblSucceededPredecessorCounter = 0.0;
		
		for (Plan plan : collaboration.getPredecessors(eventGoal)) {
			if (!collaboration.getGoalStatus(plan).equals(GOAL_STATUS.UNKNOWN))
				if (collaboration.getGoalStatus(plan).equals(GOAL_STATUS.ACHIEVED))
					dblSucceededPredecessorCounter++;
		}
		return (double)dblSucceededPredecessorCounter/((collaboration.getPredecessors(eventGoal).size() == 0) ? 1 : collaboration.getPredecessors(eventGoal).size());
	}
	
	private double checkAvailableInputRatio(Goal eventGoal) {
		
		double dblAvailableInputCounter = 0.0;
		
		for (Input input : collaboration.getInputs(eventGoal)) {
			if (!input.equals(null))
				if(collaboration.isInputAvailable(eventGoal, input))
					dblAvailableInputCounter++;
		}
		return ((double)dblAvailableInputCounter/((collaboration.getInputs(eventGoal).size() == 0) ? 1 : collaboration.getInputs(eventGoal).size()));
		
	}
	
	private double getOverallDifficultyValue(Goal eventGoal) {
		
		double dblOverallDifficultyValue = 0.0;
		
		if (collaboration.getGoalType(eventGoal).equals(FOCUS_TYPE.PRIMITIVE)) {
			switch (DIFFICULTY.valueOf(eventGoal.getPlan().getType().getProperty("@difficulty"))) {
				case NORMAL:
					return 0.0;
				case DIFFICULT:
					return 0.5;
				case MOST_DIFFICULT:
					return 1.0;
				default:
					throw new IllegalStateException("Difficulty value: " + DIFFICULTY.valueOf(eventGoal.getPlan().getType().getProperty("@difficulty")));
			}
		}
		else {
			double goalDifficultySum = 0.0;
			
			switch (DIFFICULTY.valueOf(eventGoal.getPlan().getType().getProperty("@difficulty"))) {
				case NORMAL:
					goalDifficultySum = 0.0;
					break;
				case DIFFICULT:
					goalDifficultySum = 0.5;
					break;
				case MOST_DIFFICULT:
					goalDifficultySum = 1.0;
					break;
				default:
					throw new IllegalStateException("Difficulty value: " + DIFFICULTY.valueOf(eventGoal.getPlan().getType().getProperty("@difficulty")));
			}
			
			for (Plan plan : eventGoal.getPlan().getChildren()) {
				switch (DIFFICULTY.valueOf(plan.getType().getProperty("@difficulty"))) {
					case NORMAL:
						goalDifficultySum += 0.0;
						break;
					case DIFFICULT:
						goalDifficultySum += 0.5;
						break;
					case MOST_DIFFICULT:
						goalDifficultySum += 1.0;
						break;
					default:
						throw new IllegalStateException("Difficulty value: " + DIFFICULTY.valueOf(eventGoal.getPlan().getType().getProperty("@difficulty")));
				}
			}
		}
		return dblOverallDifficultyValue;
	}
	
	// Min = 0.0 and Max = 1.0
	private double getAgencyWeight()           { return 1.0; }
	private double getAutonomyWeight()         { return 1.0; }
	private double getPredecessorRatioWeight() { return 1.0; }
	private double getInputRatioWeight()       { return 1.0; }
	private double getGoalDifficultyWeight()   { return 1.0; }
}
