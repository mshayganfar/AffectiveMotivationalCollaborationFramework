import edu.wpi.cetask.TaskModel;
import edu.wpi.disco.Agent;
import edu.wpi.disco.Disco;
import edu.wpi.disco.Interaction;
import edu.wpi.disco.User;

public class AppraisalGoalManagement {
	
	public static void main(String[] args) {
		Interaction interaciton = new Interaction(new Agent("agent"), new User("user"),
				  args.length > 0 && args[0].length() > 0 ? args[0] : null);


		interaciton.start(true);

		Disco disco = interaciton.getDisco();

		System.out.println(disco);

		TaskModel taskModel = disco.load("/TaskModel/Sandwich.xml");
//		Task task = taskModel.getTaskClass("d").newInstance();

//		System.out.println(task.toString());
		System.out.println(taskModel.toString());

		//interaciton.done(false, Propose.Should.newInstance(disco, false, task), null);
//		interaciton.occurred(false, task, null);

		//interaciton.getSystem().respond(interaciton, true, false, false);

	}
}
