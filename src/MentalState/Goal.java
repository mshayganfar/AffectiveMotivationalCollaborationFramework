package MentalState;

import edu.wpi.cetask.Plan;

public class Goal{
	
	private Plan plan;
	
	public Goal(Plan goal) {
		this.plan   = goal;
	}
	
	public Plan getPlan() {
		return this.plan;
	}
}
