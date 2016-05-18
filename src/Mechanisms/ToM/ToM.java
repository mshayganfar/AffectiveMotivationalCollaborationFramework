package Mechanisms.ToM;

import java.util.ArrayList;
import java.util.List;

import Mechanisms.Mechanisms;
import Mechanisms.Appraisal.Controllability;
import Mechanisms.Appraisal.Desirability;
import Mechanisms.Appraisal.Expectedness;
import Mechanisms.Appraisal.Relevance;
import Mechanisms.Appraisal.Controllability.CONTROLLABILITY;
import Mechanisms.Appraisal.Coping;
import Mechanisms.Appraisal.Desirability.DESIRABILITY;
import Mechanisms.Appraisal.Expectedness.EXPECTEDNESS;
import Mechanisms.Appraisal.Relevance.RELEVANCE;
import Mechanisms.Collaboration.Collaboration;
import Mechanisms.Collaboration.Collaboration.GOAL_STATUS;
import Mechanisms.Mechanisms.AGENT;
import Mechanisms.Motivation.Motivation;
import MentalState.Goal;
import MentalState.Goal.DIFFICULTY;
import MentalState.Motive.MOTIVE_TYPE;
import MetaInformation.AppraisalVector;
import MetaInformation.GoalTree;
import MetaInformation.AppraisalVector.EMOTION_INSTANCE;
import edu.wpi.cetask.Plan;
import edu.wpi.cetask.TaskClass.Input;
import edu.wpi.disco.lang.Accept;
import edu.wpi.disco.lang.Ask;
import edu.wpi.disco.lang.Propose;
import edu.wpi.disco.lang.Reject;
import MetaInformation.MentalProcesses;
import MetaInformation.Node;

public class ToM extends Mechanisms{
	
	private MentalProcesses mentalProcesses;
	
	private Collaboration collaboration;
	private Motivation motivation;
	private Coping coping;
	
	private Relevance relevance;
	private Controllability controllability;
	private Desirability desirability;
	private Expectedness expectedness;
	
	private double valence = 0.0;
	
	private int achievedPredecessorCount = 0;
	private int totalPredecessorCount    = 0;
	
	private ArrayList<Plan> unachievedChildren = new ArrayList<Plan>();
	private ArrayList<Goal> descendentGoals = new ArrayList<Goal>();
	
	public void prepareAppraisalsOfToM(MentalProcesses mentalProcesses) {
		this.collaboration = mentalProcesses.getCollaborationMechanism();
		this.motivation    = mentalProcesses.getMotivationMechanism();
		this.coping		   = mentalProcesses.getCopingMechanism();
		
		this.relevance       = mentalProcesses.getRelevanceProcess();
		this.controllability = mentalProcesses.getControllabilityProcess();
		this.desirability    = mentalProcesses.getDesirabilityProcess();
		this.expectedness    = mentalProcesses.getExpectednessProcess();
	}
	
	public AppraisalVector getReverseAppraisalValues(Goal eventGoal) {
		
		return doReverseAppraisal(getValenceValue(), eventGoal);
	}
	
	public EMOTION_INSTANCE getAnticipatedHumanEmotion(Goal eventGoal) {
		
		AppraisalVector reverseAppraisalVector = doReverseAppraisal(getValenceValue(), eventGoal);
		
		return reverseAppraisalVector.getEmotionInstance();
	}
	
	private AppraisalVector doReverseAppraisal(double valenceValue, Goal eventGoal) {
		
		AppraisalVector estimatedAppraisalVector = new AppraisalVector(mentalProcesses, eventGoal);
		
		estimatedAppraisalVector.setRelevanceValue(relevance.isEventRelevant(eventGoal));
		estimatedAppraisalVector.setDesirabilityValue(getReverseDesirability(valenceValue));
		estimatedAppraisalVector.setExpectednessValue(expectedness.isEventExpected(eventGoal));
		estimatedAppraisalVector.setControllabilityValue(getReverseControllability(eventGoal));
		
		return estimatedAppraisalVector;
	}
	
	public CONTROLLABILITY getAlternativeReverseControllability(Goal eventGoal) {
		
		Plan plan = eventGoal.getPlan();
		
		if ((eventGoal.getPlan().getGoal() instanceof Accept) || 
				(mentalProcesses.getCollaborationMechanism().getGoalStatus(plan).equals(GOAL_STATUS.ACHIEVED)) || 
				(mentalProcesses.getCollaborationMechanism().getGoalStatus(plan).equals(GOAL_STATUS.PENDING)) ||
				(mentalProcesses.getCollaborationMechanism().getGoalStatus(plan).equals(GOAL_STATUS.INPROGRESS)))
			return CONTROLLABILITY.HIGH_CONTROLLABLE;
		else if ((plan.getGoal() instanceof Ask.What) || 
				 (plan.getGoal() instanceof Ask.How) ||
				 (plan.getGoal() instanceof Ask.Who))
			return CONTROLLABILITY.LOW_CONTROLLABLE;
		else if ((plan.getGoal() instanceof Reject) ||
				(mentalProcesses.getCollaborationMechanism().getGoalStatus(plan).equals(GOAL_STATUS.FAILED)) ||
				(mentalProcesses.getCollaborationMechanism().getGoalStatus(plan).equals(GOAL_STATUS.BLOCKED)) ||
				(mentalProcesses.getCollaborationMechanism().getGoalStatus(plan).equals(GOAL_STATUS.INAPPLICABLE)))
			return CONTROLLABILITY.UNCONTROLLABLE;
		else
			throw new IllegalStateException("Illegal Reverse Controllability State for Goal:" + plan.getType());
	}
	
	private CONTROLLABILITY getReverseControllability(Goal eventGoal) {
		
		double controllabilityValue = 0.0;
		
		if ((collaboration.getGoalStatus(eventGoal.getPlan()).equals(GOAL_STATUS.BLOCKED)) ||
				(collaboration.getGoalStatus(eventGoal.getPlan()).equals(GOAL_STATUS.FAILED)) ||
				(collaboration.getGoalStatus(eventGoal.getPlan()).equals(GOAL_STATUS.INAPPLICABLE))) {
			
			double dblAlternativeRecipeRatio = collaboration.getAlternativeRecipeRatio(eventGoal);
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
			double dblOverallGoalDifficultyValue = eventGoal.getGoalEffort();
			
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
		
		if (controllabilityValue >= 0)
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
	
	private Double getAvailableInputRatio(Goal eventGoal) {
		
		double dblAvailableInputCounter = 0.0;
		
		if (collaboration.getInputs(eventGoal).size() == 0)
			return null;
		
		for (Input input : collaboration.getInputs(eventGoal)) {
			if (input != null)
				if(isInputAvailable(eventGoal.getPlan(), input))
					dblAvailableInputCounter++;
		}
		return ((double)dblAvailableInputCounter/((collaboration.getInputs(eventGoal).size() == 0) ? 1.0 : collaboration.getInputs(eventGoal).size()));
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
	}
	
	private double getAutonomyValue(Goal eventGoal) {

		double countSelfResponsible = 0.0;
		
		Plan plan = eventGoal.getPlan();
		
		if (plan.isPrimitive()) {
			if (collaboration.getResponsibleAgent(plan).equals(AGENT.OTHER))
				return 1.0;
			else if (collaboration.getResponsibleAgent(plan).equals(AGENT.SELF))
				return -1.0;
			else  if (collaboration.getResponsibleAgent(plan).equals(AGENT.UNKNOWN))
				return -0.5;
			else 
				throw new IllegalArgumentException("Illegal Agent Type: " + collaboration.getResponsibleAgent(plan));
		}
		else {
			collaboration.getResponsibleAgent(plan);
			
			for (AGENT agent : collaboration.getDescendentResponsibility()) {
				if (agent.equals(AGENT.OTHER))
					countSelfResponsible++;
				else if (agent.equals(AGENT.BOTH))
					countSelfResponsible += 0.5;
				else if (agent.equals(AGENT.SELF))
					countSelfResponsible -= 1.0;
				else if (agent.equals(AGENT.UNKNOWN))
					countSelfResponsible -= 0.5;
			}
			return ((double)countSelfResponsible/((collaboration.getDescendentResponsibility().size() == 0) ? 1.0 : collaboration.getDescendentResponsibility().size()));
		}
	}
	
	private double getAgencyValue(Goal eventGoal) {
		
		if (eventGoal.getPlan().isPrimitive()) {
			if (!(eventGoal.getPlan().getGoal() instanceof Propose.Should)) // Later, I might need to check whether the Robot proposed this goal!
				return 1.0;
			else
				return 0.0;
		}
		else {
			return 0.5;
		}
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
		
		if (!hasPreconditionFailed(eventGoal.getPlan()))
			// Here, the Robot does not know about human's private beliefs about preconditions.
			improvePreconditions++;
		
		if (!unachievedPredecessors.isEmpty())
			if (canPredecessorSucceed(eventGoal.getPlan()))
				succeedPredecessors++;
		
		if (hasUnachievedChild(eventGoal.getPlan()))
			if (canChildrenSucceed(eventGoal.getPlan()))
				succeedChildren++;

		return (double)(availableInput + improvePreconditions + succeedPredecessors + succeedChildren)/4.0;
	}

	private boolean hasUnachievedChild(Plan eventPlan) {
		
		for (Plan child : eventPlan.getChildren())
			if (!collaboration.getGoalStatus(child).equals(GOAL_STATUS.ACHIEVED))
				return true;

		return false;
	}
	
	public boolean canChildrenSucceed(Plan plan) {
		
		List<Plan> unachievedDescendents = getUnachievedChildren(plan);
		
		for (Plan child : unachievedDescendents)
			if (!(canProvideInput(child) && !hasPreconditionFailed(child) && canPredecessorSucceed(child)))
				return false;
		
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
	
	public boolean canPredecessorSucceed(Plan plan) {
		
		ArrayList<Plan> unachievedPredecessors = getUnachievedPredecessors(plan);
		
		for (Plan predecessor : unachievedPredecessors) {
			if (predecessor.isPrimitive()) {
				if (!(canProvideInput(predecessor) && !hasPreconditionFailed(predecessor) && canPredecessorSucceed(predecessor)))
					return false;
			}
			else {
				for (Plan child : predecessor.getChildren())
					if (!canPredecessorSucceed(child))
						return false;
			}
		}

		return true;
	}

	private boolean hasPreconditionFailed(Plan eventPlan) {
		
		if (eventPlan.getGoal().isApplicable() == null)
			return true;
		else if (!eventPlan.getGoal().isApplicable())
			return true;
		else
			return false;
	}
	
	public boolean canProvideInput(Plan eventPlan) {
		
		ArrayList<Input> undefinedInputs = getUndefinedInputs(eventPlan);
		ArrayList<Input> definedInputs   = getAvailableInputs(eventPlan, undefinedInputs);
		
		if (!definedInputs.isEmpty())
			return true;
		else
			return false;
	}
	
	private ArrayList<Input> getAvailableInputs(Plan eventPlan, ArrayList<Input> undefinedInputs) {
		
		ArrayList<Input> availableInputs = new ArrayList<Input>();
		
		for (Input input : undefinedInputs)
			// Here, I only check shared beliefs about available input.
			if (isInputAvailable(eventPlan, input))
				availableInputs.add(input);
		
		return availableInputs;
	}
	
	private boolean isInputAvailable(Plan eventPlan, Input input) {
		
		return (eventPlan.getGoal().isDefinedSlot(input.getName())) ? true : false;
	}
	
	private ArrayList<Input> getUndefinedInputs(Plan eventPlan) {
		
		ArrayList<Input> undefinedInputs = new ArrayList<Input>();
		
		for (Input input : eventPlan.getType().getDeclaredInputs())
			if (!input.isDefinedSlot(eventPlan.getGoal()))
				undefinedInputs.add(input);
		
		return undefinedInputs;
	}
	
	private ArrayList<Plan> getUnachievedPredecessors(Plan eventPlan) {
		
		ArrayList<Plan> unachievedPredecessors = new ArrayList<Plan>();
		
		for (Plan predecessor : eventPlan.getPredecessors())
			if (!collaboration.getGoalStatus(predecessor).equals(GOAL_STATUS.ACHIEVED))
				unachievedPredecessors.add(predecessor);
		
		return unachievedPredecessors;
	}
	
	public DESIRABILITY getReverseDesirability(double valence) {
		
		if ((valence > 0.1) && (valence < 0.45))
			return DESIRABILITY.DESIRABLE;
		else if ((valence >= 0.45) && (valence <= 1.0))
			return DESIRABILITY.HIGH_DESIRABLE;
		else if ((valence > -0.45) && (valence < -0.1))
			return DESIRABILITY.UNDESIRABLE;
		else if ((valence >= -1.0) && (valence <= -0.45))
			return DESIRABILITY.HIGH_UNDESIRABLE;
		else if ((valence >= -0.1) && (valence <= 0.1))
			return DESIRABILITY.NEUTRAL;
		else
			throw new IllegalArgumentException("Valence Value: " + valence);
	}
	
	public double getValenceValue() {
		return valence;
	}
	
	public RELEVANCE isEventRelevant(Goal eventGoal) {
		return relevance.isEventRelevant(eventGoal);
	}
	
	public CONTROLLABILITY isEventControllable(Goal eventGoal) {
		return controllability.isEventControllable(eventGoal);
	}
	
	public DESIRABILITY isEventDesirable(Goal eventGoal) {
		return desirability.isEventDesirable(eventGoal);
	}
	
	public EXPECTEDNESS isEventExpected(Goal eventGoal) {
		return expectedness.isEventExpected(eventGoal);
	}
	
	private double getAgencyWeight()              { return 1.0; }
	private double getAutonomyWeight()            { return 1.0; }
	private double getPredecessorRatioWeight()    { return 1.0; }
	private double getInputRatioWeight()          { return 1.0; }
	private double getGoalDifficultyWeight()      { return 1.0; }
	private double getAlternativeRecipeWeight()   { return 1.0; }
	private double getRecoveryProbabilityWeight() { return 1.0; }
}