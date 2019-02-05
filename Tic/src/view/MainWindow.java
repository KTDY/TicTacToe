package view;

import controller.Ai;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;


public class MainWindow extends JFrame {
    JPanel p;
    boolean vis;
    private String name = "";
    private String namePlayer = "";
    private int sizeBoard = 3;
    private boolean game = false;
    private boolean bot = false;

    public MainWindow(boolean pic) {
        setTitle("Tic Tac Toe");
        setSize(350, 370);
        setLocation(500, 300);
        p = new JPanel();
        add(p);
        p.setLayout(new BorderLayout());
        vis = pic;
    }

    public void Game(String gamerName) {
        int count = 0;
        try {
            Scanner in = new Scanner(new File("/Users/vitaliishestakov/Documents/Java5/Tic/src/model/Settings.txt"));
            while (in.hasNextLine()) {
                String s = in.nextLine();
                if (count == 0){
                    System.out.println("Size board is " + s.substring(12));
                    sizeBoard = Integer.parseInt(s.substring(12));
                    count++;
                    continue;
                }
                if(count == 1){
                    if(s.substring(12).equals("true")){
                        bot = true;
                    }
                    else {
                        bot = false;
                    }
                    count++;
                }
            }
            in.close();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        if(bot == false) {
            Client g = new Client(gamerName, "X");
            g.setStep(true);
            g.setSizeBoard(sizeBoard);
            g.setSecCon();
            g.setBounds(0, 0, 400, 400);
            add(g);
            setVisible(vis);
        }
        else {
            Ai g = new Ai(sizeBoard,3);
        }
    }

    public void ConnectGame(String gamerName) {
        Client f = new Client(gamerName, "O");
        f.getListUsers();
        try{
            Thread.sleep(500);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        String[] str = f.getUsers();
        String[] us = new String[str.length];
        int myNames = 0;
        for (int i = 0; i < str.length; i++) {
            if(str[i].startsWith(" ")){
                str[i] = str[i].substring(1);
            }
            if(str[i].equals(gamerName)){
                myNames++;
                continue;
            }

            us[i-myNames] = str[i];
        }
        JList<String> list = new JList<String>(us);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setPrototypeCellValue("Увеличенный");
        list.setBounds(200,200,200,200);
        p.add(list);
        setVisible(vis);
        if(vis) {
            list.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (!game) {
                        int index = list.getSelectedIndex();
                        namePlayer = us[index];
                        f.connectToPlayer(namePlayer);
                        f.getSizeBoard();
                        f.setBounds(0, 0, 400, 400);
                        f.setStartGame();
                        System.out.println();
                        add(f);
                        System.out.println(us[index]);
                        p.remove(list);
                        remove(list);
                        game = true;
                    }
                }
            });
        }
        else {
            System.out.println("Список свободных игроков, введите ник, чтобы подключиться");
            for (String i : us) {
                if (i == null) {
                    continue;
                }
                System.out.println(i);
            }
            Scanner in = new Scanner(System.in);
            String message = in.nextLine();
            f.connectToPlayer(message);
            f.getSizeBoard();
            f.setBounds(0, 0, 400, 400);
            f.setStartGame();
            add(f);
            p.remove(list);
            remove(list);
            game = true;
        }
    }

    public void Settings(){
        JTextField log = new JTextField("");
        log.setBounds(125,80,100,50);
        JLabel l = new JLabel("Введите размер поля");
        l.setBounds(100,50,160,20);
        JLabel ll = new JLabel("");

        log.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sizeBoard = Integer.valueOf(log.getText());
                System.out.println(sizeBoard);
                fileWrite();
            }
        });

        JRadioButton a = new JRadioButton ( "С ботом" ) ;
        JRadioButton b = new JRadioButton ( "Без бота" ) ;
        a.setBounds(200,200,100,100);
        b.setBounds(100,200,100,100);
        ActionListener rbuttom = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(e.getActionCommand());
                bot = true;
                fileWrite();
            }
        };

        ActionListener rbuttomm = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(e.getActionCommand());
                bot = false;
                fileWrite();
            }
        };
        a.addActionListener(rbuttom);
        b.addActionListener(rbuttomm);
        add(l);
        add(log);
        add(a);
        add(b);
        add(ll);
        setVisible(true);
    }
    public void fileWrite(){
        try {
            FileOutputStream fos = new FileOutputStream("/Users/vitaliishestakov/Documents/Java5/Tic/src/model/Settings.txt", false);
            fos.write(("Size board: " + sizeBoard + "\n").getBytes());
            fos.write(("Bot status: " + bot + "\n").getBytes());

        }
        catch (FileNotFoundException trace){
            trace.printStackTrace();
        }
        catch (IOException k){
            k.printStackTrace();
        }
    }

    private class MenuThread extends Thread {
        public void run()  {
            while(true) {
                System.out.println("Создать игру (@create)");
                System.out.println("Подключиться к игре (@conn)");
                System.out.println("Настройки (@settings)");
                Scanner in = new Scanner(System.in);
                String message = in.nextLine();
                if (message.equals("@create")) {
                    System.out.println("Введите ник");
                    message = in.nextLine();
                    MainWindow mw = new MainWindow(vis);
                    mw.Game(message);
                    break;
                }
                if (message.equals("@conn")) {
                    System.out.println("Введите ник");
                    message = in.nextLine();
                    MainWindow mw = new MainWindow(vis);
                    mw.ConnectGame(message);
                    break;
                }
                if (message.equals("@settings")) {
                    int pos = 0;
                    Scanner inp;
                    String s;
                    int size = 0;
                    boolean bott = false;
                    try {
                        inp = new Scanner(new File("/Users/vitaliishestakov/Documents/Java5/Tic/src/model/Settings.txt"));
                        while (inp.hasNextLine()) {
                            s = inp.nextLine();
                            if (pos == 0) {
                                size = Integer.parseInt(s.substring(12));
                                pos++;
                                continue;
                            }
                            if (pos == 1) {
                                if (s.substring(12).equals("true")) {
                                    bott = true;
                                } else {
                                    bott = false;
                                }
                                pos++;
                            }
                        }
                        inp.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Now size board: " + size + " , if you want to change need to write @setSize_*size*");
                    System.out.println("Now status bot: " + bott + " , if you want to change need to write @setBot_*true/false*");
                    System.out.println("If you want back to connect or create game neew to write @back");
                    message = in.nextLine();
                    if (message.startsWith("@setSize")) {
                        sizeBoard = Integer.parseInt(message.substring(9));
                        fileWrite();
                        System.out.println("You change size");
                        continue;
                    }
                    if (message.startsWith("@setBot")) {
                        if (message.substring(8).equals("true")) {
                            bot = true;
                        } else {
                            bot = false;
                        }
                        fileWrite();
                        System.out.println("You change bot status");
                        continue;
                    }
                    if(message.equals("@back")){
                        continue;
                    }

                }
            }
        }
    }


    public void Menu(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JLabel l = new JLabel("");
        Button createGame = new Button("Создать игру");
        Button connectGame = new Button("Подключиться к игре");
        Button settings = new Button("Настройки");
        JTextField log = new JTextField("Введите логин");
        createGame.setBounds(100,100,150,50);
        connectGame.setBounds(100,150,150,50);
        settings.setBounds(100,200,150,50);
        log.setBounds(97,250,157,50);
        p.add(createGame);
        p.add(connectGame);
        p.add(settings);
        p.add(log);
        p.add(l);
        if(!vis) {
            MenuThread writerThread = new MenuThread();
            writerThread.start();
        }

        ActionListener actionCreateGame = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (name == "") {
                    log.setForeground(Color.RED);
                } else {
                    MainWindow mw = new MainWindow(true);
                    mw.Game(name);
                }
            }
        };
        ActionListener actionConnectGame = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               if (name == "") {
                    log.setForeground(Color.RED);
                } else {
                    MainWindow mw = new MainWindow(true);
                    mw.ConnectGame(name);
                }

            }
        };

        ActionListener actionSettings = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MainWindow mw = new MainWindow(true);
                mw.Settings();
            }
        };

        log.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                name = log.getText();
                System.out.println(name);
            }
        });

        createGame.addActionListener(actionCreateGame);
        connectGame.addActionListener(actionConnectGame);
        settings.addActionListener(actionSettings);


        try {
            BufferedImage myPicture = ImageIO.read(new File("BackGroud.jpeg"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            p.add(picLabel, BorderLayout.WEST);
        }catch (IOException ex) {
            // handle exception...
        }
        setVisible(vis);
    }
}
