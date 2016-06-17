import java.util.HashMap;
import java.util.Map;

import GUI.AMCFrame;
import Mechanisms.Mechanisms.AGENT;
import Mechanisms.Action.DiscoActionsWrapper;
import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Collaboration.GoalManagement;
import MentalState.Goal;
import MetaInformation.AMCAgent;
import MetaInformation.AMCUser;
import MetaInformation.MentalProcesses;
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
	private static World world;
	
	private static MentalProcesses mentalProcesses;
	private static GoalManagement goalManagement;
	
	private static Map<String, Object> inputValues;
	
	private static Goal updateGoal(Plan plan) {

		if (plan != null) {
			while (!plan.getType().getNamespace().toString().equals(taskModel.toString()))
				plan = plan.getParent();
			
			goal = new Goal(mentalProcesses, plan);
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
			if (!(userEventItem.contributes.getGoal() instanceof Accept)) {
				if (collaboration.processUser(userEventItem.contributes, Double.parseDouble(valenceValue), Boolean.parseBoolean(postconditionStatus)).equals(WHOSE_TURN.USER)) {
					collaboration.initializeAllInputs(userEventItem.contributes, inputValues);
					userEventItem = user.generateBest(interaction);
					System.out.println("Waiting for you: ");
					return;
				}
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
			agentEventItem = agent.generateBest(interaction, true);
			userEventItem  = user.generateBest(interaction);
			if (userEventItem != null) {
				if (userEventItem.contributes.getGoal() instanceof Accept) {
					discoWrapper = new DiscoActionsWrapper(mentalProcesses);
					discoWrapper.acceptProposedTask(userEventItem.contributes, true);
					userEventItem = user.generateBest(interaction);
				}
				else
					collaboration.initializeAllInputs(userEventItem.contributes, inputValues);
			}
			if ((agentEventItem == null) || (userEventItem != null)) {
				if ((agentEventItem != null) && (collaboration.getActualFocus() != null)) {
					while (collaboration.hasLiveChild(collaboration.getActualFocus().getParent())) {
						for (Plan plan : collaboration.getPathToTop(collaboration.getLiveChild(collaboration.getActualFocus().getParent()))) {
								collaboration.setActualFocus(plan);
								if (collaboration.getResponsibleAgent(plan).equals(AGENT.SELF)) {
									collaboration.processAgent(plan, 0.0);
									collaboration.initializeAllInputs(plan, inputValues);
								}
								else {
									collaboration.initializeAllInputs(plan, inputValues);
									System.out.println("Waiting for you: ");
									return;
								}
						}
					}
				}
				if (userEventItem != null) {
					collaboration.initializeAllInputs(userEventItem.contributes, inputValues);
					for (Plan plan : collaboration.getPathToTop(userEventItem.contributes)) {
						if (collaboration.isAgentsTurn(plan)) {
							collaboration.setActualFocus(plan);
							collaboration.processAgent(plan, 0.0);
							collaboration.initializeAllInputs(plan, inputValues);
						}
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
	
	private static boolean isSecondTry(Item agentEventItem) {
		
		if (agentEventItem != null)
			if (agentEventItem.contributes.getGoal().getType().toString().equals("CheckPanelAttachmentPrimitive"))
				if (agentEventItem.contributes.getRetryOf() != null)
					return true;
		return false;
	}
	
	public static void main(String[] args) {
		
		AMCFrame frame = new AMCFrame("Affective Motivational Collaboration Framework");
		frame.pack();
		frame.setVisible(true);
		
		mentalProcesses = new MentalProcesses(args, frame, true);
		world = new World(mentalProcesses);
		Collaboration collaboration = mentalProcesses.getCollaborationMechanism();
		
		collaboration.setCollaborationWorld(world);
		
		collaboration.getInteraction().start(true);
		
		Task topTask = collaboration.getDisco().getTaskClass("InstallSolarPanel").newInstance();
		topPlan = new Plan(topTask);
		topPlan.getGoal().setShould(true);
		collaboration.getDisco().push(topPlan);
		interaction = collaboration.getInteraction();
		agent		= collaboration.getAMCAgent();
		user 		= collaboration.getAMCUser();
		
		// Input values should be manually added to this list in order!
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
		
//		interaction.getConsole().test("test/Console.test");
//		interaction.getConsole().step("test/Console.test");
	}
}