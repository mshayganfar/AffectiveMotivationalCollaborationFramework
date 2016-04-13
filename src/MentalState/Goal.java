package MentalState;

import java.util.ArrayList;

import MetaInformation.Turns;
import edu.wpi.cetask.Plan;

public class Goal {
	
	private ArrayList<Belief> Beliefs = new ArrayList<Belief>();
	private ArrayList<Motive> Motives = new ArrayList<Motive>();
	
	private Plan plan;
	private Plan parentPlan;
	private String label;
	private int currentTurn;
	private double effort;
	private Motive activeMotive = null;
	
	public Goal (Plan plan) {
		this.plan        = plan;
		this.parentPlan  = plan.getParent();
		this.label       = plan.getGoal().getType().toString();
		this.currentTurn = Turns.getInstance().getTurnNumber();
		this.effort      = 1.0;
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
	
//	public void addMotives(Motive motive) {
//		this.Motives.add(motive);
//	}
	
	public void addGoalToMentalState() {
		MentalState.getInstance().addGoal(this);
	}
	
	public ArrayList<Belief> getBeliefs() {
		return this.Beliefs;
	}
	
	public void addMotives(Motive motive) {
		if (Motives.size() == 0)
			this.setActiveMotive(motive);
		Motives.add(motive);
	}
	
	public ArrayList<Motive> getMotives() {
		return this.Motives;
	}
	
	public void setActiveMotive(Motive motive) {
		this.activeMotive = motive;
	}
	
	// This method might need to be changed after I implement the Motivation mechanism.
	public Motive getActiveMotive() {
		if (this.activeMotive == null)
			return MentalState.getInstance().getParentGoal(this).getActiveMotive();
		else
			return this.activeMotive;
	}
	
	public double getGoalEffort() {
		return this.effort;
	}
	
	public void setGoalEffort(double effort) {
		this.effort = effort;
	}
}
