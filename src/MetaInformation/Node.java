package MetaInformation;

import edu.wpi.cetask.Plan;
import edu.wpi.cetask.TaskClass;

public class Node {
	
	private Plan nodeGoalPlan;
	private TaskClass nodeTaskClass;
	private int nodeDepthValue;
	
	public Node(Plan plan, int depth) {
		this.nodeGoalPlan   = plan;
		this.nodeTaskClass  = plan.getType();
		this.nodeDepthValue = depth;
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