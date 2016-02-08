package Mechanisms.Perception;

public class Perception{
	
	public enum EMOTION {ANGRY, WORRIED, HAPPY, NEUTRAL};
	
	private double emotionValence;
	
	public Perception() {
		emotionValence = 0.0;
	}
	
	public double perceiveEmotion(EMOTION emotion, double intensity) {

		return (emotion.equals(EMOTION.HAPPY)) ? intensity : (emotion.equals(EMOTION.NEUTRAL)) ? 0 : (-1)*intensity;
	}
	
	public double getEmotionValence() { return this.emotionValence; }
}