package MetaInformation;

import MentalState.Goal;
import edu.wpi.cetask.Plan;
import edu.wpi.cetask.TaskClass;

public class Node {
	
	private Goal nodeGoal;
	private Plan nodeGoalPlan;
	private TaskClass nodeTaskClass;
	private int nodeDepthValue;
	
	public Node(Goal goal, int depth) {
		this.nodeGoal       = goal;
		this.nodeGoalPlan   = goal.getPlan();
		this.nodeTaskClass  = goal.getPlan().getType();
		this.nodeDepthValue = depth;
	}
	
	public Goal getNodeGoal() {
		return this.nodeGoal;
	}
	
	public Plan getNodeGoalPlan() {
		return this.nodeGoalPlan;
	}
	
	public int getNodeDepthValue() {
		return this.nodeDepthValue;
	}
	
	public TaskClass getNodeTaskClass() {
		return this.nodeTaskClass;
	}
}