package Mechanisms.Perception;

public class Perception{
	
	public static enum EMOTION {ANGRY, WORRIED, HAPPY, NEUTRAL};
	
	private static Perception perception = new Perception();
	
	private static double emotionValence;
	
	private Perception() {
		emotionValence = 0.0;
	}
	
	public static Perception getInstance() {
		return perception;
	}
	
	public static double perceiveEmotion(EMOTION emotion, double intensity) {

		return (emotion.equals(EMOTION.HAPPY)) ? intensity : (emotion.equals(EMOTION.NEUTRAL)) ? 0 : (-1)*intensity;
	}
	
	public static double getEmotionValence() { return emotionValence; }
}