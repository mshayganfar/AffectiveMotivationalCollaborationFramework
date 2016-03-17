package Mechanisms.Appraisal;

import java.util.ArrayList;
import java.util.List;

import Mechanisms.Collaboration.Collaboration.AGENT;
import Mechanisms.Collaboration.Collaboration.GOAL_STATUS;
import MentalState.Belief;
import MentalState.Goal;
import MetaInformation.GoalTree;
import MetaInformation.Node;
import edu.wpi.cetask.Plan;

public class Relevance extends AppraisalProcesses {
	
	public enum RELEVANCE {RELEVANT, IRRELEVANT};
	
	public Relevance(String[] args) {
		super(args);
	}
	
	public RELEVANCE isEventRelevant(Goal eventGoal) {
		
		double eventUtility = getEventUtility(eventGoal);
		double emotionalThreshold = getEmotionalThreshold();
		
		if(emotionalThreshold <= Math.abs(eventUtility))
			return RELEVANCE.RELEVANT;
		else
			return RELEVANCE.IRRELEVANT;
	}
	
	private double getEventUtility(Goal eventGoal) { 
		
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
		
		double emotionValence = perception.getEmotionValence();
		
		return (emotionValence > 0) ? (1-emotionValence) : Math.abs(emotionValence); 
	}
	
	private double getBeliefPersistence(Goal eventGoal) {
		
		int repeatedBeliefsCount = 1;
		int repeatedBeliefsSum = 0;
		double repeatedBeliefsAverage = 1;
		
		for (Belief belief : eventGoal.getBeliefs()) {
			if (belief.getTurn() == eventGoal.getTurn()) {
				if (belief.getOccurrenceCount() != 1) {
					repeatedBeliefsCount++;
					repeatedBeliefsSum += belief.getOccurrenceCount();
				}
			}
		}
		
		repeatedBeliefsCount = (repeatedBeliefsCount > 1) ? (repeatedBeliefsCount-1) : repeatedBeliefsCount;
		
		repeatedBeliefsAverage = (double)repeatedBeliefsSum/repeatedBeliefsCount;
		
		return repeatedBeliefsAverage;
	}
	
	private Belief getLastRelatedBelief(Goal goal) {
		
		return goal.getBeliefs().get(goal.getBeliefs().size()-1);
	}
	
	private double getBeliefSaliency(Goal goal) {
		
		if (goal.getPlan().equals(collaboration.getDisco().getFocus()))
			return 2.0;
		
		int firstGoalDistance  = getDistanceFromTop(goal.getPlan());
		int secondGoalDistance = getDistanceFromTop(collaboration.getDisco().getFocus());
		
		Plan lcaPlan = getLowestCommonAncestor(goal.getPlan(), collaboration.getDisco().getFocus());
		
		int lcaGoalDistance  = getDistanceFromTop(lcaPlan);
		
		double distance = firstGoalDistance + secondGoalDistance - 2*lcaGoalDistance;
		
		return (double)2.0/distance;
	}
	
	private int getDistanceFromTop(Plan goalPlan) {
		
		int count = 0;
		
		while (!goalPlan.getType().getNamespace().toString().equals(collaboration.getTaskModel().toString())) { 
			goalPlan = goalPlan.getParent();
			count++;
		}
		
		return count;
	}
	
	private Plan getLowestCommonAncestor(Plan firstGoalPlan, Plan secondGoalPlan) {
		
		Plan lcaGoalPlan = null;
		
		GoalTree goalTree = new GoalTree(collaboration.getDisco());
		
		ArrayList<Node> treeNodes = goalTree.createTree();
		
		ArrayList<Node> leveledUpNodes = levelUpNodes(treeNodes, firstGoalPlan, secondGoalPlan);
		
		if (leveledUpNodes != null) {
			lcaGoalPlan = goUpToCommonAncestor(treeNodes, leveledUpNodes);
		}
		
		return lcaGoalPlan;
	}
	
	private Plan goUpToCommonAncestor(ArrayList<Node> treeNodes, ArrayList<Node> leveledUpNodes) {
		
		Plan firstPlanAncestor, secondPlanAncestor;
		
		firstPlanAncestor  = leveledUpNodes.get(0).getNodeGoalPlan();
		secondPlanAncestor = leveledUpNodes.get(1).getNodeGoalPlan();
		
		while (!firstPlanAncestor.equals(secondPlanAncestor)) {
			firstPlanAncestor  = getParentNode(treeNodes, leveledUpNodes.get(0));
			secondPlanAncestor = getParentNode(treeNodes, leveledUpNodes.get(1));
			if ((firstPlanAncestor == null) || (secondPlanAncestor == null)) {
				return null;
			}
		}
		
		return firstPlanAncestor;
	}
	
	private Plan getParentNode(ArrayList<Node> treeNodes, Node targetNode) {
		
		for(int i = treeNodes.size()-1 ; i >= 0 ; i--) {
			if (treeNodes.get(i).equals(targetNode)) {
				for(int j = i-1 ; j >= 0 ; j--) {
					if (treeNodes.get(j).getNodeDepthValue() == targetNode.getNodeDepthValue()-1) {
						return treeNodes.get(j).getNodeGoalPlan();
					}
				}
			}
		}
		
		return null;
	}
	
	private ArrayList<Node> levelUpNodes(ArrayList<Node> treeNodes, Plan firstGoalPlan, Plan secondGoalPlan) {
		
		ArrayList<Node> twoLeveledNodes = new ArrayList<Node>();
		
		for (Node node : treeNodes) {
			if(node.getNodeGoalPlan().equals(firstGoalPlan))
				twoLeveledNodes.add(node);
			if(node.getNodeGoalPlan().equals(secondGoalPlan))
				twoLeveledNodes.add(node);
		}
		
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
		
		int urgency    = getMotiveUrgency(goal);
		int importance = getMotiveImportance(goal);
		
		return (((float)n/d) + urgency + importance);
	}
	
	private int getMotiveUrgency(Goal goal) {
		
		int urgencySuccessorValue  = 0;
		int urgencyMitigationValue = 0;
		
		List<Plan> successors = goal.getPlan().getSuccessors();
		
		for (Plan successor : successors) {
			if (collaboration.getResponsibleAgent(goal).equals(AGENT.OTHER))
				urgencySuccessorValue++;
		}
		
		urgencyMitigationValue = (isAcknowledgementMotive(goal)) ? 1 : 0;
		
		return (urgencySuccessorValue + urgencyMitigationValue);
	}
	
	private int getMotiveImportance(Goal goal) {
		
		// I need non-failed alternative recipes for a failed task.
		//if (goal.getPlan().getGoal().getDecompositions())
		return 0;
	}
	
	private boolean isAcknowledgementMotive(Goal goal) {
		return false;
	}
}