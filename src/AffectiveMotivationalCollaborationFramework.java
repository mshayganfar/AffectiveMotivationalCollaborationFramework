import Mechanisms.Appraisal.Controllability.CONTROLLABILITY;
import Mechanisms.Appraisal.Desirability.DESIRABILITY;
import Mechanisms.Appraisal.Expectedness.EXPECTEDNESS;
import Mechanisms.Appraisal.Relevance.RELEVANCE;
import Mechanisms.Collaboration.GoalManagement;
import MentalState.Belief;
import MentalState.Goal;
import MentalState.Motive;
import MetaInformation.AppraisalVector;
import MetaInformation.MentalProcesses;
import MetaInformation.Turns;
import MetaInformation.AppraisalVector.WHOSE_APPRAISAL;
import edu.wpi.cetask.Plan;
import edu.wpi.cetask.TaskModel;

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
		
		AppraisalVector appraisalVector = new AppraisalVector(mentalProcesses, recognizedGoal);
		
		RELEVANCE relevanceValue = mentalProcesses.getRelevanceProcess().isEventRelevant(recognizedGoal);
		appraisalVector.setRelevanceValue(relevanceValue);
		CONTROLLABILITY controllabilityValue = mentalProcesses.getControllabilityProcess().isEventControllable(recognizedGoal);
		appraisalVector.setControllabilityValue(controllabilityValue);
		DESIRABILITY desirabilityValue = mentalProcesses.getDesirabilityProcess().isEventDesirable(recognizedGoal);
		appraisalVector.setDesirabilityValue(desirabilityValue);
		EXPECTEDNESS expectednessValue = mentalProcesses.getExpectednessProcess().isEventExpected(recognizedGoal);
		appraisalVector.setExpectednessValue(expectednessValue);
		
		turn.setTurnAppraisals(mentalProcesses, recognizedGoal, mentalProcesses.getCollaborationMechanism().getWhoseAppraisal(recognizedGoal.getPlan()), 
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
	
	private static void initializeFramework(Goal recognizedGoal) {
		
		recognizedGoal.setGoalStatus(mentalProcesses.getCollaborationMechanism().getGoalStatus(recognizedGoal.getPlan()));
		recognizedGoal.addGoalToMentalState();
		
		Belief belief1 = new Belief(recognizedGoal);
		Belief belief2 = new Belief(recognizedGoal);
		Belief belief3 = new Belief(recognizedGoal);
		Motive motive  = new Motive(recognizedGoal);
	}
	
	public static void process() {
		
		Turns turn = Turns.getInstance();
		Goal recognizedGoal = new Goal(mentalProcesses);
		
		initializeFramework(recognizedGoal);
		
		// This is required before doing appraisals.
		mentalProcesses.getCollaborationMechanism().updatePreconditionApplicability();
		AppraisalVector appraisalVector = doAppraisal(turn, recognizedGoal);
		
		runMotivations(recognizedGoal);
		
		goalManagement.computeCostValue(recognizedGoal);
		// This needs to be done after running all the mechanisms.
		turn.updateTurn();
	}
	
	public static void main(String[] args) {
		
		mentalProcesses = new MentalProcesses(args);
		
		goalManagement = new GoalManagement(mentalProcesses);
				
		mentalProcesses.getCollaborationMechanism().getInteraction().start(true);
		
//		collaboration.getInteraction().getConsole().test("test/ABC1.test");
		mentalProcesses.getCollaborationMechanism().getInteraction().getConsole().source("test/events-astronaut-robot.txt");
		
//		System.out.println(goalManagement.computeCostValue(eventGoal) + " and " + eventGoal.getLabel() + " and " + collaboration.getDisco().getFocus().getType());
		
//		interaction.getConsole().test("test/Console.test");
//		interaction.getConsole().step("test/Console.test");
	}
}
