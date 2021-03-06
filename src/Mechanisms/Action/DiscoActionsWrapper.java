package Mechanisms.Action;

import GUI.AMCFrame;
import Mechanisms.Collaboration.Collaboration;
import MentalState.Goal;
import MetaInformation.MentalProcesses;
import edu.wpi.cetask.DecompositionClass;
import edu.wpi.cetask.Plan;
import edu.wpi.cetask.Task;
import edu.wpi.cetask.TaskClass.Input;
import edu.wpi.disco.Agenda.Plugin;
import edu.wpi.disco.Agent;
import edu.wpi.disco.Interaction;
import edu.wpi.disco.lang.Accept;
import edu.wpi.disco.lang.Ask;
import edu.wpi.disco.lang.Mention;
import edu.wpi.disco.lang.Propose;
import edu.wpi.disco.lang.Reject;
import edu.wpi.disco.lang.Say;
import edu.wpi.disco.lang.Utterance;

public class DiscoActionsWrapper {
	
	private Collaboration collaboration;
	private AMCFrame frame;
	
	public DiscoActionsWrapper(MentalProcesses mentalProcesses, AMCFrame frame) {
		this.collaboration = mentalProcesses.getCollaborationMechanism();
		this.frame = frame;
	}
	
	public AMCFrame getFrame() {
		return this.frame;
	}
	
	public void proposeTaskWho(Goal goal, boolean speaker) {
		
		// NOTE: Speaker should be false when the agent is doing coping.
		
		Plan plan = goal.getPlan();
		
		if (plan.isPrimitive()) {
			Utterance taskToPropose = new Propose.Who(collaboration.getDisco(), speaker, plan.getGoal(), !speaker);
			collaboration.getDisco().getInteraction().occurred(speaker, taskToPropose, plan);
		}
		else
			throw new IllegalArgumentException("Both of the collaborators are responsible for non-primitives!");
	}

	public void proposeTaskWho(Plan plan, boolean speaker, boolean listener) {
		
		if (plan.isPrimitive()) {
			Utterance taskToPropose = new Propose.Who(collaboration.getDisco(), speaker, plan.getGoal(), listener);
			collaboration.getDisco().getInteraction().occurred(speaker, taskToPropose, plan);
		}
		else
			throw new IllegalArgumentException("Both of the collaborators are responsible for non-primitives!");
	}
	
	public void proposeTaskWho(Plan plan, boolean speaker, boolean listener, Plan contributes) {
		
		if (plan.isPrimitive()) {
			Utterance taskToPropose = new Propose.Who(collaboration.getDisco(), speaker, plan.getGoal(), listener);
			collaboration.getDisco().getInteraction().occurred(speaker, taskToPropose, contributes);
			
			String robotUtterance = taskToPropose.format().substring(taskToPropose.format().indexOf("\"")+1, taskToPropose.format().length()-1);
			frame.getPanel().say(robotUtterance);
//			((JTextArea)frame.getPanel().getComponent("robotUtteranceTextArea")).setText(robotUtterance);
		}
		else
			throw new IllegalArgumentException("Both of the collaborators are responsible for non-primitives!");
	}
	
	// This method is only used to append a new string to the already existing output string.
	public void proposeTaskShould(Goal goal) {
		
		Plan plan = goal.getPlan();
		
		Utterance taskToPropose = Propose.Should.newInstance(collaboration.getDisco(), false, plan.getGoal());
		collaboration.getDisco().getInteraction().occurred(false, taskToPropose, plan);

		String robotUtterance = taskToPropose.format().substring(taskToPropose.format().indexOf("\"")+1, taskToPropose.format().length()-1);
		frame.getPanel().say(robotUtterance);
//		((JTextArea)frame.getPanel().getComponent("robotUtteranceTextArea")).append(robotUtterance);
	}

	public void proposeTaskShould(Goal goal, boolean speaker) {
		
		Plan plan = goal.getPlan();
		
		Utterance taskToPropose = Propose.Should.newInstance(collaboration.getDisco(), speaker, plan.getGoal());
		collaboration.getDisco().getInteraction().occurred(speaker, taskToPropose, plan);

		String robotUtterance = taskToPropose.format().substring(taskToPropose.format().indexOf("\"")+1, taskToPropose.format().length()-1);
		frame.getPanel().say(robotUtterance);
//		((JTextArea)frame.getPanel().getComponent("robotUtteranceTextArea")).setText(robotUtterance);
	}

	public void proposeTaskShould(Plan plan, boolean speaker) {
		
		Utterance taskToPropose = Propose.Should.newInstance(collaboration.getDisco(), speaker, plan.getGoal());
		collaboration.getDisco().getInteraction().occurred(speaker, taskToPropose, plan);
		
		String robotUtterance = taskToPropose.format().substring(taskToPropose.format().indexOf("\"")+1,taskToPropose.format().length()-1);
		frame.getPanel().say(robotUtterance);
//		((JTextArea)frame.getPanel().getComponent("robotUtteranceTextArea")).setText(robotUtterance);
	}
	
	public void proposeTaskWhat(Goal goal, boolean speaker, String slot, Object value) {
		
		Plan plan = goal.getPlan();
		
		Utterance taskToPropose = new Propose.What(collaboration.getDisco(), speaker, plan.getGoal(), slot, value);
		collaboration.getDisco().getInteraction().occurred(speaker, taskToPropose, plan);
	}
	
	public void proposeTaskHow(Goal goal, boolean speaker, DecompositionClass decomp) {
		
		Plan plan = goal.getPlan();
		
		if (!plan.isPrimitive()) {
			Utterance taskToPropose = new Propose.How(collaboration.getDisco(), speaker, plan, decomp);
			collaboration.getDisco().getInteraction().occurred(speaker, taskToPropose, plan);
		}
		else
			throw new IllegalArgumentException("Primitives does not need recepies!");
	}
	
	public void acceptProposedTask(Plan plan, boolean speaker) {
		
		Utterance acceptance = new Accept(collaboration.getDisco(), speaker, Propose.Should.newInstance(collaboration.getDisco(), speaker, plan.getGoal()));
		collaboration.getDisco().getInteraction().occurred(speaker, acceptance, null);
	}
	
	public void acceptProposedTask(Task task, boolean speaker) {
		
		collaboration.getDisco().getInteraction().occurred(speaker, task, null);
	}
	
	public void rejectProposedTask(Goal goal, boolean speaker) {
		
		Plan plan = goal.getPlan();
		
		Utterance rejection = new Reject(collaboration.getDisco(), speaker, Propose.Should.newInstance(collaboration.getDisco(), speaker, plan.getGoal()));
		collaboration.getDisco().getInteraction().occurred(speaker, rejection, null);
	}
	
	public void mentionTask(Goal goal, boolean speaker) {
		
		Plan plan = goal.getPlan();
		
		Utterance taskToMention = new Mention(collaboration.getDisco(), speaker, plan.getGoal(), "Just to mention!");
		collaboration.getDisco().getInteraction().occurred(speaker, taskToMention, plan);
	}
	
	public void saySomethingAboutTask(boolean speaker, String utterance) {
		
		Utterance utteranceTask = new Say.Agent(collaboration.getDisco(), utterance);
		collaboration.getDisco().getInteraction().occurred(speaker, utteranceTask, null);
		
		String robotUtterance = utteranceTask.format().substring(utteranceTask.format().indexOf("\"")+1,utteranceTask.format().length()-1);
		frame.getPanel().say(robotUtterance);
//		((JTextArea)frame.getPanel().getComponent("robotUtteranceTextArea")).setText(robotUtterance);
	}

	public void askAboutTaskWho(Goal goal, boolean speaker) {
		
		Plan plan = goal.getPlan();
		
		if (plan.isPrimitive()) {
			Utterance taskToAsk = new Ask.Who(collaboration.getDisco(), speaker, plan.getGoal());
			collaboration.getDisco().getInteraction().occurred(speaker, taskToAsk, plan);
		}
		else
			throw new IllegalArgumentException("Both of the collaborators are responsible for non-primitives");
	}
	
	public void askAboutTaskShould(Goal goal, boolean speaker) {
		
		Plan plan = goal.getPlan();
		
		Utterance taskToAsk = new Ask.Should(collaboration.getDisco(), speaker, plan.getGoal());
		collaboration.getDisco().getInteraction().occurred(speaker, taskToAsk, plan);
	}
	
	public void askAboutTaskWhat(Goal goal, boolean speaker, Input input) {
		
		Plan plan = goal.getPlan();
		
		Utterance taskToAsk = new Ask.What(collaboration.getDisco(), speaker, plan.getGoal(), input.getName());
		collaboration.getDisco().getInteraction().occurred(speaker, taskToAsk, plan);
	}
	
	public void askAboutTaskHow(Goal goal, boolean speaker) {
		
		Plan plan = goal.getPlan();
		
		if (!plan.isPrimitive()) {
			Utterance taskToAsk = new Ask.How(collaboration.getDisco(), speaker, plan.getGoal());
			collaboration.getDisco().getInteraction().occurred(speaker, taskToAsk, plan);
		}
		else
			throw new IllegalArgumentException("Primitives does not need recepies!");
	}

	public void executeTask(Goal goal, boolean actor, boolean postconditionStatus) {
		
		Plan plan = goal.getPlan();
		
		if (plan.isPrimitive()) {
			plan.getGoal().setSuccess(postconditionStatus);
			collaboration.getDisco().getInteraction().occurred(actor, plan.getGoal(), plan);
			
			if (plan.getGoal() instanceof Utterance)
				((Agent)collaboration.getDisco().getInteraction().getSystem()).say(collaboration.getDisco().getInteraction(), (Utterance)plan.getGoal());
		}
		else {
			proposeTaskShould(goal, false);
		}
	}
	
	public void executeTask(Plan plan, boolean actor, boolean postconditionStatus) {
		
		if (plan.isPrimitive()) {
			plan.getGoal().setSuccess(postconditionStatus);
			collaboration.getDisco().getInteraction().occurred(actor, plan.getGoal(), plan);
		} 
		else
			proposeTaskShould(plan, false);
	}
	
	public void executeTask() {
		
		Interaction interaction = collaboration.getInteraction();
		Plugin.Item item = interaction.getSystem().generateBest(interaction);
		collaboration.getDisco().getInteraction().occurred(false, item.task, item.contributes);
	}
}
