package com.jhchang.simplesudoku;

import java.util.ArrayList;

public class Scene {
    private ArrayList<Line> lines = new ArrayList<Line>();
    private ArrayList<Board> boards = new ArrayList<Board>();
    private int id;


    private Values values = new Values();
    private int smallFont, medFont, bigFont,
            posTop, posTopMid, posMid, posBotMid, posBot,
            fasterText, fastText, normText, slowText,
            EASY_BOARD, MED_BOARD, HARD_BOARD,
            HIGHLIGHT_COLOR, NEIGHBOR_COLOR,FIXED_COLOR,ERROR_COLOR;

    public Scene(int id){
        this.id = id;

        this.smallFont = values.getSmallFont();
        this.medFont = values.getMedFont();
        this.bigFont = values.getBigFont();

        this.posTop = values.getPosTop();
        this.posTopMid = values.getPosTopMid();
        this.posMid = values.getPosMid();
        this.posBotMid = values.getPosBotMid();
        this.posBot = values.getPosBot();

        this.fasterText = values.getFasterText();
        this.fastText = values.getFastText();
        this.normText = values.getNormText();
        this.slowText = values.getSlowText();

        this.EASY_BOARD = values.getEASY_BOARD();
        this.MED_BOARD = values.getMED_BOARD();
        this.HARD_BOARD = values.getHARD_BOARD();

        this.HIGHLIGHT_COLOR = values.getHIGHLIGHT_COLOR();
        this.NEIGHBOR_COLOR = values.getNEIGHBOR_COLOR();
        this.FIXED_COLOR = values.getFIXED_COLOR();
        this.ERROR_COLOR = values.getERROR_COLOR();
    }

    public void addLine(String line, int pos){
        int id = lines.size(); //check that this works for size 0
        Line currLine = new Line(line,pos,id,"none",fastText,medFont,false,-1,id);
        lines.add(currLine);
    }

    public void addLine(String line, int pos,String effect, int speed, int fontSize, boolean clickable, int boardID){
        int id = lines.size(); //check that this works for size 0
        Line currLine = new Line(line,pos,id,effect,speed,fontSize,clickable,boardID,id);
        lines.add(currLine);
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public void addBoard(String word, int difficulty){
        int id = boards.size();
        word = word.toUpperCase();
        Board currBoard = new Board(word,difficulty,id);
        boards.add(currBoard);
    }

    public ArrayList<Board> getBoards() {
        return boards;
    }

    public Board getThisBoard(int num) {
        return boards.get(num);
    }
}
