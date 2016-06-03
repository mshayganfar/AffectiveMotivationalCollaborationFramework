import java.util.HashMap;
import java.util.Map;

import Mechanisms.Action.DiscoActionsWrapper;
import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Collaboration.GoalManagement;
import MentalState.Goal;
import MetaInformation.AMCAgent;
import MetaInformation.AMCUser;
import MetaInformation.MentalProcesses;
import MetaInformation.Turns;
import MetaInformation.World;
import MetaInformation.Turns.WHOSE_TURN;
import MetaInformation.World.RemovingCoverTool;
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
	private static Interaction interaction;
	private static AMCUser user;
	private static AMCAgent agent;
	
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
	
	public static void goUser(String valenceValue, String postconditionStatus) {
		
		if (userEventItem != null) {
			Collaboration collaboration = mentalProcesses.getCollaborationMechanism();
			collaboration.setActualFocus(userEventItem.contributes);
			if (collaboration.processUser(userEventItem.contributes, Double.parseDouble(valenceValue), Boolean.parseBoolean(postconditionStatus)).equals(WHOSE_TURN.USER)) {
				collaboration.initializeAllInputs(userEventItem.contributes, inputValues);
				userEventItem = user.generateBest(interaction);
				System.out.println("Waiting for you: ");
				return;
			}
		}
		goAgent();
	}
	
	private static void goAgent() {
		
		Item agentEventItem;
		DiscoActionsWrapper discoWrapper;
		Collaboration collaboration = mentalProcesses.getCollaborationMechanism();
		
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
//			List<Plan> mmm = topPlan.getLiveDescendants();
//			System.out.println(topPlan.getLiveDescendants());
			agentEventItem = agent.generateBest(interaction);
			userEventItem  = user.generateBest(interaction);
			if ((agentEventItem == null) || (userEventItem != null)) {
				System.out.println(collaboration.getActualFocus());
				if ((agentEventItem != null) && (collaboration.getActualFocus() != null)) {
					while (collaboration.hasLiveChild(collaboration.getActualFocus().getParent())) {
						for (Plan plan : collaboration.getPathToTop(collaboration.getLiveChild(collaboration.getActualFocus().getParent()))) {
								collaboration.setActualFocus(plan);
								collaboration.processAgent(plan, 0.0);
								collaboration.initializeAllInputs(plan, inputValues);
						}
					}
				}
				collaboration.initializeAllInputs(userEventItem.contributes, inputValues);
				for (Plan plan : collaboration.getPathToTop(userEventItem.contributes)) {
					if (collaboration.isAgentsTurn(plan)) {
						collaboration.setActualFocus(plan);
						collaboration.processAgent(plan, 0.0);
						collaboration.initializeAllInputs(plan, inputValues);
					}
				}
				System.out.println("Waiting for you: ");
				return;
			}
			collaboration.initializeAllInputs(agentEventItem.contributes, inputValues);
			for (Plan plan : collaboration.getPathToTop(agentEventItem.contributes)) {
				if (collaboration.isUsersTurn(userEventItem, plan)) {
					System.out.println("Waiting for you: ");
					return;
				}
				collaboration.setActualFocus(plan);
				collaboration.processAgent(plan, 0.0);
				collaboration.initializeAllInputs(plan, inputValues);
			}
		}
	}
	
	public static void main(String[] args) {
		
		mentalProcesses = new MentalProcesses(args);
		World world 	= new World(mentalProcesses);
		
		mentalProcesses.getCollaborationMechanism().getInteraction().start(true);
		
		Collaboration collaboration = mentalProcesses.getCollaborationMechanism();
		Task topTask = collaboration.getDisco().getTaskClass("InstallSolarPanel").newInstance();
		topPlan = new Plan(topTask);
		topPlan.getGoal().setShould(true);
		collaboration.getDisco().push(topPlan);
		interaction = collaboration.getInteraction();
		agent		= collaboration.getAMCAgent();
		user 		= collaboration.getAMCUser();
		
		// Input values should be manually added to this list in order!
		//ArrayList<Object> inputValues = new ArrayList<Object>(Arrays.asList(WeldingTool.MY_WELDING_TOOL));
		inputValues = new HashMap<String, Object>();
		inputValues.put("WeldPaneltool", WeldingTool.MY_WELDING_TOOL);
		inputValues.put("PickUpTooltool", RemovingCoverTool.USER_TOOL);
		inputValues.put("RemoveLeftCovertool", RemovingCoverTool.USER_TOOL);
		inputValues.put("RemoveRightCovertool", RemovingCoverTool.AGENT_TOOL);
		inputValues.put("PickUpToolPrimitivetool", RemovingCoverTool.USER_TOOL);
		inputValues.put("RemoveLeftCoverPrimitivetool", RemovingCoverTool.USER_TOOL);
		inputValues.put("HandOffTooltool", RemovingCoverTool.AGENT_TOOL);
		mentalProcesses.getCollaborationMechanism().initializeAllInputs(topPlan, inputValues);
		
		goAgent();
		
//		collaboration.getInteraction().getConsole().test("test/ABC1.test");
//		mentalProcesses.getCollaborationMechanism().getInteraction().getConsole().source("test/events-astronaut-robot.txt");
		
//		System.out.println(goalManagement.computeCostValue(eventGoal) + " and " + eventGoal.getLabel() + " and " + collaboration.getDisco().getFocus().getType());
		
//		interaction.getConsole().test("test/Console.test");
//		interaction.getConsole().step("test/Console.test");
	}
}