package MetaInformation;

import java.util.ArrayList;

import MentalState.Goal;
import edu.wpi.cetask.Plan;
import edu.wpi.disco.Disco;
import edu.wpi.disco.lang.Accept;

public class GoalTree {

	private int nodeCounter = 1;
	private Node topLevelNode;
	private Disco disco;
	private MentalProcesses mentalProcesses;
	
	private ArrayList<Node> preorderTree = new ArrayList<Node>();
	
	public GoalTree(MentalProcesses mentalProcesses) {
		
		this.mentalProcesses = mentalProcesses;
		this.disco = mentalProcesses.getCollaborationMechanism().getDisco();
		
		topLevelNode = new Node(new Goal(mentalProcesses, disco.getTop(disco.getFocus())), 0);
	}
	
	public ArrayList<Node> createTree() {
		
		preorderTraverse(topLevelNode);
		
		return preorderTree;
	}
	
	private void preorderTraverse(Node node) {
		
		if (node == null) {
			nodeCounter--;
			return;
		}
		
		preorderTree.add(node);
		
		for (int i = 0 ; i < node.getNodeGoalPlan().getChildren().size() ; i++) {
			if (!(node.getNodeGoalPlan().getChildren().get(i).getGoal() instanceof Accept)) {
				preorderTraverse(createNode(node.getNodeGoalPlan().getChildren().get(i), 
						mentalProcesses.getCollaborationMechanism().getDistanceFromTop(node.getNodeGoalPlan())));
			}
		}
	}
	
	public ArrayList<Node> levelUpNodes(ArrayList<Node> treeNodes, Plan firstGoalPlan, Plan secondGoalPlan) {
		
		Node firstNode = null, secondNode = null;
		
		ArrayList<Node> twoLeveledNodes = new ArrayList<Node>();
		
		for (Node node : treeNodes) {
			if(node.getNodeGoalPlan().getType().equals(firstGoalPlan.getType()))
				firstNode = node;
			if(node.getNodeGoalPlan().getType().equals(secondGoalPlan.getType()))
				secondNode = node;
		}
		
		while (firstNode.getNodeDepthValue() > secondNode.getNodeDepthValue())
			firstNode = getParentNode(treeNodes, firstNode);
		
		while (firstNode.getNodeDepthValue() < secondNode.getNodeDepthValue())
			secondNode = getParentNode(treeNodes, secondNode);
		
		twoLeveledNodes.add(firstNode);
		twoLeveledNodes.add(secondNode);
		
		if (twoLeveledNodes.size() == 2)
			return twoLeveledNodes;
		else
			return null;
	}
	
	public Node goUpToCommonAncestor(ArrayList<Node> treeNodes, ArrayList<Node> leveledUpNodes) {
		
		Node firstNodeAncestor, secondNodeAncestor;
		
		firstNodeAncestor  = leveledUpNodes.get(0);
		secondNodeAncestor = leveledUpNodes.get(1);
		
		while (!firstNodeAncestor.getNodeTaskClass().equals(secondNodeAncestor.getNodeTaskClass())) {
			firstNodeAncestor  = getParentNode(treeNodes, firstNodeAncestor);
			secondNodeAncestor = getParentNode(treeNodes, secondNodeAncestor);
			if ((firstNodeAncestor == null) || (secondNodeAncestor == null)) {
				return null;
			}
		}
		
		return firstNodeAncestor;
	}

	private Node getParentNode(ArrayList<Node> treeNodes, Node targetNode) {
		
		for(int i = treeNodes.size()-1 ; i >= 0 ; i--) {
			if (treeNodes.get(i).equals(targetNode)) {
				for(int j = i-1 ; j >= 0 ; j--) {
					if (treeNodes.get(j).getNodeDepthValue() == targetNode.getNodeDepthValue()-1) {
						return treeNodes.get(j);
					}
				}
			}
		}
		
		return null;
	}
	
	private Node createNode(Plan goalPlan, int planDepthValue) {
		
		if (goalPlan != null)
			return new Node(new Goal(mentalProcesses, goalPlan), planDepthValue);
		else
			return null;
	}
	
	public int getNodeNumbers() {
		return this.nodeCounter;
	}
}