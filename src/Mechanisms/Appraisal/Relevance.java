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
		
		GOAL_STATUS goalStatus       = collaboration.getGoalStatus(goal.getPlan());
		List<Plan> predecessors      = collaboration.getPredecessors(goal);
		List<Plan> contributingGoals = collaboration.getContributingPlans(goal);
		
		for(Plan predecessor : predecessors) {
			// Check the status here!
		}
		
		for(Plan contributingGoal : contributingGoals) {
			// Check the status here!
		}
		
		return 1.0;
	}
}