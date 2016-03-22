package Mechanisms.Appraisal;

import edu.wpi.cetask.Plan;
import edu.wpi.cetask.TaskClass.Input;
import Mechanisms.Collaboration.Collaboration;
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
	
	public Double checkSucceededPredecessorsRatio(Goal eventGoal) {
		
		double dblSucceededPredecessorCounter = 0.0;
		
		for (Plan plan : collaboration.getPredecessors(eventGoal)) {
			if(collaboration.isPlanAchieved(plan)) // Check this with Chuck!
				dblSucceededPredecessorCounter++;
		}
		
		return (double)dblSucceededPredecessorCounter/((collaboration.getPredecessors(eventGoal).size() == 0) ? 1 : collaboration.getPredecessors(eventGoal).size());
	}
	
	public Double checkAvailableInputRatio(Goal eventGoal) {
		
		double dblAvailableInputCounter = 0.0;
		
		for (Input input : collaboration.getInputs(eventGoal)) {
			if (!input.equals(null))
				if(collaboration.isInputAvailable(eventGoal, input))
					dblAvailableInputCounter++;
		}
		return ((double)dblAvailableInputCounter/((collaboration.getInputs(eventGoal).size() == 0) ? 1 : collaboration.getInputs(eventGoal).size()));
		
	}
	
	// Min = 0.0 and Max = 1.0
	private double getAgencyWeight()           { return 1.0; }
	private double getAutonomyWeight()         { return 1.0; }
	private double getPredecessorRatioWeight() { return 1.0; }
	private double getInputRatioWeight()       { return 1.0; }
}
