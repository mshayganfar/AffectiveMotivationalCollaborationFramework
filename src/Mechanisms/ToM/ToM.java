package Mechanisms.ToM;

import Mechanisms.Mechanisms;
import Mechanisms.Appraisal.Controllability;
import Mechanisms.Appraisal.Desirability;
import Mechanisms.Appraisal.Expectedness;
import Mechanisms.Appraisal.Relevance;
import Mechanisms.Appraisal.Controllability.CONTROLLABILITY;
import Mechanisms.Appraisal.Coping;
import Mechanisms.Appraisal.Desirability.DESIRABILITY;
import Mechanisms.Appraisal.Expectedness.EXPECTEDNESS;
import Mechanisms.Appraisal.Relevance.RELEVANCE;
import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Motivation.Motivation;
import MentalState.Goal;
import MetaInformation.AppraisalVector;
import MetaInformation.AppraisalVector.EMOTION_INSTANCE;
import MetaInformation.MentalProcesses;

public class ToM extends Mechanisms{
	
	private MentalProcesses mentalProcesses;
	
	private Collaboration collaboration;
	private Motivation motivation;
	private Coping coping;
	
	private Relevance relevance;
	private Controllability controllability;
	private Desirability desirability;
	private Expectedness expectedness;
	
	private double valence = 0.0;
	
	public void prepareAppraisalsOfToM(MentalProcesses mentalProcesses) {
		this.collaboration = mentalProcesses.getCollaborationMechanism();
		this.motivation    = mentalProcesses.getMotivationMechanism();
		this.coping		   = mentalProcesses.getCopingMechanism();
		
		this.relevance       = mentalProcesses.getRelevanceProcess();
		this.controllability = mentalProcesses.getControllabilityProcess();
		this.desirability    = mentalProcesses.getDesirabilityProcess();
		this.expectedness    = mentalProcesses.getExpectednessProcess();
	}
	
	public AppraisalVector getReverseAppraisalValues(Goal eventGoal) {
		
		return doReverseAppraisal(getValenceValue(), eventGoal);
	}
	
	public EMOTION_INSTANCE getAnticipatedHumanEmotion(Goal eventGoal) {
		
		AppraisalVector reverseAppraisalVector = doReverseAppraisal(getValenceValue(), eventGoal);
		
		return reverseAppraisalVector.getEmotionInstance();
	}
	
	private AppraisalVector doReverseAppraisal(double valenceValue, Goal eventGoal) {
		
		AppraisalVector estimatedAppraisalVector = new AppraisalVector(mentalProcesses, eventGoal);
		
		estimatedAppraisalVector.setRelevanceValue(relevance.isEventRelevant(eventGoal));
		estimatedAppraisalVector.setDesirabilityValue(getDesirabilityUsingValence(valenceValue));
		estimatedAppraisalVector.setExpectednessValue(expectedness.isEventExpected(eventGoal));
		estimatedAppraisalVector.setControllabilityValue(controllability.isEventControllable(eventGoal));
		
		return estimatedAppraisalVector;
	}
	
	public DESIRABILITY getDesirabilityUsingValence(double valence) {
		
		if ((valence > 0.1) && (valence < 0.45))
			return DESIRABILITY.DESIRABLE;
		else if ((valence >= 0.45) && (valence <= 1.0))
			return DESIRABILITY.HIGH_DESIRABLE;
		else if ((valence > -0.45) && (valence < -0.1))
			return DESIRABILITY.UNDESIRABLE;
		else if ((valence >= -1.0) && (valence <= -0.45))
			return DESIRABILITY.HIGH_UNDESIRABLE;
		else if ((valence >= -0.1) && (valence <= 0.1))
			return DESIRABILITY.NEUTRAL;
		else
			throw new IllegalArgumentException("Valence Value: " + valence);
	}
	
	public double getValenceValue() {
		return valence;
	}
	
	public RELEVANCE isEventRelevant(Goal eventGoal) {
		return relevance.isEventRelevant(eventGoal);
	}
	
	public CONTROLLABILITY isEventControllable(Goal eventGoal) {
		return controllability.isEventControllable(eventGoal);
	}
	
	public DESIRABILITY isEventDesirable(Goal eventGoal) {
		return desirability.isEventDesirable(eventGoal);
	}
	
	public EXPECTEDNESS isEventExpected(Goal eventGoal) {
		return expectedness.isEventExpected(eventGoal);
	}
}