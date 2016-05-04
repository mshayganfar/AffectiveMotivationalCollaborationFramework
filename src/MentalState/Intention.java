package MentalState;

import MetaInformation.CopingElicitationFrame.COPING_STRATEGY;

public class Intention {
	
	public enum INTENTION_TARGET {AGENT, HUMAN, ENVIRONMENT};
	
	private String label;
	private Goal goal;
	private Motive motive;
	private INTENTION_TARGET intentionTarget;
	private COPING_STRATEGY copingStrategy;
	
	public Intention (Goal goal, COPING_STRATEGY copingStrategy) {
		this.goal   = goal;
		this.label  = goal.getPlan().getGoal().getType().toString();
		this.copingStrategy = copingStrategy;
		this.goal.addIntentions(this);
	}
	
	public Intention (Goal goal, Motive motive) {
		this.goal   = goal;
		this.motive = motive;
		this.label  = goal.getPlan().getGoal().getType().toString();
		this.intentionTarget = INTENTION_TARGET.ENVIRONMENT;
		this.goal.addIntentions(this);
	}
	
	public Intention (Goal goal, Motive motive, INTENTION_TARGET intentionTarget) {
		this.goal   = goal;
		this.motive = motive;
		this.label  = goal.getPlan().getGoal().getType().toString();
		this.intentionTarget = intentionTarget;
		this.goal.addIntentions(this);
	}
	
	public Goal getGoal() {
		return this.goal;
	}
	
	public Motive getIntentionMotive() {
		return this.motive;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public INTENTION_TARGET getIntentionTarget() {
		return this.intentionTarget;
	}
	
	public COPING_STRATEGY getCopingStrategy() {
		return this.copingStrategy;
	}
}
