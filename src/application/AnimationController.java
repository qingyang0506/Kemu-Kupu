package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class AnimationController implements Initializable {

	@FXML
	private MediaView mediaView;

	private File file;
	private Media media;
	private MediaPlayer mediaPlayer;

	public static Stage newStage = new Stage();

	// using a popup stage to show animation
	public static void showAniamtion(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(AnimationController.class.getResource("animation.fxml"));

		Scene scene = new Scene(root);

		newStage.setResizable(false);
		newStage.setScene(scene);
		newStage.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		int score = NewQuizController.currentScore;

		// if the score >=10, play the reward animation, otherwise play encourage
		// animation. the max score is 15
		if (score >= 10) {
			file = new File("src/animation/Reward.mp4");
		} else {
			file = new File("src/animation/Encourage.mp4");
		}

		media = new Media(file.toURI().toString());
		mediaPlayer = new MediaPlayer(media);

		mediaPlayer.setAutoPlay(true);
		mediaView.setMediaPlayer(mediaPlayer);

	}

}
