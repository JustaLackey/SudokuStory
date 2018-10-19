package com.jhchang.simplesudoku;


import android.graphics.Color;

import java.util.ArrayList;

public class SceneManager {

    private ArrayList<Scene> sceneList = new ArrayList<Scene>();


    private Values values = new Values();
    private int smallFont, medFont, bigFont,
            posTop, posTopMid, posMid, posBotMid, posBot,
            fasterText, fastText, normText, slowText,
            EASY_BOARD, MED_BOARD, HARD_BOARD,
            HIGHLIGHT_COLOR, NEIGHBOR_COLOR,FIXED_COLOR,ERROR_COLOR;

    public SceneManager() {
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

        createScenes();
    }

    private void createScenes(){
        // Init Basic Scene Values
        Scene currScene;

        //Scene 00
        currScene = new Scene(0);
        currScene.addLine("Every night you wonder whether you're asleep or dead.", posTop);
        currScene.addLine("Maybe this time you're dead.", posTopMid);
        currScene.addLine("DISMANTLE", posMid, "none", normText, bigFont,true,0);

        currScene.addBoard("DISMANTLE",HARD_BOARD);

        sceneList.add(currScene);

        //Scene 01
        currScene = new Scene(1);
        currScene.addLine("Your eyes flutter open", posTop);
        currScene.addLine("But there's nothing to see.", posTopMid);
        currScene.addLine("WALK", posMid, "none", normText, bigFont,true,0);

        currScene.addBoard("WALK",HARD_BOARD);

        sceneList.add(currScene);


    }

    /* use this if they get out of hand;
    private void scenesOneToTen(){

    }
    */

    public ArrayList<Scene> getSceneList() {
        return sceneList;
    }

    public Scene getScene(int i){
        return sceneList.get(i);
    }
}
