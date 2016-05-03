package MentalState;

public class Motive {

	public enum MOTIVE_TYPE{ACHIEVEMENT, SATISFACTION, INTERNAL_DEFAULT, EXTERNAL};
	public enum MOTIVE_INTENSITY{HIGH_POSITIVE, MEDIUM_POSITIVE, LOW_POSITIVE, HIGH_NEGATIVE, MEDIUM_NEGATIVE, LOW_NEGATIVE};
	
	private String label;
	private Goal goal;
	private MOTIVE_TYPE motiveType;
	private boolean activeMotive;
	private double motiveIntensity;
	
	public Motive (Goal goal) {
		this.goal  = goal;
		this.label = goal.getPlan().getGoal().getType().toString();
		this.motiveType   = MOTIVE_TYPE.INTERNAL_DEFAULT;
		this.activeMotive = true;
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
	
	public double getMotiveIntensity() {
		return this.motiveIntensity;
	}
	
	public boolean isActiveMotive() {
		return this.activeMotive; 
	}
	
	public MOTIVE_INTENSITY getSymbolicMotiveIntensity() {
		
		if (motiveIntensity >= 0.67)
			return MOTIVE_INTENSITY.HIGH_POSITIVE;
		else if ((motiveIntensity < 0.67) && (motiveIntensity >= 0.34))
			return MOTIVE_INTENSITY.MEDIUM_POSITIVE;
		else if ((motiveIntensity > 0.0) && (motiveIntensity < 0.34))
			return MOTIVE_INTENSITY.LOW_POSITIVE;
		else if ((motiveIntensity < 0.0) && (motiveIntensity > -0.34))
			return MOTIVE_INTENSITY.LOW_NEGATIVE;
		else if ((motiveIntensity > -0.67) && (motiveIntensity <= -0.34))
			return MOTIVE_INTENSITY.MEDIUM_NEGATIVE;
		if (motiveIntensity <= -0.67)
			return MOTIVE_INTENSITY.HIGH_NEGATIVE;
		else
			throw new IllegalArgumentException("Illegal motive intensity value:" + motiveIntensity);
	}
}
