package Mechanisms.Motivation;

import java.util.ArrayList;
import java.util.List;

import Mechanisms.Mechanisms;
import Mechanisms.Appraisal.Controllability;
import Mechanisms.Appraisal.Expectedness;
import Mechanisms.Collaboration.Collaboration.GOAL_STATUS;
import Mechanisms.Mechanisms.AGENT;
import Mechanisms.ToM.ToM;
import MentalState.Goal;
import MentalState.Motive;
import MentalState.Motive.MOTIVE_TYPE;
import MetaInformation.Turns;
import edu.wpi.cetask.Plan;
import edu.wpi.disco.lang.Propose;

public class Motivation extends Mechanisms {
	
	private ToM tom;
	private Controllability controllability;
	private Expectedness expectedness;
	
	private SatisfactionDrive satisfactionDrive;
	
	public Motivation() {
		 satisfactionDrive = new SatisfactionDrive();
	}
	
	public void prepareMotivation(ToM tom, Controllability controllability, Expectedness expectedness) {
		 this.tom  			  = tom;
		 this.controllability = controllability;
		 this.expectedness    = expectedness;
	}
	
	private Motive createSatisfactionMotive(Goal goal) {
		
		double firstSigmoidValue  = 0.0;
		double secondSigmoidValue = 0.0;
		
		double firstGradient  = 0.0;
		double secondGradient = 0.0;
		
		double valence  = tom.getValenceValue();
		double satDelta = satisfactionDrive.getSatisfactionDriveDelta();
		
		Motive satisfactionMotive;
		
		if (satDelta >= 0) {
			if (valence >= 0) {
				firstGradient  = 2.0;
				secondGradient = 8.0;
				firstSigmoidValue  = (double)1.0 / (1 + Math.exp(firstGradient * ((1 - satDelta) - valence)));
				secondSigmoidValue = (double)1.0 / (1 + Math.exp(secondGradient * (1.5 - valence)));
			}
			else {
				firstGradient  = 1.5;
				secondGradient = 1.5;
				firstSigmoidValue  = (double)1.0 / (1 + Math.exp(-firstGradient * (satDelta - (2 * Math.abs(valence)))));
				secondSigmoidValue = (double)1.0 / (1 + Math.exp(-secondGradient * (1.5 - (Math.abs(valence)))));
			}
		}
		else {
			if (valence >= 0) {
				firstGradient  = 1.5;
				secondGradient = 1.5;
				firstSigmoidValue  = (double)1.0 / (1 + Math.exp(firstGradient * (Math.abs(satDelta) - (3 * valence))));
				secondSigmoidValue = (double)1.0 / (1 + Math.exp(secondGradient * (1.5 - valence)));
			}
			else {
				firstGradient  = 2.0;
				secondGradient = 8.0;
				firstSigmoidValue  = (double)1.0 / (1 + Math.exp(-firstGradient * ((1 - Math.abs(satDelta)) - (2 * Math.abs(valence)))));
				secondSigmoidValue = (double)1.0 / (1 + Math.exp(-secondGradient * (1.5 - (Math.abs(valence)))));
			}
		}
		
		satisfactionMotive = new Motive(goal, MOTIVE_TYPE.SATISFACTION, firstSigmoidValue - secondSigmoidValue);
		goal.addMotives(satisfactionMotive);
		
		return satisfactionMotive;
	}
	
	private Motive createAchievementMotive(Goal goal) {
		
		double firstSigmoidValue  = 0.0;
		double secondSigmoidValue = 0.0;
		
		double firstGradient  = 4.0;
		double secondGradient = 10.0;
		
		Motive achievementMotive;
		
		double valence  = tom.getValenceValue();
		
		double controllabilityValue = Turns.getInstance().getControllabilityValue(controllability.isEventControllable(goal));
		double expectednessValue    = Turns.getInstance().getExpectednessValue(expectedness.isEventExpected(goal));
		
		double successProbability = controllabilityValue * expectednessValue;
		
		if (valence >= 0) {
			firstSigmoidValue  = (double)1.0 / (1 + Math.exp(firstGradient * (valence - successProbability)));
			secondSigmoidValue = (double)1.0 / (1 + Math.exp(secondGradient * (valence - successProbability)));
		}
		else {
			firstSigmoidValue  = (double)1.0 / (1 + Math.exp(-firstGradient * (valence - successProbability)));
			secondSigmoidValue = (double)1.0 / (1 + Math.exp(-secondGradient * (valence - successProbability)));
		}
		
		achievementMotive = new Motive(goal, MOTIVE_TYPE.ACHIEVEMENT, firstSigmoidValue - secondSigmoidValue);
		goal.addMotives(achievementMotive);
		
		return achievementMotive;
	}
	
	private Motive createExternalMotive(Goal goal) {
		
		if (goal.getPlan().getGoal() instanceof Propose.Should) {
			
			double sigmoidValue  = 0.0;
			
			double valence  = tom.getValenceValue();
			
			// I might need to read these values later using ToM mechanism.
			double controllabilityValue = Turns.getInstance().getControllabilityValue(controllability.isEventControllable(goal));
			double expectednessValue    = Turns.getInstance().getExpectednessValue(expectedness.isEventExpected(goal));
			
			double successProbability = controllabilityValue * expectednessValue;
			
			sigmoidValue  = (double)2.0 / (1 + Math.exp(valence - (6 * successProbability)));
			
			Motive externalMotive = new Motive(goal, MOTIVE_TYPE.EXTERNAL, sigmoidValue);
			goal.addMotives(externalMotive);
			
			return externalMotive;
		}
		
		return null;
	}

	private MOTIVE_TYPE getHighestIntensityMotive (double externalMotiveIntensity, double satisfactionMotiveIntensity, double achievementMotiveIntensity) {
		
		if (((externalMotiveIntensity >= satisfactionMotiveIntensity) && (satisfactionMotiveIntensity >= achievementMotiveIntensity)) ||
			((externalMotiveIntensity >= achievementMotiveIntensity) && (achievementMotiveIntensity <= satisfactionMotiveIntensity)))
			return MOTIVE_TYPE.EXTERNAL;
		else if (((satisfactionMotiveIntensity >= externalMotiveIntensity) && (externalMotiveIntensity >= achievementMotiveIntensity)) ||
				((satisfactionMotiveIntensity >= achievementMotiveIntensity) && (achievementMotiveIntensity <= externalMotiveIntensity)))
			return MOTIVE_TYPE.SATISFACTION;
		else
			return MOTIVE_TYPE.ACHIEVEMENT;
	}
	
	private double getDefaultInternalMotiveIntensity(Goal goal) {
		
		for (Motive motive : goal.getMotives()) {
			if (motive.getMotiveType().equals(MOTIVE_TYPE.INTERNAL_DEFAULT))
				return motive.getMotiveIntensity();
		}
		
		return 0.0;
	}
	
	private MOTIVE_TYPE arbitrateMotives(Goal goal, Motive externalMotive, Motive satisfactionMotive, Motive achievementMotive) {
		
		switch(getHighestIntensityMotive ((externalMotive == null) ? 0.0 : externalMotive.getMotiveIntensity(), 
				   satisfactionMotive.getMotiveIntensity(), 
				   achievementMotive.getMotiveIntensity())) {
		case EXTERNAL:
			if (externalMotive.getMotiveIntensity() > getDefaultInternalMotiveIntensity(goal))
				return MOTIVE_TYPE.EXTERNAL;
			else
				return MOTIVE_TYPE.INTERNAL_DEFAULT;
		case SATISFACTION:
			if (satisfactionMotive.getMotiveIntensity() > getDefaultInternalMotiveIntensity(goal))
				return MOTIVE_TYPE.SATISFACTION;
			else
				return MOTIVE_TYPE.INTERNAL_DEFAULT;
		case ACHIEVEMENT:
			if (achievementMotive.getMotiveIntensity() > getDefaultInternalMotiveIntensity(goal))
				return MOTIVE_TYPE.ACHIEVEMENT;
			else
				return MOTIVE_TYPE.INTERNAL_DEFAULT;
		default:
			throw new IllegalStateException("WRONG MOTIVE in arbitrateMotives method!");
		}
	}
	
	public MOTIVE_TYPE createMotives(Goal goal) {
		
		Motive externalMotive     = createExternalMotive(goal);
		Motive satisfactionMotive = createSatisfactionMotive(goal);
		Motive achievementMotive  = createAchievementMotive(goal);
		
		MOTIVE_TYPE winnerMotive = arbitrateMotives(goal, externalMotive, satisfactionMotive, achievementMotive);
		
		System.out.println(winnerMotive);
		
		return winnerMotive;
	}
	
	private void getGoalAntecedents(Plan plan, List<GOAL_STATUS> goalAntecedents) {
		
		if (plan.getParent() == null)
			return;
		
		goalAntecedents.add(collaboration.getGoalStatus(plan.getParent()));
		
		for (Plan predecessor : plan.getPredecessors()) {
			goalAntecedents.add(collaboration.getGoalStatus(predecessor));
		}
		
		getGoalAntecedents(plan.getParent(), goalAntecedents);
	}
	
	public double getMotiveImportance(Motive motive) {
		
		// This is based on the fact that if there is no alternative recipe, the motive is more important.
		if(motive.getGoal().getPlan().isPrimitive())
			return 1.0;
		else
			if (motive.getGoal().getPlan().getDecompositions().size() <= 1)
				return 1.0;
			else
				return 0.0;
		
		// Later, I should check whether the current alternative recipe can remove the current impasse.
	}
	
//	public double getMotiveImportance(Goal goal) {
//		
//		// I need non-failed alternative recipes for a failed task.
//		// I might need to check whether the previous shared goal is failed/blocked, etc.!
//		Plan plan = goal.getPlan();
//		
//		if (plan.isPrimitive())
//			plan = goal.getPlan().getParent();
//		
//		if (plan.getDecompositions().size() > 0) // This is wrong!!!
//			return 1.0;
//		else
//			return 0.0;
//	}
	
	public double getMotiveUrgency(Motive motive) {
		
		double urgencySuccessorValue  = 0.0;
		double urgencyMitigationValue = 0.0;
		
		for (Plan plan : motive.getGoal().getPlan().getSuccessors()) {
			if (collaboration.getResponsibleAgent(motive.getGoal().getPlan()).equals(AGENT.OTHER))
				urgencySuccessorValue++;
			else if (collaboration.getResponsibleAgent(motive.getGoal().getPlan()).equals(AGENT.BOTH))
				urgencySuccessorValue += 0.5;
		}
		
		// Later, I should check whether the goal is a tactical one, then influence urgency of the related motive.
		urgencyMitigationValue = (isAcknowledgementMotive(motive.getGoal())) ? 1 : 0;
		
		return (urgencySuccessorValue + urgencyMitigationValue);
	}
	
	private boolean isAcknowledgementMotive(Goal goal) {
		return false;
	}
	
	public double getMotiveInsistence(Motive motive) {
		
		double motiveInsistence = 0.0;
		
		List<GOAL_STATUS> goalAntecedentsStatus = new ArrayList<GOAL_STATUS>();
		
		getGoalAntecedents(motive.getGoal().getPlan(), goalAntecedentsStatus);
		
		for(GOAL_STATUS goalStatus : goalAntecedentsStatus) {
			if (goalAntecedentsStatus.equals(GOAL_STATUS.INPROGRESS) || 
					goalAntecedentsStatus.equals(GOAL_STATUS.ACHIEVED) ||
					goalAntecedentsStatus.equals(GOAL_STATUS.PENDING))
				motiveInsistence++;
			else
				motiveInsistence--;
		}
		
		return motiveInsistence;
	}
}
