package com.jhchang.simplesudoku;

public class Board {
    private String word, wType, definition;
    private int difficulty;
    private int id;

    public Board(String word, String wType, String definition, int difficulty, int id){
        this.word = word;
        this.wType = wType;
        this.definition = definition;
        this.difficulty = difficulty;
        this.id = id;
    }

    public String getWord() {
        return word;
    }
    public String getwType() { return wType; }
    public String getDefinition(){ return definition; }
    public int getDifficulty() {
        return difficulty;
    }
    public int getId() {
        return id;
    }

}
