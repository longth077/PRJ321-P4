package com;

import business.ClientThread;
import com.entity.Client;
import com.entity.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class ClientChatController {
    @FXML
    private TextField  txtHostIP, txtPort, txtMessage, txtUserName;
    @FXML
    private TextArea txtContent;
    @FXML
    private Button btnConnect;

    private ClientThread clientThread;

    //thuc hien connect toi server
    public void btnConnectActionPerformed() {
        //kiem tra ip va port
        String port = txtPort.getText();
        String hostIP = txtHostIP.getText();
//        if(!port.equals("1234") || !hostIP.equals("127.0.0.1")) {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Information");
//            alert.setHeaderText(null);
//            alert.setContentText("There is no server to connect, you should change IP or port");
//            alert.showAndWait();
//            return;
//        }
        if(clientThread == null) {
            try {
                Client client = new Client(txtUserName.getText(), "");
                Server server = new Server(txtHostIP.getText(), Integer.parseInt(txtPort.getText()));
                clientThread = new ClientThread(server, txtContent, txtMessage);
                Thread thread = new Thread(clientThread);
                thread.start();
                clientThread.send(client.getUsername()+ ":");
                txtContent.appendText("Connect to server");
                btnConnect.setDisable(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //thuc hien gui tin nhan toi server
    public void btnSendActionPerformed() {
        try {
            if(!txtMessage.getText().trim().isEmpty())
                clientThread.send(txtMessage.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
