package com.jhchang.simplesudoku;

public class Line {

    private String line;
    private int pos;
    private int timer;
    private String effect;
    private int speed;
    private int fontSize;
    private boolean clickable;
    private int boardID;
    private int id;

    public Line(String line, int pos, int timer,String effect, int speed, int fontSize, boolean clickable, int boardID, int id){
        this.line = line;
        this.pos = pos;
        this.timer = timer;
        this.effect = effect;
        this.speed = speed;
        this.fontSize = fontSize;
        this.clickable = clickable;
        this.boardID = boardID;
        this.id = id;
    }

    public String getLine() {
        return line;
    }
    public int getPos() {
        return pos;
    }
    public int getTimer() {
        return timer;
    }
    public String getEffect() {
        return effect;
    }
    public int getSpeed() {
        return speed;
    }
    public int getFontSize() {
        return fontSize;
    }
    public boolean isClickable() {
        return clickable;
    }
    public int getBoardID() {
        return boardID;
    }
    public int getId() {
        return id;
    }
}
