package business;

import com.entity.Client;
import controller.ChatBoxController;
import controller.ServerBoxController;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable{

    private Socket socket;
    private Client client;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private ServerBoxController serverBoxController;
    private TextArea txtContent;
    //bien trang thai de thread cho tin nhan tu client khi chua mo hop thoai de thong bao
    private boolean isClose = true;

    //list luu tin nhan cho khi chua mo hop thoai chat
    private List<String> listNotification;

    public ClientHandler(Socket socket, Client client) {
        this.listNotification = new ArrayList<>();
        this.socket = socket;
        this.client = client;
        try {
            dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
            dataInputStream = new DataInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //ham luu trang thai dong cua hop thoai chat de cho tin nhan thu client
    public void setClose(boolean isClose) {
        this.isClose = isClose;
    }

    public void setTxtContent(TextArea txtContent) {
        this.txtContent = txtContent;
    }

    //ham nhan tham so controller de thong bao khi co tin nhan tu client
    public void setServerBoxController(ServerBoxController controller) {
        serverBoxController = controller;
    }

    public void setContentChat(String contentChat) {
        //tao content neu client gui tin ma chua mo hop thoai tu server
        if (txtContent != null && !isClose) {
            txtContent.appendText(contentChat);
        }
        else {
            listNotification.add(contentChat);
            Platform.runLater(new Runnable(){
                @Override
                public void run() {
                    serverBoxController.notification(client.getUsername());
                }
            });

        }
    }

    public List<String> getListNotification() {
        if(!listNotification.isEmpty())
            return listNotification;
        return null;
    }

    public void send(String line) {
        try {
            dataOutputStream.writeUTF("Server: " + line);
        } catch (IOException e) {
            System.out.println("Loi cho nay");
            e.printStackTrace();
        }
    }

    //doc tin nhan tu client
    @Override
    public void run() {
        try {
            while (true) {
                Object line = dataInputStream.readUTF();
                if (line != null) {
                    setContentChat(client.getUsername() + ": " + line + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
