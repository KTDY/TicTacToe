package controller;

import java.io.*;
import java.net.Socket;

public class ClientThreadHandler extends Thread {
    private Server server;
    public Socket socket;
    private String clientName;
    private String nameEnemy;
    private BufferedReader reader;
    private PrintWriter writer;
    private boolean connect = false;

    ClientThreadHandler(Socket socket, Server server) {
        try {
            this.socket = socket;
            this.server = server;
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            this.clientName = reader.readLine();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getClientName() {
        return clientName;
    }
    public PrintWriter getWriter() {
        return writer;
    }

    public boolean getConnect(){
        return connect;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = reader.readLine();
                server.fos.write(("Name " + clientName + ": " + message + "\n").getBytes());
                System.out.println(message);
                if (message.startsWith("@connEnemy")) {
                    if(!connect) {
                        nameEnemy = message.substring(11);
                        connect = true;
                        server.sendPrivateMessage("@connEnemy " + clientName, nameEnemy);
                        System.out.println("Now status connect " + connect);
                       // writer.println("@connEnemy " + clientName);
                        continue;
                    }
                    else {
                        continue;
                    }
                }
                if(message.startsWith("@conn2Enemy")){
                    nameEnemy = message.substring(12);
                    connect = true;
                    continue;
                }
                if(message.startsWith("@getListUsers")){
                   String u = server.getFreeClient();
                    server.sendPrivateMessage("@getListUsers " + u, clientName);
                    continue;
                }
                if(message.startsWith("@exit")){
                    server.deleteUser(clientName);
                    continue;
                }
                if (message.startsWith("@getSizeBoard")) {
                    server.sendPrivateMessage("@getSizeBoard", nameEnemy);
                    continue;
                }
                if(message.startsWith("@setSizeBoard")){
                    server.sendPrivateMessage(message, nameEnemy);
                    continue;
                }
                if (connect) {
                    server.sendPrivateMessage(message, nameEnemy);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

