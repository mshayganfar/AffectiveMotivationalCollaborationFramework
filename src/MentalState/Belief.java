package MentalState;

public class Belief extends MentalState{
	
	private int occurrence;
	private Goal goal;
	
	public Belief(Goal goal) {
		this.goal = goal;
		this.goal.addBeliefs(this);
		this.occurrence = getBeliefOccurrenceCount(goal);
	}
	
	public Goal getGoal() {
		return this.goal;
	}
	
	public int getOccurrence() {
		return this.occurrence;
	}
	
	private int getBeliefOccurrenceCount(Goal goal) {
		return goal.getBeliefs().size();
	}
}
