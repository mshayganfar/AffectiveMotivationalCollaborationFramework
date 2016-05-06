package Mechanisms.Appraisal;

import java.util.ArrayList;

import Mechanisms.Mechanisms;
import Mechanisms.Appraisal.Desirability.DESIRABILITY;
import Mechanisms.Collaboration.Collaboration.GOAL_STATUS;
import MentalState.Goal;
import MetaInformation.MentalProcesses;
import edu.wpi.cetask.Plan;
import edu.wpi.cetask.Task;
import edu.wpi.cetask.TaskClass;
import edu.wpi.cetask.TaskClass.Input;
import edu.wpi.cetask.TaskClass.Precondition;

public class AnticipatedDesirability extends Mechanisms{

//	ArrayList<Plan> unachievedChildren = new ArrayList<Plan>();
	
	public AnticipatedDesirability(MentalProcesses mentalProcesses) {
		this.collaboration = mentalProcesses.getCollaborationMechanism();
		this.desirability  = mentalProcesses.getDesirabilityProcess();
	}
	
//	private ArrayList<Input> getUndefinedInputs(Plan eventPlan) {
//		
//		ArrayList<Input> undefinedInputs = new ArrayList<Input>();
//		
//		for (Input input : eventPlan.getType().getDeclaredInputs())
//			if (!input.isDefinedSlot(eventPlan.getGoal()))
//				undefinedInputs.add(input);
//		
//		return undefinedInputs;
//	}
	
	private boolean hasPreconditionFailed(Plan eventPlan) {
		
//		if (!eventPlan.getType().getPrecondition().evalCondition(eventPlan.getGoal()))
		if (!eventPlan.getGoal().isApplicable())
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
	
//	private ArrayList<Plan> getUnachievedPredecessors(Plan eventPlan) {
//		
//		ArrayList<Plan> unachievedPredecessors = new ArrayList<Plan>();
//		
//		for (Plan predecessor : eventPlan.getPredecessors())
//			if (!collaboration.getGoalStatus(predecessor).equals(GOAL_STATUS.ACHIEVED))
//				unachievedPredecessors.add(predecessor);
//		
//		return unachievedPredecessors;
//	}
	
//	private ArrayList<Input> getAvailableInputs(Plan eventPlan, ArrayList<Input> undefinedInputs) {
//		
//		ArrayList<Input> availableInputs = new ArrayList<Input>();
//		
//		for (Input input : undefinedInputs)
//			if (isInputAvailable(eventPlan, input))
//				availableInputs.add(input);
//		
//		return availableInputs;
//	}
	
//	private boolean isInputAvailable(Plan eventPlan, Input input) {
//		
//		return (collaboration.getInputValue(input) != null) ? true : false;
//	}
	
//	public boolean canPreconditionSucceed(Plan eventPlan) {
//		
//		return (collaboration.getPreconditionValue(eventPlan.getType().getPrecondition()) != null) ? true : false;
//	}
	
//	public boolean canProvideInput(Plan eventPlan) {
//		
//		ArrayList<Input> undefinedInputs = getUndefinedInputs(eventPlan);
//		ArrayList<Input> definedInputs   = getAvailableInputs(eventPlan, undefinedInputs);
//		
//		if (!definedInputs.isEmpty())
//			return true;
//		else
//			return false;
//	}
	
//	private void getUnachievedChildren(Plan plan) {
//		
//		if (plan.isPrimitive()) {
//			if (!collaboration.getGoalStatus(plan).equals(GOAL_STATUS.ACHIEVED))
//				unachievedChildren.add(plan);
//		}
//		else {
//			for (Plan child : plan.getChildren())
//				 getUnachievedChildren(child);
//		}
//	}
	
//	public boolean canChildrenSucceed(Plan plan) {
//		
//		unachievedChildren.clear();
//		getUnachievedChildren(plan);
//		
//		for (Plan child : unachievedChildren) {
//			if (!(canProvideInput(child) && canPreconditionSucceed(child) && canPredecessorSucceed(child)))
//				return false;
//		}
//		
//		return true;
//	}
	
//	public boolean canPredecessorSucceed(Plan plan) {
//		
//		ArrayList<Plan> unachievedPredecessors = getUnachievedPredecessors(plan);
//		
//		if(!unachievedPredecessors.isEmpty()) {
//			for (Plan predecessor : unachievedPredecessors) {
//				if (predecessor.isPrimitive()) {
//					if (!(canProvideInput(predecessor) && canPreconditionSucceed(predecessor) && canPredecessorSucceed(predecessor)))
//						return false;
//				}
//				else {
//					for (Plan child : predecessor.getChildren())
//						if (!canPredecessorSucceed(child))
//							return false;
//				}
//			}
//		}
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
//		return true;
//	}

	public DESIRABILITY isAnticipatedEventDesirable(Goal eventGoal) {
		
		boolean availableInput 		 = false;
		boolean improvePreconditions = false;
		boolean succeedPredecessors  = false;
		boolean succeedChildren      = false;
		
//		ArrayList<Input> undefinedInputs 	   = getUndefinedInputs(eventGoal.getPlan());
//		ArrayList<Plan> unachievedPredecessors = getUnachievedPredecessors(eventGoal.getPlan());
//		
//		if (!undefinedInputs.isEmpty())
//			if (canProvideInput(eventGoal.getPlan()))
//				availableInput = true;
//		
//		if (hasPreconditionFailed(eventGoal.getPlan()))
//			if (canPreconditionSucceed(eventGoal.getPlan()))
//				improvePreconditions = true;
//		
//		if (!unachievedPredecessors.isEmpty())
//			if (canPredecessorSucceed(eventGoal.getPlan()))
//				succeedPredecessors = true;
//		
//		if (hasUnachievedChild(eventGoal.getPlan()))
//			if (canChildrenSucceed(eventGoal.getPlan()))
//				succeedChildren = true;
//		
		if (availableInput || improvePreconditions || succeedPredecessors || succeedChildren)
			return DESIRABILITY.DESIRABLE;
		else
			return desirability.isEventDesirable(eventGoal); 
	}
}
