package MetaInformation;

import edu.wpi.cetask.Plan;

public class Node {
	
	private Plan nodeGoalPlan;
	private int nodeDepthValue;
	
	public Node(Plan plan, int depth) {
		this.nodeGoalPlan   = plan;
		this.nodeDepthValue = depth;
	}
	
	private void setNodeGoalPlan(Plan goalPlan) {
		this.nodeGoalPlan = goalPlan;
	}
	
	private void setNodeDepthValue(int depthValue) {
		this.nodeDepthValue = depthValue;
	}
	
	public Plan getNodeGoalPlan() {
		return this.nodeGoalPlan;
	}
	
	public int getNodeDepthValue() {
		return this.nodeDepthValue;
	}
}