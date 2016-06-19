package MetaInformation;

import java.awt.TextArea;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import GUI.AMCFrame;
import edu.wpi.disco.Actor;
import edu.wpi.disco.Agenda;
import edu.wpi.disco.Agent;
import edu.wpi.disco.Interaction;
import edu.wpi.disco.lang.Utterance;
import edu.wpi.disco.plugin.AuthorizedPlugin;
import edu.wpi.disco.plugin.ProposeShouldOtherPlugin;

public class DiscoAgent extends Agent{

	private MentalProcesses mentalProcesses;
	private AMCFrame frame;
	
	public DiscoAgent (String name, Actor who, AMCFrame frame) {
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
		new ProposeShouldOtherPlugin(agenda, 75);
	}

//	@Override
//	protected boolean synchronizedRespond(Interaction interaction, boolean ok, boolean guess) {
//		throw new IllegalStateException();
//	}
	
	@Override
	public void say (Interaction interaction, Utterance utterance) {
		
		String robotUtterance = utterance.format().substring(utterance.format().indexOf("\"")+1, utterance.format().length()-1);
		frame.getPanel().say(robotUtterance);
	}
}
