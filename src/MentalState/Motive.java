package MentalState;

public class Motive {

	public enum MOTIVE_TYPE{ACHIEVEMENT, SATISFACTION, INTERNAL_DEFAULT, EXTERNAL};
	
	private String label;
	private Goal goal;
	private MOTIVE_TYPE motiveType;
	private boolean activeMotive;
	private double motiveIntensity;
	
	public Motive (Goal goal) {
		this.goal  = goal;
		this.label = goal.getPlan().getGoal().getType().toString();
		this.motiveType   = MOTIVE_TYPE.INTERNAL_DEFAULT;
		this.activeMotive = false;
		this.motiveIntensity = 0.5;
		this.goal.addMotives(this);
	}
	
	public Motive (Goal goal, MOTIVE_TYPE motiveType, double motiveIntensity) {
		this.goal  = goal;
		this.label = goal.getPlan().getGoal().getType().toString();
		this.motiveType   = motiveType;
		this.activeMotive = false;
		this.motiveIntensity = motiveIntensity;
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
	
	public void activateMotive() {
		for (Motive motive : this.getGoal().getMotives()) {
			motive.deactivateMotive();
		}
		this.activeMotive = true;
	}
	
	public void deactivateMotive() {
		this.activeMotive = false;
	}
			
}
