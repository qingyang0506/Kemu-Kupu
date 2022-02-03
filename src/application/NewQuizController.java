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
import java.util.ResourceBundle;

import javafx.util.Duration;
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
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;

public class NewQuizController implements Initializable {

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

	// inner = inside of clock ie the clockHand
	@FXML
	public ImageView myInner;
	@FXML
	public Label myTimer;
	@FXML
	private Label topicTitle;
	@FXML
	private Label wordCount;
	@FXML
	private TextField userInput;
	@FXML
	private Button submit;
	@FXML
	private Button start;
	@FXML
	private Label score;
	@FXML
	private Label message;
	@FXML
	private Label hint;
	@FXML
	private Button playBack;

	@FXML
	private Slider slider;

	@FXML
	private Button skip;

	@FXML
	private Label defaultSpeed;

	private Stage stage;
	private Scene scene;
	private Parent root;

	// the topic name get from the topicSceneController
	private static String topicname;

	// boolean to check if start button is pressed;
	private static boolean started = false;

	// the word that the user enters
	private static String wordInput;
	// the currentWord that is being tested
	private static String currentWord;
	// keeping track of which word we are up to:
	private static int currentWordCount = 1;
	// counter used in the quiz to check how many attempts the user took
	private static int incorrectCount = 0;

	private static int testWordCount = 5;

	public static int currentScore = 0;

	// Arraylist contains "Correct" "Incorrect" "Skipped" based on users answers
	public ArrayList<String> wordResults = new ArrayList<String>();
	// list of all words
	public ArrayList<String> wordList = new ArrayList<String>();
	public ArrayList<Button> macronButtons = new ArrayList<Button>();

	public static String dashIndicator = "_";

	// did tuser solve in less than 12 seconds?
	public boolean solvedInTime;
	public int time = 0;
	// whether macrons should be expanded to the right. False when they are already
	// expanded.
	public boolean moveRight = true;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		wordResults.clear();
		wordList.clear();
		// add macron buttons to arrayList
		macronButtons.add(myMacronE);
		macronButtons.add(myMacronI);
		macronButtons.add(myMacronO);
		macronButtons.add(myMacronU);

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

		int distance = 60;
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
			distance = distance + 60;

		}
		// now the code will move right next time the main macron button "ā" is pressed.
		moveRight = true;
	}

	// used by macron button to enable macrons
	public void macronEnable() throws InterruptedException {

		if (moveRight) {

			macronButton.setDisable(true);
			// hide encouraging message
			message.setText("");
			// same logic as moving to left except distance moved is now positive
			int distance = 60;
			for (Button macbut : macronButtons) {

				TranslateTransition translate = new TranslateTransition();
				translate.setNode(macbut);
				translate.setDuration(Duration.millis(300));
				translate.setCycleCount(1);
				translate.setByX(distance);
				translate.setAutoReverse(false);
				translate.play();
				distance = distance + 60;
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

	// following code is used to move clock and keep track of time passed
	RotateTransition rotate = new RotateTransition();

	Timeline countTime = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			time++;

			if (time == 2) {
				// 2 seconds is an estimate of how long it takes for the word to be spoken out
				// begin rotating clockhand (called my Inner)
				rotate.setNode(myInner);
				rotate.setDuration(Duration.millis(10000)); // duration 10 seconds
				rotate.setCycleCount(TranslateTransition.INDEFINITE);
				rotate.setInterpolator(Interpolator.LINEAR);
				rotate.setByAngle(360);
				rotate.setAxis(Rotate.Z_AXIS);
				rotate.play();

			}
			// once 10 seconds of clock transition and 2 seconds to speak the word
			// stop the time and stop rotation.
			else if (time == 12) {
				solvedInTime = false; // used later on to give an extra point or not
				rotate.stop();
				myInner.setRotate(45.5); // 45.5 rotation is when it is at 12 o clock.
				time = 0;
				countTime.stop();
			}

		}
	}));

	public void startTime() {
		time = 0;
		countTime.play();
		solvedInTime = true;

	}

	public void stopTime() {
		rotate.stop();
		countTime.stop();
		myInner.setRotate(45.5);
		time = 0;

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

	// method to add "correct" or other outcomes to the wordResults array
	public void addToWordResults(String result) {
		wordResults.add(result);
	}

	// adds words to spell to an array
	public void addToWordList(String word) {
		wordList.add(word);
	}

	// let the topic as this scene title
	public void displayName(String str) {
		topicTitle.setText(str);
	}

	// adds 1 to score and updates score label
	public void addScore() {
		currentScore++;

		// if user got it coorect on the first try then update score by 2:
		if (incorrectCount == 0) {
			currentScore++;
		}
		if (solvedInTime) {
			currentScore++;
		}
		score.setText(String.valueOf(currentScore));

	}

	public String secondLetter(String word) {
		try {
			char letter = word.charAt(1);
			// return Character.toString(letter);

			// putting the letter into the dashIndicator
			char[] letterIndicatorHint = dashIndicator.toCharArray();
			System.out.println(letterIndicatorHint);
			letterIndicatorHint[2] = letter;

			return String.valueOf(letterIndicatorHint);

		} catch (Exception e) {
			return "no letter found";

		}

	}

	// help button give some instruction for user
	public void Help(MouseEvent event) throws IOException {
		InstructionController.showInstruction(event);
	}

	// method that checks the number of letters in the current word being tested for
	// the hint
	public String numberOfLetters(String word) {
		int letters = word.length();
		int count = 1;
		System.out.println(word.length());

		dashIndicator = "_";
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
				count++;
			}

		}

		String wordLength = "There are " + count + " letters \n" + dashIndicator;
		return wordLength;
	}

	// makes file which will contain 5 words used for individual test.
	public void makeQuizFile() {
		try {
			File myObj = new File("src/textFile/newQuiz.txt");
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
	public void correct() throws InterruptedException {
		// add to wordResults array
		addToWordResults("Correct!");
		addToWordList(currentWord);
		speakCorrect();
		Thread.sleep(1500);
		// score needs to increase
		addScore();

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

			// show indication of how many letters there are
			hint.setWrapText(true);
			hint.setText(numberOfLetters(currentWord));
		}

	}

	// method called when user gets incorrect second try
	// says incorrect and updates labels and fields necessary
	public void incorrectSecondTry() throws InterruptedException {

		// addtowordResultsarray
		addToWordResults("Incorrect");
		addToWordList(currentWord);

		// say incorrect once
		speakIncorrect();
		Thread.sleep(1500);

		// give the user an encouraging message:
		pickMessage();

		testWordCount--;

		// move onto next word if that word wasn't the last word
		if (currentWordCount != 5) {
			currentWordCount++;

			start.setText("Word " + currentWordCount + " of 5");

			// update the current word:
			assignCurrentWord(nextWord(currentWordCount));

			// then say the next word:
			speakCurrentWord();

			hint.setWrapText(true);
			hint.setText(numberOfLetters(currentWord));
		}

		// finished -set incorrectCount back to zero for next word
		incorrectCount = 0;

	}

	// method called when user gets incorrect first try
	// says incorrect and gives hint
	public void incorrectFirstTry() throws InterruptedException {
		// say incorrect when the user gets it wrong on the first trial
		speakIncorrect();
		incorrectCount++;

		// need to give user a hint
		hint.setWrapText(true);
		hint.setText("Hint: \n" + secondLetter(currentWord));

	}

	// methods called when quiz is finished
	public void finishQuiz(ActionEvent event) throws IOException {
		stopTime();
		System.out.println("finished quiz");

		currentWordCount = 1;

		started = false;

		// delete quiz file for new game
		deleteFile("src/textFile/newQuiz.txt");
		deleteFile(".readout.scm"); // this file is made by speaker class

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
		FXMLLoader loader = new FXMLLoader(getClass().getResource("RewardScene.fxml"));
		root = loader.load();

		RewardController rewardcontroller = loader.getController();
		rewardcontroller.displayScore(currentScore); // gives current score from this controller to the
														// reward controller
		rewardcontroller.setTextLimit(rewardcontroller.userName, 15);// set the reward scene's inputname
		// textfield at most enter 15 character

		// change labels in reward scene to show word1: failed, word2 correct etc
		//
		// does this by changing labels word1 to word5 to the words tested (with first
		// letter being upper
		// case
		// then changes labels correct1 to correct5 "skipped" "correct" "incorrect" etc
		//
		rewardcontroller.word1.setText(wordList.get(0).substring(0, 1).toUpperCase() 
				+ wordList.get(0).substring(1) + ": ");
		rewardcontroller.correct1.setText(wordResults.get(0));

		rewardcontroller.word2.setText(wordList.get(1).substring(0, 1).toUpperCase() 
				+ wordList.get(1).substring(1) + ": ");
		rewardcontroller.correct2.setText(wordResults.get(1));

		rewardcontroller.word3.setText(wordList.get(2).substring(0, 1).toUpperCase() 
					+ wordList.get(2).substring(1) + ": ");
		rewardcontroller.correct3.setText(wordResults.get(2));

		rewardcontroller.word4.setText(wordList.get(3).substring(0, 1).toUpperCase() 
				+ wordList.get(3).substring(1) + ": ");
		rewardcontroller.correct4.setText(wordResults.get(3));

		rewardcontroller.word5.setText(wordList.get(4).substring(0, 1).toUpperCase() 
				+ wordList.get(4).substring(1) + ": ");
		rewardcontroller.correct5.setText(wordResults.get(4));

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	// set all button disable condition
	public void setAllButton(Boolean disable) {
		skip.setDisable(disable);
		submit.setDisable(disable);
		playBack.setDisable(disable);
	}

	// this method runs the quiz as the user hits the submit button
	public void submit(ActionEvent event) throws IOException, InterruptedException {

		if (started) {

			// all button set disable for 2 second
			setAllButton(true);

			PauseTransition pt = new PauseTransition(Duration.millis(2000));
			pt.setOnFinished(event1 -> {
				setAllButton(false);
			});
			pt.play();

			// clear the encouraging message
			message.setText("");
			// grab the user's input
			wordInput = userInput.getText();

			start.setText("Word " + currentWordCount + " of 5");

			// if user gets it right, (first or second attempt)
			if ((wordInput.equalsIgnoreCase(currentWord))) {
				stopTime();
				correct();
				startTime();

			}

			// or if user got it wrong:
			else {
				// if this was the second trail and they got it incorrect again
				if (incorrectCount == 1) {
					stopTime();
					incorrectSecondTry();
					startTime();

				}

				// else when the user gets the word incorrect on the first trial
				else {
					incorrectFirstTry();
				}

			}

			// clear userinput
			userInput.setText("");
		} else {
			// if it hasn't, notify the user

			Alert errorAlert = new Alert(AlertType.INFORMATION); // window pops up to remind user to press start before
																	// pressing submit
			errorAlert.setTitle("Press Start");
			errorAlert.setHeaderText("You need to press start first.");
			errorAlert.showAndWait();
		}

		// when the user is on the last word and they had submitted their answer
		if ((currentWordCount == 5) && (testWordCount != 1)) {
			finishQuiz(event);

		}
	}

	// when the user presses the play back word button
	public void sayWordAgain(ActionEvent event) throws InterruptedException {
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
	public void skipWord(ActionEvent event) throws IOException, InterruptedException {

		// check if the start button has been pressed:
		if (started) {

			stopTime();
			// all button set disable for 2 second
			setAllButton(true);
			PauseTransition pt = new PauseTransition(Duration.millis(2000));
			pt.setOnFinished(event1 -> {
				setAllButton(false);
			});
			pt.play();

			addToWordResults("Skipped");
			addToWordList(currentWord);
			// give the user an encouraging message:
			pickMessage();
			// checking if user is currently on the last word of the quiz:
			testWordCount--;
			// clear textfield text
			userInput.setText("");

			// move onto next word if that word wasn't the last word
			if (currentWordCount != 5) {
				currentWordCount++;

				start.setText("Word " + currentWordCount + " of 5");

				// update the current word:
				assignCurrentWord(nextWord(currentWordCount));

				// then say the next word:
				speakCurrentWord();

				hint.setWrapText(true);
				hint.setText(numberOfLetters(currentWord));
			}
			// finished -set incorrectCount back to zero for next word
			incorrectCount = 0;

			// when the user is on the last word and they had submitted their answer
			if ((currentWordCount == 5) && (testWordCount != 1)) {
				finishQuiz(event);
			}

		} else {
			// if it hasn't, notify the user

			Alert errorAlert = new Alert(AlertType.INFORMATION); // window pops up to remind user to press start before
																	// pressing submit
			errorAlert.setTitle("Press Start");
			errorAlert.setHeaderText("You need to press start first.");
			errorAlert.showAndWait();
		}
		startTime();
	}

	// method to pick a random encouraging message:
	public void pickMessage() {
		if (!moveRight) {
			moveLeft();
		}

		// empty the encouragingMessages.txt before put 5 random words
		FileWriter fW = null;
		try {
			fW = new FileWriter("src/textFile/pickMessage");
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
		// adds all encouraging messages from file into arraylist
		List<String> messages = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader("src/textFile/encouragingMessages"))) {
			String message;
			while ((message = reader.readLine()) != null) {
				messages.add(message);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// random sort the arrayList to get random words
		Collections.shuffle(messages);

		// rename the encouraging message label as the first message in the list:
		message.setText(messages.get(0));

	}

	// method to put 5 random words in the newQuiz file
	public void putFiveWords() {

		topicname = TopicSceneController.getTopic();
		// then read the wordList to put all the words into an array to pick random
		// words

		// empty the newQuiz.txt before put 5 random words
		FileWriter fW = null;
		try {
			fW = new FileWriter("src/textFile/newQuiz.txt");
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
		try (BufferedReader reader = new BufferedReader(new FileReader("src/words/" + topicname))) {
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
			BufferedWriter bw = new BufferedWriter(new FileWriter("src/textFile/newQuiz.txt", true));
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

	// This method checks if the start button is pressed and starts the quiz
	public void checkStart(ActionEvent event) throws InterruptedException {
		// prevent the start button from creating a new quiz in the middle of a game
		if (started) {
			return;
		}
		countTime.setCycleCount(Timeline.INDEFINITE);

		System.out.println("start button is pressed");
		started = true;

		// set start button text to word 1 of 5
		start.setText("Word 1 of 5");

		// reset the score to zero
		currentScore = 0;
		score.setText(0 + "");
		// reset the testWordsCount to 5
		testWordCount = 5;

		// make the submit button text become submit if it is please press the start
		// button first
		submit.setText(" Check");

		makeQuizFile();
		putFiveWords();

		// start the quiz by first grabbing the first word:
		try {
			try (BufferedReader brTest = new BufferedReader(new FileReader("src/textFile/newQuiz.txt"))) {
				assignCurrentWord(brTest.readLine());
			}
			hint.setWrapText(true);
			hint.setText(numberOfLetters(currentWord));
		} catch (Exception e) {
			System.out.println("error is grabbing first word and assigning it to currentword");
			e.printStackTrace();
		}

		startTime();
		// speak word
		speakCurrentWord();

	}

	// this method says the currentword to the user
	public void speakCurrentWord() throws InterruptedException {

		SpeakerThread thread1 = new SpeakerThread(slider.getValue(), currentWord);
		thread1.start();

	}

	// method used to say correct out loud when user gets it correct
	public void speakCorrect() throws InterruptedException {

		// using the festival command

		SpeakerThread thread1 = new SpeakerThread("Correct");
		thread1.start();

	}

	// method used to say incorrect only once when the user spells it incorrectly
	public void speakIncorrect() throws InterruptedException {

		SpeakerThread thread1 = new SpeakerThread("Incorrect");
		thread1.start();
	}

	// method to grab the next word in the newQuiz file and return that word:
	// to be used with assign current word:
	public String nextWord(int currentWordCount) {

		System.out.println();

		hint.setText("");

		String nextWord = "";
		try {
			try (BufferedReader brTest = new BufferedReader(new FileReader("src/textFile/newQuiz.txt"))) {
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
	public void deleteFile(String filename) {

		try {
			File file = new File(filename);
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
		try {
			started = false;
			currentWordCount = 1;

			// delete quiz file for new game
			deleteFile("src/textFile/newQuiz.txt");
			deleteFile(".readout.scm");

			// and set the word Count label back to 1 of 5:
			start.setText("Word 1 of 5");
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
