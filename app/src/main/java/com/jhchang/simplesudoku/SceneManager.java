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
        currScene.addLine("Hey... want to learn magic?",posTop);
        currScene.addLine("It's simple. You just say the word.", posTopMid);
        currScene.addLine("The word is...", posMid);
        currScene.addLine("WORD", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("WORD", "noun", "a unit of language", HARD_BOARD);

        sceneList.add(currScene);

        //Scene 01
        currScene = new Scene(1);
        currScene.addLine("WORD is a very magical word.", posTop);
        currScene.addLine("I mean it. Really. It's...", posTopMid);
        currScene.addLine("TRUE", posMid, "none", normText, bigFont,true,0);

        currScene.addBoard("TRUE", "adjective", "conforming to reality or fact",HARD_BOARD);

        sceneList.add(currScene);

        //Scene 02
        currScene = new Scene(2);
        currScene.addLine("You don't believe me.",posTop);
        currScene.addLine("You don't, I can tell. But that's okay.",posTopMid);
        currScene.addLine("Magic words are everywhere. Magic words are...",posMid);
        currScene.addLine("UNIVERSAL", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("UNIVERSAL", "adjective", "applicable everywhere or in all cases", MED_BOARD);

        sceneList.add(currScene);

        //Scene 03
        currScene = new Scene(3);
        currScene.addLine("And what makes them magical?", posTop);
        currScene.addLine("Why it's their power to...", posTopMid);
        currScene.addLine("MAKE", posMid, "none", normText, bigFont,true,0);

        currScene.addBoard("MAKE","verb","to produce; cause to exist or happen",HARD_BOARD);

        sceneList.add(currScene);

        //Scene 04
        currScene = new Scene(4);
        currScene.addLine("If I use the word LAKE. A lake is created.", posTop);
        currScene.addLine("If I say the air is crisp and the water is frozen.", posTopMid);
        currScene.addLine("If I say the snow crunches beneath your step and your cheeks flush red, then it becomes...",posMid);
        currScene.addLine("COLD", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("COLD","adjective","having little or no warmth",HARD_BOARD);

        sceneList.add(currScene);

        //Scene 05
        currScene = new Scene(5);
        currScene.addLine("Did you remember a moment of winter?", posTop);
        currScene.addLine("Was there a chill?", posTopMid);
        currScene.addLine("Did you feel...", posMid);
        currScene.addLine("SOMETHING", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("SOMETHING,", "noun", "a certain undetermined or unspecified thing", MED_BOARD);

        sceneList.add(currScene);

        //Scene 06



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
