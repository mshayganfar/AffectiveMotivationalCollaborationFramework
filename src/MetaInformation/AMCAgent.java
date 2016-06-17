package MetaInformation;

import javax.swing.JTextArea;

import GUI.AMCFrame;
import edu.wpi.disco.Actor;
import edu.wpi.disco.Agenda;
import edu.wpi.disco.Agent;
import edu.wpi.disco.Interaction;
import edu.wpi.disco.lang.Utterance;
import edu.wpi.disco.plugin.AuthorizedPlugin;

public class AMCAgent extends Agent{

	MentalProcesses mentalProcesses;
	private AMCFrame frame;
	
	public AMCAgent(String name, Actor who, AMCFrame frame) {
//		super(name, new Agenda(who));
		super(name);
		this.frame = frame;
	}
	
	public void prepareAgent(MentalProcesses mentalProcesses) {
		this.mentalProcesses = mentalProcesses;
	}
	
	@Override
	public void init () {
		new AuthorizedPlugin(agenda, 225);
	}

	@Override
	protected boolean synchronizedRespond(Interaction interaction, boolean ok, boolean guess) {
		throw new IllegalStateException();
	}
	
	@Override
	public void say (Interaction interaction, Utterance utterance) {
		
		String robotUtterance = utterance.format().substring(utterance.format().indexOf("\"")+1, utterance.format().length()-1);
		((JTextArea)frame.getPanel().getComponent("robotUtteranceTextArea")).setText(robotUtterance);
	}
}
