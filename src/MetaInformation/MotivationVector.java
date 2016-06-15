package MetaInformation;

import MentalState.Goal;
import MentalState.Motive;

public class MotivationVector {
	
	private Goal goal;
	
	private Motive satisfactionMotive;
	private Motive achievementMotive;
	private Motive externalMotive;
	
	public MotivationVector (Goal goal) {
		
		this.goal               = goal;
		this.satisfactionMotive = goal.getSatisfactionMotive();
		this.achievementMotive  = goal.getAchievementMotive();
		this.externalMotive     = goal.getExternalMotive();
	}
	
	public Motive getSatisfactionMotive() {
		return this.satisfactionMotive;
	}
	
	public Motive getAchievementMotive() {
		return this.achievementMotive;
	}
	
	public Motive getExternalMotive() {
		return this.externalMotive;
	}
	
	public Goal getMotivesGoal() {
		return this.goal;
	}
}
