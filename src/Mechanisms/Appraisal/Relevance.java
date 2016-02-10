package Mechanisms.Appraisal;

import java.util.List;

import Mechanisms.Collaboration.Collaboration.GOAL_STATUS;
import MentalState.Belief;
import MentalState.Goal;
import edu.wpi.cetask.Plan;

public class Relevance extends AppraisalProcesses {
	
	public enum RELEVANCE {RELEVANT, IRRELEVANT};
	
	public Relevance(String[] args) {
		super(args);
	}
	
	public RELEVANCE isEventRelevant(Goal eventGoal) {
		
		double eventUtility = getEventUtility(eventGoal);
		double emotionalThreshold = getEmotionalThreshold();
		
		if(emotionalThreshold <= Math.abs(eventUtility))
			return RELEVANCE.RELEVANT;
		else
			return RELEVANCE.IRRELEVANT;
	}
	
	private double getEventUtility(Goal eventGoal) { 
		
		int goalStatus           = getGoalStatus(eventGoal);
		int beliefPersistence    = getBeliefPersistence(getLastRelatedBelief(eventGoal));
		int beliefSaliency       = getBeliefSaliency(eventGoal); //Distance between live nodes 
		double saliencyMagnitude = getSaliencyMagnitude(eventGoal);
		
		if (saliencyMagnitude > 0)
			return goalStatus*beliefPersistence*Math.pow(beliefSaliency, saliencyMagnitude);
		else if(saliencyMagnitude == 0)
			return 0.0;
		else 
			return -2.0;
	}
	
	private double getEmotionalThreshold() { 
		
		double emotionValence = perception.getEmotionValence();
		
		return (emotionValence > 0) ? (1-emotionValence) : Math.abs(emotionValence); 
	}
	
	private int getBeliefPersistence(Belief goalBelief) {
		
		return goalBelief.getOccurrence();
	}
	
	private Belief getLastRelatedBelief(Goal goal) {
		
		return goal.getBeliefs().get(goal.getBeliefs().size()-1);
	}
	
	private int getBeliefSaliency(Goal goal) {
		
		return 0;
	}
	
	private int getGoalStatus(Goal goal) {
		
		GOAL_STATUS goalStatus = collaboration.getGoalStatus(goal.getPlan());
		if (goalStatus.equals(GOAL_STATUS.ACHIEVED) || 
			goalStatus.equals(GOAL_STATUS.PENDING) || 
			goalStatus.equals(GOAL_STATUS.INPROGRESS))
			return 1;
		else if (goalStatus.equals(GOAL_STATUS.FAILED) || 
				goalStatus.equals(GOAL_STATUS.BLOCKED) || 
				goalStatus.equals(GOAL_STATUS.INAPPLICABLE)) 
			return -1;
		else
			return 0;
	}
	
	private double getSaliencyMagnitude(Goal goal) {
		
		int preconditionKnownValue  = (collaboration.getPreConditionStatus(goal.getPlan()) != null) ? 1 : 0;
		int postconditionKnownValue = (collaboration.getPostConditionStatus(goal.getPlan()) != null) ? 1 : 0;
		int predecessorsGoalsKnownValue             = 0;
		int contributingGoalspredecessorsKnownValue = 0;
		
		List<Plan> predecessors      = collaboration.getPredecessors(goal);
		List<Plan> contributingGoals = collaboration.getContributingPlans(goal);

		int preconditionAllValue  = 1;
		int postconditionAllValue = 1;
		int predecessorsGoalsAllValue             = predecessors.size();
		int contributingGoalspredecessorsAllValue = contributingGoals.size();
		
		for(Plan predecessor : predecessors) {
			if (collaboration.getPreConditionStatus(predecessor) != null)
				predecessorsGoalsKnownValue++;
		}
		
		for(Plan contributingGoal : contributingGoals) {
			if (collaboration.getPostConditionStatus(contributingGoal) != null)
				contributingGoalspredecessorsKnownValue++;
		}
		
		int n = preconditionKnownValue + postconditionKnownValue + predecessorsGoalsKnownValue + contributingGoalspredecessorsKnownValue;
		int d = preconditionAllValue + postconditionAllValue + predecessorsGoalsAllValue + contributingGoalspredecessorsAllValue;
		
		int urgency    = getMotiveUrgency(goal);
		int importance = getMotiveImportance(goal);
		
		return (((float)n/d) + urgency + importance);
	}
	
	private int getMotiveUrgency(Goal goal) {
		
		return 0;
	}
	
	private int getMotiveImportance(Goal goal) {
		
		return 0;
	}
}