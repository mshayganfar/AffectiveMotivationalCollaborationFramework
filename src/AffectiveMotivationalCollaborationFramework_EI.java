import GUI.AMCFrame;
import Mechanisms.Collaboration.Collaboration;
import MetaInformation.MentalProcesses;
import MetaInformation.ROSbridge;
import MetaInformation.Voice;
import MetaInformation.World;
import edu.wpi.disco.Disco;

import java.io.IOException;

import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class AffectiveMotivationalCollaborationFramework_EI {

	private static MentalProcesses mentalProcesses;
	private static World world;
	private static Voice voice;
	private static ROSbridge rosBridge;
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException{
		
		voice = new Voice ("cmu-rms-hsmm");
		rosBridge = new ROSbridge("130.215.28.106");
		
		AMCFrame frame = new AMCFrame("Affective Motivational Collaboration Framework", false);
		frame.pack();
		frame.setVisible(true);
		frame.getPanel().giveTurnToRobot();
		((JTextField)frame.getPanel().getComponent("robotEmotionTextField")).setText("NEUTRAL");
		
		mentalProcesses = new MentalProcesses(args, frame, false);
		
		frame.getPanel().setMentalProcesses(mentalProcesses);
		
		world = new World(mentalProcesses, frame);
		Collaboration collaboration = mentalProcesses.getCollaborationMechanism();
		Disco disco = collaboration.getDisco();
		
		collaboration.setCollaborationVoice(voice);
		collaboration.setCollaborationWorld(world);
		collaboration.setCollaborationROSbridge(rosBridge);
		collaboration.getWorld().setUserValence(0.0);
		
		try {
			rosBridge.rosConnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		disco.getInteraction().setOk(false);
		
		collaboration.getInteraction().start(true);
	}

}
