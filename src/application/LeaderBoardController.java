package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LeaderBoardController {

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	Label RankLabel;

	// fills leaderboard with user names and their scores and ranks
	public void displayLeaderBoard(String str) {
		RankLabel.setText(str);
	}

	// returns to main menu used by button
	public void returnMain(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
		root = loader.load();

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
