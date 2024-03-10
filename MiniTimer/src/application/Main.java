package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {
	private static int minute = 0;
	private static int second = 0;

	String timeString() {
		String minuteString = (minute < 10) ? "0" + String.valueOf(minute) : String.valueOf(minute);
		String secondString = (second < 10) ? "0" + String.valueOf(second) : String.valueOf(second);
		return (minuteString + " : " + secondString);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			var label = new Label(timeString());
			label.setFont(new Font(50));

			String[] buttonLabel = {"10min", "1min", "10sec", "RESET", "STOP", "START"};
			var button = new Button[6];
			for(int i = 0; i < button.length; i++) {
				button[i] = new Button(buttonLabel[i]);
				button[i].setMinSize(60, 30);
			}
			// 10min
			button[0].setOnAction((actionEvent) -> {
				minute += 10;

				if(minute > 99) {
					minute -= 100;
				}

				label.setText(timeString());
			});
			// 1min
			button[1].setOnAction((actionEvent) -> {
				minute++;

				if(minute > 99) {
					minute -= 100;
				}

				label.setText(timeString());
			});
			// 10sec
			button[2].setOnAction((actionEvent) -> {
				second += 10;

				if(second > 59) {
					second -= 60;
				}

				label.setText(timeString());
			});
			// RESET
			button[3].setOnAction((actionEvent) -> {
				minute = 0;
				second = 0;
				label.setText(timeString());
			});
			// START
			button[5].setOnAction((actionEvent) -> {
				for(int i = 0; i < button.length; i++) {
					if(i == 4) {
						continue;
					}
					button[i].setDisable(true);
				}

				var alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("終了");
				alert.getDialogPane().setHeaderText("終了");

				var scheduledService = new ScheduledService<Boolean>() {
					int count = minute * 60 + second;

					@Override
					protected Task<Boolean> createTask() {
						var task = new Task<Boolean>() {
							@Override
							protected Boolean call() throws Exception {
								Platform.runLater(() -> {
									if(count == 0) {
										alert.show();
									}

									minute = count / 60;
									second = count % 60;
									label.setText(timeString());
								});

								Thread.sleep(1000L);

								count--;

								return true;
							}
						};

						if(count == 0) {
							for(int i = 0; i < button.length; i++) {
								if(i == 4) {
									continue;
								}
								button[i].setDisable(false);
							}

							minute = 0;
							second = 0;
							this.cancel();
						}

						return task;
					}
				};

				// STOP
				button[4].setOnAction((e) -> {
					for(int i = 0; i < button.length; i++) {
						if(i == 4) {
							continue;
						}
						button[i].setDisable(false);
					}

					minute = 0;
					second = 0;
					label.setText(timeString());
					scheduledService.cancel();
				});

				scheduledService.start();
			});

			var gridPane = new GridPane();
			gridPane.setAlignment(Pos.CENTER);
			for(int i = 0; i < 2; i++) {
				for(int j = 0; j < 3; j++) {
					gridPane.add(button[i * 3 + j], j, i);
				}
			}

			var vBox = new VBox(label, gridPane);
			vBox.setAlignment(Pos.CENTER);

			var borderPane = new BorderPane(vBox);

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
