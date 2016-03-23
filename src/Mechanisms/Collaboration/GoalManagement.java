package Mechanisms.Collaboration;

import java.util.HashMap;
import java.util.Map;

import MentalState.Goal;

public class GoalManagement {

	public double computeCostValue(Goal eventGoal) {
		
		double costValue = 0.0;
		
		double goalPoximity    = getGoalProximity(eventGoal);
		double goalDifficulty  = getGoalDifficulty(eventGoal);
		double goalSpecificity = getGoalSpecificity(eventGoal);
		
		Map<String, Double> weights = getGoalAttributesWeights(eventGoal);
		
		double base = ((goalPoximity * weights.get("proximity")) + (goalDifficulty * weights.get("difficulty")) + (((double)1/(goalSpecificity + 1)) * weights.get("specificity")));
		
		double exponent = getGammaValue(eventGoal);
		
		costValue = Math.pow(base, exponent);
		
		return costValue;
	}
	
	private double getGoalProximity(Goal eventGoal) {
		
		return 0.0;
	}
	
	private double getGoalDifficulty(Goal eventGoal) {
		
		return 0.0;
	}

	private double getGoalSpecificity(Goal eventGoal) {
	
		return 0.0;
	}
	
	private double getGammaValue(Goal eventGoal) {
		
		return 0.0;
	}
	
	private Map<String, Double> getGoalAttributesWeights(Goal eventGoal) {
		
		Map<String, Double> weights = new HashMap<String, Double>();
		
		weights.put("proximity", 1.0);
		weights.put("difficulty", 1.0);
		weights.put("specificity", 1.0);
		
		return weights;
	}
}
