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
	
	private Collaboration collaboration;
	private Relevance relevance;
	private Controllability controllability;
	private Desirability desirability;
	private Expectedness expectedness;
	private Motivation motivation;
	
	private double valence = 0.0;
	
	public enum INFERRED_CONTEXT{AGENT_PROPOSED, HUMAN_PROPOSED, AGENT_REJECTED, HUMAN_REJECTED, AGENT_ACCEPTED, HUMAN_ACCEPTED,
		HUMAN_SELF_FAILURE, HUMAN_AGENT_FAILURE, HUMAN_OTHER_FAILURE, HUMAN_UNKOWN_FAILURE,
		AGENT_SELF_FAILURE, AGENT_AGENT_FAILURE, AGENT_OTHER_FAILURE, AGENT_UNKOWN_FAILURE,
		HUMAN_SELF_ACHIEVEMENT, HUMAN_AGENT_ACHIEVEMENT, HUMAN_OTHER_ACHIEVEMENT, HUMAN_UNKOWN_ACHIEVEMENT,
		AGENT_SELF_ACHIEVEMENT, AGENT_AGENT_ACHIEVEMENT, AGENT_OTHER_ACHIEVEMENT, AGENT_UNKOWN_ACHIEVEMENT};
	
//	public ToM() {
//	}
	
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
		INFERRED_CONTEXT context = getInferredContext(eventGoal);
		
		return getEstimatedAppraisal(valenceValue, context, eventGoal);
	}
	
	private AppraisalVector getEstimatedAppraisal(double valenceValue, INFERRED_CONTEXT context, Goal eventGoal) {
		
		AppraisalVector estimatedAppraisalVector = new AppraisalVector();
		
		estimatedAppraisalVector.setRelevanceValue(this.relevance.isEventRelevant(eventGoal));
		estimatedAppraisalVector.setDesirabilityValue(getDesirabilityUsingValence(valenceValue));
		estimatedAppraisalVector.setExpectednessValue(this.expectedness.isEventExpected(eventGoal));
		estimatedAppraisalVector.setControllabilityValue(this.controllability.isEventControllable(eventGoal));
		
		return estimatedAppraisalVector;
	}
	
	private INFERRED_CONTEXT getInferredContext(Goal eventGoal) {
		
		Plan eventPlan = eventGoal.getPlan();
		
		if ((eventPlan.getGoal() instanceof Propose.Should) && (this.collaboration.getResponsibleAgent(eventPlan)).equals(AGENT.SELF))
			return INFERRED_CONTEXT.AGENT_PROPOSED;
		else if ((eventPlan.getGoal() instanceof Propose.Should) && (this.collaboration.getResponsibleAgent(eventPlan)).equals(AGENT.OTHER))
			return INFERRED_CONTEXT.HUMAN_PROPOSED;
		else if ((eventPlan.getGoal() instanceof Reject) && (this.collaboration.getResponsibleAgent(eventPlan /*This should be changed!*/).equals(AGENT.SELF)))
//			((Reject)eventPlan.getGoal()).getProposal();
			return INFERRED_CONTEXT.AGENT_REJECTED;
		else if ((eventPlan.getGoal() instanceof Reject) && (this.collaboration.getResponsibleAgent(eventPlan /*This should be changed!*/).equals(AGENT.OTHER)))
			return INFERRED_CONTEXT.HUMAN_REJECTED;
		else if ((eventPlan.getGoal() instanceof Accept) && (this.collaboration.getResponsibleAgent(eventPlan /*This should be changed!*/).equals(AGENT.SELF)))
			return INFERRED_CONTEXT.AGENT_ACCEPTED;
		else if ((eventPlan.getGoal() instanceof Accept) && (this.collaboration.getResponsibleAgent(eventPlan /*This should be changed!*/).equals(AGENT.OTHER)))
			return INFERRED_CONTEXT.HUMAN_ACCEPTED;
		else if ((this.collaboration.getGoalStatus(eventPlan).equals(GOAL_STATUS.FAILED)) && (this.collaboration.getResponsibleAgent(eventPlan)).equals(AGENT.OTHER))
			return INFERRED_CONTEXT.HUMAN_SELF_FAILURE;
		else if ((this.collaboration.getGoalStatus(eventPlan).equals(GOAL_STATUS.FAILED)) && (this.collaboration.getResponsibleAgent(eventPlan)).equals(AGENT.SELF))
			return INFERRED_CONTEXT.AGENT_SELF_FAILURE;
		else if ((this.collaboration.getGoalStatus(eventPlan).equals(GOAL_STATUS.ACHIEVED)) && (this.collaboration.getResponsibleAgent(eventPlan)).equals(AGENT.OTHER))
			return INFERRED_CONTEXT.HUMAN_SELF_ACHIEVEMENT;
		else if ((this.collaboration.getGoalStatus(eventPlan).equals(GOAL_STATUS.ACHIEVED)) && (this.collaboration.getResponsibleAgent(eventPlan)).equals(AGENT.SELF))
			return INFERRED_CONTEXT.AGENT_SELF_ACHIEVEMENT;
		else
			throw new IllegalArgumentException("Event: " + eventPlan);
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