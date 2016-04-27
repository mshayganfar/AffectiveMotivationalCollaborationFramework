package Mechanisms.Appraisal;

import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Motivation.Motivation;
import Mechanisms.ToM.ToM;
import MentalState.Goal;
import MetaInformation.MentalProcesses;

public class Coping {
	
	private Collaboration collaboration;
	private Motivation motivation;
	private ToM tom;
	
	private Relevance relevance;
	private Controllability controllability;
	private Desirability desirability;
	private Expectedness expectedness;
	
	private DiscoActionsWrapper discoActionsWrapper;
	
	public void prepareAppraisalsOfToM(MentalProcesses mentalProcesses) {
		this.collaboration = mentalProcesses.getCollaborationMechanism();
		this.motivation    = mentalProcesses.getMotivationMechanism();
		this.tom		   = mentalProcesses.getToMMechanism();
		
		this.relevance       = mentalProcesses.getRelevanceProcess();
		this.controllability = mentalProcesses.getControllabilityProcess();
		this.desirability    = mentalProcesses.getDesirabilityProcess();
		this.expectedness    = mentalProcesses.getExpectednessProcess();
		
		discoActionsWrapper = new DiscoActionsWrapper(collaboration);
	}
	
	// Taking actions to try to remove or circumvent the stressor or to ameliorate its effects.
	private void doActiveCoping(Goal goal, boolean human) {
		discoActionsWrapper.executeTask(goal, human);
	}
	
	// Goal management: Coming up with best action strategies to handle the problem. 
	private void doPlanning() {
		
	}
	
	// Seeking advice, assistance or information.
	private void doSeekingSocialSupportForInstrumentalReasons() {
		
	}
	
	// Seeking emotional support or understanding.
	private void doSeekingSocialSupportForEmotionalReasons() {
		
	}
	
	// Putting other (internal & external) distractors aside. 
	private void doSuppressionOfCompetingActivities() {
		
	}
	
	// Waiting till an appropriate opportunity.
	private void doRestraintCoping() {
		
	}
	
	// Construing a stressful situation in positive way.
	private void doPositiveReinterpretation() {
		
	}
	
	// Acceptance of a stressor as real (specially when the event has low changeability).
	private void doAcceptance(Goal goal, boolean human) {
		discoActionsWrapper.acceptProposedTask(goal, human);
	}
	
	private void doAvoidance(Goal goal, boolean human) {
		discoActionsWrapper.rejectProposedTask(goal, human);
	}

	// Focusing on whatever is the source of stress (e.g., trying to accomodate loss).
	private void doVenting() {
		
	}
	
	// Refusing to believe that the stressor exists or trying to act that the stressor is not real.
	private void doDenial() {
		
	}
	
	// Reducing effort on attaining the goal with which the stressor is interferring.
	private void doBehavioralDisengagement() {
		
	}
	
	// Distracting the self from thinking about behavioral dimension or goal with which the stressor is interferring. 
	private void doMentalDisengagement() {
		
	}
	
	private void doMakingAmends() {
		
	}
	
	private void doShiftingResponsibility() {
		
	}
	
	private void doWishfulThinking() {
		
	}
}