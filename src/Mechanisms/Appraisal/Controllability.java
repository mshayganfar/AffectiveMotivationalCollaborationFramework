package Mechanisms.Appraisal;

import java.util.List;

import edu.wpi.cetask.Plan;
import edu.wpi.cetask.TaskClass.Input;
import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Collaboration.Collaboration.AGENT;
import MentalState.Goal;

public class Controllability extends AppraisalProcesses{

	public Controllability(Collaboration collaboration) {
		super(collaboration);
		// TODO Auto-generated constructor stub
	}

	public boolean isEventControllable(Goal eventGoal) {
		
		double dblAgency       = getAgencyValue();
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
	private Double getAgencyValue() {
		
		return 0.0;
	}
	
	// Autonomy: The quality or state of being self-governing. Self-directing freedom or self-governing state.
	private Double getAutonomyValue(Goal eventGoal) {
		// Add predecessors beside the contributers.
		double dblSelfCounter = 0;
		
//		Goal eventGoal = event.getEventRelatedGoal(mentalState); //Should be changed to recogniseGoal()
		
		List<Goal> taskContributersList = collaboration.getContributingGoals(eventGoal.getPlan());
		
		for (int i = 0; i < taskContributersList.size() ; i++)
			if(collaboration.getResponsibleAgent(taskContributersList.get(i)).equals(AGENT.SELF))
				dblSelfCounter++;
		
		return ((double)dblSelfCounter/taskContributersList.size());
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
