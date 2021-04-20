package controller;



import business.ClientHandler;
import business.ServerThread;
import com.entity.Client;
import com.entity.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.Socket;

public class Main extends Application {

    public final String SERVER_NAME = "localhost";
    public final int PORT = 1234;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../ui/ServerBox.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Server Application");
        primaryStage.setScene(new Scene(root, 500, 375));
        primaryStage.show();

        //start the server and ready for client's connection
        try{
            Server server = new Server(SERVER_NAME, PORT);
            ServerThread serverThread = new ServerThread(server);
            serverThread.setController(fxmlLoader.getController());
            new Thread(serverThread).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
