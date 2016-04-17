package Mechanisms.Motivation;

import Mechanisms.Mechanisms;
import Mechanisms.Appraisal.Controllability;
import Mechanisms.Appraisal.Expectedness;
import Mechanisms.ToM.ToM;
import MentalState.Goal;
import MentalState.Motive;
import MentalState.Motive.MOTIVE_TYPE;
import MetaInformation.Turns;
import edu.wpi.disco.lang.Propose;

public class Motivation extends Mechanisms {
	
	private ToM tom;
	private Controllability controllability;
	private Expectedness expectedness;
	
	private SatisfactionDrive satisfactionDrive;
	
	public Motivation(ToM tom, Controllability controllability, Expectedness expectedness) {
		 satisfactionDrive = new SatisfactionDrive();
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
}
