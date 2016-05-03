package MetaInformation;

import Mechanisms.Mechanisms.AGENT;
import MentalState.Goal;
import MentalState.Motive.MOTIVE_INTENSITY;

public class CopingElicitationFrame {

	public enum COPING_OBJECT {SELF, OTHER, ENVIRONMENT};
	public enum COPING_STRATEGY {PLANNING, ACTIVE_COPING, SEEKING_SOCIAL_SUPPORT_FOR_INSTRUMENTAL_REASONS, SEEKING_SOCIAL_SUPPORT_FOR_EMOTIONAL_REASONS,
		SUPPRESION_OF_COMPETING_ACTIVITIES, RESTRAINT_COPING, POSITIVE_REINTERPRETATION, ACCEPTANCE, AVOIDANCE, VENTING, DENIAL,
		BEHAVIORAL_DISENGAGEMENT, MENTAL_DISENGAGEMENT, MAKING_AMENDS, SHIFTING_RESPONSIBILITY, WISHFUL_THINKING};
	
	private COPING_OBJECT copingObject;
	private AGENT eventCause;
	private AppraisalVector copingEffect;
	private MotivationVector motiveVector;
	private COPING_STRATEGY copingStrategy;
	
	public CopingElicitationFrame(COPING_STRATEGY copingStrategy, Goal goal, COPING_OBJECT copingObject, AGENT eventCause) {
		this.copingObject   = copingObject;
		this.eventCause     = eventCause;
//		this.copingEffect   = ; This should use anticipated appraisals!
		this.motiveVector   = goal.getMotivesVector();
		this.copingStrategy = copingStrategy;
	}
	
	public boolean isThisStrategySelected() {
		// 1. Whether we need to do it? ==> Motives
		// 2. Whether we can do it?     ==> Controllability
		// 3. Whether we should do it?  ==> Desirability
		return true;
	}
	
	private boolean isThisStrategyNeeded() {
		
		switch (copingStrategy) {
			case PLANNING:
				if (((motiveVector.getSatisfactionMotive().getMotiveIntensity() > 0) || (motiveVector.getSatisfactionMotive().getMotiveIntensity() < 0)) &&
					((motiveVector.getAchievementMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.HIGH_POSITIVE)) || 
					 (motiveVector.getExternalMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.HIGH_POSITIVE))))
					return true;
				else
					return false;
			case ACTIVE_COPING:
				if (((motiveVector.getSatisfactionMotive().getMotiveIntensity() > 0) || (motiveVector.getSatisfactionMotive().getMotiveIntensity() > 0)) &&
					((motiveVector.getAchievementMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.MEDIUM_POSITIVE)) || 
					 (motiveVector.getExternalMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.MEDIUM_POSITIVE))))
					return true;
				else
					return false;
			case SEEKING_SOCIAL_SUPPORT_FOR_INSTRUMENTAL_REASONS:
				if (((motiveVector.getSatisfactionMotive().getMotiveIntensity() > 0) || (motiveVector.getSatisfactionMotive().getMotiveIntensity() > 0)) &&
					((motiveVector.getAchievementMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.LOW_POSITIVE)) || 
					 (motiveVector.getExternalMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.LOW_POSITIVE))))
					return true;
				else
					return false;
			case ACCEPTANCE:
				if ((motiveVector.getSatisfactionMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.HIGH_NEGATIVE)) &&
					((motiveVector.getAchievementMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.HIGH_NEGATIVE)) || 
					 (motiveVector.getExternalMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.HIGH_NEGATIVE))))
					return true;
				else
					return false;
			case MENTAL_DISENGAGEMENT:
				if (((motiveVector.getSatisfactionMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.LOW_NEGATIVE)) || 
					 (motiveVector.getSatisfactionMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.MEDIUM_NEGATIVE))) &&
					 (((motiveVector.getAchievementMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.LOW_NEGATIVE)) || 
					   (motiveVector.getAchievementMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.MEDIUM_NEGATIVE))) || 
					   ((motiveVector.getExternalMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.LOW_NEGATIVE) || 
						(motiveVector.getExternalMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.MEDIUM_NEGATIVE))))))
					return true;
				else
					return false;
			case SHIFTING_RESPONSIBILITY:
				if (motiveVector.getSatisfactionMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.HIGH_NEGATIVE) &&
					(((motiveVector.getAchievementMotive().getMotiveIntensity() > 0) || (motiveVector.getAchievementMotive().getMotiveIntensity() < 0)) ||
					 ((motiveVector.getExternalMotive().getMotiveIntensity() > 0) || (motiveVector.getExternalMotive().getMotiveIntensity() < 0))))
					return true;
				else
					return false;
			case WISHFUL_THINKING:
				if ((motiveVector.getSatisfactionMotive().getMotiveIntensity() > 0) &&
					 (((((motiveVector.getAchievementMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.LOW_NEGATIVE)) || 
					   (motiveVector.getAchievementMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.MEDIUM_NEGATIVE)))) ||
					   ((motiveVector.getAchievementMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.LOW_POSITIVE)) || 
						(motiveVector.getAchievementMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.MEDIUM_POSITIVE)))) ||
					    ((motiveVector.getExternalMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.LOW_NEGATIVE) || 
						 (motiveVector.getExternalMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.MEDIUM_NEGATIVE))) ||
					     (motiveVector.getExternalMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.LOW_POSITIVE) || 
						 (motiveVector.getExternalMotive().getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.MEDIUM_POSITIVE))))))
					return true;
				else
					return false;
			default:
				throw new IllegalArgumentException("Illegal Coping Strategy: " + copingStrategy);	
		}
	}
}
