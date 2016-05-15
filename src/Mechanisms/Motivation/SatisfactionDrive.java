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
	
	private ArrayList<Double> getDesirabilityWeights() {
		
		double weightSum = 0.0;
		
		ArrayList<Double> weights = new ArrayList<Double>();
		
		for (int i = 0 ; i < Turns.getInstance().getTurnNumber() ; i++) {
			weights.add(Math.pow(0.5, i));
			weightSum += Math.pow(0.5, i);
		}
		
		for (int i = 0 ; i < weights.size() ; i++) {
			weights.set(i, weights.get(i)/weightSum);
		}
		
		Collections.sort(weights);
		
		return weights;
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
		
		ArrayList<Double> weights = getDesirabilityWeights();
		
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
		
		return (satValue - satPrevValue);
	}
	
	private double getPrevSatisfactionDriveValue() {
		return prevSatisfactionValue;
	}
	
	public void updatePrevSatisfactionDriveValue(double satisfactionValue) {
		prevSatisfactionValue = satisfactionValue;
	}
}
