package MetaInformation;

import Mechanisms.Appraisal.Controllability.CONTROLLABILITY;
import MentalState.Goal;
import MentalState.Motive;
import MentalState.Motive.MOTIVE_INTENSITY;

public class CopingActivation {

	public enum COPING_OBJECT {SELF, OTHER, ENVIRONMENT};
	public enum COPING_STRATEGY {PLANNING, ACTIVE_COPING, SEEKING_SOCIAL_SUPPORT_FOR_INSTRUMENTAL_REASONS, SEEKING_SOCIAL_SUPPORT_FOR_EMOTIONAL_REASONS,
		SUPPRESION_OF_COMPETING_ACTIVITIES, RESTRAINT_COPING, POSITIVE_REINTERPRETATION, ACCEPTANCE, AVOIDANCE, VENTING, DENIAL,
		BEHAVIORAL_DISENGAGEMENT, MENTAL_DISENGAGEMENT, MAKING_AMENDS, SHIFTING_RESPONSIBILITY, WISHFUL_THINKING};
	
//	private COPING_OBJECT copingObject;
//	private AGENT eventCause;
	private AppraisalVector copingEffect;
	private MotivationVector motiveVector;
	private COPING_STRATEGY copingStrategy;
	private MentalProcesses mentalProcesses;
	private Goal goal;
	
	public CopingActivation(COPING_STRATEGY copingStrategy, MentalProcesses mentalProcesses, Goal goal/*, COPING_OBJECT copingObject, AGENT eventCause*/) {
//		this.copingObject    = copingObject;
//		this.eventCause      = eventCause;
//		this.copingEffect    = ; This should use anticipated appraisals!
		this.motiveVector    = goal.getMotivesVector();
		this.copingStrategy  = copingStrategy;
		this.mentalProcesses = mentalProcesses;
		this.goal            = goal;
	}
	
	public boolean isThisStrategySelected() {
		if (isThisStrategyNeeded())
			if(canPursueThisStrategy())
				return true;
			else
				return false;
		else
			return false;
	}
	
	private boolean isThisStrategyNeeded() {
		
		Motive satisfactionMotive = motiveVector.getSatisfactionMotive();
		Motive achievementMotive  = motiveVector.getAchievementMotive();
		Motive externalMotive     = motiveVector.getExternalMotive();
		
		switch (copingStrategy) {
			case PLANNING:
				if ((satisfactionMotive.getMotiveIntensity() >= 0) || (satisfactionMotive.getMotiveIntensity() < 0)) {
					if (achievementMotive != null)
						if (achievementMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.HIGH_POSITIVE))
							return true;
					if (externalMotive != null) {
						if (externalMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.HIGH_POSITIVE))
							return true;
						else
							return false;
					} else
						return false;
				} else
					return false;
			case ACTIVE_COPING:
				if ((satisfactionMotive.getMotiveIntensity() >= 0) || (satisfactionMotive.getMotiveIntensity() < 0)) {
					if (achievementMotive != null)
						if ((achievementMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.MEDIUM_POSITIVE)) ||
							(achievementMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.HIGH_POSITIVE)))
							return true;
					if (externalMotive != null) {
						if (externalMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.MEDIUM_POSITIVE))
							return true;
						else
							return false;
					} else
						return false;
				} else
					return false;
			case SEEKING_SOCIAL_SUPPORT_FOR_INSTRUMENTAL_REASONS:
				if ((satisfactionMotive.getMotiveIntensity() >= 0) || (satisfactionMotive.getMotiveIntensity() < 0)) {
					if (achievementMotive != null)
						if (achievementMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.LOW_POSITIVE))
							return true;
					if (externalMotive != null) {
						if (externalMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.LOW_POSITIVE))
							return true;
						else
							return false;
					} else
						return false;
				} else
					return false;
			case ACCEPTANCE:
				if (satisfactionMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.HIGH_NEGATIVE)) {
					if (achievementMotive != null)
						if (achievementMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.HIGH_NEGATIVE))
							return true;
					if (externalMotive != null) {
						if (externalMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.HIGH_NEGATIVE))
							return true;
						else
							return false;
					} else
						return false;
				} else
					return false;
			case MENTAL_DISENGAGEMENT:
				if ((satisfactionMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.LOW_NEGATIVE)) || 
					 (satisfactionMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.MEDIUM_NEGATIVE))) {
					if (achievementMotive != null)
						if ((achievementMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.LOW_NEGATIVE)) || 
							(achievementMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.MEDIUM_NEGATIVE)))
							return true;
					if (externalMotive != null) {
						if ((externalMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.LOW_NEGATIVE)) ||
							(externalMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.MEDIUM_NEGATIVE)))
							return true;
						else
							return false;
					} else
						return false;
				} else
					return false;
			case SHIFTING_RESPONSIBILITY:
				if (satisfactionMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.HIGH_NEGATIVE)) {
					if (achievementMotive != null)
						if ((achievementMotive.getMotiveIntensity() >= 0) || (achievementMotive.getMotiveIntensity() < 0))
							return true;
					if (externalMotive != null) {
						if ((externalMotive.getMotiveIntensity() >= 0) || (externalMotive.getMotiveIntensity() < 0))
							return true;
						else
							return false;
					} else
						return false;
				} else
					return false;
			case WISHFUL_THINKING:
				if (satisfactionMotive.getMotiveIntensity() >= 0) {
					if (achievementMotive != null)
						if ((achievementMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.LOW_NEGATIVE)) ||
							(achievementMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.MEDIUM_NEGATIVE)) ||
							(achievementMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.LOW_POSITIVE)) ||
							(achievementMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.MEDIUM_POSITIVE)))
							return true;
					if (externalMotive != null) {
						if ((externalMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.LOW_NEGATIVE)) ||
							(externalMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.MEDIUM_NEGATIVE)) ||
							(externalMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.LOW_POSITIVE)) ||
							(externalMotive.getSymbolicMotiveIntensity().equals(MOTIVE_INTENSITY.MEDIUM_POSITIVE)))
							return true;
						else
							return false;
					} else
						return false;
				} else
					return false;
			default:
				throw new IllegalArgumentException("Illegal Coping Strategy: " + copingStrategy);
		}
	}
	
	private boolean canPursueThisStrategy() {
		
		CONTROLLABILITY controllability = mentalProcesses.getControllabilityProcess().isEventControllable(goal);
		
		switch (copingStrategy) {
			case PLANNING:
				if (controllability.equals(CONTROLLABILITY.HIGH_CONTROLLABLE))
					return true;
				else
					return false;
			case ACTIVE_COPING:
				if ((controllability.equals(CONTROLLABILITY.HIGH_CONTROLLABLE)) || 
					(controllability.equals(CONTROLLABILITY.UNCONTROLLABLE)) ||
					(controllability.equals(CONTROLLABILITY.LOW_CONTROLLABLE)))
					return true;
				else
					return false;
			case SEEKING_SOCIAL_SUPPORT_FOR_INSTRUMENTAL_REASONS:
				if (controllability.equals(CONTROLLABILITY.LOW_CONTROLLABLE))
					return true;
				else
					return false;
			case ACCEPTANCE:
				if (controllability.equals(CONTROLLABILITY.UNCONTROLLABLE))
					return true;
				else
					return false;
			case MENTAL_DISENGAGEMENT:
				if (controllability.equals(CONTROLLABILITY.UNCONTROLLABLE))
					return true;
				else
					return false;
			case SHIFTING_RESPONSIBILITY:
				if ((controllability.equals(CONTROLLABILITY.UNCONTROLLABLE)) || (controllability.equals(CONTROLLABILITY.LOW_CONTROLLABLE)))
					return true;
				else
					return false;
			case WISHFUL_THINKING:
				if (controllability.equals(CONTROLLABILITY.UNCONTROLLABLE))
					return true;
				else
					return false;
			default:
				throw new IllegalArgumentException("Illegal Controllability Value: " + controllability);
		}
	}
	
//	private boolean isThisStrategySatisfactory() {
//		
//		DESIRABILITY desirability = mentalProcesses.getDesirabilityProcess().isEventDesirable(goal);
//		
//		switch (copingStrategy) {
//			case PLANNING:
//				if(desirability.equals(DESIRABILITY.UNDESIRABLE) || desirability.equals(DESIRABILITY.NEUTRAL) || 
//				   desirability.equals(DESIRABILITY.DESIRABLE) || desirability.equals(DESIRABILITY.HIGH_DESIRABLE))
//					return true;
//				else
//					return false;
//			case ACTIVE_COPING:
//				if(desirability.equals(DESIRABILITY.NEUTRAL) || desirability.equals(DESIRABILITY.UNDESIRABLE) ||
//				   desirability.equals(DESIRABILITY.HIGH_UNDESIRABLE))
//					return true;
//				else
//					return false;
//			case SEEKING_SOCIAL_SUPPORT_FOR_INSTRUMENTAL_REASONS:
//				if(desirability.equals(DESIRABILITY.DESIRABLE) || desirability.equals(DESIRABILITY.NEUTRAL) ||
//				   desirability.equals(DESIRABILITY.UNDESIRABLE) || desirability.equals(DESIRABILITY.HIGH_UNDESIRABLE))
//					return true;
//				else
//					return false;
//			case ACCEPTANCE:
//				if(desirability.equals(DESIRABILITY.UNDESIRABLE) || desirability.equals(DESIRABILITY.HIGH_UNDESIRABLE))
//					return true;
//				else
//					return false;
//			case MENTAL_DISENGAGEMENT:
//				if(desirability.equals(DESIRABILITY.NEUTRAL) || desirability.equals(DESIRABILITY.UNDESIRABLE) ||
//				   desirability.equals(DESIRABILITY.HIGH_UNDESIRABLE))
//					return true;
//				else
//					return false;
//			case SHIFTING_RESPONSIBILITY:
//				if(desirability.equals(DESIRABILITY.UNDESIRABLE) || desirability.equals(DESIRABILITY.HIGH_UNDESIRABLE))
//					return true;
//				else
//					return false;
//			case WISHFUL_THINKING:
//				if(desirability.equals(DESIRABILITY.DESIRABLE) || desirability.equals(DESIRABILITY.NEUTRAL) ||
//				   desirability.equals(DESIRABILITY.UNDESIRABLE) || desirability.equals(DESIRABILITY.HIGH_UNDESIRABLE))
//					return true;
//				else
//					return false;
//			default:
//				throw new IllegalArgumentException("Illegal Desirability Value: " + desirability);
//		}
//	}
}
