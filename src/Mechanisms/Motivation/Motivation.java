package Mechanisms.Motivation;

import Mechanisms.Mechanisms;
import Mechanisms.Appraisal.Controllability;
import Mechanisms.Appraisal.Expectedness;
import Mechanisms.ToM.ToM;
import MentalState.Goal;
import MentalState.Motive;
import MentalState.Motive.MOTIVE_TYPE;
import MetaInformation.MentalProcesses;
import MetaInformation.Turns;
import MetaInformation.AppraisalVector.EMOTION_INSTANCE;
import edu.wpi.disco.lang.Propose;

public class Motivation extends Mechanisms {
	
	private ToM tom;
	
	private Controllability controllability;
	private Expectedness expectedness;
	
	private SatisfactionDrive satisfactionDrive;
	
	public Motivation() {
		 satisfactionDrive = new SatisfactionDrive();
	}
	
	public void prepareMotivationMechanism(MentalProcesses mentalProcesses) {
		this.tom  			 = mentalProcesses.getToMMechanism();
		this.controllability = mentalProcesses.getControllabilityProcess();
		this.expectedness    = mentalProcesses.getExpectednessProcess();
	}
	
	private Motive createSatisfactionMotive(Goal goal) {
		
//		double firstSigmoidValue  = 0.0;
//		double secondSigmoidValue = 0.0;
		
//		double firstGradient  = 0.0;
//		double secondGradient = 0.0;
		
//		if (satDelta >= 0) {
//			if (valence >= 0) {
//				firstGradient  = 2.0;
//				secondGradient = 8.0;
//				firstSigmoidValue  = (double)1.0 / (1 + Math.exp(firstGradient * ((1 - satDelta) - valence)));
//				secondSigmoidValue = (double)1.0 / (1 + Math.exp(secondGradient * (1.5 - valence)));
//			}
//			else {
//				firstGradient  = 1.5;
//				secondGradient = 1.5;
//				firstSigmoidValue  = (double)1.0 / (1 + Math.exp(-firstGradient * (satDelta - (2 * Math.abs(valence)))));
//				secondSigmoidValue = (double)1.0 / (1 + Math.exp(-secondGradient * (1.5 - (Math.abs(valence)))));
//			}
//		}
//		else {
//			if (valence >= 0) {
//				firstGradient  = 1.5;
//				secondGradient = 1.5;
//				firstSigmoidValue  = (double)1.0 / (1 + Math.exp(firstGradient * (Math.abs(satDelta) - (3 * valence))));
//				secondSigmoidValue = (double)1.0 / (1 + Math.exp(secondGradient * (1.5 - valence)));
//			}
//			else {
//				firstGradient  = 2.0;
//				secondGradient = 8.0;
//				firstSigmoidValue  = (double)1.0 / (1 + Math.exp(-firstGradient * ((1 - Math.abs(satDelta)) - (2 * Math.abs(valence)))));
//				secondSigmoidValue = (double)1.0 / (1 + Math.exp(-secondGradient * (1.5 - (Math.abs(valence)))));
//			}
//		}

//		goal.addMotives(satisfactionMotive);
		
		return new Motive(goal, MOTIVE_TYPE.SATISFACTION, computeSatisfactionMotiveIntensity(goal));
	}
	
	private double computeSatisfactionMotiveIntensity(Goal goal) {
		
		double satisfactionMotiveValue = 0.0;
		
		double valence  = tom.getValenceValue(goal);
		double satDelta = satisfactionDrive.getSatisfactionDriveDelta();
		
		if (valence == 0)
			satisfactionMotiveValue = Math.atan(1.5*satDelta);
		else if (valence > 0)
			satisfactionMotiveValue = Math.pow(3, 2*(satDelta-1));
		else if (valence < 0) {
			satisfactionMotiveValue = -Math.pow(3, -2*(satDelta+1));
		}
		
		return satisfactionMotiveValue;
	}
	
	private Motive createAchievementMotive(Goal goal) {
		
		if (!(goal.getPlan().getGoal() instanceof Propose.Should))
			return new Motive(goal, MOTIVE_TYPE.ACHIEVEMENT, computeAchievementMotiveIntensity(goal));
		
		return null;
	}
	
	private double computeAchievementMotiveIntensity(Goal goal) {
		
		double firstSigmoidValue  = 0.0;
		double secondSigmoidValue = 0.0;
		
		double valence  = tom.getValenceValue(goal);
		
		double controllabilityValue = Turns.getInstance().getControllabilityValue(controllability.isEventControllable(goal));
		double expectednessValue    = Turns.getInstance().getExpectednessValue(expectedness.isEventExpected(goal));
		
		double successProbability = controllabilityValue * expectednessValue;
		
		EMOTION_INSTANCE humanEmotion = Turns.getInstance().getTurnHumanEmotion(goal);
		
		if (humanEmotion == null)
			throw new IllegalArgumentException("Illegal Human Emotion Instance: " + humanEmotion);
		
		if (humanEmotion.equals(EMOTION_INSTANCE.ANGER) || 
				humanEmotion.equals(EMOTION_INSTANCE.WORRY) ||
				humanEmotion.equals(EMOTION_INSTANCE.FRUSTRATION)) {
			double firstGradient  = 0.5;
			double secondGradient = 12.0;
			firstSigmoidValue  = (double)1.0 / (1 + Math.exp((firstGradient - valence) * (1.05 - successProbability)));
			secondSigmoidValue = (double)1.0 / (1 + Math.exp((secondGradient - valence) * (successProbability - 1.02)));
		}
		else {
			double firstGradient  = 2.0;
			double secondGradient = 12.0;
			firstSigmoidValue  = (double)2.0 / (1 + Math.exp((firstGradient - Math.abs(valence)) * (1.05 - successProbability)));
			secondSigmoidValue = (double)1.0 / (1 + Math.exp((secondGradient - Math.abs(valence)) * (1.1 - successProbability)));
		}
		return (firstSigmoidValue - secondSigmoidValue);
	}
	
	private Motive createExternalMotive(Goal goal) {
		
		if (goal.getPlan().getGoal() instanceof Propose.Should)
			return new Motive(goal, MOTIVE_TYPE.EXTERNAL, computeExternalMotiveIntensity(goal));
		
		return null;
	}

	private double computeExternalMotiveIntensity(Goal goal) {
		
		double firstSigmoidValue  = 0.0;
		double secondSigmoidValue = 0.0;
		
		double valence  = tom.getValenceValue(goal);
			
		// I might need to read these values later using ToM mechanism.
		double controllabilityValue = Turns.getInstance().getControllabilityValue(controllability.isEventControllable(goal));
		double expectednessValue    = Turns.getInstance().getExpectednessValue(expectedness.isEventExpected(goal));
			
		double successProbability = controllabilityValue * expectednessValue;
		
		if (valence <= 0) {
			double firstGradient  = 2.0;
			double secondGradient = 12.0;
			firstSigmoidValue  = (double)2.0 / (1 + Math.exp((firstGradient - Math.abs(valence)) * (1.05 - successProbability)));
			secondSigmoidValue = (double)1.0 / (1 + Math.exp((secondGradient - Math.abs(valence)) * (1.1 - successProbability)));
		}
		else {
			double firstGradient  = 0.5;
			double secondGradient = 12.0;
			firstSigmoidValue  = (double)1.0 / (1 + Math.exp((firstGradient - valence) * (1.05 - successProbability)));
			secondSigmoidValue = (double)1.0 / (1 + Math.exp((secondGradient - valence) * (successProbability - 1.02)));
		}
		
		return (firstSigmoidValue - secondSigmoidValue);
	}
	
	private MOTIVE_TYPE getHighestIntensityMotive (double externalMotiveIntensity, double satisfactionMotiveIntensity, double achievementMotiveIntensity) {
		
		if (externalMotiveIntensity >= satisfactionMotiveIntensity) {
			if (satisfactionMotiveIntensity >= achievementMotiveIntensity)
				return MOTIVE_TYPE.EXTERNAL;
			else {
				if (externalMotiveIntensity >= achievementMotiveIntensity)
					return MOTIVE_TYPE.EXTERNAL;
				else
					return MOTIVE_TYPE.ACHIEVEMENT;
			}
		} else {
			if (satisfactionMotiveIntensity >= achievementMotiveIntensity)
				return MOTIVE_TYPE.SATISFACTION;
			else
				return MOTIVE_TYPE.ACHIEVEMENT;
		}
	}
	
	private double getDefaultInternalMotiveIntensity(Goal goal) {
		
		for (Motive motive : goal.getMotives()) {
			if (motive.getMotiveType().equals(MOTIVE_TYPE.INTERNAL_DEFAULT))
				return motive.getMotiveIntensity();
		}
		
		return 0.0;
	}
	
	private MOTIVE_TYPE arbitrateMotives(Goal goal, Motive externalMotive, Motive satisfactionMotive, Motive achievementMotive) {
		
		return getHighestIntensityMotive ((externalMotive == null) ? -2.0 : externalMotive.getMotiveIntensity(), 
				(achievementMotive == null) ? -2.0 : achievementMotive.getMotiveIntensity(),
				satisfactionMotive.getMotiveIntensity());
				
//		switch(getHighestIntensityMotive ((externalMotive == null) ? -2.0 : externalMotive.getMotiveIntensity(), 
//				   satisfactionMotive.getMotiveIntensity(), 
//				   achievementMotive.getMotiveIntensity())) {
//			case EXTERNAL:
//				if (externalMotive.getMotiveIntensity() > getDefaultInternalMotiveIntensity(goal))
//					return MOTIVE_TYPE.EXTERNAL;
//				else
//					return MOTIVE_TYPE.INTERNAL_DEFAULT;
//			case SATISFACTION:
//				if (satisfactionMotive.getMotiveIntensity() > getDefaultInternalMotiveIntensity(goal))
//					return MOTIVE_TYPE.SATISFACTION;
//				else
//					return MOTIVE_TYPE.INTERNAL_DEFAULT;
//			case ACHIEVEMENT:
//				if (achievementMotive.getMotiveIntensity() > getDefaultInternalMotiveIntensity(goal))
//					return MOTIVE_TYPE.ACHIEVEMENT;
//				else
//					return MOTIVE_TYPE.INTERNAL_DEFAULT;
//			default:
//				throw new IllegalStateException("WRONG MOTIVE in arbitrateMotives method!");
//		}
	}
	
	public MOTIVE_TYPE createMotives(Goal goal) {
		
		Motive externalMotive     = createExternalMotive(goal);
		Motive satisfactionMotive = createSatisfactionMotive(goal);
		Motive achievementMotive  = createAchievementMotive(goal);
		
		System.out.println("SATISFACTION MOTIVE: (" + satisfactionMotive.getLabel() + "," + satisfactionMotive.getMotiveIntensity() + ")");
		
		if (achievementMotive != null)
			System.out.println("ACHIEVEMENT MOTIVE: (" + achievementMotive.getLabel() + "," + achievementMotive.getMotiveIntensity() + ")");
		else
			System.out.println("NO ACHIEVEMENT MOTIVE!");
		
		if (externalMotive != null)
			System.out.println("EXTERNAL MOTIVE: (" + externalMotive.getLabel() + "," + externalMotive.getMotiveIntensity() + ")");
		else
			System.out.println("NO EXTERNAL MOTIVE!");
		
		MOTIVE_TYPE winnerMotive = arbitrateMotives(goal, externalMotive, satisfactionMotive, achievementMotive);
		
		return winnerMotive;
	}
}
