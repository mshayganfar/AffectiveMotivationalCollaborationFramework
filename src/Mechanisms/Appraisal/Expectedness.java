package Mechanisms.Appraisal;

import Mechanisms.Collaboration.Collaboration;
import MentalState.Goal;
import edu.wpi.disco.Disco;

public class Expectedness extends AppraisalProcesses{
	
	public enum EXPECTEDNESS {MOST_EXPECTED, EXPECTED, LESS_EXPECTED, LESS_UNEXPECTED, UNEXPECTED, MOST_UNEXPECTED};
	
	public Expectedness(Collaboration collaboration) {
		this.collaboration = collaboration;
	}

	public EXPECTEDNESS isEventExpected(Goal eventGoal) {

		Disco disco = collaboration.getDisco();
		
		if (disco.getSegment().isInterruption()) {
			if (disco.getTop(disco.getFocus()).getType().isPathFrom(eventGoal.getPlan().getType()))
				return EXPECTEDNESS.UNEXPECTED;
			else
				return EXPECTEDNESS.MOST_UNEXPECTED;
		}
		else {
			if (!disco.isLastShift())
				return EXPECTEDNESS.MOST_EXPECTED;
			else
				return EXPECTEDNESS.EXPECTED;
		}
		
	}
}
