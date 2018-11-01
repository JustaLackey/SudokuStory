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
        currScene.addLine("1533, Wales",posTop);
        currScene.addLine("Brother Marcus mans the printing press for the monastery.", posTopMid);
        currScene.addLine("An important job, and one that Brother Marcus does not deserve. For he can not", posMid);
        currScene.addLine("READ", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("READ", "verb", "to comprehend something written, printed, etc.", HARD_BOARD);

        sceneList.add(currScene);

        //Scene 01
        currScene = new Scene(1);
        currScene.addLine("Brother Marcus knows the printing press inside out. He has never made a mistake.", posTop);
        currScene.addLine("Or at least, that is his", posTopMid);
        currScene.addLine("HOPE", posMid, "none", normText, bigFont,true,0);

        currScene.addBoard("HOPE", "noun", "the feeling that what is wanted can be had or that events will turn out for the best",HARD_BOARD);

        sceneList.add(currScene);

        //Scene 02
        currScene = new Scene(2);
        currScene.addLine("Brother Marcus works patiently with each page. Step-by-step, slow, but unerring.",posTop);
        currScene.addLine("He checks the copy, matches each symbol, confirms it once, twice, and then finally, sets the",posTopMid);
        currScene.addLine("TILE", posMid, "none", normText, bigFont,true,0);

        currScene.addBoard("TILE", "noun", "any of various similar slabs or pieces, as of linoleum, stone, rubber, or metal", MED_BOARD);

        sceneList.add(currScene);

        //Scene 03
        currScene = new Scene(3);
        currScene.addLine("The other monks praise his devotion to the letters.", posTop);
        currScene.addLine("None so diligent as he. None so", posTopMid);
        currScene.addLine("TENACIOUS", posMid, "none", normText, bigFont,true,0);

        currScene.addBoard("TENACIOUS","adjective","pertinacious, persistent, stubborn, or obstinate",EASY_BOARD);

        sceneList.add(currScene);

        //Scene 04
        currScene = new Scene(4);
        currScene.addLine("It kills him to hear their praise. He ducks his head, looks away.", posTop);
        currScene.addLine("His shame, they take for modesty, and so they praise him more.", posTopMid);
        currScene.addLine("He cannot help but feel a",posMid);
        currScene.addLine("FAKE", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("FAKE","adjective","not true, real, or genuine : counterfeit, sham",HARD_BOARD);

        sceneList.add(currScene);

        //Scene 05
        currScene = new Scene(5);
        currScene.addLine("He tries to learn. Has tried since the day he first became a monk.", posTop);
        currScene.addLine("He opens a book and tries to read, and it is like trying to hold water.", posTopMid);
        currScene.addLine("The letters twist and turn, jump from place to place out of order, and lead him around in a", posMid);
        currScene.addLine("LABYRINTH", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("LABYRINTH", "noun", "an intricate combination of paths in which it is difficult to find one's way", EASY_BOARD);

        sceneList.add(currScene);

        //Scene 06
        currScene = new Scene(6);
        currScene.addLine("The other monks gush over the works of others.",posTop);
        currScene.addLine("Their love of books is clear. It is all they speak of.",posTopMid);
        currScene.addLine("Beautiful, they call them and somehow Brother Marcus must hide his",posMid);
        currScene.addLine("REVULSION", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("REVULSION", "noun", "a strong feeling of repugnance, distaste, or dislike", EASY_BOARD);
        sceneList.add(currScene);

        //Scene 07
        currScene = new Scene(7);
        currScene.addLine("The lie can only last so long for Marcus.",posTop);
        currScene.addLine("His excuses not to share his reading become refusals. His evasions become avoidance.",posTopMid);
        currScene.addLine("The monks begin to whisper, talk, question and finally demand:",posMid);
        currScene.addLine("READ", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("READ", "verb", "to comprehend something written, printed, etc.", HARD_BOARD);
        sceneList.add(currScene);

        //Scene 08
        currScene = new Scene(8);
        currScene.addLine("Read for us, they say.",posTop);
        currScene.addLine("Why avoid it, they ask.",posTopMid);
        currScene.addLine("There is nothing to be afraid of. It is only letters on",posMid);
        currScene.addLine("PARCHMENT", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("PARCHMENT", "noun", "the skin of sheep, goats, etc., prepared for use as a material on which to write", MED_BOARD);
        sceneList.add(currScene);

        //Scene 09
        currScene = new Scene(9);
        currScene.addLine("Brother Marcus has nowhere to run, nowhere to hide.",posTop);
        currScene.addLine("He takes the parchment, wets his lips and when he opens his mouth, out comes",posTopMid);
        currScene.addLine("WIND", posMid, "none", normText, bigFont,true,0);

        currScene.addBoard("WIND", "noun", "air in natural motion", HARD_BOARD);
        sceneList.add(currScene);

        //Scene 10
        currScene = new Scene(10);
        currScene.addLine("Empty, meaningless wind. Nothing, but the heavy breath of a man who cannot read.",posTop);
        currScene.addLine("The monks realize the truth. Their quiet stares are all that need to be said.",posTopMid);
        currScene.addLine("There is only one punishment befitting the crime:",posMid);
        currScene.addLine("EXPULSION", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("EXPULSION", "noun", "the act of driving out or expelling", MED_BOARD);
        sceneList.add(currScene);

        //Scene 11
        currScene = new Scene(11);
        currScene.addLine("Brother Marcus is a Brother no more.",posTop);
        currScene.addLine("He is cast out from the monastery with naught but the clothes on his back.",posTopMid);
        currScene.addLine("All this, he accepts with a bowed head. This is justice. This is",posMid);
        currScene.addLine("FATE", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("FATE", "noun", "that which is inevitably predetermined; destiny", HARD_BOARD);
        sceneList.add(currScene);

        //Scene 12
        currScene = new Scene(12);
        currScene.addLine("Marcus wanders.",posTop);
        currScene.addLine("He calls himself a pilgrim, though he has no destination.",posTopMid);
        currScene.addLine("He manages to",posMid);
        currScene.addLine("LIVE", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("LIVE", "verb", "to continue to have life; remain alive", HARD_BOARD);
        sceneList.add(currScene);

        //Scene 13
        currScene = new Scene(13);
        currScene.addLine("A prayer for a coin, one day at a time, Marcus survives.",posTop);
        currScene.addLine("He travels the land, never stopping.",posTopMid);
        currScene.addLine("To towns, villages, and farms. On and on, he continues to",posMid);
        currScene.addLine("WALK", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("WALK", "verb", "to move about or travel on foot", HARD_BOARD);
        sceneList.add(currScene);

        //Scene 14
        currScene = new Scene(14);
        currScene.addLine("He watches the sun rise over castle walls, hills, fields, and glittering seas.",posTop);
        currScene.addLine("He meets all manner of folk. Farmers, nobles, and merchants. Kind and cruel alike.",posTopMid);
        currScene.addLine("Every moment of every day becomes a part of his",posMid);
        currScene.addLine("EDUCATION", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("EDUCATION", "noun", "the act or process of imparting or acquiring knowledge", MED_BOARD);
        sceneList.add(currScene);

        //Scene 15
        currScene = new Scene(15);
        currScene.addLine("Years pass, and Marcus begins to slow.",posTop);
        currScene.addLine("He has seen all that he had to.",posTopMid);
        currScene.addLine("He begins to",posMid);
        currScene.addLine("ACHE", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("ACHE", "verb", "to have or suffer a continuous, dull pain", HARD_BOARD);
        sceneList.add(currScene);

        //Scene 16
        currScene = new Scene(16);
        currScene.addLine("Marcus has learned much of the world and of its people.",posTop);
        currScene.addLine("He yearns to share that knowledge. To speak his thoughts for all to hear.",posTopMid);
        currScene.addLine("But he is only one man. Just a humble",posMid);
        currScene.addLine("MONK", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("MONK", "noun", "a man who is a member of a monastic order", HARD_BOARD);
        sceneList.add(currScene);

        //Scene 17
        currScene = new Scene(17);
        currScene.addLine("Speaking is not enough.",posTop);
        currScene.addLine("Words are",posTopMid);
        currScene.addLine("WIND", posMid, "none", normText, bigFont,true,0);

        currScene.addBoard("WIND", "noun", "air in natural motion", HARD_BOARD);
        sceneList.add(currScene);

        //Scene 18
        currScene = new Scene(18);
        currScene.addLine("Marcus needs more.",posTop);
        currScene.addLine("He needs something that would last.",posTopMid);
        currScene.addLine("He needs",posMid);
        currScene.addLine("PARCHMENT", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("PARCHMENT", "noun", "the skin of sheep, goats, etc., prepared for use as a material on which to write", MED_BOARD);
        sceneList.add(currScene);

        //Scene 19
        currScene = new Scene(19);
        currScene.addLine("To teach what he learned.",posTop);
        currScene.addLine("To reflect his thoughts.",posTopMid);
        currScene.addLine("To share his",posMid);
        currScene.addLine("SOUL", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("SOUL", "noun", "the principle of life, feeling, thought, and action in humans", HARD_BOARD);
        sceneList.add(currScene);

        //Scene 20
        currScene = new Scene(20);
        currScene.addLine("Print is not his first choice.",posTop);
        currScene.addLine("There is painting, there is song.",posTopMid);
        currScene.addLine("Things that he could do without torturing himself in the",posMid);
        currScene.addLine("MAZE", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("MAZE", "noun", "a confusing network of intercommunicating paths or passages", HARD_BOARD);
        sceneList.add(currScene);

        //Scene 21
        currScene = new Scene(21);
        currScene.addLine("But a painting can be misinterpreted.",posTop);
        currScene.addLine("A song distorted.",posTopMid);
        currScene.addLine("More than that however, is the fact that a word written is",posMid);
        currScene.addLine("READ", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("READ", "verb", "to utter aloud or render in speech (something written, printed, etc.)", HARD_BOARD);
        sceneList.add(currScene);

        //Scene 22
        currScene = new Scene(22);
        currScene.addLine("When a word is read,",posTop);
        currScene.addLine("It is not a singer’s voice the reader hears,",posTopMid);
        currScene.addLine("But the voice of them",posMid);
        currScene.addLine("SELF", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("SELF", "noun", "a person or thing referred to with respect to complete individuality", HARD_BOARD);
        sceneList.add(currScene);

        //Scene 23
        currScene = new Scene(23);
        currScene.addLine("His thoughts, they could make their own.",posTop);
        currScene.addLine("Each person who read his words would be making their own",posTopMid);
        currScene.addLine("DISCOVERY", posMid, "none", normText, bigFont,true,0);

        currScene.addBoard("DISCOVERY", "noun", "to see, get knowledge of, learn of, find, or find out", MED_BOARD);
        sceneList.add(currScene);

        //Scene 24
        currScene = new Scene(24);
        currScene.addLine("There was no other choice.",posTop);
        currScene.addLine("Marcus had to learn to read. To write.",posTopMid);
        currScene.addLine("For all those he would never meet face to face, he would",posMid);
        currScene.addLine("TALK", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("TALK", "verb", "to communicate or exchange ideas, information, etc.", HARD_BOARD);
        sceneList.add(currScene);

        //Scene 25
        currScene = new Scene(25);
        currScene.addLine("Marcus sat down with two sheets before him. One filled with letters, the other empty.",posTop);
        currScene.addLine("Already the letters squirmed and writhed like a thousand worms from a",posTopMid);
        currScene.addLine("NIGHTMARE", posMid, "none", normText, bigFont,true,0);

        currScene.addBoard("NIGHTMARE", "noun", "a terrifying dream", HARD_BOARD);
        sceneList.add(currScene);

        //Scene 26
        currScene = new Scene(26);
        currScene.addLine("Hours, days.",posTop);
        currScene.addLine("Time spent glaring at letters, forcing them to still and assemble.",posTopMid);
        currScene.addLine("But slowly, piece by piece, Marcus began to grasp the first",posMid);
        currScene.addLine("WORD", posBotMid, "none", normText, bigFont,true,0);

        currScene.addBoard("WORD", "noun", "a unit of language", MED_BOARD);
        sceneList.add(currScene);

        //Scene 27
        currScene = new Scene(27);
        currScene.addLine("Marcus began to",posTopMid);
        currScene.addLine("READ", posMid, "none", normText, bigFont,true,0);

        currScene.addBoard("READ", "verb", "to utter aloud or render in speech (something written, printed, etc.)", EASY_BOARD);
        sceneList.add(currScene);

        //Scene 28
        currScene = new Scene(28);
        currScene.addLine("And then once he was ready,",posTop);
        currScene.addLine("Marcus began to",posTopMid);
        currScene.addLine("WRITE", posMid, "none", slowText, bigFont,true,-1);
        sceneList.add(currScene);

        //Scene 29
        currScene = new Scene(29);
        currScene.addLine("END", posMid, "none", slowText, bigFont,true,-1);
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
