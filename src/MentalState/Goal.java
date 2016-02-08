package MentalState;

import java.util.ArrayList;

import edu.wpi.cetask.Plan;

public class Goal extends MentalState{
	
	private ArrayList<Belief> Beliefs = new ArrayList<Belief>();
	
	private Plan plan;
	
	public Goal(Plan plan) {
		this.plan = plan;
	}
	
	public Plan getPlan() {
		return this.plan;
	}
	
	public void addBeliefs(Belief belief) {
		Beliefs.add(belief);
	}
	
	public ArrayList<Belief> getBeliefs() {
		return this.Beliefs;
	}
}
