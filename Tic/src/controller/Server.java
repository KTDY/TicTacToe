package controller;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Server extends JPanel {
    private Map<String, ClientThreadHandler> clients = new HashMap<>();
    FileOutputStream fos;
    public Server(int port) {
        try{
            fos = new FileOutputStream("/Users/vitaliishestakov/Documents/Java5/Tic/src/model/Move.txt", true);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        try {
            ServerSocket server = new ServerSocket(port);
            while (true) {
                Socket socket = server.accept();
                ClientThreadHandler client = new ClientThreadHandler(socket, this);
                fos.write(("Client " + client.getClientName() + " connected \n").getBytes());
                clients.put(client.getClientName(), client);
                new Thread(client).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public synchronized void sendPrivateMessage(String message,  String receiver) {
        clients.get(receiver).getWriter().println(message);
    }



     String getFreeClient(){
        StringBuffer str = new StringBuffer();
        for(Map.Entry<String, ClientThreadHandler> entry : clients.entrySet()) {
            String key = entry.getKey();
            if(clients.get(key).getConnect() == false){
                str.append(key);
                str.append("-");
            }
        }
        return str.toString();
    }
    public void deleteUser(String name){
        clients.remove(name);
        try {
            fos.write(("Client " + name + " disconnected \n").getBytes());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String args[]) {
        Server s = new Server(2020);
    }
}