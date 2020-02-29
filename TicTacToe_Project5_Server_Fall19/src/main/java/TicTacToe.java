import java.io.IOException;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TicTacToe extends Application {

	Text header;
	Button serverSwitch, quitButton;
	HBox portHBox;
	Scene startScene;
	BorderPane startPane;

	HashMap<String, Scene> sceneMap = new HashMap<String, Scene>();
	ListView<String> listItems = new ListView<String>();



	Server serverConnection;

	public static void main(String[] args) {
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Let's Play Tic Tac Toe!!!");

		serverSwitch = new Button("Turn on Server");
		serverSwitch.setStyle("-fx-pref-width: 300px");
		header = new Text("TicTacToe Server");
		Text portText = new Text("port: ");
		TextField portField = new TextField("1738");
		portHBox = new HBox(portText, portField);
		quitButton = new Button("Quit");



		startPane = new BorderPane();
		startPane.setTop(header);
		startPane.setCenter(portHBox);
		startPane.setBottom(serverSwitch);
		startPane.setPadding(new Insets(20));

		startScene = new Scene(startPane, 400,600);
		sceneMap.put("server",  createServerGui());

		serverSwitch.setOnAction(e->{
			primaryStage.setScene(sceneMap.get("server"));
			primaryStage.setTitle("This is the Server");


			serverConnection = new Server(data -> {
				Platform.runLater(()->{
					listItems.getItems().add(0,data.toString());

					if(serverConnection.mySocket == null) {
						System.out.println("Server socket has failed");
					}
				});

			});

			//set port
			try {
				int port = Integer.valueOf(portField.getText());
				serverConnection.port = port;
			}
			catch(Exception c) {
				System.out.println("Improper Port. Connection failed");
			};

		});

		primaryStage.setScene(startScene);
		primaryStage.show();

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			//comment
			public void handle(WindowEvent t) {
				Platform.exit();
				try {
					serverConnection.mySocket.close();
				} catch (IOException e) {
					System.out.println("Couldn't close socket properly");
					e.printStackTrace();
					System.exit(0);
				}
			}
		});

	} //end start

	public Scene createServerGui() {

		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		pane.setStyle("-fx-background-color: coral");
		pane.setCenter(listItems);
		return new Scene(pane, 500, 400);
	}

}
