package MetaInformation;

import edu.wpi.disco.User;
import edu.wpi.disco.plugin.AuthorizedPlugin;

public class AMCUser extends User{

	MentalProcesses mentalProcesses;
	
	public AMCUser(String name) {
		super(name);
	}
	
	@Override
	public void init () {
		new AuthorizedPlugin(agenda, 225);
	}
	
	public void prepareUser(MentalProcesses mentalProcesses) {
		this.mentalProcesses = mentalProcesses;
	}
}
