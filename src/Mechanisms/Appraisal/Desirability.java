package Mechanisms.Appraisal;

import java.util.ArrayList;

import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Collaboration.Collaboration.*;
import MentalState.*;
import MetaInformation.GoalTree;
import MetaInformation.Node;
import edu.wpi.cetask.Plan;

public class Desirability extends AppraisalProcesses{
	
	public Desirability(Collaboration collaboration) {
		this.collaboration = collaboration;
	}
	public enum DESIRABILITY {HIGH_DESIRABLE, DESIRABLE, NEUTRAL, HIGH_UNDESIRABLE, UNDESIRABLE};
	
	public DESIRABILITY isEventDesirable(Goal eventGoal) {
		
		Goal topLevelGoal = collaboration.getTopLevelGoal();
		
		Plan eventGoalPlan    = eventGoal.getPlan();
		Plan topLevelGoalPlan = topLevelGoal.getPlan();
		
		if(collaboration.getActualFocus(eventGoal.getPlan()) == null)
			return DESIRABILITY.NEUTRAL;
		
		System.out.println(">>> Top Level Goal Status: " + collaboration.getGoalStatus(topLevelGoalPlan));
		
		GOAL_STATUS topLevelGoalStatus= collaboration.getGoalStatus(topLevelGoalPlan);
		
		if (topLevelGoalStatus.equals(GOAL_STATUS.ACHIEVED)) 
			return DESIRABILITY.HIGH_DESIRABLE;
		else if (topLevelGoalStatus.equals(GOAL_STATUS.FAILED)) 
			return DESIRABILITY.HIGH_UNDESIRABLE;
		else if ((topLevelGoalStatus.equals(GOAL_STATUS.BLOCKED))) /*||
				(collaboration.getGoalStatus(topLevelGoalPlan).equals(GOAL_STATUS.INAPPLICABLE)))*/ 
			return DESIRABILITY.UNDESIRABLE;
		else if ((topLevelGoalStatus.equals(GOAL_STATUS.PENDING)) || (topLevelGoalStatus.equals(GOAL_STATUS.INPROGRESS))) {

			GOAL_STATUS eventGoalStatus= collaboration.getGoalStatus(topLevelGoalPlan);
			
			System.out.println(">>> Event Goal Status: " + collaboration.getGoalStatus(eventGoalPlan));
			
			if (eventGoalStatus.equals(GOAL_STATUS.ACHIEVED)) 
				return DESIRABILITY.DESIRABLE; //postcondition
			else if (eventGoalStatus.equals(GOAL_STATUS.FAILED)) 
				return DESIRABILITY.HIGH_UNDESIRABLE; //postcondition
			else if ((eventGoalStatus.equals(GOAL_STATUS.BLOCKED))) /*||
					(collaboration.getGoalStatus(eventGoalPlan).equals(GOAL_STATUS.INAPPLICABLE)))*/ 
				return DESIRABILITY.UNDESIRABLE; //predecessors & preconditions
			else if ((eventGoalStatus.equals(GOAL_STATUS.PENDING)) || (eventGoalStatus.equals(GOAL_STATUS.INPROGRESS))) { 
				return DESIRABILITY.NEUTRAL; //live
			}
		}
		
		return DESIRABILITY.NEUTRAL;
	}
	
	private double getUtteranceUtilityWeight() { return 1.0; }
	private double getActionUtilityWeight()    { return 1.0; }
	private double getEmotionUtilityWeight()   { return 1.0; }
}
