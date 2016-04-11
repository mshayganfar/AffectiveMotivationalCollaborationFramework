package Mechanisms.Motivation;

import java.util.ArrayList;
import java.util.Collections;

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
		
		for (int i = 0 ; i < Turns.getInstance().getDesirabilities().size() ; i++) {
			desirabilityValues.add(Turns.getInstance().getDesirabilityValue(Turns.getInstance().getDesirabilities().get(i)));
		}
		
		return desirabilityValues;
	}
	
	public double getSatisfactionDriveValue() {
		
		double satisfactionValue = 0.0;
		
		ArrayList<Double> weights = getDesirabilityWeights();
		ArrayList<Double> desirabilityValues = getDesirabilityValues();
		
		for (int i = 0 ; i < Turns.getInstance().getTurnNumber() ; i++)
			satisfactionValue += weights.get(i) * desirabilityValues.get(i);
		
		return satisfactionValue;
	}
	
	public double getSatisfactionDriveDelta() {
		
		return getSatisfactionDriveValue() - getPrevSatisfactionDriveValue();
	}
	
	public double getPrevSatisfactionDriveValue() {
		return prevSatisfactionValue;
	}
	
	public void updatePrevSatisfactionDriveValue(double satisfactionValue) {
		prevSatisfactionValue = satisfactionValue;
	}
}
