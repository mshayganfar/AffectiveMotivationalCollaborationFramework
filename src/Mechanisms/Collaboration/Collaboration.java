package Mechanisms.Collaboration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Mechanisms.Mechanisms;
import MentalState.Goal;
import MentalState.MentalState;
import MetaInformation.GoalTree;
import MetaInformation.MentalProcesses;
import MetaInformation.Node;
import MetaInformation.Turns;
import MetaInformation.AMCAgent;
import MetaInformation.AMCUser;
import MetaInformation.AppraisalVector.WHOSE_APPRAISAL;
import edu.wpi.cetask.Plan;
import edu.wpi.cetask.TaskModel;
import edu.wpi.cetask.Plan.Status;
import edu.wpi.cetask.Task;
import edu.wpi.cetask.TaskClass.Input;
import edu.wpi.disco.Agent;
import edu.wpi.disco.Disco;
import edu.wpi.disco.Interaction;
import edu.wpi.disco.User;
import edu.wpi.disco.lang.Accept;
import edu.wpi.disco.lang.Propose;
import edu.wpi.disco.lang.Reject;

public class Collaboration extends Mechanisms{

	public enum INFERRED_CONTEXT{AGENT_PROPOSED, HUMAN_PROPOSED, AGENT_REJECTED, HUMAN_REJECTED, AGENT_ACCEPTED, HUMAN_ACCEPTED, 
		HUMAN_FAILED, AGENT_FAILED, HUMAN_ACHIEVED, AGENT_ACHIEVED, INPROGRESS};
	public enum GOAL_STATUS{ACHIEVED, FAILED, PENDING, BLOCKED, INPROGRESS, INAPPLICABLE, DONE, UNKNOWN};
	public enum FOCUS_TYPE{PRIMITIVE, NONPRIMITIVE};
	
	private Disco disco;
	private TaskModel taskModel;
	private Plan prevFocus;
	private Goal topLevelGoal = null;
	private MentalProcesses mentalProcesses;
	
	private boolean collaborationStatus = true;
	
	private ArrayList<AGENT> childrenResponsibinity;
	private List<Plan> predecessors = new ArrayList<Plan>();
	private List<Plan> contributingPlans = new ArrayList<Plan>();
	
	private Map<String, Boolean> preconditionsLOT = new HashMap<String, Boolean>();
	
	// These two maps have been used in Anticipated Desirability algorithm.
	private Map<String, Object> inputValues = new HashMap<String, Object>();
	private Map<String, Boolean> preconditionValues = new HashMap<String, Boolean>();
	
	private Interaction interaction;
	
	private AMCAgent agent;
	private AMCUser user;
	private Plan actualFocus;
	
	public Collaboration(String[] args) {
		
		agent = new AMCAgent("agent");
		agent.setMax(1);
		agent.init();
		
		user = new AMCUser("user");
		user.setEval(true); // Guarantees that grounding script will be evaluated.
		user.init();
		
		interaction = new Interaction(agent, user,
				  args.length > 0 && args[0].length() > 0 ? args[0] : null);
		interaction.getExternal().setEval(true);
//		interaction.start(false);
		disco = interaction.getDisco();
		
		disco.load("models/Events.xml");
		taskModel = disco.load("models/AstronautRobot.xml");
		
		prevFocus = disco.getFocus();
		
		childrenResponsibinity = new ArrayList<AGENT>();
		
		this.collaboration = this;
	}
	
	public AMCAgent getAgent() {
		return this.agent;
	}
	
	public AMCUser getUser() {
		return this.user;
	}
	
	public void prepareCollaborationMechanism(MentalProcesses mentalProcesses) {
		this.mentalProcesses = mentalProcesses;
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
	
	public void provideInputValues(Plan plan) {
		for (Input input : plan.getGoal().getType().getDeclaredInputs())
			if (collaboration.isInputAvailable(plan, input)) {
				System.out.println("IIIIIIIIIIIIIIIIIIIII: " + input.getName() + " , " + collaboration.getInputValue(plan, input.getName()));
				plan.getGoal().setSlotValue(input.getName(), collaboration.getInputValue(plan, input.getName()));
			}
	}
	
//	public Boolean isPlanAchieved(Plan plan) {
//		
////		if (!plan.isDone()) {
////			return null;
////		}
////		else {
////			if (plan.isSucceeded())
////				return true;
////			else
////				return false;
//
//		if (plan.getGoal().getSuccess() == null)
//			return null;
//		else if (plan.getGoal().getSuccess())
//			return true;
//		else
//			return false;
//	}
	
	public void setInputValue(String keyString, Object value) {
		inputValues.put(keyString, value);
	}
	
	private String getInputKeyValue(Plan plan, String inputName) {
		
		String keyString = "";
		
		while (!plan.getGoal().getType().equals(getDisco().getTop(plan).getType())) {
			keyString += plan.getParent().getDecomposition().getType().getId();
			keyString += plan.getParent().getDecomposition().findStep(plan);
			plan = plan.getParent();
		}
		return (keyString+inputName);
	}
	
	public String getApplicabilityKeyValue(Plan plan, String precondition) {
		
		String keyString = "";

		while (!plan.getGoal().getType().equals(getDisco().getTop(plan).getType())) {
//		while (plan.getParent() != null) {
			keyString += plan.getParent().getDecomposition().getType().getId();
			keyString += plan.getParent().getDecomposition().findStep(plan);
			plan = plan.getParent();
		}
		return (keyString+precondition);
	}
	
	public Object getInputValue(Plan plan, String inputName) {
		return inputValues.get(getInputKeyValue(plan, inputName));
	}
	
	public void setPreconditionValue(Plan plan, Boolean value) {
		preconditionValues.put(getApplicabilityKeyValue(plan, plan.getType().getPrecondition().getScript()), value);
	}
	
	public Boolean getPreconditionValue(Plan eventPlan) {
		return preconditionValues.get(getApplicabilityKeyValue(eventPlan, eventPlan.getType().getPrecondition().getScript()));
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
	
	public boolean isInputAvailable(Plan plan, Input input) {
		
		if(inputValues.get(getInputKeyValue(plan, input.getName())) == null)
//		if(!goal.getPlan().getGoal().isDefinedSlot(input.getName()))
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
		Plan plan     = disco.getFocus();
		String taskID = plan.getType()+"@"+Integer.toHexString(System.identityHashCode(plan));
		Goal goal     = new Goal(mentalProcesses); //MentalState.getInstance().getSpecificGoal(plan); // Change the agent type by reading the value from Disco.
		
		return goal;
	}
	
	public TaskModel getTaskModel() {
		return this.taskModel;
	}
	
	public Goal getTopLevelGoal() {
		
		if (MentalState.getInstance().getGoals().isEmpty())
			System.out.println("ERROR: No top level goal is available.");
		else {
			return MentalState.getInstance().getGoals().get(0); // First Goal is always the top level goal.
		}
		return null;
	}
	
	public GOAL_STATUS getGoalStatus(Plan plan) {
		
		Status status = plan.getStatus();
		
		Boolean preconditionApplicability = getPreconditionApplicability(plan);
		
		if (status.equals(Status.IN_PROGRESS))
			return GOAL_STATUS.INPROGRESS;
		else if (status.equals(Status.BLOCKED))
			return GOAL_STATUS.BLOCKED;
		else if (status.equals(Status.PENDING)) {
			if (plan.getGoal().isDefinedInputs())
				return GOAL_STATUS.PENDING;
			else
				return GOAL_STATUS.FAILED;
		}
		else if (status.equals(Status.FAILED)) // plan.isFailed()
			return GOAL_STATUS.FAILED;
		else if (status.equals(Status.INAPPLICABLE))
			return GOAL_STATUS.INAPPLICABLE;
		else if (preconditionApplicability != null)
			if (!preconditionApplicability)
				return GOAL_STATUS.INAPPLICABLE;
				
		if (status.equals(Status.DONE)) {
//			Boolean planAchievement = isPlanAchieved(plan);
			Boolean planAchievement = plan.getGoal().getSuccess();
			
			if (planAchievement == null)
				return GOAL_STATUS.UNKNOWN;
			else if (planAchievement)
				return GOAL_STATUS.ACHIEVED;
			else
				throw new IllegalStateException("Status: " + status.toString() + " , getSuccess()'s result: " + planAchievement);
//			if (!plan.isFailed())
//				return GOAL_STATUS.ACHIEVED;
//			else 
//				return GOAL_STATUS.FAILED;
		}
		else
			throw new IllegalStateException(status.toString());
	}
	
	public boolean doesContribute(Goal contributingGoal, Goal contributedGoal) {
		
		if (contributedGoal.getPlan().equals(contributingGoal.getPlan().getParent()))
			return true;
		else
			return false;
	}
	
	public List<Goal> getContributingGoals(Plan parentGoal) {
		
		String id;
		List<Goal> contributerGoalList = new ArrayList<Goal>();
		
		for (Plan childPlan : parentGoal.getChildren()) {
			id = childPlan.getType()+"@"+Integer.toHexString(System.identityHashCode(childPlan));
			contributerGoalList.add(new Goal(mentalProcesses)); // Change the agent type by reading the value from Disco.
		}
		
		return contributerGoalList;
	}
	
	public WHOSE_APPRAISAL getWhoseAppraisal(Plan plan) {
		
		switch (getResponsibleAgent(plan)) {
			case SELF:
			case BOTH:
				return WHOSE_APPRAISAL.SELF;
			case OTHER:
				return WHOSE_APPRAISAL.HUMAN;
			case UNKNOWN:
				return WHOSE_APPRAISAL.UNKNOWN;
			default:
				throw new IllegalArgumentException("Illegal Agent Type: " + getResponsibleAgent(plan));
		}
	}
	
	public AGENT getResponsibleAgent(Plan plan) {
		
		clearResponsibleAgents();
		extractResponsibleAgent(plan);
		return getOverallResponsibleAgent();
	}
	
	private void clearResponsibleAgents() {
		childrenResponsibinity.clear();
	}
	
	// NOTE: We assume all goals have postconditions.
	private void extractResponsibleAgent(Plan plan) {
		
		if(plan.getGoal().isPrimitive()) {
			if (plan.getGoal().getExternal() == null)
				childrenResponsibinity.add(AGENT.UNKNOWN);
			else if (plan.getGoal().getExternal() == false)
				childrenResponsibinity.add(AGENT.SELF);
			else
				childrenResponsibinity.add(AGENT.OTHER);
		}
		else {
			for (Plan childPlan : plan.getChildren())
				extractResponsibleAgent(childPlan);
		}
		return;
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
			throw new IllegalArgumentException("Illegal Agent Type!");
	}
	
//	public AGENT getResponsibleAgent(Plan plan) {
//		
//		if (plan.getGoal().getExternal() != null) {
//			if(!plan.getGoal().isPrimitive()) {
//				
//				int countResponsibles = 0;
//				
//				for (Plan childPlan : plan.getChildren()) {
//					if (childPlan.getGoal().getExternal())
//						countResponsibles++;
//					else if (childPlan.getGoal().getExternal() == false)
//						countResponsibles--;
//				}
//				
//				if (countResponsibles == plan.getChildren().size())
//					return AGENT.OTHER;
//				else if (Math.abs(countResponsibles) == plan.getChildren().size())
//					return AGENT.SELF;
//				else
//					return AGENT.BOTH;
//			}
//			else if (plan.getGoal().getExternal())
//				return AGENT.OTHER;
//			else
//				return AGENT.SELF;
//		}
//		else
//			return AGENT.BOTH;
//	}
	
	public List<Input> getInputs(Goal goal) {
		
		return goal.getPlan().getGoal().getType().getDeclaredInputs();
	}
	
//	public Goal recognizeGoal(/*Events event*/) {
//		return null; // This needs to be implemented.............................................
//	}
	
//	public RECIPE_APPLICABILITY getRecipeApplicability(Goal goal) { return RECIPE_APPLICABILITY.APPLICABLE; }
	
	private void clearPredecessors() {
		this.predecessors.clear();
	}
	
	public List<Plan> getPredecessors(Goal goal) {
		
		Plan plan = goal.getPlan();
		clearPredecessors();
		extractPredecessors(plan);
		return this.predecessors;
	}
	
	private void extractPredecessors(Plan plan) {
		
		for (Plan predecessor : plan.getPredecessors()) {
			if (predecessor.isPrimitive())
				this.predecessors.add(predecessor);
			else {
				for (Plan child : predecessor.getChildren())
					extractPredecessors(child);
			}
		}
		return;
	}
	
	public List<Plan> getContributingPlans(Goal goal) {
		
		Plan plan = goal.getParentPlan();
		clearContributingPlans();
		extractContributingPlans(plan);
		return this.contributingPlans;
	}
	
	private void clearContributingPlans() {
		this.contributingPlans.clear();
	}
	
	private void extractContributingPlans(Plan plan) {
		
		if (plan.isPrimitive()) {
			contributingPlans.add(plan);
		}
		else {
			for (Plan child : plan.getChildren())
				extractContributingPlans(child);
		}
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
	
	public GOAL_STATUS getPostConditionStatus(Plan plan) {
		
		return getGoalStatus(plan);
		
//		if (plan.getType().getPostcondition() != null)
//			return plan.getType().getPostcondition().evalCondition(plan.getGoal());
//		else
//			return null;
	}
	
	public Boolean getPreConditionStatus(Plan plan) {
		return plan.isApplicable();
	}
	
	public ArrayList<AGENT> getDescendentResponsibility() {
		return childrenResponsibinity;
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
		
//		Goal child   = new Goal(this.getDisco().getFocus());
//		Goal parent  = new Goal(this.getDisco().getFocus());
		
//		mentalState.assertGoal(1, "G1-1", null, AGENT.SELF, child, parent);
	}
	
	public Interaction getInteraction() {
		return this.interaction;
	}
	
	public ArrayList<Plan> getPathToTop(Plan plan) {
		
		ArrayList<Plan> pathToTop = new ArrayList<Plan>();
		
		while (!plan.equals(disco.getTop(plan))) {
//		while (plan.getParent() != null) {
			if (!MentalState.getInstance().isGoalInMentalState(plan))
				pathToTop.add(plan);
			plan = plan.getParent();
		}
		
		if (!MentalState.getInstance().isGoalInMentalState(disco.getTop(plan)))
			pathToTop.add(plan);
		
		Collections.reverse(pathToTop);
		
		return pathToTop;
	}
	
	public void setActualFocus(Plan plan) {
		this.actualFocus = plan;
	}
	
	public Plan getActualFocus() {
		
//		Plan actualPlan = this.disco.getFocus();
//		
//		for (Plan childPlan : actualPlan.getChildren()) {
//			// I was using this condition originally! It was preventing primitive goals to be recognized.
////			if (childPlan.getGoal().getType().equals(this.disco.getLastOccurrence().getType()))
//			if (childPlan.isLive() && childPlan.isPrimitive()) 
//				return childPlan;
//			else if (childPlan.getGoal().getExternal() != null) {
//				if (!childPlan.isLive() && childPlan.isPrimitive() && childPlan.getGoal().getExternal())
//					return childPlan;
//			}
//		}
		return this.actualFocus;
	}
	
//	private boolean isGoalApplicable(Goal eventGoal) {
//	
//		if (preconditionsLOT.get(eventGoal.getPlan().toString()) == null)
//			return false;
//		else if (preconditionsLOT.get(eventGoal.getPlan().toString()))
//			return true;
//		else
//			return false;
//	}
	
	public int getDistanceFromTop(Plan goalPlan) {
		
		int count = 1;
		
		while (!goalPlan.equals(disco.getTop(goalPlan))) { 
			goalPlan = goalPlan.getParent();
			count++;
		}
		
		return count;
	}
	
	public void updatePreconditionApplicability() {
		
		Plan plan;
		GoalTree goalTree = new GoalTree(mentalProcesses);
		ArrayList<Node> treeNodes = goalTree.createTree();
		
		for (Node node : treeNodes) {
			plan = node.getNodeGoalPlan();
			preconditionsLOT.put(getApplicabilityKeyValue(plan, plan.getType().getPrecondition().getScript()), plan.isApplicable());
		}
	}
	
	public void initializeAllInputs (Plan topPlan, Map<String, Object> inputValues) {
		
		List<Plan> livePlans = topPlan.getLiveDescendants();
		
		for (Plan plan : livePlans) {
			for (Input input : plan.getType().getDeclaredInputs()) {
				setInputValue(getInputKeyValue(plan, input.getName()), inputValues.get(input.getName()));
				plan.setSlotValue(input.getName(), inputValues.get(input.getName()));
				System.out.println("Goal: " + plan.getGoal().getType() + " , Input Name: " + input.getName() + " , Input Value: " + inputValues.get(input.getName()));
			}
		}
	}
	
	public int getTotalEdgeCount() {
		
		int nodeCounter = 0;
		
		GoalTree goalTree = new GoalTree(mentalProcesses);
		ArrayList<Node> treeNodes = goalTree.createTree();
		
		for (Node node : treeNodes)
			nodeCounter++;
		
		return (nodeCounter-1);
	}
	
	public Map<String, Boolean> getPreconditionApplicabilities() {
		return preconditionsLOT;
	}
	
	public Boolean getPreconditionApplicability(Plan plan) {
		
		return preconditionsLOT.get(getApplicabilityKeyValue(plan, plan.getType().getPrecondition().getScript()));
	}
	
	private Plan getLastContributingPlan(Plan plan) {
		
		if (plan.isPrimitive())
			return plan;
		
		return getLastContributingPlan(plan.getChildren().get(plan.getChildren().size()-1));
	}
	
	public double getAlternativeRecipeRatio(Goal eventGoal) {
		
		Plan plan = eventGoal.getPlan(); 
		
		double alternativePlansCount = plan.getDecompositions().size();
		double failedPlansCount      = plan.getFailed().size();
		
		return ((alternativePlansCount + failedPlansCount) != 0) ? (double)alternativePlansCount/(alternativePlansCount + failedPlansCount) : alternativePlansCount;
	}
	
	public INFERRED_CONTEXT getInferredContext(Goal eventGoal) {
		
		Plan eventPlan = eventGoal.getPlan();
		Task eventTask = eventPlan.getGoal();
		
		AGENT responsibleAgent = this.collaboration.getResponsibleAgent(eventPlan);
		GOAL_STATUS eventGoalStatus = this.collaboration.getGoalStatus(eventPlan);
		
		if ((eventTask instanceof Propose.Should) && (responsibleAgent.equals(AGENT.SELF)))
			return INFERRED_CONTEXT.AGENT_PROPOSED;
		else if ((eventTask instanceof Propose.Should) && (responsibleAgent.equals(AGENT.OTHER)))
			return INFERRED_CONTEXT.HUMAN_PROPOSED;
		else if ((eventTask instanceof Propose.Should) && (responsibleAgent.equals(AGENT.BOTH))) {
			if (this.collaboration.getResponsibleAgent(getLastContributingPlan(eventPlan)).equals(AGENT.SELF))
				return INFERRED_CONTEXT.AGENT_PROPOSED;
			else if (this.collaboration.getResponsibleAgent(getLastContributingPlan(eventPlan)).equals(AGENT.OTHER))
				return INFERRED_CONTEXT.HUMAN_PROPOSED;
			else
				throw new IllegalArgumentException("Responsible: " + getLastContributingPlan(eventPlan));
		}
		else if ((eventTask instanceof Reject) && (this.collaboration.getResponsibleAgent(eventPlan /*This should be changed!*/).equals(AGENT.SELF)))
//			((Reject)eventPlan.getGoal()).getProposal();
			return INFERRED_CONTEXT.AGENT_REJECTED;
		else if ((eventTask instanceof Reject) && (this.collaboration.getResponsibleAgent(eventPlan /*This should be changed!*/).equals(AGENT.OTHER)))
			return INFERRED_CONTEXT.HUMAN_REJECTED;
		else if ((eventTask instanceof Reject) && (this.collaboration.getResponsibleAgent(eventPlan /*This should be changed!*/)).equals(AGENT.BOTH)) {
			if (this.collaboration.getResponsibleAgent(getLastContributingPlan(eventPlan)).equals(AGENT.SELF))
				return INFERRED_CONTEXT.AGENT_REJECTED;
			else if (this.collaboration.getResponsibleAgent(getLastContributingPlan(eventPlan)).equals(AGENT.OTHER))
				return INFERRED_CONTEXT.HUMAN_REJECTED;
			else
				throw new IllegalArgumentException("Responsible: " + getLastContributingPlan(eventPlan));
		}
		else if ((eventTask instanceof Accept) && (this.collaboration.getResponsibleAgent(eventPlan /*This should be changed!*/).equals(AGENT.SELF)))
			return INFERRED_CONTEXT.AGENT_ACCEPTED;
		else if ((eventTask instanceof Accept) && (this.collaboration.getResponsibleAgent(eventPlan /*This should be changed!*/).equals(AGENT.OTHER)))
			return INFERRED_CONTEXT.HUMAN_ACCEPTED;
		else if ((eventTask instanceof Reject) && (this.collaboration.getResponsibleAgent(eventPlan /*This should be changed!*/)).equals(AGENT.BOTH)) {
			if (this.collaboration.getResponsibleAgent(getLastContributingPlan(eventPlan)).equals(AGENT.SELF))
				return INFERRED_CONTEXT.AGENT_ACCEPTED;
			else if (this.collaboration.getResponsibleAgent(getLastContributingPlan(eventPlan)).equals(AGENT.OTHER))
				return INFERRED_CONTEXT.HUMAN_ACCEPTED;
			else
				throw new IllegalArgumentException("Responsible: " + getLastContributingPlan(eventPlan));
		}
		else if ((eventGoalStatus.equals(GOAL_STATUS.FAILED)) && (responsibleAgent.equals(AGENT.OTHER)))
			return INFERRED_CONTEXT.HUMAN_FAILED;
		else if ((eventGoalStatus.equals(GOAL_STATUS.FAILED)) && (responsibleAgent.equals(AGENT.SELF)))
			return INFERRED_CONTEXT.AGENT_FAILED;
		else if ((eventGoalStatus.equals(GOAL_STATUS.FAILED)) && (responsibleAgent.equals(AGENT.BOTH))) {
			if (this.collaboration.getResponsibleAgent(getLastContributingPlan(eventPlan)).equals(AGENT.SELF))
				return INFERRED_CONTEXT.AGENT_FAILED;
			else if (this.collaboration.getResponsibleAgent(getLastContributingPlan(eventPlan)).equals(AGENT.OTHER))
				return INFERRED_CONTEXT.HUMAN_FAILED;
			else
				throw new IllegalArgumentException("Responsible: " + getLastContributingPlan(eventPlan));
		}
		else if ((eventGoalStatus.equals(GOAL_STATUS.ACHIEVED)) && (responsibleAgent.equals(AGENT.OTHER)))
			return INFERRED_CONTEXT.HUMAN_ACHIEVED;
		else if ((eventGoalStatus.equals(GOAL_STATUS.ACHIEVED)) && (responsibleAgent.equals(AGENT.SELF)))
			return INFERRED_CONTEXT.AGENT_ACHIEVED;
		else if ((eventGoalStatus.equals(GOAL_STATUS.ACHIEVED)) && (responsibleAgent.equals(AGENT.BOTH))) {
			if (this.collaboration.getResponsibleAgent(getLastContributingPlan(eventPlan)).equals(AGENT.SELF))
				return INFERRED_CONTEXT.AGENT_ACHIEVED;
			else if (this.collaboration.getResponsibleAgent(getLastContributingPlan(eventPlan)).equals(AGENT.OTHER))
				return INFERRED_CONTEXT.HUMAN_ACHIEVED;
			else
				throw new IllegalArgumentException("Responsible: " + getLastContributingPlan(eventPlan));
		}
		else
			return INFERRED_CONTEXT.INPROGRESS;
//			throw new IllegalArgumentException("Illegal Event Value: " + eventPlan);
	}
}
