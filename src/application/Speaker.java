package application;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Speaker {

	// this method says the current word through festival.
	// gets stretch number for festival and removes hyphens from methods.
	// then implements these by passing a bash script through runBashScript method
	// that creates a scheme file and runs the scheme file.

	/**
	 * This method creates a bash command to run.
	 * 
	 * @param speed     is the slider number from the quiz scene
	 * @param wordToSay
	 */
	public static void speakerCurrentWord(double speed, String wordToSay) {
		String voice = "akl_mi_pk06_cg";

		String wordNoHyphens = getWordNoHyphens(wordToSay);

		Double stretchDouble = getStretch(speed);

		String stretch = String.valueOf(stretchDouble);

		// creating the bash script
		String bashcommand = "echo $'(voice_" + voice + ")\n" + "(Parameter.set \\'Duration_Stretch " + stretch + ")\n"
				+ "(SayText \"" + wordNoHyphens.toLowerCase() + "\") '>.readout.scm\n" + "festival -b .readout.scm\n";

		runBashScript(bashcommand);

	}

	public static String getWordNoHyphens(String word) {

		return word.replaceAll("[-]", " ");
	}

	// converts slider value (from 0 to 100) to a stretch number (2 is twice as
	// slow, 0.5 is fast) for festival
	public static double getStretch(double speed) {

		double stretchDouble = 1;

		// at max the speed can be 2 times and and minimum it can be 0.5 times speed.
		// if scale less than 50 then it should slow down so the stretch should be
		// between 1 and 2.
		// same idea for more than 50 with stretch between 1 and 0.5.
		// formula used to convert old scale to new scale:
		// NewValue = (((OldValue - OldMin) * (NewMax - NewMin)) / (OldMax - OldMin)) +
		// NewMin

		if (speed < 50) { // new range 1 to 2
			stretchDouble = 1 - ((((speed) - 50) * 1) / 50);
		}

		else { // stretch new range 1 to 0.5
			stretchDouble = 1 - (((speed - 50) * 0.5) / 50);
		}

		return stretchDouble;
	}

	public static void runBashScript(String cmd) {

		try {
			String command = cmd;

			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
			Process process = pb.start();

			BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			int exitStatus = process.waitFor();

			if (exitStatus == 1) {
				String line;
				while ((line = stderr.readLine()) != null) {

					System.err.println(line);
				}
				Alert errorAlert = new Alert(AlertType.ERROR); // if error then an alert to close program
				errorAlert.setHeaderText("Error encountered");
				errorAlert.setContentText("Error has occured. Please exit program.");
				errorAlert.showAndWait();
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}
}
