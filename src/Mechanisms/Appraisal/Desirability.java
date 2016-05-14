package Mechanisms.Appraisal;

import Mechanisms.Collaboration.Collaboration.*;
import MentalState.*;
import MetaInformation.MentalProcesses;
import edu.wpi.cetask.Plan;

public class Desirability extends AppraisalProcesses{
	
	public enum DESIRABILITY {HIGH_DESIRABLE, DESIRABLE, NEUTRAL, HIGH_UNDESIRABLE, UNDESIRABLE, UNKNOWN};
	
	public Desirability(MentalProcesses mentalProcesses) {
		this.collaboration = mentalProcesses.getCollaborationMechanism();
	}
	
	public DESIRABILITY isEventDesirable(Goal eventGoal) {
		
		Goal topLevelGoal = collaboration.getTopLevelGoal();
		
		Plan eventGoalPlan    = eventGoal.getPlan();
		Plan topLevelGoalPlan = topLevelGoal.getPlan();
		
//		if(collaboration.getActualFocus() == null)
//			return DESIRABILITY.NEUTRAL;
		
		GOAL_STATUS topLevelGoalStatus = collaboration.getGoalStatus(topLevelGoalPlan);
		
		if (topLevelGoalStatus.equals(GOAL_STATUS.ACHIEVED)) 
			return DESIRABILITY.HIGH_DESIRABLE;
		else if (topLevelGoalStatus.equals(GOAL_STATUS.FAILED)) 
			return DESIRABILITY.HIGH_UNDESIRABLE;
		else if (topLevelGoalStatus.equals(GOAL_STATUS.BLOCKED) ||
				topLevelGoalStatus.equals(GOAL_STATUS.INAPPLICABLE) ||
				topLevelGoalStatus.equals(GOAL_STATUS.UNKNOWN))
			return DESIRABILITY.UNDESIRABLE;
		else if (topLevelGoalStatus.equals(GOAL_STATUS.PENDING) ||
				topLevelGoalStatus.equals(GOAL_STATUS.INPROGRESS)) {

			GOAL_STATUS eventGoalStatus = collaboration.getGoalStatus(eventGoalPlan);
			
			if (eventGoalStatus.equals(GOAL_STATUS.ACHIEVED)) 
				return DESIRABILITY.DESIRABLE;
			else if (eventGoalStatus.equals(GOAL_STATUS.FAILED)) 
				return DESIRABILITY.HIGH_UNDESIRABLE;
			else if (eventGoalStatus.equals(GOAL_STATUS.BLOCKED) ||
					eventGoalStatus.equals(GOAL_STATUS.INAPPLICABLE) ||
					eventGoalStatus.equals(GOAL_STATUS.UNKNOWN))
				return DESIRABILITY.UNDESIRABLE;
			else if (eventGoalStatus.equals(GOAL_STATUS.PENDING)) //) || (eventGoalStatus.equals(GOAL_STATUS.INPROGRESS))) { 
				return DESIRABILITY.NEUTRAL;
			else
				throw new IllegalArgumentException("Illegal Goal Status: " + eventGoalStatus);
		}
		else
			throw new IllegalArgumentException("Illegal Top Level Goal Status: " + topLevelGoalStatus);
	}
}
