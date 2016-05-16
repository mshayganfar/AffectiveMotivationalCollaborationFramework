package Mechanisms.Motivation;

import java.util.ArrayList;
import java.util.Collections;

import MetaInformation.AppraisalVector.WHOSE_APPRAISAL;
import MetaInformation.AppraisalVector;
import MetaInformation.Turns;

public class SatisfactionDrive {

	private static double prevSatisfactionValue = 0.0;
	
	private static SatisfactionDrive satisfactionDrive = new SatisfactionDrive();
	
	private Turns turn;
	
	public void setDriveTurn(Turns turn) {
		this.turn = turn;
	}
	
	public Turns getDriveTurn() {
		return this.turn;
	}
	
	public static SatisfactionDrive getInstance() {
		return satisfactionDrive;
	}
	
	private ArrayList<Double> getDesirabilityWeights(int turnNumber) {
		
		ArrayList<Double> weights = new ArrayList<Double>();
		
		for (int i = 0 ; i < turnNumber ; i++) {
			weights.add(Math.pow(0.5, i));
		}
		
		Collections.sort(weights);
		
		return weights;
	}
	
	private double getDesirabilityWeightsSum(int turnNumber) {
		
		double weightSum = 0.0;
		
		ArrayList<Double> weights = getDesirabilityWeights(turnNumber);
		
		for (int i = 0 ; i < turnNumber ; i++)
			weightSum += Math.pow(0.5, i);
		
		return weightSum;
	}
	
	private ArrayList<Double> getDesirabilityValues() {
		
		ArrayList<Double> desirabilityValues = new ArrayList<Double>();
		
		Turns currentTurn = Turns.getInstance();
		
		for (int i = 1 ; i <= currentTurn.getTurnNumber() ; i++) {
			ArrayList<AppraisalVector> turnAppraisalVectors = currentTurn.getTurnAppraisalVectors(i, WHOSE_APPRAISAL.SELF);
			if (turnAppraisalVectors != null)
				desirabilityValues.add(currentTurn.getTurnOverallDesirabilityValue(turnAppraisalVectors));
			else
				return null;
			
			turnAppraisalVectors.clear();
		}
		
		return desirabilityValues;
	}
	
	public double getSatisfactionDriveValue() {
		
		double satisfactionValue = 0.0;
		
		ArrayList<Double> weights = getDesirabilityWeights(Turns.getInstance().getTurnNumber());
		
		if (getDesirabilityValues() != null) {
			ArrayList<Double> desirabilityValues = getDesirabilityValues();
			
			for (int i = 0 ; i < Turns.getInstance().getTurnNumber() ; i++)
				satisfactionValue += weights.get(i) * desirabilityValues.get(i);
			
			return satisfactionValue;
		}
		else
			return prevSatisfactionValue;
	}
	
	public double getSatisfactionDriveDelta() {
		
		double satValue = getSatisfactionDriveValue();
		double satPrevValue = getPrevSatisfactionDriveValue();
		
		prevSatisfactionValue = satValue;
		
		double currentWeightSum = getDesirabilityWeightsSum(Turns.getInstance().getTurnNumber());
		double previousWeightSum = (getDesirabilityWeightsSum(Turns.getInstance().getTurnNumber()-1) == 0) ? 1.0 :
								   getDesirabilityWeightsSum(Turns.getInstance().getTurnNumber()-1);
		
		return (((double)satValue/currentWeightSum) - ((double)satPrevValue/previousWeightSum));
	}
	
	private double getPrevSatisfactionDriveValue() {
		return prevSatisfactionValue;
	}
	
	public void updatePrevSatisfactionDriveValue(double satisfactionValue) {
		prevSatisfactionValue = satisfactionValue;
	}
}
