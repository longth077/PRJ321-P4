package controller;

import business.ClientHandler;
import business.ServerThread;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.HashMap;


public class ChatBoxController {

    @FXML
    private TextField txtMessage;
    @FXML
    private TextArea txtContent;

    private String userName;

    private ClientHandler clientHandler;

    private static HashMap<String, ClientHandler> clients;

    public static void setClients() {
        clients = ServerThread.getClients();
    }

    public void setClientHandler(ClientHandler handler) {
        clientHandler = handler;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public void setTxtContentForClientHandler() {
        clients.get(userName).setTxtContent(txtContent);
    }

    public void setCloseStage() {
        clients.get(userName).setClose(true);
    }

    //thong bao trang thai ngung cho tin nhan khi mo hop thoai
    //khi hop thoai duoc mo, bat dau them tin nhan tu list vao textarea
    public void setIsRise() {
        clients.get(userName).setClose(false);
        if(clients.get(userName).getListNotification() != null) {
            for(String line : clients.get(userName).getListNotification()) {
                txtContent.appendText(line + "");
            }
            clients.get(userName).getListNotification().clear();
        }
    }

    //thuc hien gui tin nhan thong qua handler va hien thi len hop thoai chat
    public void send() {
        if(clientHandler == null)
            setClientHandler(clients.get(userName));
        if(!txtMessage.getText().trim().isEmpty()) {
            clientHandler.send(txtMessage.getText());
            txtContent.appendText("Me: " + txtMessage.getText() + "\n");
            txtMessage.setText("");
        }
    }
}
