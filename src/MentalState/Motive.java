package MentalState;

import java.util.List;

import Mechanisms.Collaboration.Collaboration.GOAL_STATUS;
import Mechanisms.Mechanisms.AGENT;
import edu.wpi.cetask.Plan;

public class Motive {

	public enum MOTIVE_TYPE{ACHIEVEMENT, SATISFACTION, INTERNAL_DEFAULT, EXTERNAL};
	public enum MOTIVE_INTENSITY{HIGH_POSITIVE, MEDIUM_POSITIVE, LOW_POSITIVE, HIGH_NEGATIVE, MEDIUM_NEGATIVE, LOW_NEGATIVE};
	
	private String label;
	private Goal goal;
	private MOTIVE_TYPE motiveType;
	private boolean activeMotive;
	private double motiveIntensity;
	private double motiveImportance;
	private double motiveUrgency;
	private double motiveInsistence;
	
	public Motive (Goal goal) {
		this.goal  = goal;
		this.label = goal.getPlan().getGoal().getType().toString();
		this.motiveType   = MOTIVE_TYPE.INTERNAL_DEFAULT;
		this.activeMotive = true;
		this.motiveIntensity  = 1.0;
		this.motiveImportance = computeMotiveImportance();
		this.motiveUrgency    = computeMotiveUrgency();
//		this.motiveInsistence = computeMotiveInsistence();
		this.goal.addMotives(this);
		MentalState.getInstance().addMotive(this);
	}
	
	public Motive (Goal goal, MOTIVE_TYPE motiveType, double motiveIntensity) {
		this.goal  = goal;
		this.label = goal.getPlan().getGoal().getType().toString();
		this.motiveType   = motiveType;
		this.activeMotive = false;
		this.motiveIntensity = motiveIntensity;
		this.motiveImportance = computeMotiveImportance();
		this.motiveUrgency    = computeMotiveUrgency();
//		this.motiveInsistence = computeMotiveInsistence();
		this.goal.addMotives(this);
		MentalState.getInstance().addMotive(this);
	}
	
//	public void addMotiveToMentalState() {
//		MentalState.getInstance().addMotive(this);
//	}
	
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
	
	public double getMotiveImportance() {
		return this.motiveImportance;
	}
	
	public double getMotiveUrgency() {
		return this.motiveUrgency;
	}
	
	public double getMotiveIntensity() {
		return this.motiveIntensity;
	}
	
//	public double getMotiveInsistence() {
//		return this.motiveInsistence;
//	}
	
	public boolean isActiveMotive() {
		return this.activeMotive; 
	}
	
	public MOTIVE_INTENSITY getSymbolicMotiveIntensity() {
		
		if ((motiveIntensity >= 0.67) && (motiveIntensity <= 1.0))
			return MOTIVE_INTENSITY.HIGH_POSITIVE;
		else if ((motiveIntensity < 0.67) && (motiveIntensity >= 0.34))
			return MOTIVE_INTENSITY.MEDIUM_POSITIVE;
		else if ((motiveIntensity >= 0.0) && (motiveIntensity < 0.34))
			return MOTIVE_INTENSITY.LOW_POSITIVE;
		else if ((motiveIntensity < 0.0) && (motiveIntensity > -0.34))
			return MOTIVE_INTENSITY.LOW_NEGATIVE;
		else if ((motiveIntensity > -0.67) && (motiveIntensity <= -0.34))
			return MOTIVE_INTENSITY.MEDIUM_NEGATIVE;
		if ((motiveIntensity <= -0.67) && (motiveIntensity >= -1.0))
			return MOTIVE_INTENSITY.HIGH_NEGATIVE;
		else
			throw new IllegalArgumentException("Illegal motive intensity value:" + motiveIntensity);
	}
	
	private double computeMotiveImportance() {
		
		// This is based on the fact that if there is no alternative recipe, the motive is more important.
		// And whether the current alternative recipe can remove the current impasse.
		Plan plan = this.getGoal().getPlan();
		
		if(plan.isPrimitive())
			return 1.0;
		else {
			if (plan.getDecompositions().size() == 0)
				return 1.0;
			else if ((plan.getDecompositions().size() >= 1) && (plan.getFailed().size() >= 1))
				return 1.0;
			else
				return 0.0;
		}
	}
	
	private double computeMotiveUrgency() {
		
		double urgencySuccessorValue  = 0.0;
		double urgencyMitigationValue = 0.0;
		
		for (Plan plan : this.getGoal().getPlan().getSuccessors()) {
			if (this.getGoal().getMentalProcesses().getCollaborationMechanism().getResponsibleAgent(plan).equals(AGENT.OTHER))
				urgencySuccessorValue++;
			else if (this.getGoal().getMentalProcesses().getCollaborationMechanism().getResponsibleAgent(plan).equals(AGENT.BOTH))
				urgencySuccessorValue += 0.5;
		}
		urgencyMitigationValue = (this.getGoal().isTactical()) ? 1.0 : 0.0;
		
		urgencySuccessorValue  = (this.getGoal().getPlan().getSuccessors().size() == 0) ? 0.0 : 
			(double)urgencySuccessorValue/this.getGoal().getPlan().getSuccessors().size();
		
		return ((double)(urgencySuccessorValue + urgencyMitigationValue))/2.0;
	}
	
	// TO DO: This method needs to be fixed.
//	public double computeMotiveInsistence() {
//		
//		double motiveInsistence = 0.0;
//		
//		List<GOAL_STATUS> goalAntecedentsStatus = new ArrayList<GOAL_STATUS>();
//		
//		getGoalAntecedents(this.getGoal().getPlan(), goalAntecedentsStatus);
//		
//		for(GOAL_STATUS goalStatus : goalAntecedentsStatus) {
//			if (goalAntecedentsStatus.equals(GOAL_STATUS.INPROGRESS) || 
//					goalAntecedentsStatus.equals(GOAL_STATUS.ACHIEVED) ||
//					goalAntecedentsStatus.equals(GOAL_STATUS.PENDING))
//				motiveInsistence++;
//			else
//				motiveInsistence--;
//		}
//		
//		return motiveInsistence;
//	}
	
	private void getGoalAntecedents(Plan plan, List<GOAL_STATUS> goalAntecedents) {
		
		if (plan.getParent() == null)
			return;
		
		goalAntecedents.add(this.getGoal().getMentalProcesses().getCollaborationMechanism().getGoalStatus(plan.getParent()));
		
		for (Plan predecessor : plan.getPredecessors()) {
			goalAntecedents.add(this.getGoal().getMentalProcesses().getCollaborationMechanism().getGoalStatus(predecessor));
		}
		
		getGoalAntecedents(plan.getParent(), goalAntecedents);
	}
}
