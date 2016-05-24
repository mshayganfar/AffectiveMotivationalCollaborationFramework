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
	
	private Goal goal;
	
	public AppraisalVector(MentalProcesses mentalProcesses, Goal eventGoal) {
		
		this.mentalProcesses = mentalProcesses;
		
		this.whoseAppraisal = WHOSE_APPRAISAL.SELF;
		this.turnNumber     = Turns.getInstance().getTurnNumber();
		
		this.relevanceValue       = RELEVANCE.UNKNOWN;
		this.desirabilityValue    = DESIRABILITY.UNKNOWN;
		this.expectednessValue    = EXPECTEDNESS.UNKNOWN;
		this.controllabilityValue = CONTROLLABILITY.UNKNOWN;
		
		this.goal = eventGoal;
	}
	
	public AppraisalVector(MentalProcesses mentalProcesses, Goal eventGoal, int turnNumber, WHOSE_APPRAISAL whoseAppraisal, RELEVANCE relevance, DESIRABILITY desirability, EXPECTEDNESS expectedness, CONTROLLABILITY controllability) {
		
		this.mentalProcesses = mentalProcesses;
		
		this.whoseAppraisal = whoseAppraisal;
		this.turnNumber     = turnNumber;
		
		this.relevanceValue       = relevance;
		this.desirabilityValue    = desirability;
		this.expectednessValue    = expectedness;
		this.controllabilityValue = controllability;
		
		this.goal = eventGoal;
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
	
	public RELEVANCE getRelevanceSymbolicValue() {
		return this.relevanceValue;
	}
	
	public DESIRABILITY getDesirabilitySymbolicValue() {
		return this.desirabilityValue;
	}
	
	public EXPECTEDNESS getExpectednessSymbolicValue() {
		return this.expectednessValue;
	}
	
	public CONTROLLABILITY getControllabilitySymbolicValue() {
		return this.controllabilityValue;
	}
	
	public WHOSE_APPRAISAL getWhoseAppraisalValue() {
		return this.whoseAppraisal;
	}
	
	public int getTurnNumber() {
		return this.turnNumber;
	}
	
	public Goal getGoal() {
		return this.goal;
	}
	
	public AppraisalVector getAppraisalVector() {
		return this;
	}
	
	public boolean isEmotionNeutral(Goal eventGoal, WHOSE_APPRAISAL whoseAppraisal) {
		if (this.desirabilityValue.equals(DESIRABILITY.NEUTRAL))		
			return true;
		else
			return false;
	}
	
	public boolean isEmotionJoy(Goal eventGoal, WHOSE_APPRAISAL whoseAppraisal) {
		if (this.relevanceValue.equals(RELEVANCE.RELEVANT))
			if ((this.desirabilityValue.equals(DESIRABILITY.DESIRABLE)) ||		
				(this.desirabilityValue.equals(DESIRABILITY.HIGH_DESIRABLE)))
				if ((this.expectednessValue.equals(EXPECTEDNESS.EXPECTED)) ||
					(this.expectednessValue.equals(EXPECTEDNESS.MOST_EXPECTED)))
					if (whoseAppraisal.equals(WHOSE_APPRAISAL.HUMAN)) {
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
	
	public boolean isEmotionGratitude(Goal eventGoal, WHOSE_APPRAISAL whoseAppraisal) {
		if (this.relevanceValue.equals(RELEVANCE.RELEVANT))
			if ((this.desirabilityValue.equals(DESIRABILITY.DESIRABLE)) ||		
				(this.desirabilityValue.equals(DESIRABILITY.HIGH_DESIRABLE)))
				if ((this.expectednessValue.equals(EXPECTEDNESS.EXPECTED)) ||
					(this.expectednessValue.equals(EXPECTEDNESS.MOST_EXPECTED)))
					if (whoseAppraisal.equals(WHOSE_APPRAISAL.HUMAN)) {
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
	
	public boolean isEmotionSadness(Goal eventGoal, WHOSE_APPRAISAL whoseAppraisal) {
		if (this.relevanceValue.equals(RELEVANCE.RELEVANT))
			if ((this.desirabilityValue.equals(DESIRABILITY.UNDESIRABLE)) ||
				(this.desirabilityValue.equals(DESIRABILITY.HIGH_UNDESIRABLE)))
				if ((this.expectednessValue.equals(EXPECTEDNESS.EXPECTED)) ||
					(this.expectednessValue.equals(EXPECTEDNESS.MOST_EXPECTED)))
					if ((this.controllabilityValue.equals(CONTROLLABILITY.UNCONTROLLABLE)) ||
						(this.controllabilityValue.equals(CONTROLLABILITY.LOW_CONTROLLABLE)))
						if (whoseAppraisal.equals(WHOSE_APPRAISAL.HUMAN)) {
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
	
	public boolean isEmotionPositiveSurprise(Goal eventGoal, WHOSE_APPRAISAL whoseAppraisal) {
		if (this.relevanceValue.equals(RELEVANCE.RELEVANT))
			if (this.expectednessValue.equals(EXPECTEDNESS.MOST_UNEXPECTED))
				if ((this.desirabilityValue.equals(DESIRABILITY.DESIRABLE)) ||
					(this.desirabilityValue.equals(DESIRABILITY.HIGH_DESIRABLE)))
					if (whoseAppraisal.equals(WHOSE_APPRAISAL.HUMAN)) {
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
	
	public boolean isEmotionAnger(Goal eventGoal, WHOSE_APPRAISAL whoseAppraisal) {
		if (this.relevanceValue.equals(RELEVANCE.RELEVANT))
			if (this.desirabilityValue.equals(DESIRABILITY.HIGH_UNDESIRABLE))
				if ((this.expectednessValue.equals(EXPECTEDNESS.EXPECTED)) ||
					(this.expectednessValue.equals(EXPECTEDNESS.MOST_EXPECTED)))
					if (this.controllabilityValue.equals(CONTROLLABILITY.UNCONTROLLABLE))
						if (whoseAppraisal.equals(WHOSE_APPRAISAL.HUMAN)) {
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
	
	public boolean isEmotionWorry(Goal eventGoal, WHOSE_APPRAISAL whoseAppraisal) {
		if (this.relevanceValue.equals(RELEVANCE.RELEVANT))
			if ((this.desirabilityValue.equals(DESIRABILITY.UNDESIRABLE)) ||
				(this.desirabilityValue.equals(DESIRABILITY.HIGH_UNDESIRABLE)))
				if (this.controllabilityValue.equals(CONTROLLABILITY.UNCONTROLLABLE))
					if (this.expectednessValue.equals(EXPECTEDNESS.UNEXPECTED))
						if (whoseAppraisal.equals(WHOSE_APPRAISAL.HUMAN)) {
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
	
	public boolean isEmotionFrustration(Goal eventGoal, WHOSE_APPRAISAL whoseAppraisal) {
		if (this.relevanceValue.equals(RELEVANCE.RELEVANT))
			if (this.desirabilityValue.equals(DESIRABILITY.UNDESIRABLE))
				if (this.controllabilityValue.equals(CONTROLLABILITY.UNCONTROLLABLE))
					if ((this.expectednessValue.equals(EXPECTEDNESS.EXPECTED)) ||
						(this.expectednessValue.equals(EXPECTEDNESS.MOST_EXPECTED)))
						if (whoseAppraisal.equals(WHOSE_APPRAISAL.HUMAN)) {
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
	
	public boolean isEmotionNegativeSurprise(Goal eventGoal, WHOSE_APPRAISAL whoseAppraisal) {
		if (this.relevanceValue.equals(RELEVANCE.RELEVANT))
			if (this.expectednessValue.equals(EXPECTEDNESS.MOST_UNEXPECTED))
				if ((this.desirabilityValue.equals(DESIRABILITY.UNDESIRABLE)) ||
					(this.desirabilityValue.equals(DESIRABILITY.HIGH_UNDESIRABLE)))
					if (whoseAppraisal.equals(WHOSE_APPRAISAL.HUMAN)) {
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
	
	public boolean isEmotionGuilt(Goal eventGoal, WHOSE_APPRAISAL whoseAppraisal) {
		if (this.relevanceValue.equals(RELEVANCE.RELEVANT))
			if ((this.desirabilityValue.equals(DESIRABILITY.UNDESIRABLE)) ||
				(this.desirabilityValue.equals(DESIRABILITY.HIGH_UNDESIRABLE)))
				if ((this.controllabilityValue.equals(CONTROLLABILITY.LOW_CONTROLLABLE)) ||
					(this.controllabilityValue.equals(CONTROLLABILITY.HIGH_CONTROLLABLE)))
					if ((this.expectednessValue.equals(EXPECTEDNESS.EXPECTED)) ||
						(this.expectednessValue.equals(EXPECTEDNESS.MOST_EXPECTED)))
						if (whoseAppraisal.equals(WHOSE_APPRAISAL.HUMAN)) {
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
	
	public EMOTION_INSTANCE getEmotionInstance() {
		
		if (isEmotionJoy(goal, whoseAppraisal))
			return EMOTION_INSTANCE.JOY;
		else if (isEmotionGratitude(goal, whoseAppraisal))
			return EMOTION_INSTANCE.GRATITUDE;
		else if (isEmotionSadness(goal, whoseAppraisal))
			return EMOTION_INSTANCE.SADNESS;
		else if (isEmotionPositiveSurprise(goal, whoseAppraisal))
			return EMOTION_INSTANCE.POSTIVE_SURPRISE;
		else if (isEmotionNegativeSurprise(goal, whoseAppraisal))
			return EMOTION_INSTANCE.NEGATIVE_SURPRISE;
		else if (isEmotionAnger(goal, whoseAppraisal))
			return EMOTION_INSTANCE.ANGER;
		else if (isEmotionWorry(goal, whoseAppraisal))
			return EMOTION_INSTANCE.WORRY;
		else if (isEmotionFrustration(goal, whoseAppraisal))
			return EMOTION_INSTANCE.FRUSTRATION;
		else if (isEmotionGuilt(goal, whoseAppraisal))
			return EMOTION_INSTANCE.GUILT;
		else if (isEmotionNeutral(goal, whoseAppraisal))
			return EMOTION_INSTANCE.NEUTRAL;
		else
			throw new IllegalStateException("No matching emotion!");
	}
}
