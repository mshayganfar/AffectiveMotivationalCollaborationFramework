package MetaInformation;

import java.util.ArrayList;

import Mechanisms.Appraisal.Controllability.CONTROLLABILITY;
import Mechanisms.Appraisal.Desirability.DESIRABILITY;
import Mechanisms.Appraisal.Expectedness.EXPECTEDNESS;
import Mechanisms.Appraisal.Relevance.RELEVANCE;
import Mechanisms.Collaboration.Collaboration.INFERRED_CONTEXT;
import MentalState.Goal;

public class AppraisalVector {
	
	public enum WHOSE_APPRAISAL{SELF, HUMAN, UNKNOWN};
	public enum EMOTION_INSTANCE{POSTIVE_SURPRISE, JOY, CONTENT, NEUTRAL, ANGER, WORRY, FRUSTRATION, NEGATIVE_SURPRISE, SHAME, GUILT, SADNESS};
	
	private MentalProcesses mentalProcesses;
	private WHOSE_APPRAISAL whoseAppraisal;
	private int turnNumber;
	
	private RELEVANCE relevanceAnticipatedValue;
	private DESIRABILITY desirabilityAnticipatedValue;
	private EXPECTEDNESS expectednessAnticipatedValue;
	private CONTROLLABILITY controllabilityAnticipatedValue;
	
	public AppraisalVector(MentalProcesses mentalProcesses) {
		
		this.mentalProcesses = mentalProcesses;
		
		this.whoseAppraisal = WHOSE_APPRAISAL.UNKNOWN;
		this.turnNumber     = -1;
		
		this.relevanceAnticipatedValue       = RELEVANCE.UNKNOWN;
		this.desirabilityAnticipatedValue    = DESIRABILITY.UNKNOWN;
		this.expectednessAnticipatedValue    = EXPECTEDNESS.UNKNOWN;
		this.controllabilityAnticipatedValue = CONTROLLABILITY.UNKNOWN;
	}
	
	public AppraisalVector(MentalProcesses mentalProcesses, int turnNumber, WHOSE_APPRAISAL whoseAppraisal, RELEVANCE relevance, DESIRABILITY desirability, EXPECTEDNESS expectedness, CONTROLLABILITY controllability) {
		
		this.mentalProcesses = mentalProcesses;
		
		this.whoseAppraisal = whoseAppraisal;
		this.turnNumber     = turnNumber;
		
		this.relevanceAnticipatedValue       = relevance;
		this.desirabilityAnticipatedValue    = desirability;
		this.expectednessAnticipatedValue    = expectedness;
		this.controllabilityAnticipatedValue = controllability;
	}
	
	public void setRelevanceValue(RELEVANCE relevance) {
		this.relevanceAnticipatedValue = relevance;
	}
	
	public void setDesirabilityValue(DESIRABILITY desirability) {
		this.desirabilityAnticipatedValue = desirability;
	}
	
	public void setExpectednessValue(EXPECTEDNESS expectedness) {
		this.expectednessAnticipatedValue = expectedness;
	}
	
	public void setControllabilityValue(CONTROLLABILITY controllability) {
		this.controllabilityAnticipatedValue = controllability;
	}
	
	public void setWhoseAppraisalValue(WHOSE_APPRAISAL whoseAppraisal) {
		this.whoseAppraisal = whoseAppraisal;
	}
	
	public void setTurnNumber(int turnNumber) {
		this.turnNumber = turnNumber;
	}
	
	public RELEVANCE getRelevanceValue() {
		return this.relevanceAnticipatedValue;
	}
	
	public DESIRABILITY getDesirabilityValue() {
		return this.desirabilityAnticipatedValue;
	}
	
	public EXPECTEDNESS getExpectednessValue() {
		return this.expectednessAnticipatedValue;
	}
	
	public CONTROLLABILITY getControllabilityValue() {
		return this.controllabilityAnticipatedValue;
	}
	
	public WHOSE_APPRAISAL getWhoseAppraisalValue() {
		return this.whoseAppraisal;
	}
	
	public int getTurnNumber() {
		return this.turnNumber;
	}
	
	public AppraisalVector getAppraisalVector() {
		return this;
	}
	
	public boolean isEmotionJoy(Goal eventGoal) {
		if (this.desirabilityAnticipatedValue.equals(DESIRABILITY.HIGH_DESIRABLE))
			return true;
		else
			return false;
	}
	
	public boolean isEmotionContent(Goal eventGoal) {
		if (this.desirabilityAnticipatedValue.equals(DESIRABILITY.DESIRABLE))
			return true;
		else
			return false;
	}
	
	public boolean isEmotionPositiveSurprise(Goal eventGoal) {
		if ((this.expectednessAnticipatedValue.equals(EXPECTEDNESS.UNEXPECTED)) ||
			(this.expectednessAnticipatedValue.equals(EXPECTEDNESS.MOST_UNEXPECTED)))
				if ((this.desirabilityAnticipatedValue.equals(DESIRABILITY.DESIRABLE)) ||
					(this.desirabilityAnticipatedValue.equals(DESIRABILITY.HIGH_DESIRABLE)))
						return true;
				else
						return false;
		else
			return false;
	}
	
	public boolean isEmotionAnger(Goal eventGoal) {
		if ((this.desirabilityAnticipatedValue.equals(DESIRABILITY.UNDESIRABLE)) ||
			(this.desirabilityAnticipatedValue.equals(DESIRABILITY.HIGH_UNDESIRABLE)))
				if (this.controllabilityAnticipatedValue.equals(CONTROLLABILITY.UNCONTROLLABLE))
					if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_PROPOSED)) ||
						(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_REJECTED)) ||
						(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_SELF_FAILURE)))
							return true;
					else
						return false;
				else
					return false;
		else
			return false;
	}
	
//	AGENT_PROPOSED, HUMAN_PROPOSED, AGENT_REJECTED, HUMAN_REJECTED, AGENT_ACCEPTED, HUMAN_ACCEPTED,
//	HUMAN_SELF_FAILURE, HUMAN_AGENT_FAILURE, HUMAN_OTHER_FAILURE, HUMAN_UNKOWN_FAILURE,
//	AGENT_SELF_FAILURE, AGENT_AGENT_FAILURE, AGENT_OTHER_FAILURE, AGENT_UNKOWN_FAILURE,
//	HUMAN_SELF_ACHIEVEMENT, HUMAN_AGENT_ACHIEVEMENT, HUMAN_OTHER_ACHIEVEMENT, HUMAN_UNKOWN_ACHIEVEMENT,
//	AGENT_SELF_ACHIEVEMENT, AGENT_AGENT_ACHIEVEMENT, AGENT_OTHER_ACHIEVEMENT, AGENT_UNKOWN_ACHIEVEMENT
//	
//	WORRY, FRUSTRATION, NEGATIVE_SURPRISE, SHAME, GUILT, SADNESS
	
	public ArrayList<EMOTION_INSTANCE> getEmotionInstance(Goal eventGoal) {
		
		ArrayList<EMOTION_INSTANCE> emotionInstances = new ArrayList<EMOTION_INSTANCE>();
		
		isEmotionJoy(eventGoal);
		isEmotionContent(eventGoal);
		isEmotionPositiveSurprise(eventGoal);
		isEmotionAnger(eventGoal);
		
		return emotionInstances;
	}
}
