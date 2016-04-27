package MetaInformation;

import Mechanisms.Appraisal.Controllability.CONTROLLABILITY;
import Mechanisms.Appraisal.Desirability.DESIRABILITY;
import Mechanisms.Appraisal.Expectedness.EXPECTEDNESS;
import Mechanisms.Appraisal.Relevance.RELEVANCE;
import Mechanisms.Collaboration.Collaboration.INFERRED_CONTEXT;
import MentalState.Goal;

public class AppraisalVector {
	
	public enum WHOSE_APPRAISAL{SELF, HUMAN, UNKNOWN};
	public enum EMOTION_INSTANCE{POSTIVE_SURPRISE, JOY, GRATITUDE, NEUTRAL, ANGER, WORRY, FRUSTRATION, NEGATIVE_SURPRISE, GUILT, SADNESS};
	
	private MentalProcesses mentalProcesses;
	private WHOSE_APPRAISAL whoseAppraisal;
	private int turnNumber;
	
	private RELEVANCE relevanceValue;
	private DESIRABILITY desirabilityValue;
	private EXPECTEDNESS expectednessValue;
	private CONTROLLABILITY controllabilityValue;
	
	public AppraisalVector(MentalProcesses mentalProcesses) {
		
		this.mentalProcesses = mentalProcesses;
		
		this.whoseAppraisal = WHOSE_APPRAISAL.UNKNOWN;
		this.turnNumber     = -1;
		
		this.relevanceValue       = RELEVANCE.UNKNOWN;
		this.desirabilityValue    = DESIRABILITY.UNKNOWN;
		this.expectednessValue    = EXPECTEDNESS.UNKNOWN;
		this.controllabilityValue = CONTROLLABILITY.UNKNOWN;
	}
	
	public AppraisalVector(MentalProcesses mentalProcesses, int turnNumber, WHOSE_APPRAISAL whoseAppraisal, RELEVANCE relevance, DESIRABILITY desirability, EXPECTEDNESS expectedness, CONTROLLABILITY controllability) {
		
		this.mentalProcesses = mentalProcesses;
		
		this.whoseAppraisal = whoseAppraisal;
		this.turnNumber     = turnNumber;
		
		this.relevanceValue       = relevance;
		this.desirabilityValue    = desirability;
		this.expectednessValue    = expectedness;
		this.controllabilityValue = controllability;
	}
	
	public void setRelevanceValue(RELEVANCE relevance) {
		this.relevanceValue = relevance;
	}
	
	public void setDesirabilityValue(DESIRABILITY desirability) {
		this.desirabilityValue = desirability;
	}
	
	public void setExpectednessValue(EXPECTEDNESS expectedness) {
		this.expectednessValue = expectedness;
	}
	
	public void setControllabilityValue(CONTROLLABILITY controllability) {
		this.controllabilityValue = controllability;
	}
	
	public void setWhoseAppraisalValue(WHOSE_APPRAISAL whoseAppraisal) {
		this.whoseAppraisal = whoseAppraisal;
	}
	
	public void setTurnNumber(int turnNumber) {
		this.turnNumber = turnNumber;
	}
	
	public RELEVANCE getRelevanceValue() {
		return this.relevanceValue;
	}
	
	public DESIRABILITY getDesirabilityValue() {
		return this.desirabilityValue;
	}
	
	public EXPECTEDNESS getExpectednessValue() {
		return this.expectednessValue;
	}
	
	public CONTROLLABILITY getControllabilityValue() {
		return this.controllabilityValue;
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
		if (this.desirabilityValue.equals(DESIRABILITY.NEUTRAL))		
			return true;
		else
			return false;
	}
	
	public boolean isEmotionJoy(Goal eventGoal, boolean user) {
		if (this.relevanceValue.equals(RELEVANCE.RELEVANT))
			if ((this.desirabilityValue.equals(DESIRABILITY.DESIRABLE)) ||		
				(this.desirabilityValue.equals(DESIRABILITY.HIGH_DESIRABLE)))
				if ((this.expectednessValue.equals(EXPECTEDNESS.EXPECTED)) ||
					(this.expectednessValue.equals(EXPECTEDNESS.MOST_EXPECTED)))
					if (user) {
						if (mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_ACHIEVED))
							return true;
						else
							return false;
					}
					else {
						if (mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_ACHIEVED))
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
	
	public boolean isEmotionGratitude(Goal eventGoal, boolean user) {
		if (this.relevanceValue.equals(RELEVANCE.RELEVANT))
			if ((this.desirabilityValue.equals(DESIRABILITY.DESIRABLE)) ||		
				(this.desirabilityValue.equals(DESIRABILITY.HIGH_DESIRABLE)))
				if ((this.expectednessValue.equals(EXPECTEDNESS.EXPECTED)) ||
					(this.expectednessValue.equals(EXPECTEDNESS.MOST_EXPECTED)))
					if (user) {
						if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_ACCEPTED)) ||
								(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_ACHIEVED)))
							return true;
						else
							return false;
					}
					else {
						if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_ACCEPTED)) ||
							(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_ACHIEVED)))
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
	
	public boolean isEmotionSadness(Goal eventGoal, boolean user) {
		if (this.relevanceValue.equals(RELEVANCE.RELEVANT))
			if ((this.desirabilityValue.equals(DESIRABILITY.UNDESIRABLE)) ||
				(this.desirabilityValue.equals(DESIRABILITY.HIGH_UNDESIRABLE)))
				if ((this.expectednessValue.equals(EXPECTEDNESS.EXPECTED)) ||
					(this.expectednessValue.equals(EXPECTEDNESS.MOST_EXPECTED)))
					if (this.controllabilityValue.equals(CONTROLLABILITY.UNCONTROLLABLE))
						if (user) {
							if (mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_FAILED))
								return true;
							else
								return false;
						}
						else {
							if (mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_FAILED))
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
	
	public boolean isEmotionPositiveSurprise(Goal eventGoal, boolean user) {
		if (this.relevanceValue.equals(RELEVANCE.RELEVANT))
			if (this.expectednessValue.equals(EXPECTEDNESS.MOST_UNEXPECTED))
				if ((this.desirabilityValue.equals(DESIRABILITY.DESIRABLE)) ||
					(this.desirabilityValue.equals(DESIRABILITY.HIGH_DESIRABLE)))
					if (user) {
						if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_PROPOSED)) ||
							(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_ACCEPTED)) ||
							(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_ACHIEVED)))
							return true;
						else
							return false;
					}
					else {
						if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_PROPOSED)) ||
							(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_ACCEPTED)) ||
							(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_ACHIEVED)))
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
		if (this.relevanceValue.equals(RELEVANCE.RELEVANT))
			if (this.desirabilityValue.equals(DESIRABILITY.HIGH_UNDESIRABLE))
				if ((this.expectednessValue.equals(EXPECTEDNESS.EXPECTED)) ||
					(this.expectednessValue.equals(EXPECTEDNESS.MOST_EXPECTED)))
					if (this.controllabilityValue.equals(CONTROLLABILITY.UNCONTROLLABLE))
						if (user) {
							if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_REJECTED)) ||
								(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_FAILED)))
								return true;
							else
								return false;
						}
						else {
							if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_REJECTED)) ||
								(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_FAILED)))
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
	
	public boolean isEmotionWorry(Goal eventGoal, boolean user) {
		if (this.relevanceValue.equals(RELEVANCE.RELEVANT))
			if ((this.desirabilityValue.equals(DESIRABILITY.UNDESIRABLE)) ||
				(this.desirabilityValue.equals(DESIRABILITY.HIGH_UNDESIRABLE)))
				if (this.controllabilityValue.equals(CONTROLLABILITY.UNCONTROLLABLE))
					if (this.expectednessValue.equals(EXPECTEDNESS.UNEXPECTED))
						if (user) {
							if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_PROPOSED)) ||
								(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_REJECTED)) ||
								(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_FAILED)) ||
								(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_FAILED)))
								return true;
							else
								return false;
						}
						else {
							if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_PROPOSED)) ||
									(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_REJECTED)) ||
									(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_FAILED)) ||
									(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_FAILED)))
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
		if (this.relevanceValue.equals(RELEVANCE.RELEVANT))
			if (this.desirabilityValue.equals(DESIRABILITY.UNDESIRABLE))
				if (this.controllabilityValue.equals(CONTROLLABILITY.UNCONTROLLABLE))
					if ((this.expectednessValue.equals(EXPECTEDNESS.EXPECTED)) ||
						(this.expectednessValue.equals(EXPECTEDNESS.MOST_EXPECTED)))
						if (user) {
							if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_PROPOSED)) ||
								(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_FAILED)))
								return true;
							else
								return false;
						}
						else {
							if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_PROPOSED)) ||
								(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_FAILED)))
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
	
	public boolean isEmotionNegativeSurprise(Goal eventGoal, boolean user) {
		if (this.relevanceValue.equals(RELEVANCE.RELEVANT))
			if (this.expectednessValue.equals(EXPECTEDNESS.MOST_UNEXPECTED))
				if ((this.desirabilityValue.equals(DESIRABILITY.UNDESIRABLE)) ||
					(this.desirabilityValue.equals(DESIRABILITY.HIGH_UNDESIRABLE)))
					if (user) {
						if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_PROPOSED)) ||
							(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_REJECTED)) ||
							(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_FAILED)))
							return true;
						else
							return false;
					}
					else {
						if ((mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_PROPOSED)) ||
							(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_REJECTED)) ||
							(mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_FAILED)))
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
		if (this.relevanceValue.equals(RELEVANCE.RELEVANT))
			if ((this.desirabilityValue.equals(DESIRABILITY.UNDESIRABLE)) ||
				(this.desirabilityValue.equals(DESIRABILITY.HIGH_UNDESIRABLE)))
				if ((this.controllabilityValue.equals(CONTROLLABILITY.LOW_CONTROLLABLE)) ||
					(this.controllabilityValue.equals(CONTROLLABILITY.HIGH_CONTROLLABLE)))
					if ((this.expectednessValue.equals(EXPECTEDNESS.EXPECTED)) ||
						(this.expectednessValue.equals(EXPECTEDNESS.MOST_EXPECTED)))
						if (user) {
							if (mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.HUMAN_FAILED))
								return true;
							else
								return false;
						}
						else {
							if (mentalProcesses.getCollaborationMechanism().getInferredContext(eventGoal).equals(INFERRED_CONTEXT.AGENT_FAILED))
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
	
	public EMOTION_INSTANCE getEmotionInstance(Goal eventGoal, boolean user) {
		
		if (isEmotionJoy(eventGoal, user))
			return EMOTION_INSTANCE.JOY;
		if (isEmotionGratitude(eventGoal, user))
			return EMOTION_INSTANCE.GRATITUDE;
		else if (isEmotionSadness(eventGoal, user))
			return EMOTION_INSTANCE.SADNESS;
		else if (isEmotionPositiveSurprise(eventGoal, user))
			return EMOTION_INSTANCE.POSTIVE_SURPRISE;
		else if (isEmotionNegativeSurprise(eventGoal, user))
			return EMOTION_INSTANCE.NEGATIVE_SURPRISE;
		else if (isEmotionAnger(eventGoal, user))
			return EMOTION_INSTANCE.ANGER;
		else if (isEmotionWorry(eventGoal, user))
			return EMOTION_INSTANCE.WORRY;
		else if (isEmotionFrustration(eventGoal, user))
			return EMOTION_INSTANCE.FRUSTRATION;
		else if (isEmotionGuilt(eventGoal, user))
			return EMOTION_INSTANCE.GUILT;
		else if (isEmotionNeutral(eventGoal, user))
			return EMOTION_INSTANCE.NEUTRAL;
		else
			throw new IllegalStateException("No matching emotion!");
	}
}
