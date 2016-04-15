import Mechanisms.Appraisal.Controllability;
import Mechanisms.Appraisal.Desirability;
import Mechanisms.Appraisal.Expectedness;
import Mechanisms.Appraisal.Relevance;
import Mechanisms.Appraisal.Desirability.DESIRABILITY;
import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Collaboration.GoalManagement;
import Mechanisms.Motivation.Motivation;
import Mechanisms.Motivation.SatisfactionDrive;
import Mechanisms.ToM.ToM;
import MentalState.Belief;
import MentalState.Goal;
import MentalState.Goal.DIFFICULTY;
import MentalState.Motive;
import MetaInformation.Turns;
import edu.wpi.cetask.Plan;
import edu.wpi.cetask.TaskModel;

public class AppraisalGoalManagement {
	
	private static TaskModel taskModel;
	private static Goal goal = null;
	private static Collaboration collaboration;
	private static Relevance relevance;
	private static Controllability controllability;
	private static Desirability desirability;
	private static Expectedness expectedness;
	private static ToM tom;
	private static Motivation motivaiton;
	
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
		
		Goal recognizedGoal = new Goal(collaboration.getActualFocus(collaboration.getDisco().getFocus()));
		recognizedGoal.addGoalToMentalState();
		
		Belief belief1 = new Belief(recognizedGoal);
		Belief belief2 = new Belief(recognizedGoal);
		Belief belief3 = new Belief(recognizedGoal);
		
		Motive motive = new Motive(recognizedGoal);
		
		System.out.println(relevance.isEventRelevant(recognizedGoal));
		System.out.println(controllability.isEventControllable(recognizedGoal));
		DESIRABILITY desirabilityValue = desirability.isEventDesirable(recognizedGoal);
		System.out.println(desirabilityValue);
		System.out.println(expectedness.isEventExpected(recognizedGoal));
		
		System.out.println(DIFFICULTY.valueOf(recognizedGoal.getPlan().getType().getProperty("@difficulty")));
		
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
		
		tom = new ToM();
		collaboration = new Collaboration(args);
		
		relevance = new Relevance(collaboration);
		controllability = new Controllability(collaboration);
		desirability = new Desirability(collaboration);
		expectedness = new Expectedness(collaboration);
		
		motivaiton = new Motivation(tom, controllability, expectedness);
		
		GoalManagement goalManagement = new GoalManagement(collaboration, relevance, desirability);
		
		collaboration.getInteraction().getConsole().source("test/events2.txt");
		
//		updateGoal(collaboration.getDisco().getFocus());
		
//		System.out.println(goalManagement.computeCostValue(eventGoal) + " and " + eventGoal.getLabel() + " and " + collaboration.getDisco().getFocus().getType());
		
//		interaction.getConsole().test("test/Console.test");
//		interaction.getConsole().step("test/Console.test");
		
//		//interaciton.done(false, Propose.Should.newInstance(disco, false, task), null);
//		interaciton.occurred(false, task, null);
//		//interaciton.getSystem().respond(interaciton, true, false, false);
	}
}
