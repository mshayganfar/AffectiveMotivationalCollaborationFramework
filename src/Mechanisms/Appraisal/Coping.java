package Mechanisms.Appraisal;

import Mechanisms.Action.Action;
import Mechanisms.Action.DiscoActionsWrapper;
import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Motivation.Motivation;
import Mechanisms.Perception.Perception;
import Mechanisms.ToM.ToM;
import MentalState.Goal;
import MentalState.Intention;
import MetaInformation.CopingActivation;
import MetaInformation.MentalProcesses;
import MetaInformation.CopingActivation.COPING_STRATEGY;

public class Coping {
	
	private MentalProcesses mentalProcesses;
	
	private Perception perception;
	private Collaboration collaboration;
	private Motivation motivation;
	private ToM tom;
	private Action action;
	
	private Relevance relevance;
	private Controllability controllability;
	private Desirability desirability;
	private Expectedness expectedness;
	
	private DiscoActionsWrapper discoActionsWrapper;
	
	public void prepareCopingMechanism(MentalProcesses mentalProcesses) {
		
		this.mentalProcesses = mentalProcesses;
		
		this.perception    = mentalProcesses.getPerceptionMechanism();
		this.collaboration = mentalProcesses.getCollaborationMechanism();
		this.motivation    = mentalProcesses.getMotivationMechanism();
		this.tom		   = mentalProcesses.getToMMechanism();
		this.action        = mentalProcesses.getActionMechanism();
		
		this.relevance       = mentalProcesses.getRelevanceProcess();
		this.controllability = mentalProcesses.getControllabilityProcess();
		this.desirability    = mentalProcesses.getDesirabilityProcess();
		this.expectedness    = mentalProcesses.getExpectednessProcess();
		
//		discoActionsWrapper  = new DiscoActionsWrapper(collaboration);
	}
	
	public void formIntentions(Goal eventGoal) {
		
		CopingActivation planningStrategy		 = new CopingActivation(COPING_STRATEGY.PLANNING, mentalProcesses, eventGoal);
		CopingActivation activeCopingStrategy    = new CopingActivation(COPING_STRATEGY.ACTIVE_COPING, mentalProcesses, eventGoal);
		CopingActivation acceptanceStrategy 	 = new CopingActivation(COPING_STRATEGY.ACCEPTANCE, mentalProcesses, eventGoal);
		CopingActivation wishfulThinkingStrategy = new CopingActivation(COPING_STRATEGY.WISHFUL_THINKING, mentalProcesses, eventGoal);
		CopingActivation mentalDisengagementStrategy    = new CopingActivation(COPING_STRATEGY.MENTAL_DISENGAGEMENT, mentalProcesses, eventGoal);
		CopingActivation shiftingResponsibilityStrategy = new CopingActivation(COPING_STRATEGY.SHIFTING_RESPONSIBILITY, mentalProcesses, eventGoal);
		CopingActivation seekingSocialSupportForInstrumentalReasonsStrategy = new CopingActivation(COPING_STRATEGY.SEEKING_SOCIAL_SUPPORT_FOR_INSTRUMENTAL_REASONS, mentalProcesses, eventGoal);
		
		if (planningStrategy.isThisStrategySelected())
			eventGoal.addIntentions(new Intention(eventGoal, COPING_STRATEGY.PLANNING));
		if (activeCopingStrategy.isThisStrategySelected())
			eventGoal.addIntentions(new Intention(eventGoal, COPING_STRATEGY.ACTIVE_COPING));
		if (acceptanceStrategy.isThisStrategySelected())
			eventGoal.addIntentions(new Intention(eventGoal, COPING_STRATEGY.ACCEPTANCE));
		if (wishfulThinkingStrategy.isThisStrategySelected())
			eventGoal.addIntentions(new Intention(eventGoal, COPING_STRATEGY.WISHFUL_THINKING));
		if (mentalDisengagementStrategy.isThisStrategySelected())
			eventGoal.addIntentions(new Intention(eventGoal, COPING_STRATEGY.MENTAL_DISENGAGEMENT));
		if (shiftingResponsibilityStrategy.isThisStrategySelected())
			eventGoal.addIntentions(new Intention(eventGoal, COPING_STRATEGY.SHIFTING_RESPONSIBILITY));
		if (seekingSocialSupportForInstrumentalReasonsStrategy.isThisStrategySelected())
			eventGoal.addIntentions(new Intention(eventGoal, COPING_STRATEGY.SEEKING_SOCIAL_SUPPORT_FOR_INSTRUMENTAL_REASONS));
	}
	
	// Taking actions to try to remove or circumvent the stressor or to ameliorate its effects.
	public void doActiveCoping(Goal goal) {
		
		System.out.println("COPING STRATEGY: Active Coping");
		
		if (perception.getEmotionValence() < 0) {
			action.acknowledgeEmotion(goal);
		}
		
		action.expressEmotion(goal);
	}
	
	// Goal management: Coming up with best action strategies to handle the problem, aka, action selection.
	public void doPlanning(Goal goal, boolean human) {
		
		System.out.println("COPING STRATEGY: Planning");
		
		discoActionsWrapper.executeTask(goal, human);
	}
	
	public void doPlanning() {
		
		System.out.println("COPING STRATEGY: (automatic) Planning");
		
		discoActionsWrapper.executeTask();
	}

	// Seeking advice, assistance or information.
	public void doSeekingSocialSupportForInstrumentalReasons() {
		
		System.out.println("COPING STRATEGY: Seeking Social Support for Instrumental Reasons");
	}
	
	// Seeking emotional support or understanding.
	private void doSeekingSocialSupportForEmotionalReasons() {
		
	}
	
	// Putting other (internal & external) distractors aside. 
	private void doSuppressionOfCompetingActivities() {
		
	}
	
	// Waiting till an appropriate opportunity, aka, procrastination.
	private void doRestraintCoping() {
		
	}
	
	// Construing a stressful situation in positive way.
	private void doPositiveReinterpretation() {
		
	}
	
	// Acceptance of a stressor as real (specially when the event has low changeability).
	public void doAcceptance(Goal goal, boolean human) {
		// discoActionsWrapper.acceptProposedTask(goal, human);
		System.out.println("COPING STRATEGY: Acceptance");
	}
	
	// Showing reflexive reaction to remove threat.
	private void doAvoidance(Goal goal, boolean human) {
		discoActionsWrapper.rejectProposedTask(goal, human);
	}

	// Focusing on whatever is the source of stress (e.g., trying to accomodate loss).
	private void doVenting() {
		
	}
	
	// Refusing to believe that the stressor exists or trying to act that the stressor is not real.
	private void doDenial() {
		
	}
	
	// Droping an intention to achieve a goal.
	private void doResignation() {
		
	}
		
	// Reducing effort on attaining the goal with which the stressor is interferring.
	private void doBehavioralDisengagement() {
		
	}
	
	// Distracting the self from thinking about behavioral dimension or goal with which the stressor is interferring. 
	public void doMentalDisengagement() {
		
		System.out.println("COPING STRATEGY: Mental Disengagement");
	}
	
	// Taking an action to redress the harm and mitigate the negative feeling(s).
	private void doMakingAmends() {
		
	}
	
	// Shifts an attribution of blame/credit from (towards) the self and towards (from) some other agent.
	public void doShiftingResponsibility() {
		
		System.out.println("COPING STRATEGY: Shifting Responsibility");
	}
	
	// Assume some intervening act or actor will improve desirability.
	public void doWishfulThinking() {
		
		System.out.println("COPING STRATEGY: Wishful Thinking");
	}
}
