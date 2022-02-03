package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;

public class SceneController {

	@FXML
	private Button statsLabel;

	private Stage stage;
	private Scene scene;
	private Parent root;

	// method used to switch to newQuiz scene when button is pressed
	public void switchToTopic(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("TopicScene.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	// method used to switch to PractiseQuiz scene when button is pressed
	public void switchToPractise(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("PractiseModule.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	// formats strings so they look professional when presented in leaderboard
	public String makeFormat(String str) {
		if (str.length() > 11) {
			while (str.length() < 24) {
				str = str + " ";
			}
			return str + "\t";
		} else if (str.length() > 4) {
			while (str.length() < 28) {
				str = str + " ";
			}
			return str + "\t";
		} else {
			while (str.length() < 32) {
				str = str + " ";
			}
			return str + "\t";
		}
	}

	// make format for rank
	public String RankFormat(String str) {
		if (str.length() != 2) {
			return str + " ";
		}
		return str;
	}

	// method used to switch to leaderBoard scene when button is pressed
	public void switchToLeaderBoard(ActionEvent event) throws IOException {

		StringBuffer sb = new StringBuffer("Rank" + "\t\t" + "\t   Name" + "\t\t\t" + "      Score" + "\n");

		try (BufferedReader br = new BufferedReader(new FileReader("src/textFile/leaderBoard.txt"))) {
			String line;
			Map<String, Integer> hashmap = new HashMap<String, Integer>();
			// put each line and its' score into hashmap
			while ((line = br.readLine()) != null) {

				String[] strings = line.split("\\s+");
				int score = Integer.parseInt(strings[strings.length - 1]);
				hashmap.put(line, score);
			}

			List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>(hashmap.entrySet());
			// sort according to hashmap values
			Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {

				@Override
				public int compare(Map.Entry<String, Integer> arg0, Map.Entry<String, Integer> arg1) {
					
					return (arg1.getValue() - arg0.getValue());
				}
			});

			int count = 1;
			// if there is less than 8 users in the leaderboard
			if (hashmap.size() <= 10) {
				for (Map.Entry<String, Integer> newEntry : infoIds) {
					String[] strings = newEntry.getKey().toString().split("\\s+");
					sb.append("   " + RankFormat(count + "") + "  \t\t" + makeFormat(strings[0]) + "  \t" + strings[1]
							+ "\n");
					count++;
				}
			} else {

				int number = 1;
				// print first 8 names and scores
				for (Map.Entry<String, Integer> newEntry : infoIds) {
					if (number < 11) {
						String[] strings = newEntry.getKey().toString().split("\\s+");
						sb.append("   " + RankFormat(number + "") + "  \t\t" + makeFormat(strings[0]) + "  \t"
								+ strings[1] + "\n");
						number++;
					}
				}
			}
		} catch (NumberFormatException e) {
			
			e.printStackTrace();
		}

		String rank = sb.toString();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("LeaderBoard.fxml"));
		root = loader.load();


		LeaderBoardController LB = loader.getController();
		LB.displayLeaderBoard(rank);

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void switchToLeaderBoard1(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("LeaderBoard1.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	private Button logoutButton;
	@FXML
	private AnchorPane scenePane;

	// method used to quit and close the window
	// Used by "quit" button in the main menu
	public void quit(ActionEvent event) {
		stage = (Stage) scenePane.getScene().getWindow();
		System.out.println("you successful logged out");
		stage.close();
	}

}
