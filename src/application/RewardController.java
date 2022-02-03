package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class RewardController {

	@FXML
	Label scoreLabel;
	@FXML
	TextField userName;

	@FXML
	Label correct1;
	@FXML
	Label correct2;
	@FXML
	Label correct3;
	@FXML
	Label correct4;
	@FXML
	Label correct5;

	@FXML
	Label word1;
	@FXML
	Label word2;
	@FXML
	Label word3;
	@FXML
	Label word4;
	@FXML
	Label word5;

	private Stage stage;
	private Scene scene;
	private Parent root;

	// display score in the reward scene when enter the reward scene.
	// This function will be called by the newQuizController when
	// reward screen is created.
	public void displayScore(int currentScore) {
		
		scoreLabel.setText(currentScore + "");
	}

	// returns to choose topic scene after pressing play again button
	public void playAgain(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("TopicScene.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}

	// return to new practice module after press play again button
	public void practiseAgain(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("PractiseModule.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}

	// returns to main menu
	public void mainMenu(ActionEvent event) {
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

	public void animation(MouseEvent event) throws IOException {
		AnimationController.showAniamtion(event);
	}

	@FXML
	Button enter;

	// set this textfield to allow 15 characters at max
	public void setTextLimit(TextField textfield, int maxLength) {
		textfield.setOnKeyTyped(event -> {
			String string = textfield.getText();

			if (string.length() > maxLength) {
				textfield.setText(string.substring(0, maxLength));
				textfield.positionCaret(string.length());
			}
		});
	}

	// enter the user name and put their name and score into leaderBoard.txt
	public void EnterName(ActionEvent event) {
		// get the user name
		String str = userName.getText();
		str = str.replace(" ", "");
		// get the user score
		int score = NewQuizController.currentScore;

		String format = "%-20s%s%n";

		BufferedWriter bw = null;
		PrintWriter pw = null;
		try {
			bw = new BufferedWriter(new FileWriter("src/textFile/leaderBoard.txt", true));
			pw = new PrintWriter(bw);
			if (!str.equals("")) {
				pw.printf(format, str, score + "");
				enter.setDisable(true);
			} else {
				Alert errorAlert = new Alert(AlertType.INFORMATION);
				errorAlert.setTitle("Name");
				errorAlert.setHeaderText("Please enter your name");
				errorAlert.showAndWait();
			}

		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				bw.close();
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
