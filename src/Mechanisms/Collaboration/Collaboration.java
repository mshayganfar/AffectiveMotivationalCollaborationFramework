package Mechanisms.Collaboration;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import Mechanisms.Mechanisms;
import Mechanisms.Collaboration.Collaboration.GOAL_STATUS;
import Mechanisms.Mechanisms.AGENT;
import MentalState.Goal;
import MetaInformation.Turns;
import edu.wpi.cetask.Plan;
import edu.wpi.cetask.TaskModel;
import edu.wpi.cetask.Plan.Status;
import edu.wpi.cetask.TaskClass.Input;
import edu.wpi.disco.Agent;
import edu.wpi.disco.Disco;
import edu.wpi.disco.Interaction;
import edu.wpi.disco.User;

public class Collaboration extends Mechanisms{

	public enum GOAL_STATUS{ACHIEVED, FAILED, PENDING, BLOCKED, INPROGRESS, INAPPLICABLE};
	public enum FOCUS_TYPE{PRIMITIVE, NONPRIMITIVE};
	public enum RECIPE_APPLICABILITY{APPLICABLE, INAPPLICABLE, UNKNOWN};
	
	private Disco disco;
	private TaskModel taskModel;
	private Plan prevFocus;
	
	private boolean collaborationStatus = true;
	
	private ArrayList<AGENT> childrenResponsibinity;
	
	private Interaction interaction;
	
	public Collaboration(String[] args) {
		
		Agent agent = new Agent("agent");
		agent.setMax(1);
		
		interaction = new Interaction(agent, new User("user"),
				  args.length > 0 && args[0].length() > 0 ? args[0] : null);
		interaction.getExternal().setEval(true);
		interaction.start(true);
		disco = interaction.getDisco();
		
		taskModel = disco.load("/TaskModel/ABC.xml");
		disco.load("/TaskModel/Events.xml");
		
		prevFocus = disco.getFocus();
		
		childrenResponsibinity = new ArrayList<AGENT>();
		
		this.collaboration = this;
	}
	
	public Turns getCurrentTurn() {
		return this.currentTurn;
	}
	
	public void updateTurn(Turns turn) {
		this.currentTurn = turn;
	}
	
	public Collaboration(String strAgent, String strUser) {
		disco = new Interaction(new Agent(strAgent),new User(strUser),null).getDisco();
		System.out.println("Collaboration started! Disco = " + disco);
	}
	
	public boolean isPlanAchieved(Plan plan) {
		
		//if (getGoalStatus(plan).equals(GOAL_STATUS.ACHIEVED))
		if (plan.isSucceeded())
			return true;
		else
			return false;
	}
	
	public boolean isGoalAchieved(Goal goal) {
		
		if (getGoalStatus(goal.getPlan()).equals(GOAL_STATUS.ACHIEVED))
			return true;
		else
			return false;
	}
	
	public boolean isGoalFocused(Goal goal) {
		
		if (getDisco().getFocus().equals(goal.getPlan()))
			return true;
		else
			return false;
	}
	
	public boolean isInputAvailable(Goal goal, Input input) {
		
		if(!goal.getPlan().getGoal().isDefinedSlot(input.getName()))
			return false;
		
		return true;
	}
	
	public boolean isGoalLive(Goal goal) {
		
		if (goal.getPlan().isLive())
			return true;
		else
			return false;
	}

	public Disco getDisco() { return disco; }
	
	public FOCUS_TYPE getGoalType(Goal goal) {

		if(goal.getPlan().getGoal().isPrimitive())
			return FOCUS_TYPE.PRIMITIVE;
		else
			return FOCUS_TYPE.NONPRIMITIVE;
	}
	
	public Goal getFocusedGoal() {
		Plan plan       = disco.getFocus();
		String taskID   = plan.getType()+"@"+Integer.toHexString(System.identityHashCode(plan));
		
		Goal goal = new Goal(plan); // Change the agent type by reading the value from Disco.
		
		return goal;
	}
	
	public TaskModel getTaskModel() {
		return this.taskModel;
	}
	
	public Goal getTopLevelGoal() {
		
		Plan plan = disco.getTop(disco.getFocus());
		String taskID = plan.getType()+"@"+Integer.toHexString(System.identityHashCode(plan));
		
		Goal goal = new Goal(plan); // Change the agent type by reading the value from Disco.
		// Also change the place where needs to hold the goal. To be globally accessible.
		return goal;
	}
	
	public GOAL_STATUS getGoalStatus(Plan plan) {
		
		Status postCondStatus = plan.getStatus();
		
		if (isPlanAchieved(plan))
			return GOAL_STATUS.ACHIEVED;
		else if (postCondStatus.equals(Status.FAILED))
			return GOAL_STATUS.FAILED;
		else if (postCondStatus.equals(Status.IN_PROGRESS))
			return GOAL_STATUS.INPROGRESS;
		else if (postCondStatus.equals(Status.BLOCKED))
			return GOAL_STATUS.BLOCKED;
		else if (postCondStatus.equals(Status.PENDING))
			return GOAL_STATUS.PENDING;
		else
			return GOAL_STATUS.INAPPLICABLE;
	}
	
	public boolean doesContribute(Goal contributingGoal, Goal contributedGoal) {
		
		if (contributedGoal.getPlan().equals(contributingGoal.getPlan().getParent()))
			return true;
		else
			return false;
	}
	
	public List<Goal> getContributingGoals(Plan parentGoal) {
		
		Plan goal;
		String id;
		
		List<Goal> contributerGoalList = new ArrayList<Goal>();
		List<Plan> contributerPlanList = parentGoal.getChildren();
		
		for (int i=0 ; i < contributerPlanList.size() ; i++) {
			goal = contributerPlanList.get(i);
			id = goal.getType()+"@"+Integer.toHexString(System.identityHashCode(goal));
			contributerGoalList.add(new Goal(goal)); // Change the agent type by reading the value from Disco.
		}
		
		return contributerGoalList;
	}
	
	// NOTE: We assume all goals have postconditions.
	public AGENT getResponsibleAgent(Goal goal) {
		
		if(goal.getPlan().getGoal().isPrimitive()) {
			if (goal.getPlan().getGoal().getExternal() == null)
				return AGENT.UNKNOWN;
			else if (goal.getPlan().getGoal().getExternal() == false)
				return AGENT.SELF;
			else
				return AGENT.OTHER;
		}
		else {
			for (Plan childPlan : goal.getPlan().getChildren()) {
				if (childPlan.getGoal().isPrimitive()) {
					childrenResponsibinity.add(getResponsibleAgent(new Goal(childPlan)));
				}
				else {
					getResponsibleAgent(new Goal(childPlan));
				}
			}
			
			return getOverallResponsibleAgent();
		}
	}

	private AGENT getOverallResponsibleAgent() {
		
		int agentCount    = 0;
		int otherCount    = 0;
		int unknownCount  = 0;
		
		for (AGENT agent : childrenResponsibinity) {
			if (agent.equals(AGENT.SELF))
				agentCount++;
			else if (agent.equals(AGENT.OTHER))
				otherCount++;
			else if (agent.equals(AGENT.UNKNOWN))
				unknownCount++;
		}
		
		if (((agentCount == 0) || (otherCount == 0)) && (unknownCount != 0))
			return AGENT.UNKNOWN;
		else if ((agentCount != 0) && (otherCount != 0))
			return AGENT.BOTH;
		else if ((agentCount != 0) && (otherCount == 0) && (unknownCount == 0))
			return AGENT.SELF;
		else if ((agentCount == 0) && (otherCount != 0) && (unknownCount == 0))
			return AGENT.OTHER;
		else
			return null;
	}
	
	public AGENT getResponsibleAgent(Plan plan) {
		
		if (plan.getGoal().getExternal() != null) {
			if(!plan.getGoal().isPrimitive()) {
				
				int countResponsibles = 0;
				
				for (Plan childPlan : plan.getChildren()) {
					if (childPlan.getGoal().getExternal())
						countResponsibles++;
					else if (childPlan.getGoal().getExternal() == false)
						countResponsibles--;
				}
				
				if (countResponsibles == plan.getChildren().size())
					return AGENT.OTHER;
				else if (Math.abs(countResponsibles) == plan.getChildren().size())
					return AGENT.SELF;
				else
					return AGENT.BOTH;
			}
			else if (plan.getGoal().getExternal())
				return AGENT.OTHER;
			else
				return AGENT.SELF;
		}
		else
			return AGENT.BOTH;
	}
	
	public List<Input> getInputs(Goal goal) {
		
		return goal.getPlan().getGoal().getType().getInputs();
	}
	
	public Goal recognizeGoal(/*Events event*/) {
		return null; // This needs to be implemented.............................................
	}
	
	public RECIPE_APPLICABILITY getRecipeApplicability(Goal goal) { return RECIPE_APPLICABILITY.APPLICABLE; }
	
	public List<Plan> getPredecessors(Goal goal) {
		
		return goal.getPlan().getPredecessors();
	}
	
	public List<Plan> getContributingPlans(Goal goal) {
		
		return goal.getPlan().getChildren();
	}
	
	public void updatePreviousFocus() {
		prevFocus = disco.getFocus();
	}
	
	public Plan getPreviousFocus() {
		return this.prevFocus;
	}
	
	private boolean getGoalOverallStatus(Plan goal) {
		
		GOAL_STATUS goalStatus = collaboration.getGoalStatus(goal);
		
		if (goalStatus.equals(GOAL_STATUS.ACHIEVED) || 
			goalStatus.equals(GOAL_STATUS.PENDING) || 
			goalStatus.equals(GOAL_STATUS.INPROGRESS))
			return true;
		else // FAILED, BLOCKED, INAPPLICABLE 
			return false;
	}
	
	public void updateCollaboraitonStatus() {
		collaborationStatus = getGoalOverallStatus(prevFocus);
	}
	
	public Boolean getPostConditionStatus(Plan plan) {
		
		if (plan.getType().getPostcondition() != null)
			return plan.getType().getPostcondition().evalCondition(plan.getGoal());
		else
			return null;
	}
	
	public Boolean getPreConditionStatus(Plan plan) {
		return plan.isApplicable();
	}
	
	public ArrayList<AGENT> getChildrenResponsibility() {
		return childrenResponsibinity;
	}
	
	public void clearChildrenResponsibility() {
		childrenResponsibinity.clear();
	}
	
	public void test() {
		
//		Fact childFact;
//		try {
//			//childFact = new Fact("My Fake Child Goal", JessEngine);
//			//Fact parentFact  = new Fact("My Fake Parent Goal", JessEngine);
//			
//			//Events event = new Events(new Fact("My Fake Belief", JessEngine), childFact, EVENT_TYPE.UTTERANCE);
//			
//		} catch (JessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println(this.getDisco().getFocus());
		System.out.println(this.getDisco().getFocus().getGoal());
		System.out.println(this.getDisco().getFocus().getParent().getGoal());
		
		Goal child   = new Goal(this.getDisco().getFocus());
		Goal parent  = new Goal(this.getDisco().getFocus());
		
//		mentalState.assertGoal(1, "G1-1", null, AGENT.SELF, child, parent);
	}
	
	public Interaction getInteraction() {
		return this.interaction;
	}
	
	public Plan getActualFocus(Plan discoFocus) {
		
		Plan actualPlan = this.disco.getFocus();
		
		for (Plan childPlan : actualPlan.getChildren()) {
			if (childPlan.getGoal().getType().equals(this.disco.getLastOccurrence().getType()))
				return childPlan;
		}
		
		return actualPlan;
	}
}
