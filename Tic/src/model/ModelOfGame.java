package model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class ModelOfGame {
    public static  String USER_SIGN = "X";
    public static  String USER_SIGN_SECOND = "O";
    public static  String AI_SIGN = "O";
    public static  String NOT_SIGN = "*";
    public static int aiLevel = 0;
    public static int gameSize;
    public  String[][] field;

    public ModelOfGame(int gameSize, String sign) {
        this.gameSize = gameSize;
        USER_SIGN = sign;
        field = new String[gameSize + 5][gameSize + 5];
        initField();
    }

    public String getPoint(int x, int y){
        return field[x][y];
    }
    //заполнение доски.
    public  void initField() {
        for (int i = 0; i < gameSize + 5; i++) {
            for (int j = 0; j < gameSize + 5; j++) {
                field[i][j] = NOT_SIGN;
            }
        }
    }




    public  void userShot(int x, int y) {

        //проверка на не занятость ячейки

        field[x][y] = USER_SIGN;

    }

    public void  userShot(int x, int y, String val){
        field[x][y] = val;
    }

    public int getGameSize(){
        return gameSize;
    }



    //проверка на не занятость ячейки
    public  boolean isCellBusy(int x, int y) {
        if (x < 0 || y < 0 || x > gameSize - 1 || y > gameSize - 1) {
            return false;
        }
        return field[x][y] != NOT_SIGN;
    }


    // проверка выигрыша по линии
    public  boolean checkLine(int start_x, int start_y, int dx, int dy, String sign) {
        for (int i = 0; i < gameSize; i++) {
            if (field[start_x + i * dx][start_y + i * dy] != sign)
                return false;
        }
        return true;
    }


    //Проверка на победу в матче (для маленького поля)
    public  boolean checkWin(String sign) {
        for (int i = 0; i < gameSize; i++) {
            // проверяем строки
            if (checkLine(i, 0, 0, 1, sign)) return true;
            // проверяем столбцы
            if (checkLine(0, i, 1, 0, sign)) return true;
        }
        // проверяем диагонали
        if (checkLine(0, 0, 1, 1, sign)) return true;
        if (checkLine(0, gameSize - 1, 1, -1, sign)) return true;
        return false;
    }





    public  boolean checkWin2() {
        String sign = USER_SIGN;
        int pos = 1;
        // проверка по строкам
        {
            for (int i = 0; i < gameSize; i++) {
                for (int j = 1; j < gameSize; j++) {
                    if((field[j-1][i] == sign) && (field[j][i] == sign)){
                        pos++;
                    }
                    if((pos == gameSize) ||(pos == 5)){
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
                    /*if (field[i][j] == sign && field[i + 1][j] == sign && field[i + 2][j] == sign && field[i + 3][j] == sign && field[i + 4][j] == sign) {
                        return true;
                    */
                    if((field[j][i-1] == sign) && (field[j][i] == sign)){
                        pos++;
                    }
                    if((pos == gameSize) ||(pos == 5)){
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
                        if (field[m][n] == sign){
                            pos++;
                        }
                        if((pos == gameSize) || (pos == 5)){
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
