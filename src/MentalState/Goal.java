package MentalState;

import java.util.ArrayList;

import Mechanisms.Collaboration.Collaboration.GOAL_STATUS;
import MetaInformation.Turns;
import edu.wpi.cetask.Plan;

public class Goal {
	
	public enum DIFFICULTY {NORMAL, DIFFICULT, MOST_DIFFICULT};
	
	private ArrayList<Belief> Beliefs       = new ArrayList<Belief>();
	private ArrayList<Intention> Intentions = new ArrayList<Intention>();
	private ArrayList<Motive> Motives       = new ArrayList<Motive>();
	
	private Plan plan;
	private Plan parentPlan;
	private String label;
	private int currentTurn;
	private double effort;
	private GOAL_STATUS goalStatus;
//	private Motive activeMotive = null;
	
	public Goal (Plan plan) {
		this.plan        = plan;
		this.parentPlan  = plan.getParent();
		this.label       = plan.getGoal().getType().toString();
		this.currentTurn = Turns.getInstance().getTurnNumber();
		this.goalStatus  = GOAL_STATUS.UNKNOWN;
		this.effort      = 1.0;
	}
	
	public Goal (Plan plan, GOAL_STATUS goalStatus) {
		this.plan        = plan;
		this.parentPlan  = plan.getParent();
		this.label       = plan.getGoal().getType().toString();
		this.currentTurn = Turns.getInstance().getTurnNumber();
		this.goalStatus  = goalStatus;
		this.effort      = 1.0;
	}
	
	public void setGoalStatus (GOAL_STATUS goalStatus) {
		this.goalStatus = goalStatus;
	}
	
	public Plan getPlan() {
		return this.plan;
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
}
