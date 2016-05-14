package Mechanisms.Appraisal;

import java.util.ArrayList;
import java.util.List;

import org.omg.CORBA.UNKNOWN;

import Mechanisms.Collaboration.Collaboration.GOAL_STATUS;
import MentalState.Belief;
import MentalState.Goal;
import MetaInformation.GoalTree;
import MetaInformation.MentalProcesses;
import MetaInformation.Node;
import edu.wpi.cetask.Plan;

public class Relevance extends AppraisalProcesses {
	
	public enum RELEVANCE {RELEVANT, IRRELEVANT, UNKNOWN};
	
	public Relevance(MentalProcesses mentalProcesses) {
		this.mentalProcesses = mentalProcesses;
		this.collaboration   = mentalProcesses.getCollaborationMechanism();
		this.motivation      = mentalProcesses.getMotivationMechanism();
		this.perception      = mentalProcesses.getPerceptionMechanism();
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
		
		int goalStatus           = getGoalStatusImpact(eventGoal);
		double beliefPersistence = getBeliefPersistence(eventGoal);
		double beliefSaliency    = getBeliefSaliency(eventGoal); // Distance between live nodes 
		double saliencyMagnitude = getSaliencyMagnitude(eventGoal);
		
		if (saliencyMagnitude > 0)
			return goalStatus*beliefPersistence*Math.pow(beliefSaliency, saliencyMagnitude);
		else if(saliencyMagnitude == 0)
			return 0.0;

		return -2.0; // Should never happen!
	}
	
	private double getEmotionalThreshold() { 
		
		double emotionValence = perception.getEmotionValence();
		
		return (emotionValence > 0) ? (1-emotionValence) : Math.abs(emotionValence); 
	}
	
	private double getBeliefPersistence(Goal eventGoal) {
		
		int repeatedBeliefsCount = 1;
		int repeatedBeliefsSum   = 0;
		double repeatedBeliefsAverage = 1.0;
		
		List<String> uniqueBeliefs = new ArrayList<String>();
		
		for (Belief belief : eventGoal.getBeliefs()) {
//			if (belief.getTurn() == eventGoal.getTurn())
			if ((belief.getOccurrenceCount() > 1) && (!uniqueBeliefs.contains(belief.getLabel()))) {
				uniqueBeliefs.add(belief.getLabel());
				repeatedBeliefsCount++;
				repeatedBeliefsSum += belief.getOccurrenceCount();
			}
		}
		
		repeatedBeliefsCount = (repeatedBeliefsCount > 1) ? (repeatedBeliefsCount-1) : repeatedBeliefsCount;
		
		repeatedBeliefsAverage = (double)repeatedBeliefsSum/repeatedBeliefsCount;
		
		return (repeatedBeliefsAverage == 0) ? 1.0 : repeatedBeliefsAverage;
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
		
		if (lcaNode != null) {
			int lcaGoalDistance  = getDistanceFromTop(lcaNode.getNodeGoalPlan());
			int distance = firstGoalDistance + secondGoalDistance - 2*lcaGoalDistance;
		
			return (distance != 0) ? (double)2.0/distance : 2.0;
		}
		else
			return 0.0;
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
		
		GoalTree goalTree = new GoalTree(mentalProcesses);
		
		ArrayList<Node> treeNodes = goalTree.createTree();
		
		ArrayList<Node> leveledUpNodes = goalTree.levelUpNodes(treeNodes, firstGoalPlan, secondGoalPlan);
		
		if (leveledUpNodes != null) {
			lcaGoalNode = goalTree.goUpToCommonAncestor(treeNodes, leveledUpNodes);
		}
		
		return lcaGoalNode;
	}
	
	private int getGoalStatusImpact(Goal goal) {
		
		GOAL_STATUS goalStatus = collaboration.getGoalStatus(goal.getPlan());
		
		if (goalStatus.equals(GOAL_STATUS.ACHIEVED) || 
			goalStatus.equals(GOAL_STATUS.PENDING) || 
			goalStatus.equals(GOAL_STATUS.INPROGRESS))
			return 1;
		else if (goalStatus.equals(GOAL_STATUS.FAILED) || 
				goalStatus.equals(GOAL_STATUS.BLOCKED) || 
				goalStatus.equals(GOAL_STATUS.INAPPLICABLE) ||
				goalStatus.equals(GOAL_STATUS.UNKNOWN))
			return -1;
		else
			throw new IllegalArgumentException("Illegal Goal Status: " + goalStatus);
	}
	
	private double getSaliencyMagnitude(Goal goal) {
		
		int knownPreconditionValue  = (collaboration.getPreconditionApplicability(goal.getPlan()) != null) ? 1 : 0;		
		int knownPostconditionValue = (!collaboration.getPostConditionStatus(goal.getPlan()).equals(GOAL_STATUS.UNKNOWN)) ? 1 : 0;
		int knownPredecessorGoalsCount  = 0;
		int knownContributingGoalsCount = 0;
		
		List<Plan> predecessors 	 = collaboration.getPredecessors(goal);
		List<Plan> contributingGoals = collaboration.getContributingPlans(goal);

		int totalPreconditionValue  = 1;
		int totalPostconditionValue = 1;
		int totalPredecessorGoalsCount  = predecessors.size();
		int totalContributingGoalsCount = contributingGoals.size();
		
		for(Plan predecessor : predecessors) {
			if (!collaboration.getPostConditionStatus(predecessor).equals(GOAL_STATUS.UNKNOWN))
				knownPredecessorGoalsCount++;
		}
		
		for(Plan contributingGoal : contributingGoals) {
			if (collaboration.getPreConditionStatus(contributingGoal) != null)
				knownContributingGoalsCount++;
		}
		
		int n = knownPreconditionValue + knownPostconditionValue + knownPredecessorGoalsCount + knownContributingGoalsCount;
		int d = totalPreconditionValue + totalPostconditionValue + totalPredecessorGoalsCount + totalContributingGoalsCount;
		
		double urgency    = this.motivation.getMotiveUrgency(goal.getActiveMotive());
		double importance = this.motivation.getMotiveImportance(goal.getActiveMotive());
		
		return (((double)n/d) + urgency + importance);
	}
}