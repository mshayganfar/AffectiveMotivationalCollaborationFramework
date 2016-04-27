package Mechanisms.Appraisal;

import Mechanisms.Collaboration.Collaboration;
import MentalState.Goal;
import edu.wpi.cetask.Task;
import edu.wpi.disco.lang.Accept;
import edu.wpi.disco.lang.Ask;
import edu.wpi.disco.lang.Mention;
import edu.wpi.disco.lang.Propose;
import edu.wpi.disco.lang.Reject;

public class DiscoActionsWrapper {
	
	private Collaboration collaboration;
	
	public DiscoActionsWrapper(Collaboration collaboration) {
		this.collaboration = collaboration;
	}
	
	private void proposeTask(Goal goal, boolean human) {
		
		Task task = collaboration.getDisco().getTaskClass(goal.getPlan().getGoal().getType().toString()).newInstance();
		Task taskToPropose = new Propose.Who(collaboration.getDisco(), human, task, null);
		collaboration.getDisco().getInteraction().occurred(human, taskToPropose, null);
	}
	
	private void acceptProposedTask(Goal goal, boolean human) {
		
		Task proposedTask = new Accept(collaboration.getDisco(), human, (Propose)goal.getPlan().getGoal());
		collaboration.getDisco().getInteraction().occurred(human, proposedTask, null);
	}
	
	private void rejectProposedTask(Goal goal, boolean human) {
		
		Task proposedTask = new Reject(collaboration.getDisco(), human, (Propose)goal.getPlan().getGoal());
		collaboration.getDisco().getInteraction().occurred(human, proposedTask, null);
	}
	
	private void mentionTask(Goal goal, boolean human) {
		
		Task taskToMention = new Mention(collaboration.getDisco(), human, goal.getPlan().getGoal());
		collaboration.getDisco().getInteraction().occurred(human, taskToMention, null);
	}
	
	private void askAboutTask(Goal goal, boolean human) {
		
		Task task = collaboration.getDisco().getTaskClass(goal.getPlan().getGoal().getType().toString()).newInstance();
		Task taskToAsk = new Ask.Who(collaboration.getDisco(), human, task);
		collaboration.getDisco().getInteraction().occurred(human, taskToAsk, null);
	}
	
	private void executeTask(Goal goal, boolean human) {
		
		Task taskToExecute = collaboration.getDisco().getTaskClass(goal.getPlan().getGoal().getType().toString()).newInstance();
		collaboration.getDisco().getInteraction().occurred(human, taskToExecute, null);
	}
}
