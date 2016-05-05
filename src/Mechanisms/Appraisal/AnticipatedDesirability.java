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

	public AnticipatedDesirability(MentalProcesses mentalProcesses) {
		this.collaboration = mentalProcesses.getCollaborationMechanism();
		this.desirability  = mentalProcesses.getDesirabilityProcess();
	}
	
	private ArrayList<Input> getUndefinedInputs(Goal eventGoal) {
		
		ArrayList<Input> undefinedInputs = new ArrayList<Input>();
		
		Plan plan = eventGoal.getPlan();
		
		for (Input input : plan.getType().getDeclaredInputs())
			if (!input.isDefinedSlot(plan.getGoal()))
				undefinedInputs.add(input);
		
		return undefinedInputs;
	}
	
	private boolean hasPreconditionFailed(Goal eventGoal) {
		
		Plan plan = eventGoal.getPlan();
		Precondition precondition = plan.getType().getPrecondition();
		
		if (!precondition.evalCondition(plan.getGoal()))
			return true;
		else
			return false;
	}
	
	private ArrayList<Plan> getUnachievedPredecessors(Goal eventGoal) {
		
		ArrayList<Plan> unachievedPredecessors = new ArrayList<Plan>();
		
		Plan plan = eventGoal.getPlan();
		
		for (Plan predecessor : plan.getPredecessors())
			if (!collaboration.getGoalStatus(plan).equals(GOAL_STATUS.ACHIEVED))
				unachievedPredecessors.add(predecessor);
		
		return unachievedPredecessors;
	}
	
	private ArrayList<Input> getAvailableInputs(ArrayList<Input> undefinedInputs) {
		
		return new ArrayList<Input>();
	}
	
	public boolean canSolveInputIssue(Goal eventGoal, ArrayList<Input> undefinedInputs) {
		
		ArrayList<Input> definedInputs = getAvailableInputs(undefinedInputs);
		
		if (!definedInputs.isEmpty())
			return true;
		else
			return false;
	}
	
	public boolean canPreconditionSucceed(Goal eventGoal) {
		
		return true;
	}
	
	public boolean canPredecessorsSucceed(Goal eventGoal, ArrayList<Plan> unachievedPredecessors) {
		
		return true;
	}

	public DESIRABILITY isAnticipatedEventDesirable(Goal eventGoal) {
		
		boolean availableInput 		 = false;
		boolean improvePreconditions = false;
		boolean succeedPredecessors  = false;
		
		ArrayList<Input> undefinedInputs 	   = getUndefinedInputs(eventGoal);
		ArrayList<Plan> unachievedPredecessors = getUnachievedPredecessors(eventGoal);
		
		if (!undefinedInputs.isEmpty())
			if (canSolveInputIssue(eventGoal, undefinedInputs))
				availableInput = true;
		
		if (hasPreconditionFailed(eventGoal))
			if (canPreconditionSucceed(eventGoal))
				improvePreconditions = true;
		
		if (!unachievedPredecessors.isEmpty())
			if (canPredecessorsSucceed(eventGoal, unachievedPredecessors))
				succeedPredecessors = true;
		
		if (availableInput || improvePreconditions || succeedPredecessors)
			return DESIRABILITY.DESIRABLE;
		else
			return desirability.isEventDesirable(eventGoal); 
	}
}
