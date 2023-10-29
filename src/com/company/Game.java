package com.company;

import java.net.IDN;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {
    // define movement kind
    public enum Move {
        NONE, UP, DOWN, LEFT, RIGHT
    }

    static boolean isGameOver = false;
    // board dimension
    static int iDimension = 4;
    // board value 2d-array
    static int[][] arrDimension;
    static Random random = new Random();
    // the list to get empty cell
    static List<Integer> listEmptyCells = new ArrayList<>();
    static boolean is1024 = false;
    static int      i1024Count = 0;

    public static void printf(String format, Object... obj) {
        System.out.printf(format, obj);
    }

    public static void print(String obj){
        System.out.print(obj);
    }

    public static void println(String obj){
        System.out.println(obj);
    }

    public static void clearScreen() {
        for(int i = 0; i < 1; i++) {
            println("");
        }
    }

    // get random number in (min, max)
    public static int rand(int min, int max){
        return random.nextInt((max - min) + 1) + min;
    }

    // get random number in (0, max)
    public static int rand(int max){
        return random.nextInt(max + 1);
    }

    // Set the 1 value in random cell
    // if isNewGame is true, should set two values
    public static void setRandomValue(int iDimension, int[][] arrTile, boolean isNewGame){
        int iX, iY;
        int iIndex, iValue;

        setArrEmptyCells(arrDimension);

        if (isNewGame){
            iIndex = rand(listEmptyCells.size()-1);
            iValue = listEmptyCells.get(iIndex).intValue();
            iX = iValue % iDimension;
            iY = iValue / iDimension;
            arrTile[iY][iX] = 1;
            listEmptyCells.remove(iIndex);

            iIndex = rand(listEmptyCells.size()-1);
            iValue = listEmptyCells.get(iIndex).intValue();
            iX = iValue % iDimension;
            iY = iValue / iDimension;
            arrTile[iY][iX] = 1;
            listEmptyCells.remove(iIndex);
            return;
        }

        iIndex = rand(listEmptyCells.size()-1);
        iValue = listEmptyCells.get(iIndex).intValue();
        iX = iValue % iDimension;
        iY = iValue / iDimension;
        arrTile[iY][iX] = 1;
        listEmptyCells.remove(iIndex);
    }

    // Get Empty Cell list from the board
    public static void setArrEmptyCells(int[][] arrTile)
    {
        int     i, j;
        int     iValue;

        listEmptyCells.clear();
        for(i = 0; i < iDimension; i++) {
            for(j = 0; j < iDimension; j++) {
                if(arrTile[i][j] == 0)
                {
                    iValue = i * iDimension + j;
                    listEmptyCells.add(iValue);
                }
            }
        }
    }

    // check the value is 1024
    public static void check1024(int iValue){
        if (iValue == 1024){
            is1024 = true;
        }
    }

    // Draw the Board
    public static void printBoard(int iDimension, int[][] arrTile){
        int     i, j;
        String  strFormat;
        int     iLargestLength = 4;

        // print box
        for(i = 1; i <= (3+iLargestLength)*iDimension; i++){
            if (i == 1){
                print("|-");
            }else if (i == (3+iLargestLength)*iDimension){
                print("|");
            }else{
                print("-");
            }
        }
        println("");

        for(i = 0; i < iDimension; i++){
            for(j = 0; j < iDimension; j++){
                if (j == iDimension-1){
                    if(arrTile[i][j] == 0)
                        strFormat = String.format("| %-" + iLargestLength + "s |", "");
                    else
                        strFormat = String.format("| %-" + iLargestLength + "d |", arrTile[i][j]);
                }else{
                    if(arrTile[i][j] == 0)
                        strFormat = String.format("| %-" + iLargestLength + "s ", "");
                    else
                        strFormat = String.format("| %-" + iLargestLength + "d ", arrTile[i][j]);
                }
                print(strFormat);
            }
            println("");
            for(j = 1; j <= (3+iLargestLength)*iDimension; j++){
                if (j == 1){
                    print("|-");
                }else if (j == (3+iLargestLength)*iDimension){
                    print("|");
                }else{
                    print("-");
                }
            }
            println("");
        }
        println("");
        println("Type w,s,a,d or h(home) to slide up/down/left/right!");
    }

    // Move Logic
    public static boolean SlideValue(int iDimension, int[][] arrTile, Move iSlideDirection, boolean bOnlyCheck){
        int         i, j, x, y;
        int         iStep;
        if (iSlideDirection == Move.UP){
            iStep = 0;
            for(i = 0; i < iDimension; i++){
                for(x = 1; x <= iDimension; x++){
                    for(y = iDimension-1; y >= 0; y--){
                        if (y-1 >= 0 && arrDimension[y-1][i] == 0 && arrDimension[y][i] != 0){
                            if (!bOnlyCheck){
                                arrDimension[y-1][i] = arrDimension[y][i];
                                arrDimension[y][i] = 0;
                            }
                            iStep++;
                        }
                    }
                }

                for(j = 0; j < iDimension; j++){
                    if (j+1 < iDimension && arrDimension[j][i] == arrDimension[j+1][i] &&
                            arrDimension[j][i] != 0 && arrDimension[j+1][i] != 0){
                        if (!bOnlyCheck){
                            arrDimension[j][i] += arrDimension[j+1][i];
                            arrDimension[j+1][i] = 0;
                            check1024(arrDimension[j][i]);
                        }
                        iStep++;
                    }
                }

                for(x = 1; x <= iDimension; x++){
                    for(y = iDimension-1; y >= 0; y--){
                        if (y-1 >= 0 && arrDimension[y-1][i] == 0 && arrDimension[y][i] != 0){
                            if (!bOnlyCheck){
                                arrDimension[y-1][i] = arrDimension[y][i];
                                arrDimension[y][i] = 0;
                            }
                            iStep++;
                        }
                    }
                }
            }

            return iStep != 0;
        }else if (iSlideDirection == Move.DOWN){
            iStep = 0;
            for(i = 0; i < iDimension; i++){
                for(x = 1; x <= iDimension; x++){
                    for(y = 0; y < iDimension; y++){
                        if (y+1 < iDimension && arrDimension[y+1][i] == 0 && arrDimension[y][i] != 0){
                            if (!bOnlyCheck){
                                arrDimension[y+1][i] = arrDimension[y][i];
                                arrDimension[y][i] = 0;
                            }
                            iStep++;
                        }
                    }
                }

                for(j = iDimension-1; j >= 0; j--){
                    if (j-1 >= 0 && arrDimension[j][i] == arrDimension[j-1][i] && arrDimension[j][i] != 0 &&
                            arrDimension[j-1][i] != 0){
                        if (!bOnlyCheck){
                            arrDimension[j-1][i] += arrDimension[j][i];
                            arrDimension[j][i] = 0;
                            check1024(arrDimension[j-1][i]);
                        }
                        iStep++;
                    }
                }

                for(x = 1; x <= iDimension; x++){
                    for(y = 0; y < iDimension; y++){
                        if (y+1 < iDimension && arrDimension[y+1][i] == 0 && arrDimension[y][i] != 0){
                            if (!bOnlyCheck){
                                arrDimension[y+1][i] = arrDimension[y][i];
                                arrDimension[y][i] = 0;
                            }
                            iStep++;
                        }
                    }
                }
            }

            return iStep != 0;
        }else if (iSlideDirection == Move.LEFT){
            iStep = 0;
            for(i = 0; i < iDimension; i++){
                for(x = 1; x <= iDimension; x++){
                    for(j = iDimension-1; j >= 0; j--){
                        if (j-1 >= 0 && arrDimension[i][j-1] == 0 && arrDimension[i][j] != 0){
                            if (!bOnlyCheck){
                                arrDimension[i][j-1] = arrDimension[i][j];
                                arrDimension[i][j] = 0;
                            }
                            iStep++;
                        }
                    }
                }

                for(j = 0; j < iDimension; j++){
                    if (j+1 < iDimension && arrDimension[i][j+1] == arrDimension[i][j] &&
                            arrDimension[i][j] != 0 && arrDimension[i][j+1] != 0){
                        if (!bOnlyCheck){
                            arrDimension[i][j] += arrDimension[i][j+1];
                            arrDimension[i][j+1] = 0;
                            check1024(arrDimension[i][j]);
                        }
                        iStep++;
                    }
                }

                for(x = 1; x <= iDimension; x++){
                    for(j = iDimension-1; j >= 0; j--){
                        if (j-1 >= 0 && arrDimension[i][j-1] == 0 && arrDimension[i][j] != 0){
                            if (!bOnlyCheck){
                                arrDimension[i][j-1] = arrDimension[i][j];
                                arrDimension[i][j] = 0;
                            }
                            iStep++;
                        }
                    }
                }
            }

            return iStep != 0;
        }else if (iSlideDirection == Move.RIGHT){
            iStep = 0;
            for(i = 0; i < iDimension; i++){
                for(x = 1; x <= iDimension; x++){
                    for(j = 0; j < iDimension; j++){
                        if (j+1 < iDimension && arrDimension[i][j+1] == 0 && arrDimension[i][j] != 0){
                            if (!bOnlyCheck){
                                arrDimension[i][j+1] = arrDimension[i][j];
                                arrDimension[i][j] = 0;
                            }
                            iStep++;
                        }
                    }
                }

                for(j = iDimension-1; j >= 0; j--){
                    if (j-1 >= 0 && arrDimension[i][j-1] == arrDimension[i][j] && arrDimension[i][j] != 0 &&
                            arrDimension[i][j-1] != 0){
                        if (!bOnlyCheck){
                            arrDimension[i][j] += arrDimension[i][j-1];
                            arrDimension[i][j-1] = 0;
                            check1024(arrDimension[i][j]);
                        }
                        iStep++;
                    }
                }

                for(x = 1; x <= iDimension; x++){
                    for(j = 0; j < iDimension; j++){
                        if (j+1 < iDimension && arrDimension[i][j+1] == 0 && arrDimension[i][j] != 0){
                            if (!bOnlyCheck){
                                arrDimension[i][j+1] = arrDimension[i][j];
                                arrDimension[i][j] = 0;
                            }
                            iStep++;
                        }
                    }
                }
            }

            return iStep != 0;
        }
        return true;
    }

    public static void main(String[] args) {
        // input string
        String strInput = "";

        Scanner scanner = new Scanner(System.in);
        do {
            is1024 = false;
            isGameOver = false;
            boolean bIsError = false;

            // Input the dimension
            do{
                try{
                    bIsError = false;
                    print("Input dimension (Default: 4, 10): ");
                    strInput = scanner.nextLine();

                    iDimension = Integer.parseInt(strInput);

                    if (iDimension > 10){
                        println("Max board dimension is 10!");
                        bIsError = true;
                    }

                    if (iDimension < 4){
                        println("Min board dimension is 4!");
                        bIsError = true;
                    }
                }catch(Exception ex){
                    if (strInput.isEmpty() || strInput.matches("[ ]{1,}")){
                        iDimension = 4;
                        bIsError = false;
                    }else{
                        println("Invalid input!");
                        bIsError = true;
                    }
                }
            }while(bIsError);

            // Initialize the array
            arrDimension = new int[iDimension][iDimension];

            // Playing Game
            clearScreen();
            setRandomValue(iDimension, arrDimension, true);
            do {
                clearScreen();
                if(is1024 && i1024Count <= 0)
                {
                    i1024Count++;
                    println("You Won!");
                    if(listEmptyCells.isEmpty())
                    {
                        break;
                    }
                    setRandomValue(iDimension, arrDimension, false);
                }

                printBoard(iDimension, arrDimension);
                print("Input: ");
                strInput = scanner.nextLine();

                if(strInput.equalsIgnoreCase("h"))
                    break;

                // check GameOver
                if (!SlideValue(iDimension, arrDimension, Move.UP, true) &&
                        !SlideValue(iDimension, arrDimension, Move.DOWN, true) &&
                        !SlideValue(iDimension, arrDimension, Move.LEFT, true) &&
                        !SlideValue(iDimension, arrDimension, Move.RIGHT, true)){
                    isGameOver = true;
                }

                if (strInput.equalsIgnoreCase("w")){
                    if (SlideValue(iDimension, arrDimension, Move.UP, false))
                        setRandomValue(iDimension, arrDimension, false);
                }else if (strInput.equalsIgnoreCase("s")){
                    if (SlideValue(iDimension, arrDimension, Move.DOWN, false))
                        setRandomValue(iDimension, arrDimension, false);
                }else if (strInput.equalsIgnoreCase("a")){
                    if (SlideValue(iDimension, arrDimension, Move.LEFT, false))
                        setRandomValue(iDimension, arrDimension, false);
                }else if (strInput.equalsIgnoreCase("d")){
                    if (SlideValue(iDimension, arrDimension, Move.RIGHT, false))
                        setRandomValue(iDimension, arrDimension, false);
                }else if (strInput.equalsIgnoreCase("h")){
                    break;
                }else{
                    System.out.print("Invalid Input! Please input w,s,a,d or h(Home)!\n");
                    System.out.print("Press any key to continue...");
                    strInput = scanner.nextLine();
                    if(strInput.equalsIgnoreCase("h"))
                        break;
                }
            }while(!isGameOver);

            clearScreen();
            println("GAME OVER!");
            print("Are you want to play again? [Y/N]: ");

            strInput = scanner.nextLine();
        }while(strInput.equalsIgnoreCase("y"));

        scanner.close();

        println("GOOD BYE");
        System.exit(0);
    }
}
