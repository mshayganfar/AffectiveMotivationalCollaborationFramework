import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Program 
{
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException 
	{
		//Voice voice = new Voice("cmu-slt-hsmm");
		//Voice voice = new Voice("cmu-bdl-hsmm");
		Voice voice = new Voice ("cmu-rms-hsmm");

		voice.say("Please give me the tool, okay!");
		voice.say("What is your name?");
	}
}
