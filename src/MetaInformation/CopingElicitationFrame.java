package MetaInformation;

import Mechanisms.Mechanisms.AGENT;
import MentalState.Goal;

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
	
	public CopingElicitationFrame(Goal goal, COPING_OBJECT copingObject, AGENT eventCause, COPING_STRATEGY copingStrategy) {
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
}
