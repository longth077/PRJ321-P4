package business;

import com.entity.Client;
import com.entity.Server;
import controller.ChatBoxController;
import controller.ServerBoxController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread implements Runnable, Serializable {

    private Server server;
    private ServerSocket serverSocket;
    private Socket socket;
    private ServerBoxController controller;
    //hashmap quan ly client handler thong qua client name
    private static HashMap<String, ClientHandler> clients = new HashMap<>();

    public ServerThread(Server server) {
        this.server = server;
        try {
             serverSocket = new ServerSocket(server.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //lay controller cua server box de xu ly
    public void setController(ServerBoxController controller) {
        this.controller = controller;
    }

    public static HashMap<String, ClientHandler> getClients() {
        return clients;
    }

    @Override
    public void run() {
        System.out.println("running");
        try {
            while (true) {
                socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String chatContent = dataInputStream.readUTF();
                Client client = new Client();

                if(chatContent != null) {
                    String user = chatContent.split(":")[0];
                    client.setUsername(user);
                    client.setSocket(socket);
                    //hien thi client ket noi vao listview
                    controller.addToList(client);

                    //lay hashmap dua vao controller
                    ChatBoxController.setClients();
                    ClientHandler clientHandler = new ClientHandler(socket, client);
                    clientHandler.setServerBoxController(controller);
                    clients.put(user, clientHandler);
                    new Thread(clientHandler).start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
