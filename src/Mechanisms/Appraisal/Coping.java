package Mechanisms.Appraisal;

import java.util.HashMap;
import java.util.Map;

import Mechanisms.Mechanisms.AGENT;
import Mechanisms.Action.Action;
import Mechanisms.Action.DiscoActionsWrapper;
import Mechanisms.Appraisal.Controllability.CONTROLLABILITY;
import Mechanisms.Appraisal.Desirability.DESIRABILITY;
import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Collaboration.GoalManagement;
import Mechanisms.Motivation.Motivation;
import Mechanisms.Perception.Perception;
import Mechanisms.ToM.ToM;
import MentalState.Goal;
import MentalState.Intention;
import MetaInformation.CopingActivation;
import MetaInformation.MentalProcesses;
import MetaInformation.Turns;
import MetaInformation.CopingActivation.COPING_STRATEGY;
import edu.wpi.cetask.DecompositionClass;
import edu.wpi.cetask.Plan;
import edu.wpi.cetask.TaskClass.Input;
import edu.wpi.disco.Agent;
import edu.wpi.disco.Disco;
import edu.wpi.disco.lang.Ask;
import edu.wpi.disco.lang.Say;

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
		
		if (didHumanAsk(goal))
			respondToHuman(goal);
	}
	
	private void respondToHuman(Goal goal) {
		
		if (didHumanAskAboutTaskWhat(goal)) {
			Input input = null;// = goal.getPlan().getGoal();
			if (knowInputValue(goal, input)) {
				Object value = getInputValue(goal, input); 
				discoActionsWrapper.proposeTaskWhat(goal, false, input.getName(), value);
			}
		}
		
		if (didHumanAskAboutTaskHow(goal)) {
			DecompositionClass askedDecomp = null; // I should ask how to get this!
			if (askedDecomp == null) {
				if (knowHowToDo(goal)) {
					DecompositionClass decomp = getAlternativeRecipe(goal);
					discoActionsWrapper.proposeTaskHow(goal, false, decomp);
				}
			}
			else {
				if (knowHowToDo(goal, askedDecomp)) {
					discoActionsWrapper.proposeTaskHow(goal, false, askedDecomp);
				}
			}
		}
		
		if (didHumanAskAboutTaskShould(goal)) {
			if (controllability.isEventControllable(goal).equals(CONTROLLABILITY.HIGH_CONTROLLABLE))
				discoActionsWrapper.acceptProposedTask(goal, false);
			else if (controllability.isEventControllable(goal).equals(CONTROLLABILITY.UNCONTROLLABLE))
				discoActionsWrapper.rejectProposedTask(goal, false);
		}
		
		if (didHumanAskAboutTaskWho(goal)) {
			if (knowWhoShouldDo(goal)) {
				AGENT responsibleAgent = collaboration.getResponsibleAgent(goal.getPlan());
				if (responsibleAgent.equals(AGENT.SELF))
					discoActionsWrapper.proposeTaskWho(goal, false);
				else if (responsibleAgent.equals(AGENT.OTHER))
					discoActionsWrapper.proposeTaskWho(goal, true);
				else if (responsibleAgent.equals(AGENT.BOTH))
					discoActionsWrapper.saySomethingAboutTask(goal, false, "Both of us are responsible for this task!"); // --> This might be wrong! What do we need to do here?!
			}
			else
				discoActionsWrapper.saySomethingAboutTask(goal, false, "I do not know who is responsible for this task!");
		}
	}
	
	private boolean didHumanAskAboutTaskWho(Goal goal) {
		return (goal.getPlan().getGoal() instanceof Ask.Who) ? true : false;
	}
	
	private boolean didHumanAskAboutTaskShould(Goal goal) {
		return (goal.getPlan().getGoal() instanceof Ask.Should) ? true : false;
	}
	
	private DecompositionClass getAlternativeRecipe(Goal goal) {
		return goal.getPlan().getDecompositions().get(0);
	}
	
	private boolean didHumanAskAboutTaskHow(Goal goal) {
		return (goal.getPlan().getGoal() instanceof Ask.How) ? true : false;
	}
	
	private Object getInputValue(Goal goal, Input input) {
		
		Object value = input.getSlotValue(goal.getPlan().getGoal());
		
		if (value == null)
			value = collaboration.getInputValue(input);
		
		return value;
	}
	
	private boolean didHumanAskAboutTaskWhat(Goal goal) {
		return (goal.getPlan().getGoal() instanceof Ask.What) ? true : false;
	}
	
	private boolean didHumanAsk(Goal goal) {
		return (goal.getPlan().getGoal() instanceof Ask) ? true : false;
	}
	
	// The same Planning used in Disco.
	public void doPlanning(Goal goal, boolean human) {
		
		System.out.println("COPING STRATEGY: Planning");
		
		discoActionsWrapper.executeTask(goal, human);
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
		
		if ((new Agent("agent")).generateBest(collaboration.getInteraction()).task != null)
//			|| (goal.getPlan().getGoal() instanceof Propose.Should)) --> Check this later whether it is required?
			return true;
		else
			return false;
	}
	
	private boolean knowInputValue(Goal goal, Input input) {
		
		if (!input.isDefinedSlot(goal.getPlan().getGoal()))
			if (collaboration.getInputValue(input) == null)
				return false;
		
		return true;
	}
	
	private boolean knowInputValue(Goal goal) {
		
		for (Input input : goal.getPlan().getType().getDeclaredInputs())
			if (!input.isDefinedSlot(goal.getPlan().getGoal()))
				if (collaboration.getInputValue(input) == null)
					return false;
		
		return true;
	}
	
	private Input getUnknownInput(Goal goal) {
		
		for (Input input : goal.getPlan().getType().getDeclaredInputs())
			if (!input.isDefinedSlot(goal.getPlan().getGoal()))
				if (collaboration.getInputValue(input) == null)
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
	
	private Goal getMinCostGoal() {
		
		Map<Goal, Double> costValues = new HashMap<Goal, Double>();
		
		GoalManagement goalManagement = new GoalManagement(mentalProcesses);
		
		Disco disco = collaboration.getDisco();
		
		for(Plan alternativePlan : disco.getTop(disco.getFocus()).getLiveDescendants()) {
			Goal alternativeGoal = new Goal(mentalProcesses, alternativePlan);
			costValues.put(alternativeGoal, goalManagement.computeCostValue(alternativeGoal));
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
	public boolean doMentalDisengagement(Goal goal) {
		
		System.out.println("COPING STRATEGY: Mental Disengagement");
		
		Goal minCostGoal = getMinCostGoal();
		
		if (minCostGoal != null) {
			discoActionsWrapper.proposeTaskShould(minCostGoal, false);
			return true;
		}
		else {
			discoActionsWrapper.saySomethingAboutTask(goal, false, "I do not know what to do!");
			return false;
		}
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
