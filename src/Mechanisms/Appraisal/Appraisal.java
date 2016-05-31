package Mechanisms.Appraisal;

import Mechanisms.Mechanisms;
import Mechanisms.Appraisal.Controllability.CONTROLLABILITY;
import Mechanisms.Appraisal.Desirability.DESIRABILITY;
import Mechanisms.Appraisal.Expectedness.EXPECTEDNESS;
import Mechanisms.Appraisal.Relevance.RELEVANCE;
import MentalState.Goal;
import MetaInformation.AppraisalVector;
import MetaInformation.Turns;
import MetaInformation.AppraisalVector.APPRAISAL_TYPE;
import MetaInformation.MentalProcesses;

public class Appraisal extends Mechanisms {

	public Appraisal() { }
	
	public void prepareAppraisal(MentalProcesses mentalProcesses) {
		this.mentalProcesses = mentalProcesses;
	}
	
	public AppraisalVector doAppraisal(Turns turn, Goal recognizedGoal, APPRAISAL_TYPE appraisalType) {
		
		AppraisalVector appraisalVector = new AppraisalVector(mentalProcesses, recognizedGoal, appraisalType);
		
		RELEVANCE relevanceValue = mentalProcesses.getRelevanceProcess().isEventRelevant(recognizedGoal);
		appraisalVector.setRelevanceValue(relevanceValue);
		CONTROLLABILITY controllabilityValue = mentalProcesses.getControllabilityProcess().isEventControllable(recognizedGoal);
		appraisalVector.setControllabilityValue(controllabilityValue);
		DESIRABILITY desirabilityValue = mentalProcesses.getDesirabilityProcess().isEventDesirable(recognizedGoal);
		appraisalVector.setDesirabilityValue(desirabilityValue);
		EXPECTEDNESS expectednessValue = mentalProcesses.getExpectednessProcess().isEventExpected(recognizedGoal);
		appraisalVector.setExpectednessValue(expectednessValue);
		
//		mentalProcesses.getCollaborationMechanism().getWhoseAppraisal(recognizedGoal.getPlan())
		turn.setTurnAppraisals(mentalProcesses, recognizedGoal, appraisalType,
				appraisalVector.getRelevanceSymbolicValue(), appraisalVector.getDesirabilitySymbolicValue(), appraisalVector.getControllabilitySymbolicValue(), 
				appraisalVector.getExpectednessSymbolicValue());
		
//		for(AppraisalVector vector : Turns.getInstance().getCurrentAppraisalVectors()) {
//			System.out.println(vector.getTurnNumber() + ", " + vector.getAppraisalType() + ", " + vector.getRelevanceSymbolicValue() + ", " + 
//					vector.getDesirabilitySymbolicValue() + ", " + vector.getExpectednessSymbolicValue() + ", " + 
//					vector.getControllabilitySymbolicValue());
//			System.out.println("EMOTION INSTANCE: " + vector.getEmotionInstance());
//		}
		
		return appraisalVector;
	}
	
	// The return value can be computed according to the emotional status of the human.
	protected double getHumanEmotionalThreshold() { return 0.0; }
}
