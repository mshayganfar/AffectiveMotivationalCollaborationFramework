package Mechanisms.Appraisal;

import Mechanisms.Mechanisms;
import MentalState.Goal;
import MetaInformation.MentalProcesses;
import edu.wpi.disco.Disco;

public class Expectedness extends Mechanisms{
	
	public enum EXPECTEDNESS {MOST_EXPECTED, EXPECTED, LESS_EXPECTED, LESS_UNEXPECTED, UNEXPECTED, MOST_UNEXPECTED, UNKNOWN};
	
	public Expectedness(MentalProcesses mentalProcesses) {
		this.collaboration = mentalProcesses.getCollaborationMechanism();
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
//		else if (disco.getLastOccurrence().isUnexplained()) {
//		if (disco.getTop(disco.getFocus()).getType().isPathFrom(eventGoal.getPlan().getType()))
//			return EXPECTEDNESS.UNEXPECTED;
//		else
//			return EXPECTEDNESS.MOST_UNEXPECTED;
	}
}
