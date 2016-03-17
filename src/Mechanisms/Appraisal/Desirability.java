package Mechanisms.Appraisal;

import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Collaboration.Collaboration.*;
import MentalState.*;
import edu.wpi.cetask.Plan;

public class Desirability extends AppraisalProcesses{
	
	public Desirability(Collaboration collaboration) {
		super(collaboration);
		// TODO Auto-generated constructor stub
	}
	public enum DESIRABILITY {HIGH_DESIRABLE, DESIRABLE, NEUTRAL, HIGH_UNDESIRABLE, UNDESIRABLE};
	
	public DESIRABILITY isEventDesirable(Goal eventGoal) {
		
		Goal topLevelGoal = collaboration.getTopLevelGoal();
		
		Plan graphGoalPlan    = eventGoal.getPlan();
		Plan topLevelGoalPlan = topLevelGoal.getPlan();
		
		if(collaboration.recognizeGoal() == null)
			return DESIRABILITY.NEUTRAL;
		
		if (collaboration.getGoalStatus(topLevelGoalPlan).equals(GOAL_STATUS.ACHIEVED)) return DESIRABILITY.HIGH_DESIRABLE;
		else if (collaboration.getGoalStatus(topLevelGoalPlan).equals(GOAL_STATUS.FAILED)) return DESIRABILITY.HIGH_UNDESIRABLE;
		else if ((collaboration.getGoalStatus(topLevelGoalPlan).equals(GOAL_STATUS.BLOCKED)) ||
				(collaboration.getGoalStatus(topLevelGoalPlan).equals(GOAL_STATUS.INAPPLICABLE))) return DESIRABILITY.UNDESIRABLE;
		else if ((collaboration.getGoalStatus(topLevelGoalPlan).equals(GOAL_STATUS.PENDING)) ||
				(collaboration.getGoalStatus(topLevelGoalPlan).equals(GOAL_STATUS.INPROGRESS))) {

				if (collaboration.getGoalStatus(graphGoalPlan).equals(GOAL_STATUS.ACHIEVED)) return DESIRABILITY.DESIRABLE; //postcondition
				else if (collaboration.getGoalStatus(graphGoalPlan).equals(GOAL_STATUS.FAILED)) return DESIRABILITY.HIGH_UNDESIRABLE; //postcondition
				else if ((collaboration.getGoalStatus(graphGoalPlan).equals(GOAL_STATUS.BLOCKED)) ||
						(collaboration.getGoalStatus(graphGoalPlan).equals(GOAL_STATUS.INAPPLICABLE))) return DESIRABILITY.UNDESIRABLE; //predecessors & preconditions
				else if ((collaboration.getGoalStatus(graphGoalPlan).equals(GOAL_STATUS.PENDING)) ||
						(collaboration.getGoalStatus(topLevelGoalPlan).equals(GOAL_STATUS.INPROGRESS))) { return DESIRABILITY.NEUTRAL; //live
				}
		}
		
		return DESIRABILITY.NEUTRAL;
	}
	
	private double getUtteranceUtilityWeight() { return 1.0; }
	private double getActionUtilityWeight()    { return 1.0; }
	private double getEmotionUtilityWeight()   { return 1.0; }
}
