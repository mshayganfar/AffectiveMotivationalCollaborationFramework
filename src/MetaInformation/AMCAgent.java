package MetaInformation;

import edu.wpi.disco.Actor;
import edu.wpi.disco.Agenda;
import edu.wpi.disco.Agent;
import edu.wpi.disco.Interaction;
import edu.wpi.disco.plugin.AuthorizedPlugin;

public class AMCAgent extends Agent{

	MentalProcesses mentalProcesses;
	
	public AMCAgent(String name, Actor who) {
//		super(name, new Agenda(who));
		super(name);
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
}
