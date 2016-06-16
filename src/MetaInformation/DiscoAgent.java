package MetaInformation;

import edu.wpi.disco.Actor;
import edu.wpi.disco.Agenda;
import edu.wpi.disco.Agent;
import edu.wpi.disco.Interaction;
import edu.wpi.disco.lang.Utterance;
import edu.wpi.disco.plugin.AuthorizedPlugin;
import edu.wpi.disco.plugin.ProposeShouldOtherPlugin;

public class DiscoAgent extends Agent{

	MentalProcesses mentalProcesses;
	
	public DiscoAgent (String name, Actor who) {
//		super(name, new Agenda(who));
		super(name);
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
		
		// I should set the robot's text area here.
		System.out.println(utterance);
	}
}
