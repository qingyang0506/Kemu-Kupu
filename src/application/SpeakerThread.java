package application;

/**
 * 
 * This class uses two constructors, one used for English words and another for
 * Māori. One constructor sets english to true so when the SpeakerThread is run
 * it executes the if english = true part of the code. Otherwise it calls
 * Speaker.java with the right inputs and that class speaks out the word.
 *
 */
public class SpeakerThread extends Thread {

	String wordToSpeak;
	boolean english;
	double speed;

	public SpeakerThread(String wordToSay) {
		wordToSpeak = wordToSay;
		english = true;
	}

	// constructor for Māori words. basically if this class is passed the
	// slidernumber from the quiz scene
	// then it knows the word is Māori.
	public SpeakerThread(double slidernumber, String wordToSay) {
		english = false;
		speed = slidernumber;
		wordToSpeak = wordToSay;
	}

	@Override
	public void run() {

		if (english) {
			try {
				// using the festival command
				String command = "echo \"" + wordToSpeak + "\" | festival --tts";
				ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
				Process process = pb.start();
				process.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

			Speaker.speakerCurrentWord(speed, wordToSpeak);
		}

	}
}