package MentalState;

public class Motive {

	public enum MOTIVE_TYPE{INTERNAL, EXTERNAL};
	
	private String label;
	private Goal goal;
	private MOTIVE_TYPE motiveType;
	
	public Motive (Goal goal) {
		this.goal  = goal;
		this.label = goal.getPlan().getGoal().getType().toString();
		this.motiveType = MOTIVE_TYPE.INTERNAL;
		this.goal.addMotives(this);
	}
	
	public Goal getGoal() {
		return this.goal;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public MOTIVE_TYPE getMotiveType() {
		return this.motiveType;
	}
}
