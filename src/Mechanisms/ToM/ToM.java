package Mechanisms.ToM;

import Mechanisms.Mechanisms;
import Mechanisms.Appraisal.Controllability;
import Mechanisms.Appraisal.Desirability;
import Mechanisms.Appraisal.Expectedness;
import Mechanisms.Appraisal.Relevance;
import Mechanisms.Appraisal.Controllability.CONTROLLABILITY;
import Mechanisms.Appraisal.Desirability.DESIRABILITY;
import Mechanisms.Appraisal.Expectedness.EXPECTEDNESS;
import Mechanisms.Appraisal.Relevance.RELEVANCE;
import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Collaboration.Collaboration.GOAL_STATUS;
import Mechanisms.Collaboration.Collaboration.INFERRED_CONTEXT;
import Mechanisms.Motivation.Motivation;
import MentalState.Goal;
import MetaInformation.AppraisalVector;
import MetaInformation.MentalProcesses;
import edu.wpi.cetask.Plan;
import edu.wpi.cetask.Task;
import edu.wpi.disco.lang.Accept;
import edu.wpi.disco.lang.Propose;
import edu.wpi.disco.lang.Reject;

public class ToM extends Mechanisms{
	
	private MentalProcesses mentalProcesses;
	
	private Collaboration collaboration;
	private Relevance relevance;
	private Controllability controllability;
	private Desirability desirability;
	private Expectedness expectedness;
	private Motivation motivation;
	
	private double valence = 0.0;
	
	public void prepareAppraisalsOfToM(MentalProcesses mentalProcesses) {
		this.collaboration = mentalProcesses.getCollaborationMechanism();
		this.motivation    = mentalProcesses.getMotivationMechanism();
		
		this.relevance       = mentalProcesses.getRelevanceProcess();
		this.controllability = mentalProcesses.getControllabilityProcess();
		this.desirability    = mentalProcesses.getDesirabilityProcess();
		this.expectedness    = mentalProcesses.getExpectednessProcess();
	}
	
	public AppraisalVector getReverseAppraisalValues(Goal eventGoal) {
		
		double valenceValue      = getValenceValue();
		INFERRED_CONTEXT context = this.collaboration.getInferredContext(eventGoal);
		
		return getAnticipatedAppraisal(valenceValue, context, eventGoal);
	}
	
	private AppraisalVector getAnticipatedAppraisal(double valenceValue, INFERRED_CONTEXT context, Goal eventGoal) {
		
		AppraisalVector estimatedAppraisalVector = new AppraisalVector(mentalProcesses);
		
		estimatedAppraisalVector.setRelevanceValue(this.relevance.isEventRelevant(eventGoal));
		estimatedAppraisalVector.setDesirabilityValue(getDesirabilityUsingValence(valenceValue));
		estimatedAppraisalVector.setExpectednessValue(this.expectedness.isEventExpected(eventGoal));
		estimatedAppraisalVector.setControllabilityValue(this.controllability.isEventControllable(eventGoal));
		
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
		return this.relevance.isEventRelevant(eventGoal);
	}
	
	public CONTROLLABILITY isEventControllable(Goal eventGoal) {
		return this.controllability.isEventControllable(eventGoal);
	}
	
	public DESIRABILITY isEventDesirable(Goal eventGoal) {
		return this.desirability.isEventDesirable(eventGoal);
	}
	
	public EXPECTEDNESS isEventExpected(Goal eventGoal) {
		return this.expectedness.isEventExpected(eventGoal);
	}
}