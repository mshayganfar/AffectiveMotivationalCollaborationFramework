package Mechanisms.Appraisal;

import MentalState.Goal;

public class Relevance extends AppraisalProcesses {
	
	public enum RELEVANCE {RELEVANT, IRRELEVANT};
	
	public RELEVANCE isEventRelevant(Goal eventGoal) {
		
		double eventUtility = getEventUtility();
		if(eventUtility > getHumanEmotionalThreshold())
			return RELEVANCE.RELEVANT;
		else
			return RELEVANCE.IRRELEVANT;
	}
	
	private double getEventUtility() { return 0.0; }
}