import java.util.ArrayList;
import java.util.Arrays;

import Mechanisms.Appraisal.Controllability.CONTROLLABILITY;
import Mechanisms.Appraisal.Desirability.DESIRABILITY;
import Mechanisms.Appraisal.Expectedness.EXPECTEDNESS;
import Mechanisms.Appraisal.Relevance.RELEVANCE;
import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Collaboration.GoalManagement;
import MentalState.Belief;
import MentalState.Goal;
import MentalState.Motive;
import MetaInformation.AppraisalVector;
import MetaInformation.MentalProcesses;
import MetaInformation.Turns;
import MetaInformation.AppraisalVector.WHOSE_APPRAISAL;
import edu.wpi.cetask.Plan;
import edu.wpi.cetask.Plan.Status;
import edu.wpi.cetask.Task;
import edu.wpi.cetask.TaskModel;
import edu.wpi.disco.Agenda.Plugin.Item;
import edu.wpi.disco.Agent;
import edu.wpi.disco.Interaction;

public class AffectiveMotivationalCollaborationFramework {
	
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
		
		recognizedGoal.setGoalStatus(mentalProcesses.getCollaborationMechanism().getGoalStatus(recognizedGoal.getPlan()));
		recognizedGoal.addGoalToMentalState();
		
		Belief belief1 = new Belief(recognizedGoal);
		Belief belief2 = new Belief(recognizedGoal);
		Belief belief3 = new Belief(recognizedGoal);
		Motive motive  = new Motive(recognizedGoal);
	}
	
//	public static void process(Plan eventPlan, double valenceValue) {
	public static void process(String valenceValue) {
		Turns turn = Turns.getInstance();
//		Goal recognizedGoal = new Goal(mentalProcesses, eventPlan);
		Goal recognizedGoal = new Goal(mentalProcesses);
		
		recognizedGoal.addGoalToMentalState();
		
		initializeFramework(recognizedGoal);
		
//		mentalProcesses.getPerceptionMechanism().setEmotionValence(valenceValue); //Double.parseDouble(valenceValue)
		mentalProcesses.getPerceptionMechanism().setEmotionValence(Double.parseDouble(valenceValue));
		
		// This is required before doing appraisals.
		mentalProcesses.getCollaborationMechanism().updatePreconditionApplicability();
		
		AppraisalVector appraisalVector = doAppraisal(turn, recognizedGoal);
		
		System.out.println("Human's Emotion: " + mentalProcesses.getToMMechanism().getAnticipatedHumanEmotion(recognizedGoal));
		
		runMotivations(recognizedGoal);
		
		runCoping(recognizedGoal);
//		recognizedGoal.toSting();
		
		runAction(recognizedGoal);
//		goalManagement.computeCostValue(recognizedGoal);
		
		// This needs to be done after running all the mechanisms.
		turn.updateTurn();
	}
	
	private static void runPlan() {
		
		Item eventItem;
		Collaboration collaboration = mentalProcesses.getCollaborationMechanism();
		Interaction interaction 	= collaboration.getInteraction();
		Agent agent 				= collaboration.getAgent();
		Task topTask 				= collaboration.getDisco().getTaskClass("InstallSolarPanel").newInstance();
		Plan topPlan                = new Plan(topTask);
		
		collaboration.getDisco().push(topPlan);
		
		while (!topPlan.getStatus().equals(Status.DONE)) {
			eventItem = agent.generateBest(interaction);
			System.out.println(eventItem.contributes.getGoal().getType());
			for (Plan plan : collaboration.getPathToTop(eventItem.contributes))
				if (eventItem.contributes.getGoal().equals(eventItem.task))
					System.out.println(eventItem.contributes.getGoal().getType());
//					process(eventItem.contributes, 1.0);
		}
	}
	
	public static void main(String[] args) {
		
		mentalProcesses = new MentalProcesses(args);
		
		mentalProcesses.getCollaborationMechanism().getInteraction().start(true);
		
//		runPlan();
		
//		collaboration.getInteraction().getConsole().test("test/ABC1.test");
		mentalProcesses.getCollaborationMechanism().getInteraction().getConsole().source("test/events-astronaut-robot.txt");
		
		// Input values should be manually added to this list in order!
		ArrayList<String> inputValues = new ArrayList<String>(Arrays.asList("WeldingTool"));
		
//		mentalProcesses.getCollaborationMechanism().initializeAllInputs(inputValues);
		
//		System.out.println(goalManagement.computeCostValue(eventGoal) + " and " + eventGoal.getLabel() + " and " + collaboration.getDisco().getFocus().getType());
		
//		interaction.getConsole().test("test/Console.test");
//		interaction.getConsole().step("test/Console.test");
	}
}
