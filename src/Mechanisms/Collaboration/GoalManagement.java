package Mechanisms.Collaboration;

import java.util.HashMap;
import java.util.Map;

import Mechanisms.Appraisal.Controllability;
import Mechanisms.Appraisal.Desirability;
import Mechanisms.Appraisal.Relevance;
import Mechanisms.Appraisal.Controllability.CONTROLLABILITY;
import Mechanisms.ToM.ToM;
import MentalState.Goal;
import MetaInformation.MentalProcesses;

public class GoalManagement {
	
	private Collaboration collaboration;
	private ToM tom;
	private Relevance relevance;
	private Desirability desirability;
	private Controllability controllability;
	
	public GoalManagement(MentalProcesses mentalProcesses) {
		this.collaboration   = mentalProcesses.getCollaborationMechanism();
		this.tom             = mentalProcesses.getToMMechanism();
		this.relevance 	     = mentalProcesses.getRelevanceProcess();
		this.desirability    = mentalProcesses.getDesirabilityProcess();
		this.controllability = mentalProcesses.getControllabilityProcess();
	}

	public double computeCostValue(Goal disengagedGoal, Goal eventGoal) {
		
		double goalProximity   = eventGoal.getGoalProximity();
		double goalDifficulty  = eventGoal.getGoalDifficulty();
		double goalSpecificity = eventGoal.getGoalSpecificity();
		
		Map<String, Double> weights = getGoalAttributesWeights(eventGoal);
		
		double base      = ((goalProximity * weights.get("proximity")) + (goalDifficulty * weights.get("difficulty")) + (((double)1/(goalSpecificity + 1)) * weights.get("specificity")));
		double exponent  = getGammaValue(disengagedGoal, eventGoal, base, 1.0, 1.0);
		double costValue = Math.pow(base, exponent);
		
//		System.out.println("Base --------------> " + base);
//		System.out.println("Exponent ----------> " + exponent);
//		System.out.println("Overall Cost-------> " + costValue);
		
		return costValue;
	}
	
	private double getGammaValue(Goal disengagedGoal, Goal eventGoal, double base, double alpha, double beta) {
		
		double relevance              = getRelevanceValue(eventGoal);
		double controllability        = getControllabilityValue(eventGoal, false);
		double reverseControllability = getControllabilityValue(eventGoal, true);
		
		return (double)1.0/((relevance * (alpha*controllability + beta*reverseControllability)) + 1);
		
//		if (disengagedGoal.getPlan().getParent().getGoal().getType().equals(eventGoal.getPlan().getParent().getGoal().getType()))
//			return (double)1.0/((relevance * (alpha*controllability + beta*reverseControllability)) + 1);
//		else
//			return (1-((double)1.0/((relevance * (alpha*controllability + beta*reverseControllability)) + 1)));
		
//		System.out.println("C_r: " + controllability + " ,C_h: " + reverseControllability);
		
//		if (base >= 1.0)
//			return (double)1.0/((relevance * (alpha*controllability + beta*reverseControllability)) + 1);
//		else
//			return (1-((double)1.0/((relevance * (alpha*controllability + beta*reverseControllability)) + 1)));
	}
	
	private double getRelevanceValue(Goal eventGoal) {
		
		return relevance.getEventRelevanceValue(eventGoal);
//		switch(relevance.isEventRelevant(eventGoal)) {
//			case RELEVANT:
//				return 1.0;
//			case IRRELEVANT:
//				return 0.0;
//			default:
//				throw new IllegalArgumentException("Illegal Relevance Value: " + relevance.isEventRelevant(eventGoal));
//		}
	}
	
	private double getControllabilityValue(Goal eventGoal, boolean human) {
		
		CONTROLLABILITY controllabilitySymbol;
		
		if (!human)
			controllabilitySymbol = controllability.isEventControllable(eventGoal);
		else
			controllabilitySymbol = tom.getReverseControllability(eventGoal);

		switch(controllabilitySymbol) {
			case HIGH_CONTROLLABLE:
				return 1.0;
			case LOW_CONTROLLABLE:
				return 0.5;
			case UNCONTROLLABLE:
				return 0.0;
			default:
				throw new IllegalArgumentException("Illegal Controllability Value: " + controllability.isEventControllable(eventGoal));
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
