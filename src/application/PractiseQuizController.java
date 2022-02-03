package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PractiseQuizController implements Initializable {

	@FXML
	public Button macronButton;
	@FXML
	public Button myMacronE;
	@FXML
	public Button myMacronI;
	@FXML
	public Button myMacronO;
	@FXML
	public Button myMacronU;

	@FXML
	private Label wordCount;
	@FXML
	private TextField userInput;
	@FXML
	private Button submit;
	@FXML
	private Button start;

	@FXML
	private Label message;
	@FXML
	private Label hint;

	@FXML
	private Slider slider;

	@FXML
	private Button skip;

	@FXML
	private Label defaultSpeed;

	private Stage stage;
	private Scene scene;
	private Parent root;

	// boolean to check if start button is pressed;
	private static boolean started = false;

	private static boolean next = false;

	// the word that the user enters
	private static String wordInput;
	// the currentWord that is being tested
	private static String currentWord;
	// keeping track of which word we are up to:
	private static int currentWordCount = 1;
	// counter used in the quiz to check how many attempts the user took
	private static int incorrectCount = 0;
	// number of words tested
	private static int testWordCount = 5;
	// whether macrons should be expanded to the right. False when they are already
	// expanded.
	public boolean moveRight = true;
	public ArrayList<Button> macronButtons = new ArrayList<Button>();
	// whether the user is on the last word
	public boolean lastWord = false;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// add macron buttons to arrayList
		macronButtons.add(myMacronE);
		macronButtons.add(myMacronI);
		macronButtons.add(myMacronO);
		macronButtons.add(myMacronU);

		// skip button only visible after correct spelling is revealed
		skip.setVisible(false);

		// check slider number constantly
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				// get stretch value from slider by using between 50 and 100 meaning 100% speed
				// to 200% speed and
				// range from 50 to 0 meaning 100% speed to 50%
				double festivalSpeed = slider.getValue();
				double stretchDouble = 1;

				// reusing same formula from speaker thread
				// "NewValue = (((OldValue - OldMin) * (NewMax - NewMin)) / (OldMax - OldMin)) +
				// NewMin"
				if (festivalSpeed > 50) { // new range 1 to 2
					stretchDouble = 1 - ((((100 - festivalSpeed) - 50) * 1) / 50);
				}

				else { // stretch new range 1 to 0.5
					stretchDouble = 1 - (((100 - festivalSpeed - 50) * 0.5) / 50);
				}

				double roundedSpeed = Math.round(stretchDouble * 100);
				String str = Double.toString(roundedSpeed);
				str.replace(".", "");

				String result = null;
				// converts string to make it appear divided by 100.
				// ie 135 is converted to 1.35 by taking first character of string and
				// last two and putting . in the middle.
				// slightly different method for numbers less than 100.
				if (roundedSpeed >= 100) {
					result = str.substring(0, 1) + "." + str.substring(1, 2);
				} else {
					result = "0." + str.substring(0, 1);
				}

				defaultSpeed.setText(result);

			}

		});
	}

	// each of these addMacron methods are tied to the respective button.
	// they append their respective macron to the text and then retract all the
	// macrons with "moveleft()"
	// puts focus back to end of text field so user doesnt have to click a lot.
	public void addMacronE() {

		userInput.appendText("ē");
		moveLeft();
		userInput.requestFocus();
		userInput.appendText("");
	}

	public void addMacronI() {

		userInput.appendText("ī");
		moveLeft();
		userInput.requestFocus();
		userInput.appendText("");
	}

	public void addMacronO() {

		userInput.appendText("ō");
		moveLeft();
		userInput.requestFocus();
		userInput.appendText("");
	}

	public void addMacronU() {

		userInput.appendText("ū");
		moveLeft();
		userInput.requestFocus();
		userInput.appendText("");
	}

	public void moveLeft() {
		// Each macron button Transitons to the left.
		// the distance each moves depends on how far away it already was.
		// for loop is used and distance increases each loop

		int distance = 57;
		for (Button macbut : macronButtons) {
			macbut.setDisable(true);

			TranslateTransition translate = new TranslateTransition();
			translate.setNode(macbut);
			translate.setDuration(Duration.millis(300));
			translate.setCycleCount(1);
			// buttons move negative distance
			translate.setByX(-distance);
			translate.setAutoReverse(false);
			translate.play();
			distance = distance + 57;

		}
		// now the code will move right next time the main macron button "ā" is pressed.
		moveRight = true;
	}

	// used by macron button to enable macrons
	public void macronEnable() throws InterruptedException {

		if (moveRight) {

			macronButton.setDisable(true);

			// same logic as moving to left except distance moved is now positive
			int distance = 57;
			for (Button macbut : macronButtons) {

				TranslateTransition translate = new TranslateTransition();
				translate.setNode(macbut);
				translate.setDuration(Duration.millis(300));
				translate.setCycleCount(1);
				translate.setByX(distance);
				translate.setAutoReverse(false);
				translate.play();
				distance = distance + 57;
				// enable buttons after a pause
				PauseTransition pt = new PauseTransition(Duration.millis(400));
				pt.setOnFinished(event1 -> {
					macronButton.setDisable(false);
					macbut.setDisable(false);
				});
				pt.play();

			}
			moveRight = false;

		} else {
			// if macrons are already expanded then append "ā" and retract with moveLeft()
			userInput.appendText("ā");
			macronButton.setDisable(true);
			moveLeft();
			userInput.requestFocus();
			userInput.appendText("");

			// pause for a moment to stop errors
			PauseTransition pt = new PauseTransition(Duration.millis(400));
			pt.setOnFinished(event1 -> {
				macronButton.setDisable(false);
			});
			pt.play();

		}
	}

	@FXML // continuously check for key combination
	private void handleOnKeyPressed(KeyEvent event) {

		// keycombinations for Alt + vowel
		KeyCombination altA = new KeyCodeCombination(KeyCode.A, KeyCodeCombination.ALT_DOWN);
		KeyCombination altE = new KeyCodeCombination(KeyCode.E, KeyCodeCombination.ALT_DOWN);
		KeyCombination altI = new KeyCodeCombination(KeyCode.I, KeyCodeCombination.ALT_DOWN);
		KeyCombination altO = new KeyCodeCombination(KeyCode.O, KeyCodeCombination.ALT_DOWN);
		KeyCombination altU = new KeyCodeCombination(KeyCode.U, KeyCodeCombination.ALT_DOWN);

		// enter is treated as pressing enter
		if (event.getCode().equals(KeyCode.ENTER)) {
			submit.fire();
		}
		// if key combination is alt+A is observed then
		// append ā to text field
		// same logic for other vowels
		else if (altA.match(event)) {
			userInput.appendText("ā");

		} else if (altE.match(event)) {
			userInput.appendText("ē");

		} else if (altI.match(event)) {
			userInput.appendText("ī");

		} else if (altO.match(event)) {
			userInput.appendText("ō");

		} else if (altU.match(event)) {
			userInput.appendText("ū");

		}

	}

	public String secondLetter(String word) {
		try {
			char letter = word.charAt(1);
			return Character.toString(letter);

		} catch (Exception e) {
			return "no letter found";

		}

	}

	// method that shows users some letters when they get the first attempt wrong
	public String addLetters(String word) {
		// the min length words is 2
		// show the second letter and if the word is longer than 3 letters then show a
		// second letter
		int letters = word.length();

		// else pick 2 random letters
		// random 2 numbers between the length of the word
		Random random = new Random();
		int letter2 = random.nextInt(letters - 0) + 0;
		while (letter2 == 1) {
			random = new Random();
			letter2 = random.nextInt(letters - 0) + 0;
		}
		System.out.println(letter2);
		String dashIndicator = "";
		for (int i = 0; i < letters; i++) {

			char letter = word.charAt(i);
			// System.out.println(letter);

			if (i == 1) {
				dashIndicator = dashIndicator + " " + letter;
			}

			else {
				// showing user where the spaces and dashes are
				if (letter == ' ') {
					if (i == letter2) {
						// grab next letter if its a space
						dashIndicator = dashIndicator + " " + word.charAt(letter2 + 1);
					} else {
						dashIndicator = dashIndicator + " " + " ";
					}

				} else if (letter == '-') {
					if (i == letter2) {
						// grab next letter if its a hyphen
						dashIndicator = dashIndicator + " " + word.charAt(letter2 + 1);
					} else {
						dashIndicator = dashIndicator + " " + "-";
					}

				} else {
					if (i == letter2) {
						// put in the second letter
						dashIndicator = dashIndicator + " " + letter;
					} else {
						dashIndicator = dashIndicator + " " + "_";
					}

				}
			}

		}

		System.out.println(dashIndicator);
		return dashIndicator;

	}

	// method that checks the number of letters in the current word being tested for
	// the hint
	public String numberOfLetters(String word) {
		int letters = word.length();
		System.out.println(word.length());

		String dashIndicator = "_";
		// print dashs to indicate the number of letters? like hangman
		for (int i = 1; i < letters; i++) {
			char letter = word.charAt(i);

			// showing user where the spaces and dashes are
			if (letter == ' ') {
				dashIndicator = dashIndicator + " " + " ";
			} else if (letter == '-') {
				dashIndicator = dashIndicator + " " + "-";
			} else {
				dashIndicator = dashIndicator + " " + "_";
			}

		}

		System.out.println(dashIndicator);
		return dashIndicator;
	}

	// help button give some instruction for user
	public void Help(MouseEvent event) throws IOException {
		InstructionController.showPractiseInstruction(event);
	}

	// makes file which will contain 5 words used for individual test.
	public void makeQuizFile() {
		try {
			File myObj = new File("src/textFile/pracQuiz.txt");
			if (myObj.createNewFile()) {
				// used for code writing and debugging
				System.out.println("File created: " + myObj.getName());
			} else {
				// used for code writing and debugging
				System.out.println("new quiz file already exists");
			}
		} catch (IOException e) {
			System.err.println("New quiz file failed to be created");
			e.printStackTrace();
		}
	}

	// methods called when user gets answer correct
	// says correct and updates necessary fields and labels
	public void correct(ActionEvent event) throws IOException {

		speakCorrect();

		// finished -set incorrectCount back to zero for next word
		incorrectCount = 0;

		testWordCount--;

		// move onto next word if that word wasn't the last word
		if (currentWordCount != 5) {
			currentWordCount++;

			// change the label telling the user which word they are up to
			start.setText("Word " + currentWordCount + " of 5");

			// update the current word:
			assignCurrentWord(nextWord(currentWordCount));

			// then say the next word:
			speakCurrentWord();

			// show indication of how many letters are in that word
			hint.setWrapText(true);
			hint.setText(numberOfLetters(currentWord));
		}

		if (lastWord) {
			finishQuiz(event);
		}

		if (currentWordCount == 5) {
			lastWord = true;
		}

	}

	// method called when user gets incorrect second try
	// says incorrect and updates labels and fields necessary
	public void incorrectSecondTry() {
		// say incorrect once
		speakIncorrect();

		message.setWrapText(true);
		message.setText("The correct spelling is " + currentWord);

		testWordCount--;
		next = true;
		skip.setVisible(true);
		submit.setVisible(false);

	}

	// method called when user gets incorrect first try
	// says incorrect and gives hint
	public void incorrectFirstTry() {
		// say incorrect when the user gets it wrong on the first trial
		speakIncorrect();
		incorrectCount++;

		// need to give user a hint
		// hint.setText("The second letter of the word is: " +
		// secondLetter(currentWord));
		hint.setWrapText(true);
		hint.setText(addLetters(currentWord));

	}

	// methods called when quiz is finished
	public void finishQuiz(ActionEvent event) throws IOException {
		System.out.println("finished quiz");

		currentWordCount = 1;

		started = false;

		// delete quiz file for new game
		deleteFile();

		// and set the word Count label back to 1 of 5:
		start.setText("Word 1 of 5");

		// go to reward scene
		try {
			// delay for 1.5 s.
			Thread.sleep(1500);
		} catch (Exception e) {
			System.out.println(e);
		}

		// gets reward scene ready
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PractiseScoreScene.fxml"));
		root = loader.load();

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	// set all button disable condition
	public void setAllButton(Boolean disable) {
		skip.setDisable(disable);
		submit.setDisable(disable);
	}

	// this method runs the quiz as the user hits the submit button
	public void submit(ActionEvent event) throws IOException {

		// check if the start button has been pressed:
		if (started == false) {
			// if it hasn't, notify the user

			Alert errorAlert = new Alert(AlertType.INFORMATION); // window pops up to remind user to press start before
																	// pressing submit
			errorAlert.setTitle("Press Start");
			errorAlert.setHeaderText("You need to press start first.");
			errorAlert.showAndWait();
		}

		else {

			// all button set disable for 2 second
			setAllButton(true);

			PauseTransition pt = new PauseTransition(Duration.millis(2000));
			pt.setOnFinished(event1 -> {
				setAllButton(false);
			});
			pt.play();

			if (!next) {

				// clear the encouraging message
				message.setText("");
				// grab the user's input
				wordInput = userInput.getText();

				start.setText("Word " + currentWordCount + " of 5");

				// if user gets it right, (first or second attempt)
				if ((wordInput.equalsIgnoreCase(currentWord))) {
					correct(event);
				}

				// or if user got it wrong:
				else {
					// if this was the second trail and they got it incorrect again
					if (incorrectCount == 1) {
						incorrectSecondTry();
					}

					// else when the user gets the word incorrect on the first trial
					else {
						incorrectFirstTry();
					}

				}
			}

			// clear userinput
			userInput.setText("");

		}

	}

	// when the user presses the play back word button
	public void sayWordAgain(ActionEvent event) {
		if (started) {
			speakCurrentWord();
		} else {
			// if it hasn't, notify the user

			Alert errorAlert = new Alert(AlertType.INFORMATION); // window pops up to remind user to press start before
																	// pressing submit
			errorAlert.setTitle("Press Start");
			errorAlert.setHeaderText("You need to press start first.");
			errorAlert.showAndWait();
		}
	}

	// method to skip a word when the don't know button is pressed
	public void skipWord(ActionEvent event) throws IOException {

		// check if the start button has been pressed:
		if (started) {

			// all button set disable for 2 second
			setAllButton(true);

			PauseTransition pt = new PauseTransition(Duration.millis(2000));
			pt.setOnFinished(event1 -> {
				setAllButton(false);
			});
			pt.play();

			if (next) {
				testWordCount--;
				// clear textfield text and correct answer
				userInput.setText("");
				message.setText("");
				next = false;
				skip.setVisible(false);
				submit.setVisible(true);

				// move onto next word if that word wasn't the last word
				if (currentWordCount != 5) {

					currentWordCount++;

					start.setText("Word " + currentWordCount + " of 5");

					// update the current word:
					assignCurrentWord(nextWord(currentWordCount));

					// then say the next word:
					speakCurrentWord();

					// show indication of how many letters are in that word
					hint.setWrapText(true);
					hint.setText(numberOfLetters(currentWord));
				} // finished -set incorrectCount back to zero for next word
				incorrectCount = 0;

				// when the user is on the last word and they had submitted their answer
				if ((lastWord) && (testWordCount != 1)) {
					finishQuiz(event);
				}

				if (currentWordCount == 5) {
					lastWord = true;
				}
			}

		} else {
			// if it hasn't, notify the user

			Alert errorAlert = new Alert(AlertType.INFORMATION); // window pops up to remind user to press start before
																	// pressing submit
			errorAlert.setTitle("Press Start");
			errorAlert.setHeaderText("You need to press start first.");
			errorAlert.showAndWait();
		}
	}

	// method to put 5 random words in the newQuiz file
	public void putFiveWords() {

		// then read the wordList to put all the words into an array to pick random
		// words

		// empty the newQuiz.txt before put 5 random words
		FileWriter fW = null;
		try {
			fW = new FileWriter("src/textFile/pracQuiz.txt");
			fW.write("");
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				fW.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		// puts words from topic file into an arraylist
		List<String> words = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader("src/words/AllWords"))) {
			String word;
			while ((word = reader.readLine()) != null) {
				words.add(word);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// random sort the arrayList to get random words
		Collections.shuffle(words);

		// putting the words into the file
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("src/textFile/pracQuiz.txt", true));
			PrintWriter pw = new PrintWriter(bw);
			for (int i = 0; i < 5; i++) {
				pw.print(words.get(i) + "\n");
			}
			pw.close();
		} catch (IOException e) {
			// used for debugging
			System.out.println("Error in putting the words into the file");
			e.printStackTrace();
		}
	}

	// method that assigns the currentword:
	public void assignCurrentWord(String word) {
		currentWord = word;
	}

	// This method checks if the start button is pressed
	public void checkStart(ActionEvent event) {
		// prevent the start button from creating a new quiz in the middle of a game
		if (started) {
			return;
		}
		System.out.println("start button is pressed");
		started = true;

		// set start button text to word 1 of 5
		start.setText("Word 1 of 5");

		// reset the testWordsCount to 5
		testWordCount = 5;

		// make the submit button text become submit if it is please press the start
		// button first
		submit.setText(" Check");
		// didn't return boolean instead because scenebuilder actions only allows void
		// methods
		// return started = true;
		makeQuizFile();
		putFiveWords();

		// start the quiz by first grabbing the first word:
		try {
			@SuppressWarnings("resource")
			BufferedReader brTest = new BufferedReader(new FileReader("src/textFile/pracQuiz.txt"));
			assignCurrentWord(brTest.readLine());
		} catch (Exception e) {
			System.out.println("error is grabbing first word and assigning it to currentword");
			e.printStackTrace();
		}

		// speak word
		Speaker.speakerCurrentWord(slider.getValue(), currentWord);

		// show indication of how many letters are in that word
		hint.setText(numberOfLetters(currentWord));

	}

	// this method says the currentword to the user
	public void speakCurrentWord() {

		Speaker.speakerCurrentWord(slider.getValue(), currentWord);
	}

	// method used to say correct out loud when user gets it correct
	public void speakCorrect() {

		// using the festival command
		try {
			String command = "echo \"correct\" | festival --tts";
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
			Process process = pb.start();
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// method used to say incorrect only once when the user spells it incorrectly
	public void speakIncorrect() {

		try {
			// using the festival command
			String command = "echo \"incorrect \" | festival --tts";
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
			Process process = pb.start();
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// method to grab the next word in the newQuiz file and return that word:
	// to be used with assign current word:
	public String nextWord(int currentWordCount) {

		hint.setText("");

		String nextWord = "";
		try {
			try (BufferedReader brTest = new BufferedReader(new FileReader("src/textFile/pracQuiz.txt"))) {
				brTest.readLine(); // skip first word
				if (currentWordCount == 2) {
					nextWord = brTest.readLine();
				} else if (currentWordCount == 3) {
					brTest.readLine(); // skip second line as well
					nextWord = brTest.readLine();
				} else if (currentWordCount == 4) {
					brTest.readLine(); // skip second line as well
					brTest.readLine(); // skip third line as well
					nextWord = brTest.readLine();
				} else if (currentWordCount == 5) {
					brTest.readLine(); // skip second line as well
					brTest.readLine(); // skip third line as well
					brTest.readLine(); // skip forth line as well
					nextWord = brTest.readLine();
				}
			}
		} catch (Exception e) {
			// used for debugging
			System.out.println("Error in grabbing the next word");
			e.printStackTrace();
		}

		return nextWord;
	}

	// after the quiz is complete, delete the newQuiz list
	public void deleteFile() {

		try {
			File file = new File("src/textFile/pracQuiz.txt");
			// if the file is deleted:
			if (file.delete()) {
				// used for code writing and debugging:
				System.out.println(file.getName() + "deleted");
			} else {
				// used for debugging
				System.out.println("failed to delete file");
			}
		} catch (Exception e) {
			// used for debugging
			System.out.println("Error in deleting quiz file");
			e.printStackTrace();
		}
	}

	// return main menu
	public void mainMenu(ActionEvent event) {
		started = false;
		try {
			root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
