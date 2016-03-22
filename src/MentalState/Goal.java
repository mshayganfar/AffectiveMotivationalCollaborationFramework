package MentalState;

import java.util.ArrayList;

import MentalState.Motive.MOTIVE_TYPE;
import MetaInformation.Turns;
import edu.wpi.cetask.Plan;

public class Goal {
	
	private ArrayList<Belief> Beliefs = new ArrayList<Belief>();
	private ArrayList<Motive> Motives = new ArrayList<Motive>();
	
	private Plan plan;
	private Plan parentPlan;
	private String label;
	private int currentTurn;
	private Motive activeMotive;
	
	public Goal (Plan plan) {
		this.plan         = plan;
		this.parentPlan   = plan.getParent();
		this.label        = plan.getGoal().getType().toString();
		this.currentTurn  = Turns.getInstance().getTurnNumber();
		MentalState.getInstance().addGoal(this);
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
		Beliefs.add(belief);
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
	
	public Motive getActiveMotive() {
		return this.activeMotive;
	}
}
