package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			var label = new Label("00:00");
			label.setAlignment(Pos.CENTER);

			String[] buttonLabel = {"10min", "1min", "10sec", "RESET", "STOP", "START"};
			var button = new Button[6];
			for(int i = 0; i < button.length; i++) {
				button[i] = new Button(buttonLabel[i]);
			}

			var gridPane = new GridPane();
			for(int i = 0; i < 2; i++) {
				for(int j = 0; j < 3; j++) {
					gridPane.add(button[i * 3 + j], j, i);
				}
			}

			var vBox = new VBox(label, gridPane);

			var borderPane = new BorderPane();
			borderPane.setCenter(vBox);

			var scene = new Scene(borderPane, 400, 400);

			primaryStage.setScene(scene);
			primaryStage.setTitle("Timer");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
