package MentalState;

import java.util.ArrayList;
import java.util.List;

import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Collaboration.Collaboration.GOAL_STATUS;
import MentalState.Motive.MOTIVE_TYPE;
import MetaInformation.GoalTree;
import MetaInformation.MentalProcesses;
import MetaInformation.MotivationVector;
import MetaInformation.Node;
import MetaInformation.Turns;
import edu.wpi.cetask.Plan;

public class Goal {
	
	public enum DIFFICULTY {NORMAL, DIFFICULT, MOST_DIFFICULT};
	
	private ArrayList<Belief> Beliefs       = new ArrayList<Belief>();
	private ArrayList<Motive> Motives       = new ArrayList<Motive>();
	private ArrayList<Intention> Intentions = new ArrayList<Intention>();
	private ArrayList<Goal> descendentGoals = new ArrayList<Goal>();
	
	private Plan plan;
	private Plan parentPlan;
	private String label;
	private int currentTurn;
	private double effort;
	private boolean tactical;
	private GOAL_STATUS goalStatus;
	private Collaboration collaboration;
	private MentalProcesses mentalProcesses;
//	private Motive activeMotive = null;
	
	public Goal (MentalProcesses mentalProcesses) {
		this.mentalProcesses = mentalProcesses;
		this.collaboration   = mentalProcesses.getCollaborationMechanism();
		this.plan            = collaboration.getActualFocus();
		this.parentPlan      = plan.getParent();
		this.label           = plan.getGoal().getType().toString();
		this.currentTurn     = Turns.getInstance().getTurnNumber();
		this.goalStatus      = GOAL_STATUS.UNKNOWN;
		this.effort          = getGoalDifficultyValue(this);
		this.tactical        = false;
	}
	
	//This constructor either should not be used or the associated goal's effort should be set manually.
	public Goal (MentalProcesses mentalProcesses, Plan plan) {
		this.mentalProcesses = mentalProcesses;
		this.collaboration   = mentalProcesses.getCollaborationMechanism();
		this.plan            = plan;
		this.parentPlan      = plan.getParent();
		this.label           = plan.getGoal().getType().toString();
		this.currentTurn     = Turns.getInstance().getTurnNumber();
		this.goalStatus      = GOAL_STATUS.UNKNOWN;
		this.effort          = -1;
		this.tactical        = false;
	}
	
	public Goal (MentalProcesses mentalProcesses, Plan plan, boolean tactical) {
		this.mentalProcesses = mentalProcesses;
		this.collaboration   = mentalProcesses.getCollaborationMechanism();
		this.plan            = plan;
		this.parentPlan      = plan.getParent();
		this.label           = plan.getGoal().getType().toString();
		this.currentTurn     = Turns.getInstance().getTurnNumber();
		this.goalStatus      = GOAL_STATUS.UNKNOWN;
		this.effort          = getGoalDifficultyValue(this);
		this.tactical        = tactical;
	}
	
	public Goal (MentalProcesses mentalProcesses, Plan plan, GOAL_STATUS goalStatus) {
		this.mentalProcesses = mentalProcesses;
		this.collaboration   = mentalProcesses.getCollaborationMechanism();
		this.plan            = collaboration.getActualFocus();;
		this.parentPlan      = plan.getParent();
		this.label           = plan.getGoal().getType().toString();
		this.currentTurn     = Turns.getInstance().getTurnNumber();
		this.goalStatus      = goalStatus;
		this.effort          = getGoalDifficultyValue(this);
		this.tactical        = false;
	}
	
	public boolean isTactical() {
		return this.tactical;
	}
	
	public void setGoalStatus (GOAL_STATUS goalStatus) {
		this.goalStatus = goalStatus;
	}
	
	public Plan getPlan() {
		return this.plan;
	}
	
	public MentalProcesses getMentalProcesses() {
		return this.mentalProcesses;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public Plan getParentPlan() {
		return this.parentPlan;
	}
	
	public void addBeliefs(Belief belief) {
		this.Beliefs.add(belief);
	}
	
	public void addIntentions(Intention intention) {
		this.Intentions.add(intention);
	}
	
	public void addMotives(Motive motive) {
		this.Motives.add(motive);
	}
	
	public void addGoalToMentalState() {
		MentalState.getInstance().addGoal(this);
	}
	
	public ArrayList<Belief> getBeliefs() {
		return this.Beliefs;
	}
	
	public ArrayList<Intention> getIntentions() {
		return this.Intentions;
	}
	
	public ArrayList<Motive> getMotives() {
		return this.Motives;
	}
	
//	public void addMotives(Motive motive) {
//	if (Motives.size() == 0)
//		this.setActiveMotive(motive);
//	Motives.add(motive);
//}
	
//	public void setActiveMotive(Motive motive) {
//		this.activeMotive = motive;
//	}
	
	// This method might need to be changed after I implement the Motivation mechanism.
//	public Motive getActiveMotive() {
//		if (this.activeMotive == null)
//			return MentalState.getInstance().getParentGoal(this).getActiveMotive();
//		else
//			return this.activeMotive;
//	}
	
	public Motive getActiveMotive() {
		
		for(Motive motive : this.Motives) {
			if (motive.isActiveMotive())
				return motive;
		}
		return null;
	}
	
	public double getGoalEffort() {
		return this.effort;
	}
	
	public void setGoalEffort(double effort) {
		this.effort = effort;
	}
	
	public Motive getSatisfactionMotive() {
		for (Motive motive : this.Motives) {
			if (motive.getMotiveType().equals(MOTIVE_TYPE.SATISFACTION))
				return motive;
		}
		return null;
	}
	
	public Motive getAchievementMotive() {
		for (Motive motive : this.Motives) {
			if (motive.getMotiveType().equals(MOTIVE_TYPE.ACHIEVEMENT))
				return motive;
		}
		return null;
	}
	
	public Motive getExternalMotive() {
		for (Motive motive : this.Motives) {
			if (motive.getMotiveType().equals(MOTIVE_TYPE.EXTERNAL))
				return motive;
		}
		return null;
	}
	
	public MotivationVector getMotivesVector() {
		return new MotivationVector(this);
	}
	
	private int getDistanceFromTop(Plan goalPlan) {
		
		int count = 0;
		
		while (!goalPlan.equals(collaboration.getDisco().getTop(goalPlan))) { 
			goalPlan = goalPlan.getParent();
			count++;
		}
		
		return count;
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
	
	private ArrayList<Node> levelUpNodes(ArrayList<Node> treeNodes, Plan firstGoalPlan, Plan secondGoalPlan) {
		
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
	
	private Node goUpToCommonAncestor(ArrayList<Node> treeNodes, ArrayList<Node> leveledUpNodes) {
		
		Node firstNodeAncestor, secondNodeAncestor;
		
		firstNodeAncestor  = leveledUpNodes.get(0);
		secondNodeAncestor = leveledUpNodes.get(1);
		
		while (!firstNodeAncestor.getNodeTaskClass().equals(secondNodeAncestor.getNodeTaskClass())) {
			firstNodeAncestor  = getParentNode(treeNodes, leveledUpNodes.get(0));
			secondNodeAncestor = getParentNode(treeNodes, leveledUpNodes.get(1));
			if ((firstNodeAncestor == null) || (secondNodeAncestor == null)) {
				return null;
			}
		}
		
		return firstNodeAncestor;
	}
	
	private Node getLowestCommonAncestor(Plan firstGoalPlan, Plan secondGoalPlan) {
		
		Node lcaGoalNode = null;
		
		GoalTree goalTree = new GoalTree(mentalProcesses);
		
		ArrayList<Node> treeNodes = goalTree.createTree();
		
		ArrayList<Node> leveledUpNodes = levelUpNodes(treeNodes, firstGoalPlan, secondGoalPlan);
		
		if (leveledUpNodes != null) {
			lcaGoalNode = goUpToCommonAncestor(treeNodes, leveledUpNodes);
		}
		
		return lcaGoalNode;
	}
	
	private int getGoalsDistance (Plan firstGoal, Plan secondGoal) {
		
		if (firstGoal.getType().equals(secondGoal.getType()))
			return 1;
		
		int firstGoalDistance  = getDistanceFromTop(firstGoal);
		int secondGoalDistance = getDistanceFromTop(secondGoal);
		
		Node lcaNode = getLowestCommonAncestor(firstGoal, secondGoal);
		
		int lcaGoalDistance  = getDistanceFromTop(lcaNode.getNodeGoalPlan());
		
		int distance = firstGoalDistance + secondGoalDistance - 2*lcaGoalDistance;
		
		return distance;
	}
	
	public double getGoalProximity() {
		
		int totalEdgeCount = collaboration.getTotalEdgeCount();
		
		double minProximity = (double)1.0/totalEdgeCount;
		double proximity    = (double)getGoalsDistance(this.getPlan(), collaboration.getDisco().getFocus())/totalEdgeCount;
		
		if(proximity >= minProximity)
			return proximity;
		else
			return minProximity;
	}
	
	private int getMaxGoalHeight() {
		
		int maxHeight = -1;
		
		GoalTree goalTree = new GoalTree(mentalProcesses);
		ArrayList<Node> treeNodes = goalTree.createTree();
		
		for (Node node : treeNodes)
			if (maxHeight < node.getNodeDepthValue())
				maxHeight = node.getNodeDepthValue();
		
		return maxHeight;
	}
	
	private double getGoalHeightRatio() {
		
		int maxDepth = -1, goalDepth = 0;
		
		GoalTree goalTree = new GoalTree(mentalProcesses);
		ArrayList<Node> treeNodes = goalTree.createTree();
		
		for (int i = 0 ; i < treeNodes.size() ; i++) {
			Node node = treeNodes.get(i);
			if (node.getNodeGoalPlan().getType().equals(this.getPlan().getType())) {
				if (node.getNodeGoalPlan().getChildren().size() != 0) {
					while ((node.getNodeDepthValue() >= maxDepth) && (i < treeNodes.size())) {
						maxDepth = node.getNodeDepthValue();
						node = treeNodes.get(i++);
					}
					break;
				}
				else {
					maxDepth = node.getNodeDepthValue();
					break;
				}
			}
		}
		
		for (Node node : treeNodes) {
			if (node.getNodeGoalPlan().getType().equals(this.getPlan().getType())) {
				goalDepth = node.getNodeDepthValue();
				break;
			}
		}
		return ((double)((maxDepth - goalDepth) + 1)/(getMaxGoalHeight() + 1));
	}
	
	private ArrayList<Goal> findPredecessors(ArrayList<Node> treeNodes, List<Plan> planPredecessors) {
		
		ArrayList<Goal> predecessors = new ArrayList<Goal>(); 
		
		for(Plan plan : planPredecessors) {
			for (Node node : treeNodes) {
				if (node.getNodeGoalPlan().getType().equals(plan.getType()))
					predecessors.add(node.getNodeGoal());
			}
		}
		
		return predecessors;
	}
	
	private ArrayList<Goal> findDescendants(ArrayList<Node> treeNodes) {
		
		int index = 0;
		
		ArrayList<Goal> descendants = new ArrayList<Goal>();
		
		for (int i = 0 ; i < treeNodes.size() ; i++) {
			if (treeNodes.get(i).getNodeGoalPlan().getType().equals(this.getPlan().getType())) {
				index = i+1;
				while ((index < treeNodes.size()) && (treeNodes.get(index).getNodeDepthValue() > treeNodes.get(i).getNodeDepthValue())) {
					descendants.add(treeNodes.get(index).getNodeGoal());
					index++;
				}
				return descendants;
			}
		}
		
		return descendants;
	}
	
	private double getDescendentEffort() {
		
		double effortSum = getGoalDifficultyValue(this);
		
		ArrayList<Goal> descendants = findDescendants(new GoalTree(mentalProcesses).createTree());
		
		for (Goal descendent : descendants) {
			effortSum += getGoalDifficultyValue(descendent);
		}
		
		return ((double)effortSum/getTotalGoalsDifficultyValue());
	}
	
	private double getPredecessorEffort() {
		
		double effortSum = 0.0;
		
		ArrayList<Goal> predecessors = findPredecessors(new GoalTree(mentalProcesses).createTree(), this.getPlan().getPredecessors());
		
		for (Goal predecessor : predecessors) {
			effortSum += getPredecessorGoalDifficultyValue(predecessor);
		}
		
		return ((double)effortSum/getTotalGoalsDifficultyValue());
	}
	
	public double getGoalDifficulty() {
		
		double goalDifficulty = 0.0;
		
		double goalHeight        = getGoalHeightRatio();
		double predecessorEffort = getPredecessorEffort();
		double descendentEffort  = getDescendentEffort();
		
		goalDifficulty = ((double)(goalHeight + 1)/(getMaxGoalHeight() + 1)) * (predecessorEffort + descendentEffort);
		
		return goalDifficulty;
	}
	
	private Integer getGoalDepth() {
		
		GoalTree goalTree = new GoalTree(mentalProcesses);
		
		ArrayList<Node> treeNodes = goalTree.createTree();
		
		for (Node node : treeNodes) {
			if (node.getNodeGoalPlan().getType().equals(this.getPlan().getType()))
				return node.getNodeDepthValue();
		}
		
		return null;
	}
	
	private double getTotalGoalsDifficultyValue() {
		
		double totalDifficulty = 0.0;
		GoalTree goalTree = new GoalTree(mentalProcesses);
		ArrayList<Node> treeNodes = goalTree.createTree();
		
		for (Node node : treeNodes) {
			switch (DIFFICULTY.valueOf(node.getNodeGoalPlan().getType().getProperty("@difficulty"))) {
				case NORMAL:
					totalDifficulty += 1.0;
					break;
				case DIFFICULT:
					totalDifficulty += 2.0;
					break;
				case MOST_DIFFICULT:
					totalDifficulty += 3.0;
					break;
				default:
					throw new IllegalStateException("Illegal Difficulty Value: " + DIFFICULTY.valueOf(node.getNodeGoalPlan().getType().getProperty("@difficulty")));
			}
		}
		 return totalDifficulty;
	}
	
	private double getGoalDifficultyValue(Goal goal) {
		
		Plan plan = goal.getPlan();
		
		switch (DIFFICULTY.valueOf(plan.getType().getProperty("@difficulty"))) {
			case NORMAL:
				return 1.0;
			case DIFFICULT:
				return 2.0;
			case MOST_DIFFICULT:
				return 3.0;
			default:
				throw new IllegalStateException("Illegal Difficulty Value: " + DIFFICULTY.valueOf(plan.getType().getProperty("@difficulty")));
		}
	}
	
	private double getPredecessorGoalDifficultyValue(Goal goal) {
		
		if (goal.getPlan().isPrimitive())
			return getGoalDifficultyValue(goal);
		else {
			double goalDifficultySum = getGoalDifficultyValue(goal);
			List<Goal> descendents   = getDescendentGoals(goal);
			
			for (Goal descendent : descendents)
				goalDifficultySum += getGoalDifficultyValue(descendent);

			return goalDifficultySum;
		}
	}
	
	private List<Goal> getDescendentGoals(Goal goal) {
		
		descendentGoals.clear();
		extractDescendentGoals(goal);
		return descendentGoals;
	}
	
	private void extractDescendentGoals(Goal goal) {
		
		int i;
		
		GoalTree goalTree = new GoalTree(mentalProcesses);
		ArrayList<Node> treeNodes = goalTree.createTree();
		
		for (i = 0 ; i < treeNodes.size() ; i++)
			if (treeNodes.get(i).getNodeGoalPlan().getType().equals(goal.getPlan().getType()))
				break;
		for (i = i+1 ; i < treeNodes.size() ; i++)
			descendentGoals.add(treeNodes.get(i).getNodeGoal());
	}
	
	public double getGoalSpecificity() {
		
		Integer goalDepth = getGoalDepth();
		
		if (goalDepth == null) System.out.println("Goal Management: Goal was not found in the tree!");
		
		// Later, I might need to change to number of descendents instead of children. 
		int goalDegree = this.getPlan().getChildren().size();
		
		return ((double)goalDepth/(goalDegree+1));
	}
}
