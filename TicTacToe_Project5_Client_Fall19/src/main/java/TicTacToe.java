import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.input.MouseEvent;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TicTacToe extends Application {

	Scene startScene;
	TextField portNumber,ipAddr,c1;
	Label ip,port,title;
	Button clientChoice,b1;
	HBox buttonBox, portBox, ipBox;
	VBox clientBox;
	BorderPane startPane;
	ListView<String> listItems =  new ListView<String>();
	HashMap<String, Scene> sceneMap = new HashMap<String, Scene>();

	//play scene (scene #2)
	Text header, textChoose;
	ListView<String> listItems2;

	Label highScore1, highScore2, highScore3, cheese,displayWinner,loser;
	Label winner = new Label();
	Button playButton = new Button("Play");
	Button easyButton = new Button("Easy");
	Button mediumButton = new Button("Medium");
	Button hardButton = new Button("Expert");
	BorderPane choicePane;
	VBox choiceButtonBox;
	HBox choiceHBox, choicePaneBox;

	//GameScene (scene #3)
	Button gridButton1 = new Button();
	Button gridButton2 = new Button();
	Button gridButton3 = new Button();
	Button gridButton4 = new Button();
	Button gridButton5 = new Button();
	Button gridButton6 = new Button();
	Button gridButton7 = new Button();
	Button gridButton8 = new Button();
	Button gridButton9 = new Button();



	//Finish scene (scene #4)
	Label playAgain;
	Button playAgainButton = new Button("Play Again");
	HBox playAgainBox;
	VBox gameOverBox;

	Client clientConnection;
    ArrayList<Button> buttonList = new ArrayList<Button>();
	public static void main(String[] args) {
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Let's Play Tic Tac Toe!!!");

		this.clientChoice = new Button("Play!");
		this.clientChoice.setStyle("-fx-pref-width: 300px");

		port = new Label("Enter Port Number: ");
		ip = new Label("Enter IP address: ");
		title = new Label("Tic Tac Toe");
		title.setStyle("-fx-font: 18 arial;");
		portNumber = new TextField("1738");
		ipAddr = new TextField("127.0.0.1");
		portNumber.setMinWidth(80);
		portBox = new HBox(30,port, portNumber);
		ipBox = new HBox(30,ip,ipAddr);
		buttonBox = new HBox(clientChoice);
		VBox startBoxes = new VBox(20,title,portBox,ipBox,buttonBox);
		startPane = new BorderPane();
		startPane.setPadding(new Insets(70));
		startPane.setCenter(startBoxes);
		startScene = new Scene(startPane, 450,350);



		primaryStage.setScene(startScene);
		primaryStage.show();

		playButton.setOnAction(e-> {
			if(clientConnection.info.difficulty == "medium") {
				clientConnection.info.challengeRequest = true;
				clientConnection.send();

				primaryStage.setScene(sceneMap.get("Game"));
			}
			if(clientConnection.info.difficulty == "easy") {
				clientConnection.info.challengeRequest = true;
				clientConnection.send();

				primaryStage.setScene(sceneMap.get("Game"));
			}
			if(clientConnection.info.difficulty == "expert") {
				clientConnection.info.challengeRequest = true;
				clientConnection.send();

				primaryStage.setScene(sceneMap.get("Game"));
			}
			listItems.getItems().clear();
			listItems.getItems().add("Please select a difficulty");

		});

		//difficulty buttons
		easyButton.setOnAction(e-> {
			clientConnection.info.difficulty = "easy";
			listItems.getItems().clear();
			listItems.getItems().add("Difficulty set to easy");
			//clientConnection.send();

		});

		mediumButton.setOnAction(e-> {
			clientConnection.info.difficulty = "medium";
			listItems.getItems().clear();
			listItems.getItems().add("Difficulty set to medium");
			//clientConnection.send();

		});

		hardButton.setOnAction(e-> {
			clientConnection.info.difficulty = "expert";
			listItems.getItems().clear();
			listItems.getItems().add("Difficulty set to expert");
			//clientConnection.send();

		});

		//game board buttons
		gridButton1.setOnAction(e-> {
			clientConnection.info.move = 0;

			if(clientConnection.info.board.get(0) == -1){
				clientConnection.info.playMoveRequest = true;
			}
			//clientConnection.send();

			if(clientConnection.info.board.get(0) == 0){
				gridButton1.setText("O");
				clientConnection.info.playMoveRequest = false;
			}
			if(clientConnection.info.board.get(0) == 1){
				gridButton1.setText("X");
				clientConnection.info.playMoveRequest = false;
			}

			clientConnection.send();

		});

		gridButton2.setOnAction(e-> {
			clientConnection.info.move = 1;

			if(clientConnection.info.board.get(1) == -1){
				clientConnection.info.playMoveRequest = true;
			}
			//clientConnection.send();

			if(clientConnection.info.board.get(1) == 0){
				gridButton2.setText("O");
				clientConnection.info.playMoveRequest = false;
			}
			if(clientConnection.info.board.get(1) == 1){
				gridButton2.setText("X");
				clientConnection.info.playMoveRequest = false;
			}

			clientConnection.send();

		});

		gridButton3.setOnAction(e-> {
			clientConnection.info.move = 2;

			if(clientConnection.info.board.get(2) == -1){
				clientConnection.info.playMoveRequest = true;
			}
			//clientConnection.send();

			if(clientConnection.info.board.get(2) == 0){
				gridButton3.setText("O");
				clientConnection.info.playMoveRequest = false;
			}
			if(clientConnection.info.board.get(2) == 1){
				gridButton3.setText("X");
				clientConnection.info.playMoveRequest = false;
			}

			clientConnection.send();

		});

		gridButton4.setOnAction(e-> {
			clientConnection.info.move = 3;

			if(clientConnection.info.board.get(3) == -1){
				clientConnection.info.playMoveRequest = true;
			}
			//clientConnection.send();

			if(clientConnection.info.board.get(3) == 0){
				gridButton4.setText("O");
				clientConnection.info.playMoveRequest = false;
			}
			if(clientConnection.info.board.get(3) == 1){
				gridButton4.setText("X");
				clientConnection.info.playMoveRequest = false;
			}

			clientConnection.send();


		});

		gridButton5.setOnAction(e-> {
			clientConnection.info.move = 4;

			if(clientConnection.info.board.get(4) == -1){
				clientConnection.info.playMoveRequest = true;
			}
			//clientConnection.send();

			if(clientConnection.info.board.get(4) == 0){
				gridButton5.setText("O");
				clientConnection.info.playMoveRequest = false;
			}
			if(clientConnection.info.board.get(4) == 1){
				gridButton5.setText("X");
				clientConnection.info.playMoveRequest = false;
			}

			clientConnection.send();


		});

		gridButton6.setOnAction(e-> {
			clientConnection.info.move = 5;

			if(clientConnection.info.board.get(5) == -1){
				clientConnection.info.playMoveRequest = true;
			}
			//clientConnection.send();

			if(clientConnection.info.board.get(5) == 0){
				gridButton6.setText("O");
				clientConnection.info.playMoveRequest = false;
			}
			if(clientConnection.info.board.get(5) == 1){
				gridButton6.setText("X");
				clientConnection.info.playMoveRequest = false;
			}

			clientConnection.send();


		});

		gridButton7.setOnAction(e-> {
			clientConnection.info.move = 6;

			if(clientConnection.info.board.get(6) == -1){
				clientConnection.info.playMoveRequest = true;
			}
			//clientConnection.send();

			if(clientConnection.info.board.get(6) == 0){
				gridButton7.setText("O");
				clientConnection.info.playMoveRequest = false;
			}
			if(clientConnection.info.board.get(6) == 1){
				gridButton7.setText("X");
				clientConnection.info.playMoveRequest = false;
			}

			clientConnection.send();


		});

		gridButton8.setOnAction(e-> {
			clientConnection.info.move = 7;

			if(clientConnection.info.board.get(7) == -1){
				clientConnection.info.playMoveRequest = true;
			}
			//clientConnection.send();

			if(clientConnection.info.board.get(7) == 0){
				gridButton8.setText("O");
				clientConnection.info.playMoveRequest = false;
			}
			if(clientConnection.info.board.get(7) == 1){
				gridButton8.setText("X");
				clientConnection.info.playMoveRequest = false;
			}

			clientConnection.send();


		});

		gridButton9.setOnAction(e-> {
			clientConnection.info.move = 8;

			if(clientConnection.info.board.get(8) == -1){
				clientConnection.info.playMoveRequest = true;
			}
			//clientConnection.send();

			if(clientConnection.info.board.get(8) == 0){
				gridButton9.setText("O");
				clientConnection.info.playMoveRequest = false;
			}
			if(clientConnection.info.board.get(8) == 1){
				gridButton9.setText("X");
				clientConnection.info.playMoveRequest = false;
			}

			clientConnection.send();
			//clientConnection.send();


		});

		playAgainButton.setOnAction(e-> {
			//TODO
			//set a playing again request
			clientConnection.info.playAgainRequest = true;
			clientConnection.send();
			primaryStage.setScene(sceneMap.get("choice"));
			primaryStage.setTitle("This is a client");

		});






		//choice button that connect the client to the server
		this.clientChoice.setOnAction(e-> {
			primaryStage.setScene(sceneMap.get("choice"));
			primaryStage.setTitle("This is a client");

			clientConnection = new Client(data -> {

				Platform.runLater(() -> {

					if (clientConnection.socketClient != null) {
						listItems.getItems().clear();
						listItems.getItems().add("Connected");
					}
					if (clientConnection.socketClient != null) {
						highScore1.setText("High Score 1: " + clientConnection.info.scores.get(0));
						highScore2.setText("High Score 2: " + clientConnection.info.scores.get(1));
						highScore3.setText("High Score 3: " + clientConnection.info.scores.get(2));

						if (primaryStage.getScene() == sceneMap.get("Game")) {
							for (int loopIndex = 0; loopIndex < 9; loopIndex++) {

								if (clientConnection.info.board.get(loopIndex) == -1) {
									buttonList.get(loopIndex).setText(" ");
								}
								if (clientConnection.info.board.get(loopIndex) == 0) {
									buttonList.get(loopIndex).setText("O");
								}
								if (clientConnection.info.board.get(loopIndex) == 1) {
									buttonList.get(loopIndex).setText("X");
								}
							}
						}

						if (clientConnection.info.gameWon == true && !(clientConnection.info.winner.equals("None"))) {
							clientConnection.info.gameWon = false;
							if (clientConnection.info.winner.equals("Server")) {
								winner.setText("YOU LOSE!");
							}
							else if(clientConnection.info.winner.equals("Player")) {
								winner.setText("YOU WIN!");
							}
							else if(clientConnection.info.winner.equals("Draw")) {
								winner.setText("DRAW!");
							}

							sceneMap.put("Finish", createClientFinish());
							primaryStage.setScene(sceneMap.get("Finish"));


						}
					} else {
						System.out.println("Failed to connect to server. Exiting..");
						Platform.exit();
						System.exit(-1);
					}

				});
			});

			//set ip and port and start server
			try {
				clientConnection.port = Integer.valueOf(portNumber.getText());;
				clientConnection.ipAddress = ipAddr.getText();
				clientConnection.start();
			} catch (Exception c) {
				System.out.println("Improper IP Address or Port. Connection failed");
				Platform.exit();
				try {
					clientConnection.in.close();
					clientConnection.out.close();
					clientConnection.socketClient.close();
					System.out.println("Socket and IO Streams were closed");
				} catch (IOException e2) {
					System.out.println("Couldn't close socket/IO Streams properly");
					System.exit(0);
				}
			}
		});

		c1 = new TextField();
		b1 = new Button("Send");
		b1.setOnAction(e->{
			c1.getText();
			clientConnection.send();
			c1.clear();

		});
		listItems2 = new ListView<String>();

		sceneMap.put("choice", createChoiceScene());
		sceneMap.put("client",  createClientGui());
		sceneMap.put("Game",  createGameScene());

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				try {
					clientConnection.in.close();
					clientConnection.out.close();
					clientConnection.socketClient.close();
					System.out.println("Socket and IO Streams were closed");
				} catch (IOException e) {
					System.out.println("Couldn't close socket/IO Streams properly");
					System.exit(0);
				}
			}
		});
	} // End of start

	public Scene createClientGui() {

		clientBox = new VBox(10, c1,b1,listItems2);
		clientBox.setStyle("-fx-background-color: blue");
		return new Scene(clientBox, 400, 300);

	}

	public Scene createChoiceScene() {
		textChoose = new Text("Choose a Difficulty");

		highScore1 = new Label();
		highScore2 = new Label();
		highScore3 = new Label();

		//listItems2 = new ListView<String>();
		choicePane = new BorderPane();
		choiceHBox = new HBox(50, easyButton, mediumButton, hardButton);
		choicePaneBox = new HBox(50, highScore1, highScore2, highScore3);
		choiceButtonBox = new VBox(50, choicePaneBox, textChoose, choiceHBox, playButton);
		choicePane.setCenter(choiceButtonBox);
		choicePane.setBottom(listItems);



		return new Scene(choicePane, 700, 700);
	}

	public Scene createGameScene() {

		Text text = new Text("Tic Tac Toe");

		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setMaxSize(500, 500);
		gridPane.setPadding(new Insets(15));

		gridButton1.setMaxWidth(500);
		gridButton1.setMaxHeight(500);
		gridButton2.setMaxWidth(500);
		gridButton2.setMaxHeight(500);
		gridButton3.setMaxWidth(500);
		gridButton3.setMaxHeight(500);
		gridButton4.setMaxWidth(500);
		gridButton4.setMaxHeight(500);
		gridButton5.setMaxWidth(500);
		gridButton5.setMaxHeight(500);
		gridButton6.setMaxWidth(500);
		gridButton6.setMaxHeight(500);
		gridButton7.setMaxWidth(500);
		gridButton7.setMaxHeight(500);
		gridButton8.setMaxWidth(500);
		gridButton8.setMaxHeight(500);
		gridButton9.setMaxWidth(500);
		gridButton9.setMaxHeight(500);


        buttonList.add(gridButton1);
        buttonList.add(gridButton2);
        buttonList.add(gridButton3);
        buttonList.add(gridButton4);
        buttonList.add(gridButton5);
        buttonList.add(gridButton6);
        buttonList.add(gridButton7);
        buttonList.add(gridButton8);
        buttonList.add(gridButton9);


		gridPane.add(gridButton1, 0, 0);
		gridPane.add(gridButton2, 1, 0);
		gridPane.add(gridButton3, 2, 0);
		gridPane.add(gridButton4, 0, 1);
		gridPane.add(gridButton5, 1, 1);
		gridPane.add(gridButton6, 2, 1);
		gridPane.add(gridButton7, 0, 2);
		gridPane.add(gridButton8, 1, 2);
		gridPane.add(gridButton9, 2, 2);

		GridPane.setHgrow(gridButton1, Priority.ALWAYS);
		GridPane.setVgrow(gridButton1, Priority.ALWAYS);
		GridPane.setHgrow(gridButton2, Priority.ALWAYS);
		GridPane.setVgrow(gridButton2, Priority.ALWAYS);
		GridPane.setHgrow(gridButton3, Priority.ALWAYS);
		GridPane.setVgrow(gridButton3, Priority.ALWAYS);
		GridPane.setHgrow(gridButton4, Priority.ALWAYS);
		GridPane.setVgrow(gridButton4, Priority.ALWAYS);
		GridPane.setHgrow(gridButton5, Priority.ALWAYS);
		GridPane.setVgrow(gridButton5, Priority.ALWAYS);
		GridPane.setHgrow(gridButton6, Priority.ALWAYS);
		GridPane.setVgrow(gridButton6, Priority.ALWAYS);
		GridPane.setHgrow(gridButton7, Priority.ALWAYS);
		GridPane.setVgrow(gridButton7, Priority.ALWAYS);
		GridPane.setHgrow(gridButton8, Priority.ALWAYS);
		GridPane.setVgrow(gridButton8, Priority.ALWAYS);
		GridPane.setHgrow(gridButton9, Priority.ALWAYS);
		GridPane.setVgrow(gridButton9, Priority.ALWAYS);

		VBox vBox = new VBox(15);
		vBox.getChildren().addAll(text, gridPane);
		VBox.setVgrow(gridPane, Priority.ALWAYS);
		vBox.setAlignment(Pos.CENTER);
		vBox.setPadding(new Insets(15));

		return new Scene(vBox, 700, 700);
	}

	public Scene createClientFinish() {


		playAgain = new Label("Do You Want To Play Again?");

		playAgainBox = new HBox(30, playAgainButton);
		gameOverBox = new VBox(30, winner,playAgain, playAgainBox);

		gameOverBox.setStyle("-fx-background-color: #dfcaae");
		gameOverBox.setPadding(new Insets(30));



		return new Scene(gameOverBox, 300, 400);
	}

}
