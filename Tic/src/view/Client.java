package view;

import model.ModelOfGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client extends JPanel {
    private int linesCount, cellSize,FIELD_SIZE;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private boolean step = false;
    private boolean fire = false;
    private int x;
    private int y;
    private String name;
    private String sign;
    private ModelOfGame game;
    private boolean win = false;
    private boolean draw = false;
    private int shots = 0;
    private int maxshots = 0;
    String[] sArray;
    private boolean startGame = false;
    private Sender sender;
    private boolean dark = false;
    private boolean secCon = false;

    public Client(String name, String sing) { //передавать размер поля и прочие настройки
        this.name = name;
        this.sign = sing;
        try {
            socket = new Socket("localhost", 2020);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Receiver receiver = new Receiver();
        sender = new Sender();

        receiver.start();
        writer.println(name);
        mouse();

        FIELD_SIZE = 350;

    }
    void setStep(boolean st){
        this.step = st;
    }
    boolean getStartGame(){
        return startGame;
    }
    void setFire(boolean lox){
        this.fire = lox;
    }
    boolean getFire(){
        return fire;
    }
    void setSizeBoard(int sizeBoard){
        if(name.equals("lox")){
            System.out.println("we here " + sizeBoard);
        }
        game = new ModelOfGame(sizeBoard, sign);
        linesCount = game.getGameSize();
        cellSize = FIELD_SIZE/linesCount;
        maxshots = game.getGameSize() * game.getGameSize();
        System.out.println("Ждем подключения");
        repaint();
    }
    void getSizeBoard(){
        writer.println("@getSizeBoard");
    }

    void getListUsers(){
        writer.println("@getListUsers");
    }
    String[] getUsers(){
        return sArray;
    }


    void mouse(){ //устанавливает в переменные клетку куда нажали
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (step) {
                    x = e.getX() / cellSize;
                    y = e.getY() / cellSize;
                    System.out.println("fire on " + x + "-"+ y);
                    setFire(true);
                    setStep(false); //сделали ход и меняем статус

                    if (!game.isCellBusy(x, y)) { //можем туда стрелять
                        game.userShot(x,y);
                        shots++;
                        if(shots == maxshots && !game.checkWin2()){
                            draw = true;
                        }
                        if(game.checkWin2()){
                            win = true;
                        }
                        String message = x + "-" + y; //сообщение с данными о нажатии
                        writer.println(message); //отправляем сообщение с полем
                        if(win == true) {
                            writer.println("@uLose");
                            writer.println("@exit");
                        }
                        if(draw == true){
                            writer.println("@weLose");
                            writer.println("@exit");
                        }
                    }
                    fire = false;
                    repaint();
                }
            }
        });
    }
    void setSecCon(){
        secCon = true;
    }
    void setStartGame(){
        startGame = true;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(startGame) {
            if (draw) {
                String str = "Ничья";
                g.setColor(Color.RED);
                g.drawString(str, 125, FIELD_SIZE / 2);
            }
            if (win) {
                String str;
                if (shots % 2 == 0) {
                    str = "Победа O";
                } else {
                    str = "Победа X";
                }
                g.setColor(Color.RED);
                g.drawString(str, 125, FIELD_SIZE / 2);
            }

            if (!draw && !win) {
                ImageIcon BackGround = new ImageIcon("Fon.png");
                Image myBackGround = BackGround.getImage();
                g.drawImage(myBackGround,0,0,this);
                // Рисуем линии, которые представляют собой сетку
                for (int i = 0; i <= this.linesCount; i++) {
                    g.drawLine(0, i * this.cellSize, FIELD_SIZE, i * this.cellSize);
                    g.drawLine(i * this.cellSize, 0, i * this.cellSize, FIELD_SIZE);
                }
                for (int i = 0; i < linesCount; i++) {
                    for (int j = 0; j < linesCount; j++) {
                        if (game.getPoint(i, j).equals("X")) {
                            // Рисуем крестик
                            g.setColor(Color.RED);
                            g.drawLine((i * cellSize), (j * cellSize), (i + 1) * cellSize, (j + 1) * cellSize);
                            g.drawLine((i + 1) * cellSize, (j * cellSize), (i * cellSize), (j + 1) * cellSize);
                        }
                        if (game.getPoint(i, j).equals("O")) {
                            // Рисуем нолик
                            g.setColor(Color.BLUE);
                            g.drawOval((i * cellSize), (j * cellSize), cellSize, cellSize);
                        }

                    }
                }
            }
        }
        else {
            System.out.println("Ждем соперника");
            String str = "Ждем соперника";
            g.setColor(Color.RED);
            g.drawString(str, 125, FIELD_SIZE / 2);
        }
    }

    void connectToPlayer(String namePlayer){
        writer.println("@connEnemy " + namePlayer);
    }

    private class Sender extends Thread {
        int count = 0;
        @Override
        public void run() {
            while (true) { //постоянно ждем возможности сходить
                if (getStartGame()) {
                    try{
                        Thread.sleep(500);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    if(count == 0){
                        System.out.println("Игра началась!");
                        count++;
                    }
                    if (step) {
                        System.out.println("Доска сейчас");
                        showBoard();
                        Scanner in = new Scanner(System.in);
                        dark = true;
                        System.out.println("Делай выстрел!");
                        String message = in.nextLine();
                        String[] pif = message.split("-");
                        int xPos = Integer.parseInt(pif[0]);
                        int yPos = Integer.parseInt(pif[1]);
                        if (!game.isCellBusy(xPos, yPos)) { //можем туда стрелять
                            game.userShot(xPos, yPos);
                            shots++;
                            if (shots == maxshots && !game.checkWin2()) {
                                System.out.println("Это была хорошая игра, но она закончилась ничьей");
                                showBoard();
                                draw = true;
                            }
                            if (game.checkWin2()) {
                                win = true;
                            }
                            String mes = xPos + "-" + yPos; //сообщение с данными о нажатии
                            writer.println(message); //отправляем сообщение с полем
                            if (win == true) {
                                System.out.println("Ты победил! Отправляйся в паб отмечать победу!!!");
                                showBoard();
                                writer.println("@uLose");
                                writer.println("@exit");
                                System.exit(0);
                            }
                            if (draw == true) {
                                writer.println("@weLose");
                                writer.println("@exit");
                                System.exit(0);
                            }
                            showBoard();
                            setStep(false);
                            continue;
                        }
                    }
                }

            }
        }
    }

    void showBoard(){
        for (int i = 0; i < game.getGameSize(); i++) {
            for (int j = 0; j < game.getGameSize(); j++) {
                System.out.print(game.getPoint(j,i));
            }
            System.out.println();
        }
    }


    private class Receiver extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    String message = reader.readLine();

                    if (message.startsWith("@quit")) {
                        // break;
                        System.exit(0);
                    }
                    if(message.startsWith("@uLose")){
                        System.out.println("Повезет в другой раз");
                        win = true;
                        writer.println("@exit");
                        break;
                    }

                    if(message.startsWith("@weLose")){
                        draw = true;
                        writer.println("@exit");
                        break;
                    }
                    if(message.startsWith("@connEnemy")){
                        startGame = true;

                        setStartGame();
                        sender.start();
                        repaint();
                        writer.println("@conn2Enemy " + message.substring(11));
                        continue;
                    }
                    if(message.startsWith("@getListUsers")){
                       String t =  message.substring(13);
                       sArray = t.split("-");
                       continue;
                    }

                    if(message.startsWith("@getSizeBoard")){
                        writer.println("@setSizeBoard " + game.getGameSize());
                        continue;
                    }
                    if(message.startsWith("@setSizeBoard")){
                        setSizeBoard(Integer.valueOf(message.substring(14)));
                        continue;
                    }
                    String[] sArray = message.split("-");
                    String val;
                    if(sign.equals("X")){
                        val = "O";
                    }
                    else {
                        val = "X";
                    }
                    game.userShot(Integer.parseInt(sArray[0]),Integer.parseInt(sArray[1]), val);
                    shots++;
                    step = true;
                    setStep(true);
                    System.out.println("Противник выстрелил по " + message);
                    if(!secCon){
                        sender.start();
                        secCon = true;
                    }
                    showBoard();
                    repaint();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
