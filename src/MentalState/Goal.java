package MentalState;

import java.util.ArrayList;

import MetaInformation.Turns;
import edu.wpi.cetask.Plan;

public class Goal {
	
	private ArrayList<Belief> Beliefs = new ArrayList<Belief>();
	
	private Plan plan;
	private String label;
	private int currentTurn;
	
	public Goal(Plan plan) {
		this.plan  = plan;
		this.label = plan.getGoal().getType().toString();
		this.currentTurn = Turns.getInstance().getTurnNumber();
	}
	
	public Plan getPlan() {
		return this.plan;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public void addBeliefs(Belief belief) {
		Beliefs.add(belief);
	}
	
	public ArrayList<Belief> getBeliefs() {
		return this.Beliefs;
	}
}
