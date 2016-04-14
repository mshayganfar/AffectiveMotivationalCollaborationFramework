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
	
	private double createSatisfactionMotive(Goal goal) {
		
		double firstSigmoidValue  = 0.0;
		double secondSigmoidValue = 0.0;
		
		double firstGradient  = 0.0;
		double secondGradient = 0.0;
		
		double valence  = tom.getValenceValue();
		double satDelta = satisfactionDrive.getSatisfactionDriveDelta();
		
		if (satDelta >= 0) {
			if (valence >= 0) {
				firstGradient  = 2.0;
				secondGradient = 8.0;
				firstSigmoidValue  = (double)1 / (1 + Math.exp(firstGradient * ((1 - satDelta) - valence)));
				secondSigmoidValue = (double)1 / (1 + Math.exp(secondGradient * (1.5 - valence)));
				
				goal.addMotives(new Motive(goal, MOTIVE_TYPE.SATISFACTION, firstSigmoidValue - secondSigmoidValue));
			}
			else {
				firstGradient  = 1.5;
				secondGradient = 1.5;
				firstSigmoidValue  = (double)1 / (1 + Math.exp(-firstGradient * (satDelta - (2 * Math.abs(valence)))));
				secondSigmoidValue = (double)1 / (1 + Math.exp(-secondGradient * (1.5 - (Math.abs(valence)))));
				
				goal.addMotives(new Motive(goal, MOTIVE_TYPE.SATISFACTION, firstSigmoidValue - secondSigmoidValue));
			}
		}
		else {
			if (valence >= 0) {
				firstGradient  = 1.5;
				secondGradient = 1.5;
				firstSigmoidValue  = (double)1 / (1 + Math.exp(firstGradient * (Math.abs(satDelta) - (3 * valence))));
				secondSigmoidValue = (double)1 / (1 + Math.exp(secondGradient * (1.5 - valence)));
				
				goal.addMotives(new Motive(goal, MOTIVE_TYPE.SATISFACTION, firstSigmoidValue - secondSigmoidValue));
			}
			else {
				firstGradient  = 2.0;
				secondGradient = 8.0;
				firstSigmoidValue  = (double)1 / (1 + Math.exp(-firstGradient * ((1 - Math.abs(satDelta)) - (2 * Math.abs(valence)))));
				secondSigmoidValue = (double)1 / (1 + Math.exp(-secondGradient * (1.5 - (Math.abs(valence)))));
				
				goal.addMotives(new Motive(goal, MOTIVE_TYPE.SATISFACTION, firstSigmoidValue - secondSigmoidValue));
			}
		}

		double satMotiveValue = firstSigmoidValue - secondSigmoidValue;
		
		return satMotiveValue;
	}
	
	private double createAchievementMotive(Goal goal) {
		
		double firstSigmoidValue  = 0.0;
		double secondSigmoidValue = 0.0;
		
		double firstGradient  = 4.0;
		double secondGradient = 10.0;
		
		double valence  = tom.getValenceValue();
		
		double controllabilityValue = Turns.getInstance().getControllabilityValue(controllability.isEventControllable(goal));
		double expectednessValue    = Turns.getInstance().getExpectednessValue(expectedness.isEventExpected(goal));
		
		double successProbability = controllabilityValue * expectednessValue;
		
		if (valence >= 0) {
			firstSigmoidValue  = (double)1 / (1 + Math.exp(firstGradient * (valence - successProbability)));
			secondSigmoidValue = (double)1 / (1 + Math.exp(secondGradient * (valence - successProbability)));
			
			goal.addMotives(new Motive(goal, MOTIVE_TYPE.ACHIEVEMENT, firstSigmoidValue - secondSigmoidValue));
		}
		else {
			firstSigmoidValue  = (double)1 / (1 + Math.exp(-firstGradient * (valence - successProbability)));
			secondSigmoidValue = (double)1 / (1 + Math.exp(-secondGradient * (valence - successProbability)));
			
			goal.addMotives(new Motive(goal, MOTIVE_TYPE.ACHIEVEMENT, firstSigmoidValue - secondSigmoidValue));
		}
		
		return firstSigmoidValue - secondSigmoidValue;
	}
	
	private double getExternalMotiveValue(Goal goal, Motive motive) {
		
		return 0.5;
	}
	
	private double createExternalMotive(Goal goal) {
		
		if (collaboration.getDisco().getLastOccurrence() instanceof Propose.Should) {
			Motive motive = new Motive(goal, MOTIVE_TYPE.EXTERNAL, 0.5); // How should I comopute external motive's intensity?!
			goal.addMotives(motive);
			return getExternalMotiveValue(goal, motive);
		}
		return -2.0;
	}

	public void createMotives(Goal goal) {
		
		double externalMotiveValue     = createExternalMotive(goal);
		double satisfactionMotiveValue = createSatisfactionMotive(goal);
		double achievementMotiveValue  = createAchievementMotive(goal);
	}
}
