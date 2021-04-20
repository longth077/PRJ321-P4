package controller;

import com.entity.Client;
import com.sun.deploy.net.MessageHeader;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServerBoxController {

    @FXML
    public ListView<Client> clients;
    @FXML
    private VBox mainPane;

    private ChatBoxController chatBoxController;

    public List<Client> clientList = new ArrayList<>();

    public void initialize() {

    }

    public void addToList(Client client) {
        clientList.add(client);
        showList();
    }

    //them danh sach client connect
    public void showList() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clients.getItems().clear();
                if(clients.getItems() != null) {
                    for(Client clientToShow : clientList) {
                        clients.getItems().add(clientToShow);
                    }
                }
            }
        });
    }

    //mo hop thoai chat voi client
    public void listClientMouseClicked(MouseEvent event) {
        if(event.getClickCount() == 2) {
            if(clients.getItems().isEmpty() || clients.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText(null);
                alert.setContentText("You must double click on user of list.");
                alert.showAndWait();
                return;
            }
            String clientName = clients.getSelectionModel().getSelectedItem().getUsername();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../ui/ChatBox.fxml"));
            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 400, 300));
            stage.setTitle("Chat with " + clientName);
            chatBoxController = fxmlLoader.getController();
            //truyen thong tin username vao chatBoxcontroller
            chatBoxController.setUserName(clientName);
            //truyen txtContent tu chatbox vao client handler
            chatBoxController.setTxtContentForClientHandler();
            //thong bao trang thai ngung cho tin nhan khi mo hop thoai
            chatBoxController.setIsRise();

            stage.show();
            //khi dong cua so chat thi se thong bao den trang thai cho tin nhan de thong bao
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    chatBoxController.setCloseStage();
                }
            });
        }
    }

    //thong bao co tin nhan neu client gui tin ma chua mo hop thoai
    public void notification(String userName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("You have a message from " + userName + ".");
        alert.showAndWait();
        return;
    }

}
