import Mechanisms.Collaboration.Collaboration;
import MetaInformation.MentalProcesses;
import MetaInformation.World;
import edu.wpi.disco.Disco;

public class AffectiveMotivationalCollaborationFramework_EI {

	private static MentalProcesses mentalProcesses;
	private static World world;
	
	public static void main(String[] args) {
		
		mentalProcesses = new MentalProcesses(args, false);
		world = new World(mentalProcesses);
		Collaboration collaboration = mentalProcesses.getCollaborationMechanism();
		Disco disco = collaboration.getDisco();
		
		collaboration.setCollaborationWorld(world);
		collaboration.getWorld().setUserValence(0.0);
		
		disco.getInteraction().setOk(false);
		
		collaboration.getInteraction().start(true);
	}

}
