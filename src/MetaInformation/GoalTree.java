package MetaInformation;

import java.util.ArrayList;

import edu.wpi.cetask.Plan;
import edu.wpi.disco.Disco;

public class GoalTree {

	private int depthCounter = 1;
	
	private Node topLevelNode;

	ArrayList<Node> preorderTree = new ArrayList<Node>();
	
	public GoalTree(Disco disco) {
		
		topLevelNode = new Node(disco.getTop(disco.getFocus()), 0);
	}
	
	public ArrayList<Node> createTree() {
		
		preorderTraverse(topLevelNode);
		
		return preorderTree;
	}
	
	private void preorderTraverse(Node node) {
		
		if (node == null) {
			depthCounter--;
			return;
		}
		
		preorderTree.add(node);
		
		for (int i = 0 ; i < node.getNodeGoalPlan().getChildren().size() ; i++) {
			
			preorderTraverse(createNode(node.getNodeGoalPlan().getChildren().get(i), depthCounter++));
		}
	}
	
	private Node createNode(Plan goalPlan, int planDepthValue) {
		
		if (goalPlan != null)
			return new Node(goalPlan, planDepthValue);
		else
			return null;
	}
}