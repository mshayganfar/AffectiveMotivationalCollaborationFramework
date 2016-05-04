package Mechanisms.Action;

import Mechanisms.Mechanisms;
import Mechanisms.Appraisal.Coping;
import MentalState.Goal;
import MentalState.Intention;
import MetaInformation.MentalProcesses;
import MetaInformation.Turns;
import MetaInformation.CopingElicitationFrame.COPING_STRATEGY;

public class Action extends Mechanisms{

	private Coping coping;
	
	private Goal goal;
	private DiscoActionsWrapper discoActionsWrapper;
	
	public Action(Goal goal) {
		
		this.goal = goal;
		discoActionsWrapper  = new DiscoActionsWrapper(collaboration);
	}
	
	public void prepareCopingMechanism(MentalProcesses mentalProcesses) {
		this.coping = mentalProcesses.getCopingMechanism();
	}
	
	public void act(Goal goal) {
		
		System.out.println("Agent chose the following coping strtegies in turn: " + Turns.getInstance().getTurnNumber());
		
		for (Intention intention : goal.getIntentions()) {
			if (intention.getCopingStrategy().equals(COPING_STRATEGY.SEEKING_SOCIAL_SUPPORT_FOR_INSTRUMENTAL_REASONS))
				coping.doSeekingSocialSupportForInstrumentalReasons();
			if (intention.getCopingStrategy().equals(COPING_STRATEGY.ACTIVE_COPING))
				coping.doActiveCoping(goal);
			if (intention.getCopingStrategy().equals(COPING_STRATEGY.PLANNING))
				coping.doPlanning(goal, false);
			if (intention.getCopingStrategy().equals(COPING_STRATEGY.WISHFUL_THINKING))
				coping.doWishfulThinking();
			if (intention.getCopingStrategy().equals(COPING_STRATEGY.MENTAL_DISENGAGEMENT))
				coping.doMentalDisengagement();
			if (intention.getCopingStrategy().equals(COPING_STRATEGY.SHIFTING_RESPONSIBILITY))
				coping.doShiftingResponsibility();
			if (intention.getCopingStrategy().equals(COPING_STRATEGY.ACCEPTANCE))
				coping.doAcceptance(goal, false);
		}
	}
	
	public DiscoActionsWrapper getDiscoActions() {
		return this.discoActionsWrapper;
	}
	
	public void executeDomainAction(DomainAction action) {
		action.run();
	}
	
	public void say(String utterance) {
		System.out.println(utterance);
	}
	
	public void ask(String utterance) {
		System.out.println(utterance);
	}
}
