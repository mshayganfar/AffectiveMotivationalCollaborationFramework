import Mechanisms.Appraisal.AppraisalProcesses;
import Mechanisms.Appraisal.Relevance;
import MentalState.Belief;
import MentalState.Goal;
import edu.wpi.cetask.TaskModel;
import edu.wpi.disco.Agent;
import edu.wpi.disco.Disco;
import edu.wpi.disco.Interaction;
import edu.wpi.disco.User;

public class AppraisalGoalManagement {
	
	public static void testAppraisal() { System.out.println("appraisal works!"); }
	
	public static void main(String[] args) {
		
//		Goal eventGoal1 = new Goal(null);
//		Belief belief1 = new Belief(eventGoal1);
//		Belief belief2 = new Belief(eventGoal1);
//		Belief belief3 = new Belief(eventGoal1);
//		
//		Goal eventGoal2 = new Goal(null);
//		Belief belief4 = new Belief(eventGoal2);
//		Belief belief5 = new Belief(eventGoal2);
//		
//		Relevance relevance = new Relevance(args);
//		relevance.isEventRelevant(eventGoal1);
//		
//		relevance.isEventRelevant(eventGoal2);
		
		Interaction interaciton = new Interaction(new Agent("agent"), new User("user"),
				  args.length > 0 && args[0].length() > 0 ? args[0] : null);

		interaciton.start(true);

		Disco disco = interaciton.getDisco();

		System.out.println(disco);

		TaskModel taskModel = disco.load("/TaskModel/Sandwich.xml");
//		disco.load("/TaskModel/Events.xml");

		
//		Task task = taskModel.getTaskClass("d").newInstance();
//
//		System.out.println(task.toString());
//		System.out.println(taskModel.toString());
//
//		//interaciton.done(false, Propose.Should.newInstance(disco, false, task), null);
//		interaciton.occurred(false, task, null);
//
//		//interaciton.getSystem().respond(interaciton, true, false, false);

	}
}
