package Mechanisms.Appraisal;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextArea;

import GUI.AMCFrame;
import Mechanisms.Mechanisms.AGENT;
import Mechanisms.Action.Action;
import Mechanisms.Action.DiscoActionsWrapper;
import Mechanisms.Appraisal.Controllability.CONTROLLABILITY;
import Mechanisms.Appraisal.Desirability.DESIRABILITY;
import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Collaboration.GoalManagement;
import Mechanisms.Collaboration.Collaboration.GOAL_STATUS;
import Mechanisms.Motivation.Motivation;
import Mechanisms.Perception.Perception;
import Mechanisms.ToM.ToM;
import MentalState.Goal;
import MentalState.Intention;
import MetaInformation.CopingActivation;
import MetaInformation.MentalProcesses;
import MetaInformation.Turns;
import MetaInformation.CopingActivation.COPING_STRATEGY;
import MetaInformation.World.USER_VALENCE;
import edu.wpi.cetask.DecompositionClass;
import edu.wpi.cetask.Plan;
import edu.wpi.cetask.Task;
import edu.wpi.cetask.TaskClass.Input;
import edu.wpi.disco.Disco;
import edu.wpi.disco.Interaction;
import edu.wpi.disco.lang.Ask;
import edu.wpi.disco.lang.Propose.Success;
import edu.wpi.disco.lang.Say;

public class Coping {
	
	private MentalProcesses mentalProcesses;
	private AMCFrame frame;
	
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
	
	public Coping(AMCFrame frame) {
		this.frame = frame;
	}
	
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
		
		discoActionsWrapper  = new DiscoActionsWrapper(mentalProcesses, frame);
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
		
		if (didHumanAsk(goal))
			respondToHuman(goal);
		
		if (isTaskDelegationPossible(goal)) {
//			Plan successorPlan = getDelegationSuccessor(goal.getPlan().getRetryOf());
//			successorPlan.getGoal().setExternal(false);
//			discoActionsWrapper.proposeTaskWho(successorPlan, false, false, null);
			delegateTask(goal, false, true);
		}
	}
	
	private boolean isTaskDelegationPossible(Goal goal) {
		
		Plan plan = goal.getPlan().getRetryOf();
		if (plan != null) {
			if (collaboration.getGoalStatus(plan).equals(GOAL_STATUS.FAILED))
				if (collaboration.getResponsibleAgent(plan).equals(AGENT.SELF))
					if (!plan.getRetry().isDone())
						if (collaboration.getWorld().getUserValence().equals(USER_VALENCE.NEGATIVE))
//							if (getDelegationSuccessor(plan) != null)
							return true;
		}
		return false;
	}
	
	private void delegateTask(Goal goal, boolean delegatee, boolean delegator) {
		
		Plan plan = goal.getPlan();
		plan.getGoal().setExternal(true);
		discoActionsWrapper.proposeTaskWho(plan, delegatee, delegator, null);
	}
	
	private Plan getDelegationSuccessor(Plan plan) {
		
		for (Plan successor : plan.getRetry().getParent().getSuccessors()) {
			if (collaboration.getResponsibleAgent(successor).equals(AGENT.OTHER))
				if (!successor.isDone())
					return successor;
		}
		return null;
	}
	
	private void respondToHuman(Goal goal) {
		
		// This needs to be fixed. --> I do not know why!!!
		if (didHumanAskAboutTaskWhat(goal)) {
			String inputName = ((Ask.What)goal.getPlan().getGoal()).getSlot();
			if (knowInputValue(goal, inputName))
				discoActionsWrapper.proposeTaskWhat(goal, false, inputName, getInputValue(goal, inputName));
			else
				discoActionsWrapper.rejectProposedTask(goal, false);
		}
		
		if (didHumanAskAboutTaskHow(goal)) {
			if (knowHowToDo(goal))
				discoActionsWrapper.proposeTaskHow(goal, false, getAlternativeRecipe(goal));
			else
				discoActionsWrapper.rejectProposedTask(goal, false);
		}
		
		if (didHumanAskAboutTaskShould(goal)) {
			if (controllability.isEventControllable(goal).equals(CONTROLLABILITY.HIGH_CONTROLLABLE))
				discoActionsWrapper.acceptProposedTask(goal.getPlan(), false);
			else if (controllability.isEventControllable(goal).equals(CONTROLLABILITY.UNCONTROLLABLE))
				discoActionsWrapper.rejectProposedTask(goal, false);
			else
				doSeekingSocialSupportForEmotionalReasons();
		}
		
		if (didHumanAskAboutTaskWho(goal)) {
			if (knowWhoShouldDo(goal)) {
				AGENT responsibleAgent = collaboration.getResponsibleAgent(goal.getPlan());
				if (responsibleAgent.equals(AGENT.SELF))
					discoActionsWrapper.proposeTaskWho(goal, false);
				else if (responsibleAgent.equals(AGENT.OTHER))
					discoActionsWrapper.proposeTaskWho(goal, true);
				else if (responsibleAgent.equals(AGENT.BOTH))
					discoActionsWrapper.saySomethingAboutTask(false, "Both of us are responsible for this task!"); // --> This might be wrong! What do we need to do here?!
			}
			else
				discoActionsWrapper.saySomethingAboutTask(false, "I do not know who is responsible for this task!");
		}
	}
	
	private boolean didHumanAskAboutTaskWho(Goal goal) {
		return (goal.getPlan().getGoal() instanceof Ask.Who) ? true : false;
	}
	
	private boolean didHumanAskAboutTaskShould(Goal goal) {
		return (goal.getPlan().getGoal() instanceof Ask.Should) ? true : false;
	}
	
	private DecompositionClass getAlternativeRecipe(Goal goal) {
		// TO DO: I can choose between available alternative recipes.
		return goal.getPlan().getDecompositions().get(0);
	}
	
	private boolean didHumanAskAboutTaskHow(Goal goal) {
		return (goal.getPlan().getGoal() instanceof Ask.How) ? true : false;
	}
	
	private Object getInputValue(Goal goal, String inputName) {
		
		Object value = null;
		Task askWhatGoal = ((Ask.What) goal.getPlan().getGoal()).getGoal();
		
		if (askWhatGoal.isDefinedSlot(inputName))
			value = (String) askWhatGoal.getSlotValue(inputName);
		
		if (value == null)
			value = collaboration.getInputValue(goal.getPlan(), inputName);
		
		return value;
	}
	
	private boolean didHumanAskAboutTaskWhat(Goal goal) {
		return (goal.getPlan().getGoal() instanceof Ask.What) ? true : false;
	}
	
	private boolean didHumanAsk(Goal goal) {
		return (goal.getPlan().getGoal() instanceof Ask) ? true : false;
	}
	
	// The same Planning used in Disco.
	public void doPlanning(Goal goal, boolean human, boolean postconditionStatus) {
		
		System.out.println("COPING STRATEGY: Planning");
		
//		if ((goal.getPlan().getGoal().getExternal() == null) || (!goal.getPlan().getGoal().getExternal()))
		discoActionsWrapper.executeTask(goal, human, postconditionStatus);
	}
	
	public void doPlanning() {
		
		System.out.println("COPING STRATEGY: (automatic) Planning");
		
		discoActionsWrapper.executeTask();
	}

	// Seeking advice, assistance or information.
	public void doSeekingSocialSupportForInstrumentalReasons(Goal goal) {
		
		System.out.println("COPING STRATEGY: Seeking Social Support for Instrumental Reasons");
		
		if (perception.getEmotionValence() >= 0) {
			if (!knowWhetherAchieve(goal))
				discoActionsWrapper.askAboutTaskShould(goal, false);
			if (!knowInputValue(goal))
				discoActionsWrapper.askAboutTaskWhat(goal, false, getUnknownInput(goal));
			if (!knowHowToDo(goal))
				discoActionsWrapper.askAboutTaskHow(goal, false);
			if (!knowWhoShouldDo(goal))
				discoActionsWrapper.askAboutTaskWho(goal, false);
		}
	}
	
	private boolean knowWhetherAchieve(Goal goal) {
		
		Interaction interaction = collaboration.getInteraction();
		
		if (interaction.getSystem().generateBest(interaction) != null)
//			|| (goal.getPlan().getGoal() instanceof Propose.Should)) --> Check this later whether it is required?
			return true;
		else
			return false;
	}
	
	private boolean knowInputValue(Goal goal, String inputName) {
		
		Plan plan = goal.getPlan();
		
		if (!plan.getGoal().isDefinedSlot(inputName))
			if (collaboration.getInputValue(plan, inputName) == null)
				return false;
		
		return true;
	}
	
	private boolean knowInputValue(Goal goal) {
		
		for (Input input : goal.getPlan().getType().getDeclaredInputs())
			if (!input.isDefinedSlot(goal.getPlan().getGoal()))
				if (collaboration.getInputValue(goal.getPlan(), input.getName()) == null)
					return false;
		
		return true;
	}
	
	private Input getUnknownInput(Goal goal) {
		
		for (Input input : goal.getPlan().getType().getDeclaredInputs())
			if (!input.isDefinedSlot(goal.getPlan().getGoal()))
				if (collaboration.getInputValue(goal.getPlan(), input.getName()) == null)
					return input;
		
		return null;
	}
	
	private boolean knowHowToDo(Goal goal, DecompositionClass askedDecomp) {
		
		for (DecompositionClass decomp : goal.getPlan().getDecompositions())
			if (decomp.getId().equals(askedDecomp.getId()))
				return true;
		
		return false;
	}
	
	private boolean knowHowToDo(Goal goal) {
		
		// Later, I should check for on the fly recipes!
		if (goal.getPlan().getDecompositions().size() != 0)
			return true;
		else
			return false;
	}
	
	private boolean knowWhoShouldDo(Goal goal) {
		
		if (collaboration.getResponsibleAgent(goal.getPlan()).equals(AGENT.UNKNOWN))
			return false;
		else
			return true;
	}
	
	// Acceptance of a stressor as real (specially when the event has low changeability).
	public void doAcceptance(Goal goal, boolean human) {

		System.out.println("COPING STRATEGY: Acceptance");
		
		new Say.Agent(collaboration.getDisco(), "I believe we cannot continue this collaboration!");
		System.exit(0);
	}
	
	// Shifts an attribution of blame/credit from (towards) the self and towards (from) some other agent.
	public void doShiftingResponsibility(Goal goal) {
		
		System.out.println("COPING STRATEGY: Shifting Responsibility");
		
		Plan plan = goal.getPlan();
		
		if (plan.isPrimitive()) {
			if (collaboration.getResponsibleAgent(plan).equals(AGENT.SELF))
				discoActionsWrapper.proposeTaskWho(goal, true);
			else if (collaboration.getResponsibleAgent(plan).equals(AGENT.OTHER))
				discoActionsWrapper.proposeTaskWho(goal, false);
			else
				throw new IllegalArgumentException("Illegal Responsibility Value: " + collaboration.getResponsibleAgent(plan));
		}
		else
			throw new IllegalArgumentException("Illegal Shifting Responsibility Operation on a Non-Primitive Goal.");
	}
	
	private Goal getMinCostGoal(Goal disengagedGoal) {
		
		Map<Goal, Double> costValues = new HashMap<Goal, Double>();
		
		GoalManagement goalManagement = new GoalManagement(mentalProcesses);
		
		Disco disco = collaboration.getDisco();
		
		for(Plan alternativePlan : disco.getTop(disco.getFocus()).getLiveDescendants()) {
			if (alternativePlan.isPrimitive() && (!alternativePlan.getGoal().getType().equals(disengagedGoal.getPlan().getGoal().getType()))) {
				Goal alternativeGoal = new Goal(mentalProcesses, alternativePlan, true);
				costValues.put(alternativeGoal, goalManagement.computeCostValue(disengagedGoal, alternativeGoal));
			}
		}
		
		double min = 10.0;
		Goal minCostGoal = null;
		
		for (Map.Entry<Goal, Double> entry : costValues.entrySet()) {
			if(min > entry.getValue()) {
				min = entry.getValue();
				minCostGoal = entry.getKey();
			}
		}
		
		return minCostGoal;
	}
	
	// Distracting the self from thinking about behavioral dimension or goal with which the stressor is interferring. 
	// Goal management: Coming up with best action strategies to handle the problem, aka, action selection.
	public Boolean doMentalDisengagement(Goal goal, boolean postconditionStatus) {
		
		System.out.println("COPING STRATEGY: Mental Disengagement");
		
		Goal minCostGoal = getMinCostGoal(goal);
		
		if (minCostGoal != null) {
			discoActionsWrapper.proposeTaskShould(minCostGoal, false);
			discoActionsWrapper.executeTask(minCostGoal, false, postconditionStatus);
			collaboration.setDisengagedPlan(goal.getPlan().getRetry());
			Plan parentPlan = minCostGoal.getPlan().getParent();
			while (isAnotherGoal(parentPlan)) {
				Plan plan = getAnotherGoal(parentPlan);
				if (collaboration.getResponsibleAgent(plan).equals(AGENT.SELF))
					collaboration.processAgent(plan, perception.getEmotionValence());
				else {
					discoActionsWrapper.proposeTaskShould(plan, false);
					return null;
				}
			}
			discoActionsWrapper.mentionTask(goal, false);
			return true;
		}
		else {
			discoActionsWrapper.saySomethingAboutTask(false, "GOAL MANAGEMENT: I do not know what else I can do!");
			return false;
		}
	}
	
	private boolean isAnotherGoal(Plan parentPlan) {
		
		for (Plan plan : parentPlan.getChildren()) {
			if (plan.isLive())
				return true;
		}
		return false;
	}
	
	private Plan getAnotherGoal(Plan parentPlan) {
		
		for (Plan plan : parentPlan.getChildren()) {
			if (plan.isLive())
				return plan;
		}
		return null;
	}

	// Assume some intervening act or actor will improve desirability.
	public void doWishfulThinking(Goal goal) {
		
		System.out.println("COPING STRATEGY: Wishful Thinking");
		
		Turns.getInstance().setDesirabilityValue(goal, DESIRABILITY.DESIRABLE);
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
	
	// Showing reflexive reaction to remove threat.
	private void doAvoidance(Goal goal, boolean human) {
//		discoActionsWrapper.rejectProposedTask(goal, human);
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
	
	// Taking an action to redress the harm and mitigate the negative feeling(s).
	private void doMakingAmends() {
		
	}
}
