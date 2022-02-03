package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Node;

public class TopicSceneController implements Initializable {

	@FXML
	private Button statsLabel;

	private Stage stage;
	private Scene scene;
	private Parent root;

	// set static field to record the topic
	private static String Topic;

	// get the Topic from newQuizController through call this method
	public static String getTopic() {
		return Topic;
	}

	@FXML
	private ChoiceBox<String> myChoiceBox;

	private String[] topics = { "Babies", "Colours", "Compass Points", "Days of the week 2", "Days of Week 1",
			"Engineering", "Feelings", "Months of the year", "Months of Year 1", "Software", "Uni life", "Weather",
			"Work" };

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		myChoiceBox.getItems().addAll(topics);

	}

	// switch to main menu
	public void switchToMain(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
		root = loader.load();

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	// method used to switch to topicQuiz scene
	public void switchToTopic1(ActionEvent event) {
		try {
			Topic = myChoiceBox.getValue();

			// if the player not choose topic will give a message to choose topic
			if (Topic == null) {
				Alert errorAlert = new Alert(AlertType.INFORMATION); // remind user to choose a topic if they didnt.
				errorAlert.setTitle("Topic");
				errorAlert.setHeaderText("Please select a topic for the quiz.");
				errorAlert.showAndWait();

			} else {

				FXMLLoader loader = new FXMLLoader(getClass().getResource("Topic1.fxml"));
				root = loader.load();

				NewQuizController newquiz = loader.getController();
				newquiz.displayName(Topic);

				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			}
		} catch (Exception e) {

		}

	}

}
