package controller;

import java.util.Random;
import java.util.Scanner;


public class Ai {
    public String NOT_SIGN = "*";
    public String USER_SIGN = "X";
    public String AI_SIGN = "O";
    public int gameSize;
    public  String[][] field;
    public int aiLevel;
    public int x, y;



    public Ai(int gameSize, int aiLevel){
        this.gameSize = gameSize;
        field = new String[gameSize+5][gameSize+5];
        this.aiLevel = aiLevel;
        modeAgainstAI();
    }

    public void modeAgainstAI() {
        int count = 0;
        initField();
        printField();
        while (true) {
            // x и y при нажатии мышки хз как сделано (ниже с клавы если)
            //System.out.println("Стреляет игрок");
            do
            {
                System.out.println("Стреляет игрок");
                Scanner sc = new Scanner(System.in);
                x = sc.nextInt() - 1;
                y = sc.nextInt() - 1;

            }
            while (isCellBusy(x, y));
            userShot(x, y);
            count++;
            if (count == Math.pow(gameSize, 2)) {
                printField();
                System.out.println("Ничья блять))))");
                break;
            }
            aiShot();
            System.out.println("Выстрел бота");
            printField();
            count++;
            if (checkWin2()) {
                System.out.println("WIN!!!");
                break;
            }
            if (count == Math.pow(gameSize, 2)) {
                printField();
                System.out.println("Ничья блять))))");
                break;
            }

        }
    }
    public  void printField() {
        for (int i = 0; i <= gameSize; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 0; i < gameSize; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < gameSize; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }
    }
    public void aiShot()
    {
        boolean ai_win = false;
        boolean user_win = false;
        // aiLevel = 2
        // Находим выигрышный ход
        if (aiLevel == 2)
        {
            for (int i = 0; i < gameSize; i++)
            {
                for (int j = 0; j < gameSize; j++)
                {
                    if (!isCellBusy(i, j))
                    {
                        field[i][j] = AI_SIGN;
                        if (checkWin2())
                        {
                            x = i;
                            y = j;
                            ai_win = true;
                        }
                        field[i][j] = NOT_SIGN;
                    }
                }
            }
        }
        // aiLevel = 1
        // Блокировка хода пользователя, если он побеждает на следующем ходу
        if (aiLevel > 0)
        {
            if (!ai_win)
            {
                for (int i = 0; i < gameSize; i++)
                {
                    for (int j = 0; j < gameSize; j++)
                    {
                        if (!isCellBusy(i, j))
                        {
                            field[i][j] = USER_SIGN;
                            if (checkWin2())
                            {
                                x = i;
                                y = j;
                                user_win = true;
                            }
                            field[i][j] = NOT_SIGN;
                        }
                    }
                }
            }
        }
        // aiLevel = 0
        if (!ai_win && !user_win)
        {
            do
            {
                Random rnd = new Random();
                x = rnd.nextInt(gameSize);
                y = rnd.nextInt(gameSize);
            }
            while (isCellBusy(x, y));
        }
        field[x][y] = AI_SIGN;
    }



    public  void initField() {
        for (int i = 0; i < gameSize + 5; i++) {
            for (int j = 0; j < gameSize + 5; j++) {
                field[i][j] = NOT_SIGN;
            }
        }
    }


    public  void userShot(int x, int y) {

        field[x][y] = USER_SIGN;
    }







    public  boolean isCellBusy(int x, int y) {
        if (x < 0 || y < 0 || x > gameSize - 1 || y > gameSize - 1) {
            return false;
        }
        return !field[x][y].equals(NOT_SIGN);
    }


    public  boolean checkWin2() {
        String sign = USER_SIGN;
        int pos = 1;
        // проверка по строкам
        {
            for (int i = 0; i < gameSize; i++) {
                for (int j = 1; j < gameSize; j++) {
                    if((field[j-1][i].equals(sign)) && (field[j][i].equals(sign))){
                        pos++;
                    }
                    if((pos == gameSize) ||(pos == 5)){
                        //printField();
                        return true;
                    }
                }
                pos = 1;
            }
        }
        pos = 1;
        // проверка по столбцам
        {
            for (int j = 0; j < gameSize; j++) {
                for (int i = 1; i < gameSize; i++) {
                    if((field[j][i-1].equals(sign)) && (field[j][i].equals(sign))){
                        pos++;
                    }
                    if((pos == gameSize) ||(pos == 5)){
                        //printField();
                        return true;
                    }
                }
                pos = 1;
            }
        }
        pos = 0;

        // проверка диагоналей
        {
            for (int i = 0; i < gameSize; i++) {
                for (int j = 0; j < gameSize; j++) {
                    for (int m = i, n = j; m < gameSize && n < gameSize; m++, n++) {
                        if (field[m][n].equals(sign)){
                            pos++;
                        }
                        if((pos == gameSize) || (pos == 5)){
                            //printField();
                            return true;

                        }
                    }
                    pos = 0;
                }
            }
        }

        return false;
    }


}
