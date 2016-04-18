package MentalState;

import java.util.ArrayList;

import MetaInformation.Node;

public class Belief {
	
	private String label;
	private Goal goal;
	
	public Belief (Goal goal) {
		this.goal  = goal;
		this.label = goal.getPlan().getGoal().getType().toString();
		this.goal.addBeliefs(this);
	}
	
	public Goal getGoal() {
		return this.goal;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public int getOccurrenceCount() {
		
		int beliefCount = 0;
		
		ArrayList<Belief> beliefs = this.goal.getBeliefs();
		
		for (Belief belief : beliefs) {
			if (belief.label.equals(this.label))
				beliefCount++;
		}
		
		return beliefCount;
	}
}
