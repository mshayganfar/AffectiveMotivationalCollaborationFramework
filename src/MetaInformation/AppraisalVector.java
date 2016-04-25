package MetaInformation;

import java.util.ArrayList;

import javax.swing.text.Position;

import com.sun.msv.datatype.xsd.PositiveIntegerType;

import Mechanisms.Appraisal.Controllability.CONTROLLABILITY;
import Mechanisms.Appraisal.Desirability.DESIRABILITY;
import Mechanisms.Appraisal.Expectedness.EXPECTEDNESS;
import Mechanisms.Appraisal.Relevance.RELEVANCE;
import Mechanisms.Collaboration.Collaboration.INFERRED_CONTEXT;
import MentalState.Goal;

public class AppraisalVector {
	
	public enum WHOSE_APPRAISAL{SELF, HUMAN, UNKNOWN};
	public enum EMOTION_INSTANCE{POSTIVE_SURPRISE, JOY, NEUTRAL, ANGER, WORRY, FRUSTRATION, NEGATIVE_SURPRISE, GUILT, SADNESS};
	
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
	
	public boolean isEmotionNeutral(Goal eventGoal, boolean user) {
		if (this.desirabilityAnticipatedValue.equals(DESIRABILITY.NEUTRAL))		
			return true;
		else
			return false;
	}
	
	public boolean isEmotionJoy(Goal eventGoal, boolean user) {
		if (this.relevanceAnticipatedValue.equals(RELEVANCE.RELEVANT))
			if ((this.desirabilityAnticipatedValue.equals(DESIRABILITY.DESIRABLE)) ||		
				(this.desirabilityAnticipatedValue.equals(DESIRABILITY.HIGH_DESIRABLE)))
				return true;
			else
				return false;
		else
			return false;
	}
	
	public boolean isEmotionSadness(Goal eventGoal, boolean user) {
		if (this.relevanceAnticipatedValue.equals(RELEVANCE.RELEVANT))
			if ((this.desirabilityAnticipatedValue.equals(DESIRABILITY.UNDESIRABLE)) ||
				(this.desirabilityAnticipatedValue.equals(DESIRABILITY.HIGH_UNDESIRABLE)))
				return true;
			else
				return false;
		else
			return false;
	}
	
	public boolean isEmotionPositiveSurprise(Goal eventGoal, boolean user) {
		if (this.relevanceAnticipatedValue.equals(RELEVANCE.RELEVANT))
			if ((this.expectednessAnticipatedValue.equals(EXPECTEDNESS.UNEXPECTED)) ||
				(this.expectednessAnticipatedValue.equals(EXPECTEDNESS.MOST_UNEXPECTED)))
					if ((this.desirabilityAnticipatedValue.equals(DESIRABILITY.DESIRABLE)) ||
						(this.desirabilityAnticipatedValue.equals(DESIRABILITY.HIGH_DESIRABLE)))
						if (user) {
							if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_PROPOSED)) ||
								(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_ACCEPTED)) ||
								(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_SELF_ACHIEVEMENT)))
								return true;
							else
								return false;
						}
						else {
							if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_PROPOSED)) ||
								(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_ACCEPTED)) ||
								(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_SELF_ACHIEVEMENT)))
								return true;
							else
								return false;
						}
					else
						return false;
			else
				return false;
		else
			return false;
	}
	
	public boolean isEmotionAnger(Goal eventGoal, boolean user) {
		if (this.relevanceAnticipatedValue.equals(RELEVANCE.RELEVANT))
			if ((this.desirabilityAnticipatedValue.equals(DESIRABILITY.UNDESIRABLE)) ||
				(this.desirabilityAnticipatedValue.equals(DESIRABILITY.HIGH_UNDESIRABLE)))
					if (this.controllabilityAnticipatedValue.equals(CONTROLLABILITY.UNCONTROLLABLE))
						if (user) {
							if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_REJECTED)) ||
								(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_SELF_FAILURE)))
								return true;
							else
								return false;
						}
						else {
							if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_REJECTED)) ||
								(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_SELF_FAILURE)))
								return true;
							else
								return false;
						}
					else
						return false;
			else
				return false;
		else
			return false;
	}
	
	public boolean isEmotionWorry(Goal eventGoal, boolean user) {
		if (this.relevanceAnticipatedValue.equals(RELEVANCE.RELEVANT))
			if ((this.desirabilityAnticipatedValue.equals(DESIRABILITY.UNDESIRABLE)) ||
				(this.desirabilityAnticipatedValue.equals(DESIRABILITY.HIGH_UNDESIRABLE)))
				if (this.controllabilityAnticipatedValue.equals(CONTROLLABILITY.UNCONTROLLABLE))
					if (this.expectednessAnticipatedValue.equals(EXPECTEDNESS.EXPECTED))
						if (user) {
							if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_PROPOSED)) ||
								(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_REJECTED)) ||
								(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_SELF_FAILURE)) ||
								(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_SELF_FAILURE)))
								return true;
							else
								return false;
						}
						else {
							if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_PROPOSED)) ||
									(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_REJECTED)) ||
									(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_SELF_FAILURE)) ||
									(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_SELF_FAILURE)))
									return true;
								else
									return false;
						}
					else
						return false;
				else
					return false;
			else
				return false;
		else
			return false;
	}
	
	public boolean isEmotionFrustration(Goal eventGoal, boolean user) {
		if (this.relevanceAnticipatedValue.equals(RELEVANCE.RELEVANT))
			if ((this.desirabilityAnticipatedValue.equals(DESIRABILITY.UNDESIRABLE)) ||
				(this.desirabilityAnticipatedValue.equals(DESIRABILITY.HIGH_UNDESIRABLE)))
				if (this.controllabilityAnticipatedValue.equals(CONTROLLABILITY.CONTROLLABLE))
					if (user) {
						if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_PROPOSED)) ||
							(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_REJECTED)) ||
							(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_SELF_FAILURE)))
							return true;
						else
							return false;
					}
					else {
						if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_PROPOSED)) ||
							(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_REJECTED)) ||
							(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_SELF_FAILURE)))
							return true;
						else
							return false;
					}
				else
					return false;
			else
				return false;
		else
			return false;
	}
	
	public boolean isEmotionNegativeSurprise(Goal eventGoal, boolean user) {
		if (this.relevanceAnticipatedValue.equals(RELEVANCE.RELEVANT))
			if ((this.expectednessAnticipatedValue.equals(EXPECTEDNESS.UNEXPECTED)) ||
					(this.expectednessAnticipatedValue.equals(EXPECTEDNESS.MOST_UNEXPECTED)))
					if ((this.desirabilityAnticipatedValue.equals(DESIRABILITY.UNDESIRABLE)) ||
						(this.desirabilityAnticipatedValue.equals(DESIRABILITY.HIGH_UNDESIRABLE)))
						if (user) {
							if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_PROPOSED)) ||
								(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_REJECTED)) ||
								(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_SELF_FAILURE)))
								return true;
							else
								return false;
						}
						else {
							if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_PROPOSED)) ||
								(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_REJECTED)) ||
								(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_SELF_FAILURE)))
								return true;
							else
								return false;
						}
					else
						return false;
			else
				return false;
		else
			return false;
	}
	
	public boolean isEmotionGuilt(Goal eventGoal, boolean user) {
		if (this.relevanceAnticipatedValue.equals(RELEVANCE.RELEVANT))
			if ((this.desirabilityAnticipatedValue.equals(DESIRABILITY.UNDESIRABLE)) ||
				(this.desirabilityAnticipatedValue.equals(DESIRABILITY.HIGH_UNDESIRABLE)))
				if (this.controllabilityAnticipatedValue.equals(CONTROLLABILITY.UNCONTROLLABLE))
					if (user) {
						if (mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_SELF_FAILURE))
							return true;
						else
							return false;
					}
					else {
						if (mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_SELF_FAILURE))
							return true;
						else
							return false;
					}
				else
					return false;
			else
				return false;
		else
			return false;
	}
	
	public ArrayList<EMOTION_INSTANCE> getEmotionInstance(Goal eventGoal, boolean user) {
		
		ArrayList<EMOTION_INSTANCE> emotionInstances = new ArrayList<EMOTION_INSTANCE>();
		
		if (isEmotionJoy(eventGoal, user))
			emotionInstances.add(EMOTION_INSTANCE.JOY);
		if (isEmotionSadness(eventGoal, user))
			emotionInstances.add(EMOTION_INSTANCE.SADNESS);
		if (isEmotionPositiveSurprise(eventGoal, user))
			emotionInstances.add(EMOTION_INSTANCE.POSTIVE_SURPRISE);
		if (isEmotionNegativeSurprise(eventGoal, user))
			emotionInstances.add(EMOTION_INSTANCE.NEGATIVE_SURPRISE);
		if (isEmotionAnger(eventGoal, user))
			emotionInstances.add(EMOTION_INSTANCE.ANGER);
		if (isEmotionWorry(eventGoal, user))
			emotionInstances.add(EMOTION_INSTANCE.WORRY);
		if (isEmotionFrustration(eventGoal, user))
			emotionInstances.add(EMOTION_INSTANCE.FRUSTRATION);
		if (isEmotionGuilt(eventGoal, user))
			emotionInstances.add(EMOTION_INSTANCE.GUILT);
		else if (isEmotionNeutral(eventGoal, user))
			emotionInstances.add(EMOTION_INSTANCE.NEUTRAL);
		
		return emotionInstances;
	}
}
