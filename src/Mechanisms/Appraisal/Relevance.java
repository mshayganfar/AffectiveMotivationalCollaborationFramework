package Mechanisms.Appraisal;

import java.util.ArrayList;
import java.util.List;

import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Collaboration.Collaboration.AGENT;
import Mechanisms.Collaboration.Collaboration.GOAL_STATUS;
import Mechanisms.Perception.Perception;
import MentalState.Belief;
import MentalState.Goal;
import MetaInformation.GoalTree;
import MetaInformation.Node;
import MetaInformation.Turns;
import edu.wpi.cetask.Plan;

public class Relevance extends AppraisalProcesses {
	
	public enum RELEVANCE {RELEVANT, IRRELEVANT};
	
	private Collaboration collaboration;
	
	public Relevance(Collaboration collaboration) {
		this.collaboration = collaboration;
	}
	
	public RELEVANCE isEventRelevant(Goal eventGoal) {
		
		double eventUtility = getEventUtility(eventGoal);
		double emotionalThreshold = getEmotionalThreshold();
		
		if(emotionalThreshold <= Math.abs(eventUtility))
			return RELEVANCE.RELEVANT;
		else
			return RELEVANCE.IRRELEVANT;
	}
	
	public double getEventUtility(Goal eventGoal) { 
		
		int goalStatus           = getGoalStatus(eventGoal);
		double beliefPersistence = getBeliefPersistence(eventGoal);
		double beliefSaliency    = getBeliefSaliency(eventGoal); //Distance between live nodes 
		double saliencyMagnitude = getSaliencyMagnitude(eventGoal);
		
		if (saliencyMagnitude > 0)
			return goalStatus*beliefPersistence*Math.pow(beliefSaliency, saliencyMagnitude);
		else if(saliencyMagnitude == 0)
			return 0.0;
		else 
			return -2.0;
	}
	
	private double getEmotionalThreshold() { 
		
		double emotionValence = Perception.getEmotionValence();
		
		return (emotionValence > 0) ? (1-emotionValence) : Math.abs(emotionValence); 
	}
	
	private double getBeliefPersistence(Goal eventGoal) {
		
		int repeatedBeliefsCount = 1;
		int repeatedBeliefsSum = 0;
		double repeatedBeliefsAverage = 1;
		
		List<String> uniqueBeliefs = new ArrayList<String>();
		
		for (Belief belief : eventGoal.getBeliefs()) {
//			if (belief.getTurn() == eventGoal.getTurn()) {
				if ((belief.getOccurrenceCount() > 1) && (!uniqueBeliefs.contains(belief.getLabel()))) {
					uniqueBeliefs.add(belief.getLabel());
					repeatedBeliefsCount++;
					repeatedBeliefsSum += belief.getOccurrenceCount();
				}
//			}
		}
		
		repeatedBeliefsCount = (repeatedBeliefsCount > 1) ? (repeatedBeliefsCount-1) : repeatedBeliefsCount;
		
		repeatedBeliefsAverage = (double)repeatedBeliefsSum/repeatedBeliefsCount;
		
		return (repeatedBeliefsAverage == 0) ? 1 : repeatedBeliefsAverage;
	}
	
	private Belief getLastRelatedBelief(Goal goal) {
		
		return goal.getBeliefs().get(goal.getBeliefs().size()-1);
	}
	
	private double getBeliefSaliency(Goal goal) {
		
		if (goal.getPlan().equals(collaboration.getDisco().getFocus()))
			return 2.0;
		
		int firstGoalDistance  = getDistanceFromTop(goal.getPlan());
		int secondGoalDistance = getDistanceFromTop(collaboration.getDisco().getFocus());
		
		Node lcaNode = getLowestCommonAncestor(goal.getPlan(), collaboration.getDisco().getFocus());
		
		int lcaGoalDistance  = getDistanceFromTop(lcaNode.getNodeGoalPlan());
		
		double distance = firstGoalDistance + secondGoalDistance - 2*lcaGoalDistance;
		
		return (double)2.0/distance;
	}
	
	private int getDistanceFromTop(Plan goalPlan) {
		
		int count = 0;
		
		while (!goalPlan.equals(collaboration.getDisco().getTop(goalPlan))) { 
			goalPlan = goalPlan.getParent();
			count++;
		}
		
		return count;
	}
	
	private Node getLowestCommonAncestor(Plan firstGoalPlan, Plan secondGoalPlan) {
		
		Node lcaGoalNode = null;
		
		GoalTree goalTree = new GoalTree(collaboration.getDisco());
		
		ArrayList<Node> treeNodes = goalTree.createTree();
		
		ArrayList<Node> leveledUpNodes = levelUpNodes(treeNodes, firstGoalPlan, secondGoalPlan);
		
		if (leveledUpNodes != null) {
			lcaGoalNode = goUpToCommonAncestor(treeNodes, leveledUpNodes);
		}
		
		return lcaGoalNode;
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
	
	private int getGoalStatus(Goal goal) {
		
		GOAL_STATUS goalStatus = collaboration.getGoalStatus(goal.getPlan());
		if (goalStatus.equals(GOAL_STATUS.ACHIEVED) || 
			goalStatus.equals(GOAL_STATUS.PENDING) || 
			goalStatus.equals(GOAL_STATUS.INPROGRESS))
			return 1;
		else if (goalStatus.equals(GOAL_STATUS.FAILED) || 
				goalStatus.equals(GOAL_STATUS.BLOCKED) || 
				goalStatus.equals(GOAL_STATUS.INAPPLICABLE)) 
			return -1;
		else
			return 0;
	}
	
	private double getSaliencyMagnitude(Goal goal) {
		
		int preconditionKnownValue  = (collaboration.getPreConditionStatus(goal.getPlan()) != null) ? 1 : 0;
		int postconditionKnownValue = (collaboration.getPostConditionStatus(goal.getPlan()) != null) ? 1 : 0;
		int predecessorsGoalsKnownValue             = 0;
		int contributingGoalspredecessorsKnownValue = 0;
		
		List<Plan> predecessors      = collaboration.getPredecessors(goal);
		List<Plan> contributingGoals = collaboration.getContributingPlans(goal);

		int preconditionAllValue  = 1;
		int postconditionAllValue = 1;
		int predecessorsGoalsAllValue             = predecessors.size();
		int contributingGoalspredecessorsAllValue = contributingGoals.size();
		
		for(Plan predecessor : predecessors) {
			if (collaboration.getPreConditionStatus(predecessor) != null)
				predecessorsGoalsKnownValue++;
		}
		
		for(Plan contributingGoal : contributingGoals) {
			if (collaboration.getPostConditionStatus(contributingGoal) != null)
				contributingGoalspredecessorsKnownValue++;
		}
		
		int n = preconditionKnownValue + postconditionKnownValue + predecessorsGoalsKnownValue + contributingGoalspredecessorsKnownValue;
		int d = preconditionAllValue + postconditionAllValue + predecessorsGoalsAllValue + contributingGoalspredecessorsAllValue;
		
		double urgency    = getMotiveUrgency(goal);
		double importance = getMotiveImportance(goal);
		
		return (((double)n/d) + urgency + importance);
	}
	
	private double getMotiveUrgency(Goal goal) {
		
		double urgencySuccessorValue  = 0;
		double urgencyMitigationValue = 0;
		
		List<Plan> successors = goal.getPlan().getSuccessors();
		
		for (Plan successor : successors) {
			if (collaboration.getResponsibleAgent(goal).equals(AGENT.OTHER))
				urgencySuccessorValue++;
			else if (collaboration.getResponsibleAgent(goal).equals(AGENT.BOTH))
				urgencySuccessorValue += 0.5;
		}
		
		urgencyMitigationValue = (isAcknowledgementMotive(goal)) ? 1 : 0;
		
		return (urgencySuccessorValue + urgencyMitigationValue);
	}
	
	private double getMotiveImportance(Goal goal) {
		
		// I need non-failed alternative recipes for a failed task.
		// I might need to check whether the previous shared goal is failed/blocked, etc.!
		Plan plan = goal.getPlan();
		
		if (plan.isPrimitive())
			plan = goal.getPlan().getParent();
		
		if (plan.getDecompositions().size() > 0)
			return 1.0;
		else
			return 0.0;
	}
	
	private boolean isAcknowledgementMotive(Goal goal) {
		return false;
	}
}