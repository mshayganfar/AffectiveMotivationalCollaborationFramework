package Mechanisms.Collaboration;

import java.util.HashMap;
import java.util.Map;

import Mechanisms.Appraisal.Desirability;
import Mechanisms.Appraisal.Relevance;
import MentalState.Goal;
import MetaInformation.MentalProcesses;

public class GoalManagement {
	
	private Collaboration collaboration;
	private Relevance relevance;
	private Desirability desirability;
	
	public GoalManagement(MentalProcesses mentalProcesses) {
		this.collaboration = mentalProcesses.getCollaborationMechanism();
		this.relevance 	   = mentalProcesses.getRelevanceProcess();
		this.desirability  = mentalProcesses.getDesirabilityProcess();
	}

	public double computeCostValue(Goal eventGoal) {
		
		double costValue = 0.0;
		
		int goalPoximity       = eventGoal.getGoalProximity();
		double goalDifficulty  = eventGoal.getGoalDifficulty();
		double goalSpecificity = eventGoal.getGoalSpecificity();
		
		Map<String, Double> weights = getGoalAttributesWeights(eventGoal);
		
		double base = ((goalPoximity * weights.get("proximity")) + (goalDifficulty * weights.get("difficulty")) + (((double)1/(goalSpecificity + 1)) * weights.get("specificity")));
		
		double exponent = getGammaValue(eventGoal, 3, 2);
		
		costValue = Math.pow(base, exponent);
		
		return costValue;
	}
	
	private double getGammaValue(Goal eventGoal, double C, double alpha) {
		
		double relevance    = getRelevanceValue(eventGoal);
		double desirability = getDesirabilityValue(eventGoal);
		
		double reverseRelevance    = getReverseRelevanceValue(eventGoal);
		double reverseDesirability = getReverseDesirabilityValue(eventGoal);
		
		return -C*(((relevance + 1) * desirability) + (alpha * (reverseRelevance + 1) * reverseDesirability));
	}
	
	private double getRelevanceValue(Goal eventGoal) {
		
		switch(relevance.isEventRelevant(eventGoal)) {
			case RELEVANT:
				return 1.0;
			case IRRELEVANT:
				return 0.0;
			default:
				throw new IllegalArgumentException("Illegal Relevance Value: " + relevance.isEventRelevant(eventGoal));
		}
	}
	
	private double getDesirabilityValue(Goal eventGoal) {
		
		switch(desirability.isEventDesirable(eventGoal)) {
			case HIGH_DESIRABLE:
				return 1.0;
			case DESIRABLE:
				return 0.5;
			case NEUTRAL:
				return 0.0;
			case UNDESIRABLE:
				return -0.5;
			case HIGH_UNDESIRABLE:
				return -1.0;
			default:
				throw new IllegalArgumentException("Illegal Desirability Value: " + desirability.isEventDesirable(eventGoal));
		}
	}
	
	private double getReverseRelevanceValue(Goal eventGoal) {
		return 1.0; // This should get its value using reverse appraisal in ToM.
	}
	
	private double getReverseDesirabilityValue(Goal eventGoal) {
		return 0.0; // This should get its value using reverse appraisal in ToM.
	}
	
	private Map<String, Double> getGoalAttributesWeights(Goal eventGoal) {
		
		Map<String, Double> weights = new HashMap<String, Double>();
		
		weights.put("proximity", 1.0);
		weights.put("difficulty", 1.0);
		weights.put("specificity", 1.0);
		
		return weights;
	}
}
