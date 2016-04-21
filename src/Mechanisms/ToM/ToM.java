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
import MetaInformation.MentalProcesses;
import edu.wpi.cetask.Plan;
import edu.wpi.disco.lang.Propose;

public class ToM extends Mechanisms{
	
	private Collaboration collaboration;
	private Relevance relevance;
	private Controllability controllability;
	private Desirability desirability;
	private Expectedness expectedness;
	private Motivation motivation;
	
	private double valence = 0.0;
	
	public enum INFERRED_CONTEXT{AGENT_PROPOSED, HUMAN_PROPOSED, AGENT_REJECTED, HUMAN_REJECTED, AGENT_ACCEPTED, HUMAN_ACCEPTED,
		HUMAN_SELF_FAILUE, HUMAN_AGENT_FAILUE, HUMAN_OTHER_FAILUE, HUMAN_UNKOWN_FAILUE,
		AGENT_SELF_FAILUE, AGENT_AGENT_FAILUE, AGENT_OTHER_FAILUE, AGENT_UNKOWN_FAILUE,
		HUMAN_SELF_ACHIEVEMENT, HUMAN_AGENT_ACHIEVEMENT, HUMAN_OTHER_ACHIEVEMENT, HUMAN_UNKOWN_ACHIEVEMENT,
		AGENT_SELF_ACHIEVEMENT, AGENT_AGENT_ACHIEVEMENT, AGENT_OTHER_ACHIEVEMENT, AGENT_UNKOWN_ACHIEVEMENT};
	
	public ToM(MentalProcesses mentalProcesses) {
		this.collaboration = mentalProcesses.getCollaborationMechanism();
		this.motivation    = mentalProcesses.getMotivationMechanism();
		
		this.relevance       = mentalProcesses.getRelevanceProcess();
		this.controllability = mentalProcesses.getControllabilityProcess();
		this.desirability    = mentalProcesses.getDesirabilityProcess();
		this.expectedness    = mentalProcesses.getExpectednessProcess();
	}
	
	public ReverseAppraisalVector getReverseAppraisalValues(Goal eventGoal) {
		
		double valenceValue      = getValenceValue();
		INFERRED_CONTEXT context = getInferredContext(eventGoal);
		
		return getEstimatedAppraisal(valenceValue, context);
	}
	
	private ReverseAppraisalVector getEstimatedAppraisal(double valenceValue, INFERRED_CONTEXT context) {
		
		ReverseAppraisalVector estimatedAppraisalVector = new ReverseAppraisalVector();
		
		estimatedAppraisalVector.setRelevanceValue(RELEVANCE.RELEVANT);
		estimatedAppraisalVector.setDesirabilityValue(DESIRABILITY.DESIRABLE);
		estimatedAppraisalVector.setExpectednessValue(EXPECTEDNESS.EXPECTED);
		estimatedAppraisalVector.setControllabilityValue(CONTROLLABILITY.CONTROLLABLE);
		
		return estimatedAppraisalVector;
	}
	
	private INFERRED_CONTEXT getInferredContext(Goal eventGoal) {
		
		Plan eventPlan = eventGoal.getPlan();
		
		if ((eventPlan.getGoal() instanceof Propose.Should) && (this.collaboration.getResponsibleAgent(eventPlan)).equals(AGENT.SELF))
			return INFERRED_CONTEXT.AGENT_PROPOSED;
		else if ((eventPlan.getGoal() instanceof Propose.Should) && (this.collaboration.getResponsibleAgent(eventPlan)).equals(AGENT.OTHER))
			return INFERRED_CONTEXT.HUMAN_PROPOSED;
		else if (1==1/*Agent Rejected*/)
			return INFERRED_CONTEXT.AGENT_REJECTED;
		else if (1==1/*Human Rejected*/)
			return INFERRED_CONTEXT.HUMAN_REJECTED;
		else if (1==1/*Agent Accepted*/)
			return INFERRED_CONTEXT.AGENT_ACCEPTED;
		else if (1==1/*Human Accepted*/)
			return INFERRED_CONTEXT.HUMAN_ACCEPTED;
		else if ((this.collaboration.getGoalStatus(eventPlan).equals(GOAL_STATUS.FAILED)) && (this.collaboration.getResponsibleAgent(eventPlan)).equals(AGENT.OTHER))
			return INFERRED_CONTEXT.HUMAN_SELF_FAILUE;
		else if ((this.collaboration.getGoalStatus(eventPlan).equals(GOAL_STATUS.FAILED)) && (this.collaboration.getResponsibleAgent(eventPlan)).equals(AGENT.SELF))
			return INFERRED_CONTEXT.AGENT_SELF_FAILUE;
		else if ((this.collaboration.getGoalStatus(eventPlan).equals(GOAL_STATUS.ACHIEVED)) && (this.collaboration.getResponsibleAgent(eventPlan)).equals(AGENT.OTHER))
			return INFERRED_CONTEXT.HUMAN_SELF_ACHIEVEMENT;
		else if ((this.collaboration.getGoalStatus(eventPlan).equals(GOAL_STATUS.ACHIEVED)) && (this.collaboration.getResponsibleAgent(eventPlan)).equals(AGENT.SELF))
			return INFERRED_CONTEXT.AGENT_SELF_ACHIEVEMENT;
		else
			return null; //should be changed
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
	
	public void estimateAppraisalValues(Goal eventGoal) {
		
	}
	
	public class ReverseAppraisalVector {
		
		private RELEVANCE relevanceEstimate;
		private DESIRABILITY desirabilityEstimate;
		private EXPECTEDNESS expectednessEstimate;
		private CONTROLLABILITY controllabilityEstimate;
		
		public ReverseAppraisalVector() {
			this.relevanceEstimate = RELEVANCE.UNKNOWN;
			this.desirabilityEstimate = DESIRABILITY.UNKNOWN;
			this.expectednessEstimate = EXPECTEDNESS.UNKNOWN;
			this.controllabilityEstimate = CONTROLLABILITY.UNKNOWN;
		}
		
		private void setRelevanceValue(RELEVANCE relevance) {
			this.relevanceEstimate = relevance;
		}
		
		private void setDesirabilityValue(DESIRABILITY desirability) {
			this.desirabilityEstimate = desirability;
		}
		
		private void setExpectednessValue(EXPECTEDNESS expectedness) {
			this.expectednessEstimate = expectedness;
		}
		
		private void setControllabilityValue(CONTROLLABILITY controllability) {
			this.controllabilityEstimate = controllability;
		}
	}
}