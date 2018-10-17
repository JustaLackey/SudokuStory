package com.jhchang.simplesudoku;

public class Board {
    private String word;
    private int difficulty;
    private int id;

    public Board(String word, int difficulty, int id){
        this.word = word;
        this.difficulty = difficulty;
        this.id = id;
    }

    public String getWord() {
        return word;
    }
    public int getDifficulty() {
        return difficulty;
    }
    public int getId() {
        return id;
    }

}
