package com.jhchang.simplesudoku;

import java.util.*;

/**
 * Contains a valid Sudoku number and uses an image to display to the world.
 * May contain a zero to denote the fact that the cell is empty.
 *
 * @Noah Keck
 * @v1.2.2
 * @2/16/2018
 */
public class Cell
{
    public enum CellState{EMPTY, CLICKED, RCLICKED, GUESSED, FILLED, HARDSET};

    private Sudoku world;
    private int num, hiddenNum, timer, row, col, vState;
    private boolean fixed,error;
    private ArrayList<Integer> miniNums;
    private CellState state = CellState.EMPTY;

    public Cell(int row, int column)
    {
        this.row = row;
        col = column;
        num = 0;
        miniNums = new ArrayList<Integer>();
        fixed = true;
        error = false;
        vState = 0;
    }
    public int getNum(){return num;}
    public int getHiddenNum(){return hiddenNum;}
    public void setNum(int number)
    {
        num = number;
        if (num == 0){
            state = CellState.EMPTY;
        }
        else{
            hiddenNum = num;
            state = CellState.HARDSET;
        }
    }

    public boolean getFixed(){return fixed;}
    public void setFixed(boolean fix){
        fixed = fix;
    }

    public boolean getError(){return error;}
    public void setError(boolean err){
        error = err;
    }

    public int getvState() { return vState; }
    public void setvState(int vState) { this.vState = vState; }
}