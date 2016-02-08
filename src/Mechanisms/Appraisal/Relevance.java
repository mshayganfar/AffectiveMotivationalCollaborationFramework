package Mechanisms.Appraisal;

import MentalState.Belief;
import MentalState.Goal;

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
		
		int beliefPersistence = getBeliefPersistence(getLastRelatedBelief(eventGoal));
		System.out.println(beliefPersistence);
		
		return 0.0; 
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
}