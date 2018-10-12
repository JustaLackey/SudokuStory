package com.jhchang.simplesudoku;


/* Java program for Sudoku generator  */
// NOTE, THIS ONLY WORKS WITH NUMBERS FACTORABLE BY THEMSELVES, 9x9, 4x4
// does not work with 6x6

import java.util.*;
import static java.lang.System.*;

public class Sudoku
{
    private int difficulty = 40; // number of starting numbers, lower is harder, higher is easier
    private final int chances = 10;
    private final int startingClues = 81;
    private Cell[][] board;
    private boolean[][] tried;
    private long startTime;
    public Random rand;
    public static int size, numberOfSolutions;

    /**
     * Use this constructor to set the default size of board
     */

    /**
     * Creates a new SodokuBoard based on given size used as a multiplier
     *
     * @param size values 1 through 4, 1 is the smallest, 4 is the largest
     */
    public Sudoku(int size, int diff)
    {
        this.size = size;
        if(diff > 1){
            this.difficulty = Math.round(2*(size*size)/5);
        }else if(diff > 0){
            this.difficulty = Math.round((size*size)/2);
        }else{
            this.difficulty = Math.round(3*(size*size)/5);
        }
        rand = new Random();
        createBoard();
    }


    public void createCells()
    {
        board = new Cell[size][size];
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[0].length; j++){
                board[j][i] = new Cell(j,i);
                //addition of different factors of the location:
                //offset due to grid walls + current position (in array) + undo the image centering (done automatically by greenfoot)
            }
        }
    }

    /**
     * Sudoku creation will be done in the order of these steps:
     *
     * 1. Assign values to each cell in the grid.
     * 2. Set the images to each cell.
     * 3. Remove images in groups, ensuring that the puzzle only has one solution
     * 4. Puzzle is completed once 20-30 clues / images remain
     */
    public void createBoard()
    {
        do{
            createCells();
            setNums(0,0);
            startTime = System.nanoTime();
            tried = new boolean[size][size];
        }while(/*!removeNums(rand.nextInt(9), rand.nextInt(9), startingClues, chances, convertToMatrix())*/!removeNums());
    }

    /**
     * Sets a blank grid of cells to numbers that follow sudoku rules using recursion.
     *
     * @param r current row
     * @param c current column
     */
    public boolean setNums(int r, int c)
    {
        if (r >= size)
            return true;
        if (c >= size)
            return setNums(r+1, 0);

        ArrayList<Integer> nums = new ArrayList<Integer>();
        for(int i=0;i<size;i++){
            nums.add(i+1);
        }
        //ArrayList<Integer> nums = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        int num = 0;
        while (true){
            board[r][c].setNum(0);
            if (nums.isEmpty())
                return false;
            int index = rand.nextInt(nums.size());
            num = nums.get(index);
            nums.remove(index);
            if (checkSquare(r, c, num) && checkRow(r, num) && checkColumn(c, num)){
                board[r][c].setNum(num);
                if ( setNums(r, c+1) )
                    break;
            }
        }
        return true;
    }

    /**
     * Converts to a matrix of integers first. Then removes a specific group of numbers based on the clue count.
     * It checks that the removed numbers leave one unique solution. If it does, it also removes this from the board.
     * If it doesn't work, it recreates the integer matrix and trys again.
     */
    public boolean removeNums()
    {
        int clueCount = size*size, r = 0, c = 0; //clue count only works with perfect squares, adjust for 6x6
        int[][] b = convertToMatrix();
        while (clueCount > difficulty){ //this doesn't work for 4x4 or 6x6
            while (true){
                r = rand.nextInt(size);
                c = rand.nextInt(size);
                if(clueCount > difficulty+Math.round(3*(size*size)/10)){ //groups of 4
                    if (r != (size-1) && c != (size-1) && b[r][c] != 0 && b[r][c+1] != 0 && b[r+1][c] != 0 && b[r+1][c+1] != 0){
                        for (int i = 0; i < 2; i++){
                            b[r][c+i] = 0;
                            b[r+1][c+i] = 0;
                        }
                        break;
                    }
                }
                else if (clueCount > difficulty+Math.round(1*(size*size)/5)){ //groups of 2
                    if (c != (size-1) && b[r][c] != 0 && b[r][c+1] != 0){ //extends to the right
                        for (int i = 0; i < 2; i++)
                            b[r][c+i] = 0;
                        break;
                    }
                    else if (r != (size-1) && b[r][c] != 0 && b[r+1][c] != 0){ //extends below
                        for (int i = 0; i < 2; i++)
                            b[r+i][c] = 0;
                        break;
                    }
                }
                else if (b[r][c] != 0){ //1 by 1
                    b[r][c] = 0;
                    break;
                }
            }

            numberOfSolutions = 0;
            try{
                checkForOneSolution(0,0,b.clone());
            }
            catch(Exception e){
                //System.out.println(e);
            }

            if (numberOfSolutions <= 1){
                for(int i = 0; i < board.length; i++){
                    for(int j = 0; j < board[i].length; j++){
                        board[i][j].setNum(b[i][j]);
                        if(b[i][j] == 0){
                            board[i][j].setFixed(false);
                        }

                    }
                }
                clueCount -= clueCount > difficulty+Math.round(3*(size*size)/10) ? 4 : clueCount > difficulty+Math.round(1*(size*size)/5) ? 2 : 1;
            }
            else
                b = convertToMatrix();

            if (System.nanoTime() - startTime > Math.pow(10, 9) * 5){ // break out of loop if 5 seconds pass
                out.println("Failed to reduce puzzle. Reached a clue count of: " + clueCount);
                return false;
            }
        }
        return true;
    }

    /**
     *
     */
    public boolean removeNums(int r, int c, int clueCount, int remain, int[][] boardCopy)
    {
        if (remain <= 0)
            return false;
        if (tried[r][c])
            return removeNums(rand.nextInt(size), rand.nextInt(size), clueCount, remain, boardCopy);
        if (clueCount <= difficulty){
            for(int i = 0; i < board.length; i++){
                for(int j = 0; j < board[i].length; j++){
                    board[i][j].setNum(boardCopy[i][j]);
                    if(boardCopy[i][j] == 0){
                        board[i][j].setFixed(false);
                    }
                }
            }
            return true;
        }
        int[][] b = boardCopy.clone();
        boolean right = false;
        if(clueCount > Math.round(3*(size*size)/5)){ //groups of 4
            if (r != (size-1) && c != (size-1) && !tried[r][c+1] && !tried[r+1][c] && !tried[r+1][c+1]){
                for (int i = 0; i < 2; i++){
                    b[r][c+i] = 0;
                    b[r+1][c+i] = 0;
                    tried[r][c+i] = true;
                    tried[r+1][c+i] = true;
                }
            }
            else
                return removeNums(rand.nextInt(size), rand.nextInt(size), clueCount, remain, boardCopy);
        }
        else if (clueCount > Math.round((size*size)/2)){ //groups of 2
            if (c != (size-1) && !tried[r][c+1]){ //extends to the right
                for (int i = 0; i < 2; i++){
                    b[r][c+i] = 0;
                    tried[r][c+1] = true;
                }
                right = true;
            }
            else if (r != (size-1) && !tried[r+1][c]){ //extends below
                for (int i = 0; i < 2; i++){
                    b[r+i][c] = 0;
                    tried[r+i][c] = true;
                }
            }
            else
                return removeNums(rand.nextInt(size), rand.nextInt(size), clueCount, remain, boardCopy);
        }
        else{ //1 by 1
            b[r][c] = 0;
            tried[r][c] = true;
        }

        numberOfSolutions = 0;
        try{
            checkForOneSolution(0,0,b.clone());
        }
        catch(Exception e){
            //System.out.println(e);
        }
        System.out.println("Sol: " + numberOfSolutions + " Clues: " + clueCount);
        if (numberOfSolutions <= 1)
            if (removeNums(rand.nextInt(size), rand.nextInt(size), clueCount - (clueCount > Math.round(3*(size*size)/5) ? 4 : clueCount > Math.round((size*size)/2) ? 2 : 1), chances, b))
                return true;
        //if this removal did not work currently or in the future, it will the execute the following code

        if (clueCount > Math.round(3*(size*size)/5)){ //reset booleans that weren't tried back to normal
            tried[r][c+1] = false;
            tried[r+1][c] = false;
            tried[r+1][c+1] = false;
        }
        else if (clueCount > Math.round((size*size)/2)){
            if (right)
                tried[r][c+1] = false;
            else
                tried[r+1][c] = false;
        }
        return removeNums(rand.nextInt(size), rand.nextInt(size), clueCount, --remain, boardCopy);
    }

    /**
     * This method should check every possible combination of numbers to see if its a valid solution.
     * It should finally return true when the first blank number it checked runs out of numbers to check.
     *
     * @param r current row
     * @param c current column
     * @param boardCopy the numbers remaining in a sudoku grid
     */
    public boolean checkForOneSolution(int r, int c, int[][] boardCopy) throws Exception
    {
        if (r >= size){
            if (++numberOfSolutions > 1)
                throw new Exception("Found more than one solution.");
            return false;
        }
        if (c >= size)
            return checkForOneSolution(r+1, 0, boardCopy);
        if (boardCopy[r][c] != 0) //already has a valid number
            return checkForOneSolution(r, c+1, boardCopy);
        int num = 0;
        while (true){
            boardCopy[r][c] = 0;
            num++;
            if (num >= size+1) //no more numbers
                return true;
            if (checkSquare(r, c, num, boardCopy) && checkRow(r, num, boardCopy) && checkColumn(c, num, boardCopy)){
                boardCopy[r][c] = num;
                checkForOneSolution(r, c+1, boardCopy);
            }
        }
    }

    private int[][] convertToMatrix()
    {
        int[][] boardCopy = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[i].length; j++)
                boardCopy[i][j] = board[i][j].getNum();
        return boardCopy;
    }

    public boolean checkSquare(int r, int c, int num)
    {
        return checkSquare(r, c, num, convertToMatrix());
    }

    public boolean checkRow(int r, int num)
    {
        return checkRow(r, num, convertToMatrix());
    }

    public boolean checkColumn(int c, int num)
    {
        return checkColumn(c, num, convertToMatrix());
    }

    /**
     * @param r the row of cell 0-8
     * @param c the column of cell 0-8
     * @param num the number its checking for 1-9
     */
    public boolean checkSquare(int r, int c, int num, int[][] boardCopy)
    {

        ArrayList<int[]> breakNine = new ArrayList<int[]>(); //break points for 9x9, though this is ugly, I think this faster than doing it with a loop
        int[] nb = {3,3}; breakNine.add(nb); nb = new int[] {6,3}; breakNine.add(nb); nb = new int[] {9,3}; breakNine.add(nb);
        nb = new int[] {3,6}; breakNine.add(nb); nb = new int[] {6,6}; breakNine.add(nb); nb = new int[] {9,6}; breakNine.add(nb);
        nb = new int[] {3,9}; breakNine.add(nb); nb = new int[] {6,9}; breakNine.add(nb); nb = new int[] {9,9}; breakNine.add(nb);
        int bWidth = 3;
        int bHeight = 3;

        ArrayList<int[]> breakSix = new ArrayList<int[]>(); //break points for 6x6
        int[] sb = {3,2}; breakSix.add(sb); sb = new int[] {6,2}; breakSix.add(sb);
        sb = new int[] {3,4}; breakSix.add(sb);sb = new int[] {6,4}; breakSix.add(sb);
        sb = new int[] {3,6}; breakSix.add(sb);sb = new int[] {6,6}; breakSix.add(sb);

        ArrayList<int[]> breakFour = new ArrayList<int[]>(); // break points for 4x4
        int[] fb = {2,2}; breakFour.add(fb); fb = new int[] {4,2}; breakFour.add(fb);
        fb = new int[] {2,4}; breakFour.add(fb); fb = new int[] {4,4}; breakFour.add(fb);

        /*
        if (r < 3)
            r = 0;
        else if (r < 6)
            r = 3;
        else
            r = 6;
        if (c < 3)
            c = 0;
        else if (c < 6)
            c = 3;
        else
            c = 6;
        for (int i = r; i < r+3; i++){
            for (int j = c; j < c+3; j++){
                if (boardCopy[i][j] == num)
                    return false;
            }
        }
        return true;
        */
        ArrayList<int[]> breakPoints;
        int[] bPoint = new int[2];
        if(size == 9){
            breakPoints = breakNine;
        }else if(size == 6){
            breakPoints = breakSix;
            bHeight = 2;
        }else if(size == 4){
            breakPoints = breakFour;
            bWidth = 2;
            bHeight = 2;
        }else{
            breakPoints = breakNine;
        }
        for(int b = 0;b<breakPoints.size();b++){
            if(r< breakPoints.get(b)[0] & c< breakPoints.get(b)[1]){
                bPoint = breakPoints.get(b);
                //System.out.println("break, x: "+bPoint[0]+", y: "+bPoint[1]);
                break;
            }
        }
        for(int xx = bPoint[0]-bWidth;xx<bPoint[0];xx++){
            for(int yy = bPoint[1]-bHeight;yy<bPoint[1];yy++){
                if (boardCopy[xx][yy] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @param r the row of cell 0-8
     * @param num the number its checking for 1-9
     */
    public boolean checkRow(int r, int num, int[][] boardCopy)
    {
        for (int value : boardCopy[r])
            if (value == num)
                return false;
        return true;
    }

    /**
     * @param c the column of cell 0-8
     * @param num the number its checking for 1-9
     */
    public boolean checkColumn(int c, int num, int[][] boardCopy)
    {
        for (int[] row : boardCopy)
            if (row[c] == num)
                return false;
        return true;
    }

    public Cell[][] getBoard() {
        return board;
    }
    //graphics based methods
}
