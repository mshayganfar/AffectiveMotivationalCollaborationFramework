import java.util.HashMap;
import java.util.Map;

import Mechanisms.Mechanisms.AGENT;
import Mechanisms.Action.DiscoActionsWrapper;
import Mechanisms.Appraisal.Controllability.CONTROLLABILITY;
import Mechanisms.Appraisal.Desirability.DESIRABILITY;
import Mechanisms.Appraisal.Expectedness.EXPECTEDNESS;
import Mechanisms.Appraisal.Relevance.RELEVANCE;
import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Collaboration.GoalManagement;
import Mechanisms.ToM.ToM;
import MentalState.Belief;
import MentalState.Goal;
import MentalState.Motive;
import MetaInformation.AMCAgent;
import MetaInformation.AMCUser;
import MetaInformation.AppraisalVector;
import MetaInformation.MentalProcesses;
import MetaInformation.Turns;
import MetaInformation.World;
import MetaInformation.AppraisalVector.APPRAISAL_TYPE;
import MetaInformation.World.WeldingTool;
import edu.wpi.cetask.Plan;
import edu.wpi.cetask.Plan.Status;
import edu.wpi.cetask.Task;
import edu.wpi.cetask.TaskModel;
import edu.wpi.disco.Agenda.Plugin.Item;
import edu.wpi.disco.Interaction;
import edu.wpi.disco.lang.Accept;

public class AffectiveMotivationalCollaborationFramework {
	
	public static Plan consolePlan;
	private static Item userEventItem;
	private static Plan topPlan;
	
	private static TaskModel taskModel;
	private static Goal goal = null;
	
	private static MentalProcesses mentalProcesses;
	private static GoalManagement goalManagement;
//	private static SatisfactionDrive satisfactionDrive = new SatisfactionDrive();
	
	private static Map<String, Object> inputValues;
	
	private static Goal updateGoal(Plan plan) {

		if (plan != null) {
			while (!plan.getType().getNamespace().toString().equals(taskModel.toString()))
				plan = plan.getParent();
			
			goal = new Goal(mentalProcesses, plan);
//			goal = plan.getGoal().getType(); // Was TaskClass
		}
		else
			System.out.println("Needs to be changed for the top level goal before execution!");
		
		return goal;
	}

//	public static void updateCollaborationStatus() {
//		
//		collaboration.updateCollaboraitonStatus();
//		collaboration.updatePreviousFocus();
//		System.out.println("Previous Focus: " + collaboration.getPreviousFocus().getType());
//	}
	
	private static AppraisalVector doAppraisal(Turns turn, Goal recognizedGoal, APPRAISAL_TYPE appraisalType) {
		
		AppraisalVector appraisalVector = new AppraisalVector(mentalProcesses, recognizedGoal, appraisalType);
		
		RELEVANCE relevanceValue = mentalProcesses.getRelevanceProcess().isEventRelevant(recognizedGoal);
		appraisalVector.setRelevanceValue(relevanceValue);
		CONTROLLABILITY controllabilityValue = mentalProcesses.getControllabilityProcess().isEventControllable(recognizedGoal);
		appraisalVector.setControllabilityValue(controllabilityValue);
		DESIRABILITY desirabilityValue = mentalProcesses.getDesirabilityProcess().isEventDesirable(recognizedGoal);
		appraisalVector.setDesirabilityValue(desirabilityValue);
		EXPECTEDNESS expectednessValue = mentalProcesses.getExpectednessProcess().isEventExpected(recognizedGoal);
		appraisalVector.setExpectednessValue(expectednessValue);
		
//		mentalProcesses.getCollaborationMechanism().getWhoseAppraisal(recognizedGoal.getPlan())
		turn.setTurnAppraisals(mentalProcesses, recognizedGoal, appraisalType,
				appraisalVector.getRelevanceSymbolicValue(), appraisalVector.getDesirabilitySymbolicValue(), appraisalVector.getControllabilitySymbolicValue(), 
				appraisalVector.getExpectednessSymbolicValue());
		
		for(AppraisalVector vector : Turns.getInstance().getCurrentAppraisalVectors()) {
			System.out.println(vector.getTurnNumber() + ", " + vector.getAppraisalType() + ", " + vector.getRelevanceSymbolicValue() + ", " + 
					vector.getDesirabilitySymbolicValue() + ", " + vector.getExpectednessSymbolicValue() + ", " + 
					vector.getControllabilitySymbolicValue());
			System.out.println("EMOTION INSTANCE: " + vector.getEmotionInstance());
		}
		
		return appraisalVector;
	}
	
	private static void runMotivations(Goal goal) {
		mentalProcesses.getMotivationMechanism().createMotives(goal);
	}
	
	private static void runCoping(Goal goal) {
		mentalProcesses.getCopingMechanism().formIntentions(goal);
	}
	
	private static void runAction(Goal goal, boolean postconditionStatus) {
		mentalProcesses.getActionMechanism().act(goal, postconditionStatus);
	}
	
	private static void initializeFramework(Goal recognizedGoal) {
		
		recognizedGoal.addGoalToMentalState();
		
		Belief belief1 = new Belief(recognizedGoal);
		Belief belief2 = new Belief(recognizedGoal);
		Belief belief3 = new Belief(recognizedGoal);
		Motive motive  = new Motive(recognizedGoal);
	}
	
	public static void processAgent(Plan eventPlan, double valenceValue) {
		
		Turns turn = Turns.getInstance();
		
		ToM tom = mentalProcesses.getToMMechanism();
		Goal recognizedGoal = new Goal(mentalProcesses, eventPlan);
		
		initializeFramework(recognizedGoal);
		
		// This is required before doing reverse appraisals.
		mentalProcesses.getPerceptionMechanism().setEmotionValence(valenceValue);
		
		// This is required before doing appraisals.
		mentalProcesses.getCollaborationMechanism().updatePreconditionApplicability();
		
		AppraisalVector appraisalVector = doAppraisal(turn, recognizedGoal, APPRAISAL_TYPE.APPRAISAL);
		
		tom.doReverseAppraisal(recognizedGoal);
		System.out.println("Human's Emotion (before coping): " + tom.getAnticipatedHumanEmotion(tom.getReverseAppraisalValues(recognizedGoal)));
		
		runMotivations(recognizedGoal);
		
		runCoping(recognizedGoal);
//		recognizedGoal.toSting();
		
		runAction(recognizedGoal, true);
//		goalManagement.computeCostValue(recognizedGoal);
		
		tom.doReverseAppraisal(recognizedGoal);
		System.out.println("Human's Emotion (after coping): " + tom.getAnticipatedHumanEmotion(tom.getReverseAppraisalValues(recognizedGoal)));
		
		appraisalVector = doAppraisal(turn, recognizedGoal, APPRAISAL_TYPE.REAPPRAISAL);
		
		recognizedGoal.setGoalStatus(mentalProcesses.getCollaborationMechanism().getGoalStatus(recognizedGoal.getPlan()));
		
		// This needs to be done after running all the mechanisms.
		turn.updateTurn();
	}
	
	public static void processUser(Plan eventPlan, double valenceValue, Boolean postconditionStatus) {
		
		Turns turn = Turns.getInstance();
		
		ToM tom = mentalProcesses.getToMMechanism();
		Goal recognizedGoal = new Goal(mentalProcesses, eventPlan);
		
		initializeFramework(recognizedGoal);
		
		DiscoActionsWrapper discoWrapper = new DiscoActionsWrapper(mentalProcesses);
		
		if (recognizedGoal.getPlan().isPrimitive())
			discoWrapper.executeTask(recognizedGoal, true, postconditionStatus);
		else
			discoWrapper.proposeTaskShould(recognizedGoal, true);
		
		mentalProcesses.getPerceptionMechanism().setEmotionValence(valenceValue);
		
		mentalProcesses.getCollaborationMechanism().updatePreconditionApplicability();
		
		AppraisalVector appraisalVector = doAppraisal(turn, recognizedGoal, APPRAISAL_TYPE.APPRAISAL);
		
		if (postconditionStatus == null)
			mentalProcesses.getPerceptionMechanism().setEmotionValence(0.0);
		else if (postconditionStatus)
			mentalProcesses.getPerceptionMechanism().setEmotionValence(0.4);
		else
			mentalProcesses.getPerceptionMechanism().setEmotionValence(-0.4);
		tom.doReverseAppraisal(recognizedGoal);
		System.out.println("Human's Emotion (before coping): " + tom.getAnticipatedHumanEmotion(tom.getReverseAppraisalValues(recognizedGoal)));
		
		runMotivations(recognizedGoal);
		
		runCoping(recognizedGoal);
		
		runAction(recognizedGoal, postconditionStatus);
		
		tom.doReverseAppraisal(recognizedGoal);
		System.out.println("Human's Emotion (after coping): " + tom.getAnticipatedHumanEmotion(tom.getReverseAppraisalValues(recognizedGoal)));
		
		turn.updateTurn();
	}
	
	public static void goUser(String valenceValue, String postconditionStatus) {
		
		if (userEventItem != null) {
			mentalProcesses.getCollaborationMechanism().setActualFocus(userEventItem.contributes);
			processUser(userEventItem.contributes, Double.parseDouble(valenceValue), Boolean.parseBoolean(postconditionStatus));
		}
		goAgent();
	}
	
	private static void goAgent() {
		
		Item agentEventItem;
		DiscoActionsWrapper discoWrapper;
		Collaboration collaboration = mentalProcesses.getCollaborationMechanism();
		Interaction interaction 	= collaboration.getInteraction();
		AMCAgent agent 				= collaboration.getAgent();
		AMCUser user 				= collaboration.getUser();
		
		userEventItem = user.generateBest(interaction);
		
		if (userEventItem != null) {
			if (userEventItem.contributes.getGoal() instanceof Accept) {
				discoWrapper = new DiscoActionsWrapper(mentalProcesses);
				discoWrapper.acceptProposedTask(userEventItem.contributes, true);
				userEventItem = user.generateBest(interaction);
			}
			else
				collaboration.initializeAllInputs(userEventItem.contributes, inputValues);
		}
		
		while (!topPlan.getStatus().equals(Status.DONE)) {
			agentEventItem = agent.generateBest(interaction);
			if (agentEventItem == null) return;
			collaboration.initializeAllInputs(agentEventItem.contributes, inputValues);
			System.out.println("IMPORTANT >>>>>>>>>>>>>>>>>>" + agentEventItem.contributes.getGoal().getType());
			for (Plan plan : collaboration.getPathToTop(agentEventItem.contributes)) {
				System.out.println("User: " + userEventItem);
				System.out.println(plan.getGoal().getType());
				System.out.println(plan.getGoal().getType() + " >>>>>>>>>>> Responsible: " + collaboration.getResponsibleAgent(plan));
				
				if (isUsersTurn(plan)) {
					System.out.println("Waiting for you: ");
					return;
				}
				
				collaboration.setActualFocus(plan);
				processAgent(plan, 0.0);
			}
		}
	}
	
	private static boolean isUsersTurn(Plan plan) {
		
		int agentPlanDepth = 0, userPlanDepth = 0;
		
		Collaboration collaboration = mentalProcesses.getCollaborationMechanism();
		
		if (collaboration.getResponsibleAgent(plan).equals(AGENT.OTHER))
			return true;
		
		agentPlanDepth = userPlanDepth = 0;
		if (!plan.isPrimitive() && (userEventItem != null)) {// && (!(userEventItem.contributes.getGoal() instanceof Accept))) {
			agentPlanDepth = collaboration.getDistanceFromTop(plan);
			userPlanDepth  = collaboration.getDistanceFromTop(userEventItem.contributes);
			System.out.println(agentPlanDepth + " , " + userPlanDepth);
		}
		
		if (userEventItem != null) { 
			if (agentPlanDepth > userPlanDepth)
				return true;
			else if ((agentPlanDepth == userPlanDepth) && (plan.getParent().equals(userEventItem.contributes.getParent())))
				return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		
		mentalProcesses = new MentalProcesses(args);
		World world = new World(mentalProcesses);
		
		mentalProcesses.getCollaborationMechanism().getInteraction().start(true);
		
		Collaboration collaboration = mentalProcesses.getCollaborationMechanism();
		Task topTask = collaboration.getDisco().getTaskClass("InstallSolarPanel").newInstance();
		topPlan = new Plan(topTask);
		topPlan.getGoal().setShould(true);
		collaboration.getDisco().push(topPlan);
		
		// Input values should be manually added to this list in order!
		//ArrayList<Object> inputValues = new ArrayList<Object>(Arrays.asList(WeldingTool.MY_WELDING_TOOL));
		inputValues = new HashMap<String, Object>();
		inputValues.put("tool", WeldingTool.MY_WELDING_TOOL);
		mentalProcesses.getCollaborationMechanism().initializeAllInputs(topPlan, inputValues);
		
		goAgent();
		
//		collaboration.getInteraction().getConsole().test("test/ABC1.test");
//		mentalProcesses.getCollaborationMechanism().getInteraction().getConsole().source("test/events-astronaut-robot.txt");
		
//		System.out.println(goalManagement.computeCostValue(eventGoal) + " and " + eventGoal.getLabel() + " and " + collaboration.getDisco().getFocus().getType());
		
//		interaction.getConsole().test("test/Console.test");
//		interaction.getConsole().step("test/Console.test");
	}
}
