package MentalState;

import java.util.ArrayList;

public class Belief extends MentalState{
	
	private String label;
	private Goal goal;
	private int turn;
	
	public Belief(Goal goal) {
		this.goal = goal;
		this.label = goal.getPlan().getGoal().getType().toString();
		this.turn = currentTurn.getCurrentTurn();
		this.goal.addBeliefs(this);
	}
	
	public Goal getGoal() {
		return this.goal;
	}
	
	public int getTurn() {
		return this.turn;
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
