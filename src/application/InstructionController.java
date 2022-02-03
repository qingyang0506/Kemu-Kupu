package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class InstructionController implements Initializable {

	@FXML
	Label label;

	private static Stage newStage = new Stage();

	// using a popup stage to show instructions
	// used by question mark in top right corner of games module screen
	public static void showInstruction(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(InstructionController.class.getResource("instruction.fxml"));

		Scene scene = new Scene(root);

		newStage.setScene(scene);
		newStage.show();
	}

	// used by question mark in top right corner of practice quiz screen
	public static void showPractiseInstruction(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(InstructionController.class.getResource("PractiseInstruction.fxml"));

		Scene scene = new Scene(root);

		newStage.setScene(scene);
		newStage.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		label.setText("1.click start button to start quiz\n" + "2.The speaker icons is play back buttons.\n"
				+ "3.The tick is submit buttons\n" + "4.Behind the tick is the don't know button\n"
				+ "   to skip the word\n" + "5.Macron:click macron button and enter the\n"
				+ "  character on keyboard or press the left Alt\n" + "  and character at same time");

	}

}
