import javax.swing.Spring;
import javax.xml.parsers.ParserConfigurationException;

import marytts.MaryInterface;
import marytts.exceptions.*;
import marytts.util.data.audio.*;
import marytts.LocalMaryInterface;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import marytts.util.dom.DomUtils;

import java.io.IOException;
import java.io.Reader;
import javax.sound.sampled.*;

public class Voice {
	private MaryInterface marytts;
	private AudioPlayer ap;
	
	public Voice(String voiceName)
	{	
		
		try 
		{
			marytts = new LocalMaryInterface();
		} 
		catch (MaryConfigurationException e) 
		{
			e.printStackTrace();
		}
	
		System.out.println(marytts.getAvailableVoices());
		
		marytts.setVoice(voiceName);
		System.out.println(marytts.getAudioEffects());
				
		
	}
	
	public void say (String input) 
	{
		try
		{
			AudioInputStream audioStream = marytts.generateAudio(input);		
			AudioFormat format = audioStream.getFormat();			
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);			 
			SourceDataLine audioLine = (SourceDataLine) AudioSystem.getLine(info);
			
			audioLine.open(format);		
			audioLine.start();
			
			int BUFFER_SIZE = 4096;
			 
			byte[] bytesBuffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			 
			while ((bytesRead = audioStream.read(bytesBuffer)) != -1) 
			{
			    audioLine.write(bytesBuffer, 0, bytesRead);
			}
			
			audioLine.drain();
			audioLine.close();
			audioStream.close();
			
			Thread.sleep(600);
		}
		catch(SynthesisException ex)
		{
			System.err.println("Error saying phrase.");
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	String inputAsXML = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<maryxml xmlns=\"http://mary.dfki.de/2002/MaryXML\" version=\"0.5\" xml:lang=\"en-US\">" +
			"<p>%1$s %2$s"
			+ "</prosody></p></maryxml>", input);
	
	private String getEmotionSettings(String emotion)
	{
		if(emotion.equals("angry"))
		{
			return "<prosody contour=\"(0%,+0st)(100%,-0st)\" pitch=\"+24%\" rate=\"+40%\">";
		}
		else if(emotion.equals("sad"))
		{
			return "<prosody contour=\"(0%, -2st)(60%,-2st)\" pitch=\"-5%\" rate=\"-5%\">";
		}
		else if(emotion.equals("happy"))
		{
			return "<prosody contour=\"(0%,0st)(100%,0st)\" pitch=\"+5%\" rate=\"+120%\">";
		}
		else if(emotion.equals("nervous"))
		{
			return "<prosody contour=\"(0%,-7st)(100%,+7st)\" pitch=\"+24%\" rate=\"+28%\">";
		}
		else return "<prosody>";
	}
	*/
	
	
}
