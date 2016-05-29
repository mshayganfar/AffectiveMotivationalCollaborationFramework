import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.UserException;

import Mechanisms.Mechanisms.AGENT;
import Mechanisms.Action.DiscoActionsWrapper;
import Mechanisms.Appraisal.Controllability.CONTROLLABILITY;
import Mechanisms.Appraisal.Desirability.DESIRABILITY;
import Mechanisms.Appraisal.Expectedness.EXPECTEDNESS;
import Mechanisms.Appraisal.Relevance.RELEVANCE;
import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Collaboration.GoalManagement;
import MentalState.Belief;
import MentalState.Goal;
import MentalState.Motive;
import MetaInformation.AMCAgent;
import MetaInformation.AMCUser;
import MetaInformation.AppraisalVector;
import MetaInformation.MentalProcesses;
import MetaInformation.Turns;
import MetaInformation.World;
import MetaInformation.AppraisalVector.WHOSE_APPRAISAL;
import MetaInformation.World.WeldingTool;
import edu.wpi.cetask.Plan;
import edu.wpi.cetask.Plan.Status;
import edu.wpi.cetask.Task;
import edu.wpi.cetask.TaskModel;
import edu.wpi.disco.Agenda.Plugin.Item;
import edu.wpi.disco.Agent;
import edu.wpi.disco.Disco;
import edu.wpi.disco.Interaction;
import edu.wpi.disco.User;
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
	
	private static AppraisalVector doAppraisal(Turns turn, Goal recognizedGoal) {
		
		AppraisalVector appraisalVector = new AppraisalVector(mentalProcesses, recognizedGoal, WHOSE_APPRAISAL.SELF);
		
		RELEVANCE relevanceValue = mentalProcesses.getRelevanceProcess().isEventRelevant(recognizedGoal);
		appraisalVector.setRelevanceValue(relevanceValue);
		CONTROLLABILITY controllabilityValue = mentalProcesses.getControllabilityProcess().isEventControllable(recognizedGoal);
		appraisalVector.setControllabilityValue(controllabilityValue);
		DESIRABILITY desirabilityValue = mentalProcesses.getDesirabilityProcess().isEventDesirable(recognizedGoal);
		appraisalVector.setDesirabilityValue(desirabilityValue);
		EXPECTEDNESS expectednessValue = mentalProcesses.getExpectednessProcess().isEventExpected(recognizedGoal);
		appraisalVector.setExpectednessValue(expectednessValue);
		
//		mentalProcesses.getCollaborationMechanism().getWhoseAppraisal(recognizedGoal.getPlan())
		turn.setTurnAppraisals(mentalProcesses, recognizedGoal, WHOSE_APPRAISAL.SELF, 
				appraisalVector.getRelevanceSymbolicValue(), appraisalVector.getDesirabilitySymbolicValue(), appraisalVector.getControllabilitySymbolicValue(), 
				appraisalVector.getExpectednessSymbolicValue());
		
		for(AppraisalVector vector : Turns.getInstance().getCurrentAppraisalVectors()) {
			System.out.println(vector.getTurnNumber() + ", " + vector.getWhoseAppraisalValue() + ", " + vector.getRelevanceSymbolicValue() + ", " + 
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
	
	private static void runAction(Goal goal) {
		mentalProcesses.getActionMechanism().act(goal);
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
		Goal recognizedGoal = new Goal(mentalProcesses, eventPlan);
		
		initializeFramework(recognizedGoal);
		
		mentalProcesses.getPerceptionMechanism().setEmotionValence(valenceValue);
//		mentalProcesses.getPerceptionMechanism().setEmotionValence(Double.parseDouble(valenceValue));
		
		// This is required before doing appraisals.
		mentalProcesses.getCollaborationMechanism().updatePreconditionApplicability();
		
		mentalProcesses.getCollaborationMechanism().provideInputValues(recognizedGoal.getPlan());
		
		AppraisalVector appraisalVector = doAppraisal(turn, recognizedGoal);
		
		System.out.println("Human's Emotion: " + mentalProcesses.getToMMechanism().getAnticipatedHumanEmotion(recognizedGoal));
		
		runMotivations(recognizedGoal);
		
		runCoping(recognizedGoal);
//		recognizedGoal.toSting();
		
		runAction(recognizedGoal);
//		goalManagement.computeCostValue(recognizedGoal);
		
		recognizedGoal.setGoalStatus(mentalProcesses.getCollaborationMechanism().getGoalStatus(recognizedGoal.getPlan()));
		
		// This needs to be done after running all the mechanisms.
		turn.updateTurn();
	}
	
	public static void processUser(Plan eventPlan, double valenceValue) {
		Turns turn = Turns.getInstance();
		Goal recognizedGoal = new Goal(mentalProcesses, eventPlan);
		
		initializeFramework(recognizedGoal);
		
		DiscoActionsWrapper discoWrapper = new DiscoActionsWrapper(mentalProcesses);
		
		discoWrapper.executeTask(recognizedGoal, true);
		
//		mentalProcesses.getPerceptionMechanism().setEmotionValence(valenceValue);
//		
//		mentalProcesses.getCollaborationMechanism().updatePreconditionApplicability();
//		
//		mentalProcesses.getCollaborationMechanism().provideInputValues(recognizedGoal.getPlan());
//		
//		AppraisalVector appraisalVector = doAppraisal(turn, recognizedGoal);
//		
//		System.out.println("Human's Emotion: " + mentalProcesses.getToMMechanism().getAnticipatedHumanEmotion(recognizedGoal));
//		
//		runMotivations(recognizedGoal);
//		
//		runCoping(recognizedGoal);
//		
//		runAction(recognizedGoal);
//		
//		recognizedGoal.setGoalStatus(mentalProcesses.getCollaborationMechanism().getGoalStatus(recognizedGoal.getPlan()));
		
		turn.updateTurn();
	}
	
	public static void goUser(String valenceValue) {
		
		mentalProcesses.getCollaborationMechanism().setActualFocus(userEventItem.contributes);
		processUser(userEventItem.contributes, Double.parseDouble(valenceValue));
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
		}
			
		while (!topPlan.getStatus().equals(Status.DONE)) {
			agentEventItem = agent.generateBest(interaction);
			if (agentEventItem == null) return;
			System.out.println("IMPORTANT >>>>>>>>>>>>>>>>>>" + agentEventItem.contributes.getGoal().getType());
			for (Plan plan : collaboration.getPathToTop(agentEventItem.contributes)) {
//				if (userEventItem.contributes.getGoal() instanceof Accept) {
//					userEventItem = null;
//					System.out.println(userEventItem);
//					consolePlan = plan;
//					System.out.println("-------------------->" + userEventItem.contributes.getRetryOf());
//					return;
//				}
				System.out.println("User: " + userEventItem);
				System.out.println(plan.getGoal().getType());
				System.out.println(plan.getGoal().getType() + " >>>>>>>>>>> Responsible: " + collaboration.getResponsibleAgent(plan));
				
				if (isUsersTurn(plan))
					return;
				
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
		
		if ((userEventItem != null) && (agentPlanDepth >= userPlanDepth)) {
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
		Map<String, Object> inputValues = new HashMap<String, Object>();
		inputValues.put("tool", WeldingTool.MY_WELDING_TOOL);
		mentalProcesses.getCollaborationMechanism().initializeAllInputs(inputValues);
		
		goAgent();
		
//		collaboration.getInteraction().getConsole().test("test/ABC1.test");
//		mentalProcesses.getCollaborationMechanism().getInteraction().getConsole().source("test/events-astronaut-robot.txt");
		
//		System.out.println(goalManagement.computeCostValue(eventGoal) + " and " + eventGoal.getLabel() + " and " + collaboration.getDisco().getFocus().getType());
		
//		interaction.getConsole().test("test/Console.test");
//		interaction.getConsole().step("test/Console.test");
	}
}
