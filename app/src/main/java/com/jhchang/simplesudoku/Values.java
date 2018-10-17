package com.jhchang.simplesudoku;

import android.graphics.Color;

public class Values {



    private final int smallFont = 32;
    private final int medFont = 64;
    private final int bigFont = 100;

    private final int posTop = 0;
    private final int posTopMid = 1;
    private final int posMid = 2;
    private final int posBotMid = 3;
    private final int posBot = 4;

    private final int fasterText = 3;
    private final int fastText = 6;
    private final int normText = 24;
    private final int slowText = 58;

    private final int EASY_BOARD = 0;
    private final int MED_BOARD = 1;
    private final int HARD_BOARD = 2;

    private final int HIGHLIGHT_COLOR = Color.argb(255,0,0,255);
    private final int NEIGHBOR_COLOR = Color.argb(255,200,200,200);
    private final int FIXED_COLOR = Color.argb(255,50,50,50);
    private final int ERROR_COLOR = Color.RED;

    public Values(){
        
    }

    public int getSmallFont() {
        return smallFont;
    }

    public int getMedFont() {
        return medFont;
    }

    public int getBigFont() {
        return bigFont;
    }

    public int getPosTop() {
        return posTop;
    }

    public int getPosTopMid() {
        return posTopMid;
    }

    public int getPosMid() {
        return posMid;
    }

    public int getPosBotMid() {
        return posBotMid;
    }

    public int getPosBot() {
        return posBot;
    }

    public int getFasterText() {
        return fasterText;
    }

    public int getFastText() {
        return fastText;
    }

    public int getNormText() {
        return normText;
    }

    public int getSlowText() {
        return slowText;
    }

    public int getEASY_BOARD() {
        return EASY_BOARD;
    }

    public int getMED_BOARD() {
        return MED_BOARD;
    }

    public int getHARD_BOARD() {
        return HARD_BOARD;
    }

    public int getHIGHLIGHT_COLOR() {
        return HIGHLIGHT_COLOR;
    }

    public int getNEIGHBOR_COLOR() {
        return NEIGHBOR_COLOR;
    }

    public int getFIXED_COLOR() {
        return FIXED_COLOR;
    }

    public int getERROR_COLOR() {
        return ERROR_COLOR;
    }
}
