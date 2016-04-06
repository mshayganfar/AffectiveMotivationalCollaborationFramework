import Mechanisms.Appraisal.Controllability;
import Mechanisms.Appraisal.Desirability;
import Mechanisms.Appraisal.Expectedness;
import Mechanisms.Appraisal.Relevance;
import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Collaboration.GoalManagement;
import MentalState.Belief;
import MentalState.Goal;
import MentalState.Motive;
import edu.wpi.cetask.Plan;
import edu.wpi.cetask.TaskModel;

public class AppraisalGoalManagement {
	
	private static TaskModel taskModel;
	private static Goal goal = null;
	private static Collaboration collaboration;
	private static Relevance relevance;
	
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
		
		System.out.println(collaboration.getActualFocus(collaboration.getDisco().getFocus()));
		
		if(collaboration.getDisco().getLastOccurrence() == null)
			return false;
		else
			return true;
	}
	
	public static void main(String[] args) {
		
//		Turns turn = Turns.getInstance();
		
		collaboration = new Collaboration(args);
		
		relevance = new Relevance(collaboration);
		Controllability controllability = new Controllability(collaboration);
		Desirability desirability = new Desirability(collaboration);
		Expectedness expectedness = new Expectedness(collaboration);
		
		GoalManagement goalManagement = new GoalManagement(collaboration, relevance, desirability);
		
		collaboration.getInteraction().getConsole().source("test/events2.txt");
		
		updateGoal(collaboration.getDisco().getFocus());
		System.out.println(collaboration.getDisco().getFocus());
		
		Goal eventGoal = new Goal(collaboration.getDisco().getFocus());

		Belief belief1 = new Belief(eventGoal);
		Belief belief2 = new Belief(eventGoal);
		Belief belief3 = new Belief(eventGoal);
		
		Motive motive = new Motive(eventGoal);
		
		System.out.println(relevance.isEventRelevant(eventGoal));
		
//		System.out.println(controllability.isEventControllable(eventGoal));
//		System.out.println(desirability.isEventDesirable(eventGoal));
//		System.out.println(expectedness.isEventExpected(eventGoal));
		
//		System.out.println(goalManagement.computeCostValue(eventGoal) + " and " + eventGoal.getLabel() + " and " + collaboration.getDisco().getFocus().getType());
		
		
//		interaction.getConsole().test("test/Console.test");
//		interaction.getConsole().step("test/Console.test");
		
		
//		//interaciton.done(false, Propose.Should.newInstance(disco, false, task), null);
//		interaciton.occurred(false, task, null);
//		//interaciton.getSystem().respond(interaciton, true, false, false);
	}
}
