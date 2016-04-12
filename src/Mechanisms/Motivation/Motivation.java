package Mechanisms.Motivation;

import Mechanisms.Mechanisms;
import Mechanisms.Appraisal.Controllability;
import Mechanisms.Appraisal.Expectedness;
import Mechanisms.ToM.ToM;
import MentalState.Goal;
import MentalState.Motive;
import MetaInformation.Turns;

public class Motivation extends Mechanisms {
	
	private ToM tom;
	private Controllability controllability;
	private Expectedness expectedness;
	
	private Motive externalMotive;
	private Motive achievementMotive;
	private Motive satisfactionMotive;
	
	private SatisfactionDrive satisfactionDrive;
	
	public Motivation(ToM tom, Controllability controllability, Expectedness expectedness) {
		 satisfactionDrive = new SatisfactionDrive();
		 this.tom  			  = tom;
		 this.controllability = controllability;
		 this.expectedness    = expectedness;
	}
	
	public double getSatisfactionMotiveValue() {
		
		double firstGradient  = 4.0;
		double secondGradient = 10.0;
		
		double valence  = tom.getValenceValue();
		double satDelta = satisfactionDrive.getSatisfactionDriveDelta();
		
		double firstSigmoidValue  = (double)1 / (1 + Math.exp(firstGradient * (valence - satDelta)));
		double secondSigmoidValue = (double)1 / (1 + Math.exp(secondGradient * (valence - satDelta)));
		
		double satMotiveValue = firstSigmoidValue - secondSigmoidValue;
		
		return satMotiveValue;
	}
	
	public double getAchievementMotiveValue(Goal goal) {
		
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
		}
		else {
			firstSigmoidValue  = (double)1 / (1 + Math.exp(-firstGradient * (valence - successProbability)));
			secondSigmoidValue = (double)1 / (1 + Math.exp(-secondGradient * (valence - successProbability)));
		}
		
		double achMotiveValue = firstSigmoidValue - secondSigmoidValue;
		
		return achMotiveValue;
	}
}
