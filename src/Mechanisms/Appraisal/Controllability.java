package Mechanisms.Appraisal;

import edu.wpi.cetask.Plan;
import edu.wpi.cetask.TaskClass.Input;

import java.util.ArrayList;
import java.util.List;

import Mechanisms.Collaboration.Collaboration.GOAL_STATUS;
import MentalState.Goal;
import MentalState.Goal.DIFFICULTY;
import MentalState.Motive.MOTIVE_TYPE;
import MetaInformation.GoalTree;
import MetaInformation.MentalProcesses;
import MetaInformation.Node;

public class Controllability extends AppraisalProcesses{

	public enum CONTROLLABILITY {HIGH_CONTROLLABLE, LOW_CONTROLLABLE, UNCONTROLLABLE, UNKNOWN};
	
	private int achievedPredecessorCount = 0;
	private int totalPredecessorCount    = 0;
	
	ArrayList<Plan> unachievedChildren = new ArrayList<Plan>();
	ArrayList<Goal> descendentGoals = new ArrayList<Goal>();
	
	public Controllability(MentalProcesses mentalProcesses) {
		this.collaboration   = mentalProcesses.getCollaborationMechanism();
		this.mentalProcesses = mentalProcesses;
	}

	public CONTROLLABILITY isEventControllable(Goal eventGoal) {
		
		double controllabilityValue = 0.0;
		
		System.out.println(collaboration.getGoalStatus(eventGoal.getPlan()));
		
		// I consider UNKNOWN goal status *not* as a failure.
		if ((collaboration.getGoalStatus(eventGoal.getPlan()).equals(GOAL_STATUS.BLOCKED)) ||
				(collaboration.getGoalStatus(eventGoal.getPlan()).equals(GOAL_STATUS.FAILED)) ||
				(collaboration.getGoalStatus(eventGoal.getPlan()).equals(GOAL_STATUS.INAPPLICABLE))) {
				
				// Next two values are only used for goal failure cases.
				double dblAlternativeRecipeRatio = getAlternativeRecipeRatio(eventGoal);
				// This value is useful when the agent needs to check recovery from a failure.
				double dblRecoveryProbability    = getRecoveryProbability(eventGoal);
				
				controllabilityValue = ((double)((dblAlternativeRecipeRatio * getAlternativeRecipeWeight()) +
										(dblRecoveryProbability * getRecoveryProbabilityWeight())))
										/(getAlternativeRecipeWeight() + getRecoveryProbabilityWeight());
		}
		else {
			double dblAgencyValue  			 	 = getAgencyValue(eventGoal);
			double dblAutonomyValue			 	 = getAutonomyValue(eventGoal);
			Double dblPredecessorsRatio		 	 = getSucceededPredecessorsRatio(eventGoal.getPlan());
			Double dblInputsRatio 			 	 = getAvailableInputRatio(eventGoal);
			double dblOverallGoalDifficultyValue = getOverallDifficultyValue(eventGoal);
			
			controllabilityValue = ((double)((dblAgencyValue * getAgencyWeight()) + 
											 (dblAutonomyValue * getAutonomyWeight()) + 
											 (((dblPredecessorsRatio == null) ? 0.0 : dblPredecessorsRatio) * getPredecessorRatioWeight()) + 
											 (((dblInputsRatio == null) ? 0.0 : dblInputsRatio) * getInputRatioWeight()) +
											 (dblOverallGoalDifficultyValue * getGoalDifficultyWeight())))
											 /(getAgencyWeight() + getAutonomyWeight() + 
											  ((dblPredecessorsRatio == null) ? 0.0 : getPredecessorRatioWeight()) + 
											  ((dblInputsRatio == null) ? 0.0 : getInputRatioWeight()) + 
											  getGoalDifficultyWeight());
		}
		
		if (controllabilityValue >= 0) // Not sure if human emotion threshold is needed here!
			if (controllabilityValue > 0.5)
				return CONTROLLABILITY.HIGH_CONTROLLABLE;
			else
				return CONTROLLABILITY.LOW_CONTROLLABLE;
		else
			return CONTROLLABILITY.UNCONTROLLABLE;
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
	
	// Agency: The capacity, condition, or state of acting or of exerting power.
	private double getAgencyValue(Goal eventGoal) {
		
		if (eventGoal.getPlan().isPrimitive()) {
			if (!eventGoal.getActiveMotive().getMotiveType().equals(MOTIVE_TYPE.EXTERNAL))
				return 1.0;
			else
				return 0.0;
		}
		else {
			// TO DO: This can be an average of the children's non-external count.
			return 0.5;
		}
//			double motiveTypeSum = 0.0;
//			int countMotives = 0;
//			descendentGoals.clear();
//			getDescendentGoals(eventGoal);
//			for (Goal descendent : descendentGoals) {
//				if (!descendent.getActiveMotive().getMotiveType().equals(MOTIVE_TYPE.EXTERNAL))
//					motiveTypeSum++;
//				countMotives++;
//			}
//			return (double)motiveTypeSum/((countMotives == 0) ? 1 : countMotives);
	}
	
	// Autonomy: The quality or state of being self-governing. Self-directing freedom or self-governing state.
	private double getAutonomyValue(Goal eventGoal) {

		double countSelfResponsible = 0.0;
		
		Plan plan = eventGoal.getPlan();
		
		if (plan.isPrimitive()) {
			if (collaboration.getResponsibleAgent(plan).equals(AGENT.SELF))
				return 1.0;
			else if (collaboration.getResponsibleAgent(plan).equals(AGENT.OTHER))
				return -1.0;
			else  if (collaboration.getResponsibleAgent(plan).equals(AGENT.UNKNOWN))
				return -0.5;
			else 
				throw new IllegalArgumentException("Illegal Agent Type: " + collaboration.getResponsibleAgent(plan));
		}
		else {
			collaboration.getResponsibleAgent(plan);
			
			for (AGENT agent : collaboration.getDescendentResponsibility()) {
				if (agent.equals(AGENT.SELF))
					countSelfResponsible++;
				else if (agent.equals(AGENT.BOTH))
					countSelfResponsible += 0.5;
				else if (agent.equals(AGENT.OTHER))
					countSelfResponsible -= 1.0;
				else if (agent.equals(AGENT.UNKNOWN))
					countSelfResponsible -= 0.5;
			}
			return ((double)countSelfResponsible/((collaboration.getDescendentResponsibility().size() == 0) ? 1.0 : collaboration.getDescendentResponsibility().size()));
		}
	}
	
	private Double getSucceededPredecessorsRatio(Plan eventPlan) {
		
		checkSucceededPredecessors(eventPlan);
		
		int achievedCount = achievedPredecessorCount;
		int totalCount    = totalPredecessorCount;
		
		achievedPredecessorCount = 0;
		totalPredecessorCount    = 0;
		
		if (totalCount == 0)
			return null;
		
		return ((double)achievedCount/totalCount);  
	}
	
	private void checkSucceededPredecessors(Plan eventPlan) {
		
		for (Plan predecessor : eventPlan.getPredecessors()) {
			if (predecessor.isPrimitive()) {
				if (collaboration.getGoalStatus(predecessor).equals(GOAL_STATUS.ACHIEVED))
					achievedPredecessorCount++;
				totalPredecessorCount++;
			}
			else {
				for (Plan child : predecessor.getChildren())
					checkSucceededPredecessors(child);
			}
		}
		
//		double dblSucceededPredecessorCounter = 0.0;
//		
//		for (Plan plan : collaboration.getPredecessors(eventGoal)) {
//			if (!collaboration.getGoalStatus(plan).equals(GOAL_STATUS.UNKNOWN))
//				if (collaboration.getGoalStatus(plan).equals(GOAL_STATUS.ACHIEVED))
//					dblSucceededPredecessorCounter++;
//		}
//		return (double)dblSucceededPredecessorCounter/((collaboration.getPredecessors(eventGoal).size() == 0) ? 1 : collaboration.getPredecessors(eventGoal).size());
	}
	
	private ArrayList<Plan> getUnachievedPredecessors(Plan eventPlan) {
		
		ArrayList<Plan> unachievedPredecessors = new ArrayList<Plan>();
		
		for (Plan predecessor : eventPlan.getPredecessors())
			if (!collaboration.getGoalStatus(predecessor).equals(GOAL_STATUS.ACHIEVED))
				unachievedPredecessors.add(predecessor);
		
		return unachievedPredecessors;
	}
	
	private ArrayList<Input> getUndefinedInputs(Plan eventPlan) {
		
		ArrayList<Input> undefinedInputs = new ArrayList<Input>();
		
		for (Input input : eventPlan.getType().getDeclaredInputs())
			if (!input.isDefinedSlot(eventPlan.getGoal()))
				undefinedInputs.add(input);
		
		return undefinedInputs;
	}
	
	private ArrayList<Input> getAvailableInputs(Plan eventPlan, ArrayList<Input> undefinedInputs) {
		
		ArrayList<Input> availableInputs = new ArrayList<Input>();
		
		for (Input input : undefinedInputs)
			// I check only private beliefs about input availabilities (since they include shared beliefs about input availabilities).
			if (isInputAvailable(eventPlan, input))
				availableInputs.add(input);
		
		return availableInputs;
	}
	
	private boolean isInputAvailable(Plan eventPlan, Input input) {
		
		return (collaboration.getInputValue(eventPlan, input.getName()) != null) ? true : false;
	}
	
	public boolean canProvideInput(Plan eventPlan) {
		
		ArrayList<Input> undefinedInputs = getUndefinedInputs(eventPlan);
		ArrayList<Input> definedInputs   = getAvailableInputs(eventPlan, undefinedInputs);
		
		if (!definedInputs.isEmpty())
			return true;
		else
			return false;
	}
	
	public boolean canPreconditionSucceed(Plan eventPlan) {
		
		if (collaboration.getPreconditionValue(eventPlan) == null)
			return false;
		else if (collaboration.getPreconditionValue(eventPlan))
			return true;
		else
			return false;
	}
	
	public boolean canPredecessorSucceed(Plan plan) {
		
		ArrayList<Plan> unachievedPredecessors = getUnachievedPredecessors(plan);
		
		if(!unachievedPredecessors.isEmpty()) {
			for (Plan predecessor : unachievedPredecessors) {
				if (predecessor.isPrimitive()) {
					if (!(canProvideInput(predecessor) && canPreconditionSucceed(predecessor) && canPredecessorSucceed(predecessor)))
						return false;
				}
				else {
					for (Plan child : predecessor.getChildren())
						if (!canPredecessorSucceed(child))
							return false;
				}
			}
		}
		// I found no need for the following code, since no unachieved predecessor requires no inspection.
		
//		else {
//			if (plan.isPrimitive()) {
//				if (!(canProvideInput(plan) && canPreconditionSucceed(plan)))
//					return false;
//			}
//			else {
//				for (Plan child : plan.getChildren())
//					if (!canPredecessorSucceed(child))
//						return false;
//			}
//		}
		return true;
	}
	
	private List<Plan> getUnachievedChildren(Plan plan) {
		
		unachievedChildren.clear();
		extractUnachievedChildren(plan);
		return unachievedChildren;
	}
	
	private void extractUnachievedChildren(Plan plan) {
		
		if (plan.isPrimitive()) {
			if (!collaboration.getGoalStatus(plan).equals(GOAL_STATUS.ACHIEVED))
				unachievedChildren.add(plan);
		}
		else {
			for (Plan child : plan.getChildren())
				extractUnachievedChildren(child);
		}
	}
	
	public boolean canChildrenSucceed(Plan plan) {
		
		List<Plan> unachievedDescendents = getUnachievedChildren(plan);
		
		for (Plan child : unachievedDescendents)
			if (!(canProvideInput(child) && canPreconditionSucceed(child) && canPredecessorSucceed(child)))
				return false;
		
		return true;
	}
	
	private Double getAvailableInputRatio(Goal eventGoal) {
		
		double dblAvailableInputCounter = 0.0;
		
		if (collaboration.getInputs(eventGoal).size() == 0)
			return null;
		
		for (Input input : collaboration.getInputs(eventGoal)) {
			if (input != null)
				if(collaboration.isInputAvailable(eventGoal, input))
					dblAvailableInputCounter++;
		}
		return ((double)dblAvailableInputCounter/((collaboration.getInputs(eventGoal).size() == 0) ? 1.0 : collaboration.getInputs(eventGoal).size()));
	}
	
	private double getOverallDifficultyValue(Goal eventGoal) {
		
		Plan plan = eventGoal.getPlan();
		
		if (plan.isPrimitive()) {
			switch (DIFFICULTY.valueOf(plan.getType().getProperty("@difficulty"))) {
				case NORMAL:
					return 0.0;
				case DIFFICULT:
					return 0.5;
				case MOST_DIFFICULT:
					return 1.0;
				default:
					throw new IllegalStateException("Difficulty value: " + DIFFICULTY.valueOf(plan.getType().getProperty("@difficulty")));
			}
		}
		else {
			int goalDifficultyCount = 0;
			double goalDifficultySum = 0.0;
			
			switch (DIFFICULTY.valueOf(plan.getType().getProperty("@difficulty"))) {
				case NORMAL:
					goalDifficultySum = 0.0;
					goalDifficultyCount++;
					break;
				case DIFFICULT:
					goalDifficultySum = 0.5;
					goalDifficultyCount++;
					break;
				case MOST_DIFFICULT:
					goalDifficultySum = 1.0;
					goalDifficultyCount++;
					break;
				default:
					throw new IllegalStateException("Difficulty value: " + DIFFICULTY.valueOf(plan.getType().getProperty("@difficulty")));
			}
			
			List<Goal> descendents = getDescendentGoals(eventGoal);
			
			for (Goal descendent : descendents) {
				switch (DIFFICULTY.valueOf(descendent.getPlan().getType().getProperty("@difficulty"))) {
					case NORMAL:
						goalDifficultySum += 0.0;
						goalDifficultyCount++;
						break;
					case DIFFICULT:
						goalDifficultySum += 0.5;
						goalDifficultyCount++;
						break;
					case MOST_DIFFICULT:
						goalDifficultySum += 1.0;
						goalDifficultyCount++;
						break;
					default:
						throw new IllegalStateException("Difficulty value: " + DIFFICULTY.valueOf(descendent.getPlan().getType().getProperty("@difficulty")));
				}
			}
			return ((double)goalDifficultySum/goalDifficultyCount);
		}
	}
	
	private double getAlternativeRecipeRatio(Goal eventGoal) {
		
		Plan plan = eventGoal.getPlan(); 
		
		double alternativePlansCount = plan.getDecompositions().size();
		double failedPlansCount      = plan.getFailed().size();
		
		return ((alternativePlansCount + failedPlansCount) != 0) ? (double)alternativePlansCount/(alternativePlansCount + failedPlansCount) : alternativePlansCount;
	}
	
	private boolean hasPreconditionFailed(Plan eventPlan) {
		
//		Boolean preconditionStatus = collaboration.getPreconditionApplicabilities().get(
//				collaboration.getApplicabilityKeyValue(eventPlan, eventPlan.getType().getPrecondition().getScript()));
//		
//		if (preconditionStatus == null)
//			return true;
//		else if (preconditionStatus)
//			return false;
//		else
//			return true;
		
		if (eventPlan.getGoal().isApplicable() == null)
			return true;
		else if (!eventPlan.getGoal().isApplicable())
			return true;
		else
			return false;
	}
	
	private boolean hasUnachievedChild(Plan eventPlan) {
		
		for (Plan child : eventPlan.getChildren())
			if (!collaboration.getGoalStatus(child).equals(GOAL_STATUS.ACHIEVED))
				return true;

		return false;
	}
	
	public double getRecoveryProbability(Goal eventGoal) {
		
		double availableInput 		= 0.0;
		double improvePreconditions = 0.0;
		double succeedPredecessors  = 0.0;
		double succeedChildren      = 0.0;
		
		ArrayList<Input> undefinedInputs 	   = getUndefinedInputs(eventGoal.getPlan());
		ArrayList<Plan> unachievedPredecessors = getUnachievedPredecessors(eventGoal.getPlan());
		
		if (!undefinedInputs.isEmpty())
			if (canProvideInput(eventGoal.getPlan()))
				availableInput++;
		
		if (hasPreconditionFailed(eventGoal.getPlan()))
			if (canPreconditionSucceed(eventGoal.getPlan()))
				improvePreconditions++;
		
		if (!unachievedPredecessors.isEmpty())
			if (canPredecessorSucceed(eventGoal.getPlan()))
				succeedPredecessors++;
		
		if (hasUnachievedChild(eventGoal.getPlan()))
			if (canChildrenSucceed(eventGoal.getPlan()))
				succeedChildren++;

		return (double)(availableInput + improvePreconditions + succeedPredecessors + succeedChildren)/4.0;
	}
	
	// Min = 0.0 and Max = 1.0
	private double getAgencyWeight()              { return 1.0; }
	private double getAutonomyWeight()            { return 1.0; }
	private double getPredecessorRatioWeight()    { return 1.0; }
	private double getInputRatioWeight()          { return 1.0; }
	private double getGoalDifficultyWeight()      { return 1.0; }
	private double getAlternativeRecipeWeight()   { return 1.0; }
	private double getRecoveryProbabilityWeight() { return 1.0; }
}
