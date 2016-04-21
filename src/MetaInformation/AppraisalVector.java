package MetaInformation;

import Mechanisms.Appraisal.Controllability.CONTROLLABILITY;
import Mechanisms.Appraisal.Desirability.DESIRABILITY;
import Mechanisms.Appraisal.Expectedness.EXPECTEDNESS;
import Mechanisms.Appraisal.Relevance.RELEVANCE;

public class AppraisalVector {
	private RELEVANCE relevanceEstimate;
	private DESIRABILITY desirabilityEstimate;
	private EXPECTEDNESS expectednessEstimate;
	private CONTROLLABILITY controllabilityEstimate;
	
	public AppraisalVector() {
		this.relevanceEstimate = RELEVANCE.UNKNOWN;
		this.desirabilityEstimate = DESIRABILITY.UNKNOWN;
		this.expectednessEstimate = EXPECTEDNESS.UNKNOWN;
		this.controllabilityEstimate = CONTROLLABILITY.UNKNOWN;
	}
	
	public void setRelevanceValue(RELEVANCE relevance) {
		this.relevanceEstimate = relevance;
	}
	
	public void setDesirabilityValue(DESIRABILITY desirability) {
		this.desirabilityEstimate = desirability;
	}
	
	public void setExpectednessValue(EXPECTEDNESS expectedness) {
		this.expectednessEstimate = expectedness;
	}
	
	public void setControllabilityValue(CONTROLLABILITY controllability) {
		this.controllabilityEstimate = controllability;
	}
	
	public AppraisalVector getAppraisalVector() {
		return this;
	}
}
