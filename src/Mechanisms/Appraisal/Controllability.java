package Mechanisms.Appraisal;

import java.util.List;

import edu.wpi.cetask.Plan;
import edu.wpi.cetask.TaskClass.Input;
import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Collaboration.Collaboration.AGENT;
import Mechanisms.Collaboration.Collaboration.FOCUS_TYPE;
import MentalState.Goal;
import MentalState.MentalState;
import MentalState.Motive.MOTIVE_TYPE;

public class Controllability extends AppraisalProcesses{

	public Controllability(Collaboration collaboration) {
		this.collaboration = collaboration;
	}

	public boolean isEventControllable(Goal eventGoal) {
		
		double dblAgency       = getAgencyValue(eventGoal);
		double dblAutonomy     = getAutonomyValue(eventGoal);
		double dblPredecessors = checkSucceededPredecessorsRatio(eventGoal);
		double dblInputs       = checkAvailableInputRatio(eventGoal);
		
		double utilityValue = (double)((dblAgency * getAgencyWeight()) + (dblAutonomy * getAutonomyWeight()) + 
						(dblPredecessors * getPredecessorRatioWeight()) + (dblInputs * getInputRatioWeight()))
						/(getAgencyWeight() + getAutonomyWeight() + getPredecessorRatioWeight() + getInputRatioWeight());
		
		if(utilityValue >= getHumanEmotionalThreshold())
			return true;
		else
			return false;
	}
	
	// Agency: The capacity, condition, or state of acting or of exerting power.
	public double getAgencyValue(Goal eventGoal) {
		
		if (collaboration.getGoalType(eventGoal).equals(FOCUS_TYPE.PRIMITIVE)) {
			if (eventGoal.getActiveMotive().getMotiveType().equals(MOTIVE_TYPE.INTERNAL))
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
						if (goal.getActiveMotive().getMotiveType().equals(MOTIVE_TYPE.INTERNAL))
							motiveTypeSum++;
						countMotives++;
					}
			}
			return (double)motiveTypeSum/((countMotives == 0) ? 1 : countMotives);
		}
	}
	
	// Autonomy: The quality or state of being self-governing. Self-directing freedom or self-governing state.
	public double getAutonomyValue(Goal eventGoal) {

		double countSelfResponsible = 0;
		
		if (collaboration.getGoalType(eventGoal).equals(FOCUS_TYPE.PRIMITIVE)) {
			if (collaboration.getResponsibleAgent(eventGoal).equals(AGENT.SELF))
				return 1.0;
			else
				return 0.0;
		}
		else {
			for (Plan plan : eventGoal.getPlan().getChildren()) {
				if (collaboration.getResponsibleAgent(plan).equals(AGENT.SELF))
					countSelfResponsible++;
				else if (collaboration.getResponsibleAgent(plan).equals(AGENT.BOTH))
					countSelfResponsible += 0.5;
			}
			
			return ((double)countSelfResponsible/((eventGoal.getPlan().getChildren().size() == 0) ? 1 : eventGoal.getPlan().getChildren().size()));
		}
	}
	
	private Double checkSucceededPredecessorsRatio(Goal eventGoal) {
		
		double dblSucceededPredecessorCounter = 0.0;
		
//		Goal eventGoal = event.getEventRelatedGoal(mentalState);
		
		List<Plan> predecessorGoalList = collaboration.getPredecessors(eventGoal);
		
		if(predecessorGoalList.size() > 0) {
			for (int i = 0; i < predecessorGoalList.size() ; i++) {
				if(collaboration.isPlanAchieved(predecessorGoalList.get(i)))
					dblSucceededPredecessorCounter++;
			}
			
			return (double)dblSucceededPredecessorCounter/predecessorGoalList.size();
		}
		else
			return dblSucceededPredecessorCounter;
	}
	
	private Double checkAvailableInputRatio(Goal eventGoal) {
		
		double dblAvailableInputCounter = 0.0;
		
//		Goal eventGoal = event.getEventRelatedGoal(mentalState);
		
		List<Input> goalInputsList = collaboration.getInputs(eventGoal);
		
		if(goalInputsList.size() > 0) {
			for (int i = 0; i < goalInputsList.size() ; i++) {
				if (!goalInputsList.get(i).equals(null))
					if(collaboration.isInputAvailable(eventGoal, goalInputsList.get(i)))
						dblAvailableInputCounter++;
			}
			return ((double)dblAvailableInputCounter/goalInputsList.size());
		}
		else
			return 1.0;
		
	}
	
	// Min = 0.0 and Max = 1.0
	private double getAgencyWeight()           { return 1.0; }
	private double getAutonomyWeight()         { return 1.0; }
	private double getPredecessorRatioWeight() { return 1.0; }
	private double getInputRatioWeight()       { return 1.0; }
}
