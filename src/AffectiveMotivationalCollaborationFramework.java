import Mechanisms.Appraisal.Desirability.DESIRABILITY;
import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Motivation.SatisfactionDrive;
import MentalState.Belief;
import MentalState.Goal;
import MentalState.Motive;
import MetaInformation.AppraisalVector;
import MetaInformation.MentalProcesses;
import MetaInformation.Turns;
import edu.wpi.cetask.Plan;
import edu.wpi.cetask.TaskModel;

public class AffectiveMotivationalCollaborationFramework {
	
	private static TaskModel taskModel;
	private static Goal goal = null;
	
	private static MentalProcesses mentalProcesses;
	
	private static Goal updateGoal(Plan plan) {

		if (plan != null) {
			while (!plan.getType().getNamespace().toString().equals(taskModel.toString()))
				plan = plan.getParent();
			
			goal = new Goal(plan);
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
	
	public static boolean doAppraisal() {
		
		Turns turn = Turns.getInstance();
		
		Collaboration collaboration = mentalProcesses.getCollaborationMechanism();
		
		Goal recognizedGoal = new Goal(collaboration.getActualFocus(collaboration.getDisco().getFocus()));
		recognizedGoal.setGoalStatus(collaboration.getGoalStatus(recognizedGoal.getPlan()));
		recognizedGoal.addGoalToMentalState();
		
		Belief belief1 = new Belief(recognizedGoal);
		Belief belief2 = new Belief(recognizedGoal);
		Belief belief3 = new Belief(recognizedGoal);
		
		Motive motive = new Motive(recognizedGoal);
//		System.out.println("Recipe Count: " + motivation.getMotiveImportance(motive));
		
		AppraisalVector test1 = mentalProcesses.getToMMechanism().getReverseAppraisalValues(recognizedGoal);
		
		collaboration.updatePreconditionApplicability();
		System.out.println(collaboration.getPreconditionApplicabilities());
		
		System.out.println(mentalProcesses.getRelevanceProcess().isEventRelevant(recognizedGoal));
		System.out.println(mentalProcesses.getControllabilityProcess().isEventControllable(recognizedGoal));
		DESIRABILITY desirabilityValue = mentalProcesses.getDesirabilityProcess().isEventDesirable(recognizedGoal);
		System.out.println(desirabilityValue);
		System.out.println(mentalProcesses.getExpectednessProcess().isEventExpected(recognizedGoal));
		
//		System.out.println(DIFFICULTY.valueOf(recognizedGoal.getPlan().getType().getProperty("@difficulty")));
		
		turn.updateTurnDesirability(desirabilityValue);
		
		SatisfactionDrive test = new SatisfactionDrive();
		System.out.println(test.getSatisfactionDriveDelta());
		test.updatePrevSatisfactionDriveValue(test.getSatisfactionDriveValue());
		
		turn.updateTurn();
		
		if(collaboration.getDisco().getLastOccurrence() == null)
			return false;
		else
			return true;
	}
	
	public static void main(String[] args) {
		
		mentalProcesses = new MentalProcesses(args);
		
//		GoalManagement goalManagement = new GoalManagement(collaboration, relevance, desirability);
		
		mentalProcesses.getCollaborationMechanism().getInteraction().start(true);
		
//		collaboration.getInteraction().getConsole().test("test/ABC1.test");
		mentalProcesses.getCollaborationMechanism().getInteraction().getConsole().source("test/events2.txt");
		
//		updateGoal(collaboration.getDisco().getFocus());
		
//		System.out.println(goalManagement.computeCostValue(eventGoal) + " and " + eventGoal.getLabel() + " and " + collaboration.getDisco().getFocus().getType());
		
//		interaction.getConsole().test("test/Console.test");
//		interaction.getConsole().step("test/Console.test");

//		//interaciton.done(false, Propose.Should.newInstance(disco, false, task), null);
//		interaciton.occurred(false, task, null);
//		//interaciton.getSystem().respond(interaciton, true, false, false);
	}
}
