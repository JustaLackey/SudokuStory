package com.jhchang.simplesudoku;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class InfiniteView extends SurfaceView implements Runnable {


    volatile boolean playing;
    private Thread gameThread = null;
    private SceneManager sceneManager;
    private Scene sceneObj;
    private Line lineObj;
    private Board boardObj;
    private Sudoku sudokuGen;

    private ArrayList<Line> lineList = new ArrayList<Line>();

    private Paint paint, boardPaint, scenePaint, numPaint,transPaint, devPaint, toolsPaint, defPaint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private boolean iprogFlag, bsFlag,moveFlag,effectFlag,refreshFlag, winFlag;
    //a screenX holder
    private int screenX, screenY, level, bgColor,fontColor, iProg, currScene, boxSize, boardSelect,
            selectStartY, toolStartY, winCounter, boardType;
    private String currWord;
    //int fTimer1, fTimer2, fTimer3, fTimer4, dTimer1, dTimer2, dTimer3, dTimer4;
    private int[] fTimer = new int[5];
    private int[] dTimer = new int[5];
    private int[] bsTimer = new int [9];
    private int[] targetX = new int[9];
    private int[] targetY = new int[9];
    private int[] activeTouch = {-1,-1};
    private int effectTimer = 0; private int moveTimer = 0; private int lingerTimer = 0; private int winTimer = 0;
    //context to be used in onTouchEvent to cause the activity transition from GameAvtivity to MainActivity.
    private Context context;
    //board dimensions
    private int[] boardNine=new int[4];
    private int[] boardSix=new int[4];
    private int[] boardFour=new int[4];

    private int[] bCoords = new int[4];
    //board
    //ArrayList<int[]> currBoard=new ArrayList<int[]>();
    private int board;
    private Cell[][] currBoard;
    ArrayList<int[]> candList=new ArrayList<int[]>();
    int[] squareSelect = new int[2];


    private Values values = new Values();
    private int smallFont, medFont, boardFont, bigFont,
            posTop, posTopMid, posMid, posBotMid, posBot,
            fasterText, fastText, normText, slowText,
            EASY_BOARD, MED_BOARD, HARD_BOARD,
            HIGHLIGHT_COLOR, NEIGHBOR_COLOR,FIXED_COLOR,ERROR_COLOR,REGULAR_COLOR,FONT_COLOR,BG_COLOR,SELECT_COLOR;


    public InfiniteView(Context context, int screenX, int screenY) {
        super(context);

        //initializing context
        this.context = context;
        //player = new Player(context, screenX, screenY);
        this.screenX = screenX;
        this.screenY = screenY;


        initValues();

        sceneManager = new SceneManager();

        //countMisses = 0;
        iprogFlag = false; bsFlag = false; moveFlag = false; effectFlag = false; refreshFlag = false;
        level = 0;
        surfaceHolder = getHolder();
        paint = new Paint(); boardPaint = new Paint(); scenePaint = new Paint(); numPaint = new Paint();
        devPaint = new Paint(); toolsPaint = new Paint(); transPaint = new Paint(); defPaint = new Paint();
        bgColor = BG_COLOR;
        fontColor = FONT_COLOR;
        for(int i = 0;i<5;i++){
            fTimer[i] = 0;
            dTimer[i] = 0;
        }
        winCounter = 0;
        //iProg = 0; //init should be 0
        iProg = -1; // iProg starts at -1. maybe bad idea
        currScene = 4; //starts at 0, change for starting level
        boardType = 9;
    }

    private void initValues(){  //This should account for all screen sizes

        if(screenX < 1080){ //change boxsize as appropriately a la media queries
            boxSize = 96;  //maybe better way is to do it percentage based
        }else{             //but then letters need to scale as well
            boxSize = 108;  //probably doable, probably better, for now will stick with flat value
        }

        boardNine[0] = Math.round(screenX/2) - (48+boxSize*9)/2;
        boardNine[1] = 200;
        boardNine[2] = boardNine[0] + (48+boxSize*9);
        boardNine[3] = boardNine[1] + (48+boxSize*9);

        boardFour[0] = Math.round(screenX/2) - (48+boxSize*4)/2;
        boardFour[1] = boardNine[3] - (48+boxSize*4);
        boardFour[2] = boardFour[0] + (48+boxSize*4);
        boardFour[3] = boardFour[1] + (48+boxSize*4);

        this.smallFont = values.getSmallFont();
        this.medFont = values.getMedFont();
        this.boardFont = values.getBoardFont();
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

        this.REGULAR_COLOR = values.getREGULAR_COLOR();
        this.HIGHLIGHT_COLOR = values.getHIGHLIGHT_COLOR();
        this.NEIGHBOR_COLOR = values.getNEIGHBOR_COLOR();
        this.FIXED_COLOR = values.getFIXED_COLOR();
        this.ERROR_COLOR = values.getERROR_COLOR();

        this.BG_COLOR = values.getBG_COLOR();
        this.FONT_COLOR = values.getFONT_COLOR();
        this.SELECT_COLOR = values.getSELECT_COLOR();


        toolStartY = boardNine[3]+64;
        selectStartY = boardNine[3]+64+108+64+(3*bigFont/4);
    }

    private Cell[][] generateBoard(String msg, int diff){
        int[] square = new int[4]; //input value 0-8, fixed num value 0-1, select value 0-1, highlight value 0-1
        int boardSize = msg.length();
        board = boardSize;
        //int boardSize = 4;


        sudokuGen = new Sudoku(boardSize, diff);

        return sudokuGen.getBoard();

    }

    private void fadeOut(Canvas c){
        transPaint.setColor(bgColor);
        transPaint.setAlpha(effectTimer);
        c.drawRect(0,0,screenX,screenY,transPaint);

        if(!effectFlag){
            if(effectTimer + 7 <  255){
                effectTimer+=7;
            }else{
                effectTimer = 255;
            }
        }
        if(effectTimer>=255){
            effectTimer = 255;
            effectFlag = true;
        }
        if(effectFlag){
            System.out.println(iProg);
            effectFlag = false;
            //effectTimer=0;
            iProg++;
            System.out.println(iProg);
        }
    }
    private void fadeIn(Canvas c){
        if(effectTimer > 0){
            transPaint.setColor(bgColor);
            transPaint.setAlpha(effectTimer);
            c.drawRect(0,0,screenX,screenY,transPaint);
        }

        if(!effectFlag){
            if(effectTimer - 10 > 0){
                effectTimer-=10;
            }else{
                effectTimer = 0;
            }
        }
        if(effectTimer<=0){
            effectTimer = 0;
            effectFlag = true;
        }
        if(effectFlag){
            effectFlag = false;
        }
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update() {
        if(winFlag){
            for(int x = 0; x<winCounter;x++){
                for(int y = 0;y<winCounter;y++){
                    currBoard[x][y].setvState(currBoard[x][y].getvState()+1);
                }
            }
            winTimer++;
            if(currWord.length() == 4 && winTimer > 12){
                if(winCounter < currWord.length()){
                    winCounter++;
                }
                winTimer = 0;
            }else if(winTimer > 6){
                if(winCounter < currWord.length()){
                    winCounter++;
                }
                winTimer = 0;
            }
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            canvas.drawColor(bgColor);


            //this may or may not be a good place to put this
            //scene get

            switch(iProg){
                /*
                case 10:
                    winState();
                    break;
                case 9:
                    if(lingerTimer < 45){
                        drawAvailNum(canvas, boardObj.getWord(),FONT_COLOR);
                    }
                    lingerTimer++;
                    if(lingerTimer > 60){
                        lingerTimer = 0;
                        iProg++;
                    }
                    break;
                case 8:
                    //draw sudoku board
                    drawBoard(canvas, boardObj.getWord(),fontColor,REGULAR_COLOR);
                    drawTools(canvas);
                    drawAvailNum(canvas, boardObj.getWord(),FONT_COLOR);
                    drawDefinition(canvas, boardObj.getwType(),boardObj.getDefinition());
                    //drawBoard(canvas, "WAKE",Color.WHITE,Color.BLACK);
                    fadeOut(canvas);
                    drawAvailNum(canvas, boardObj.getWord(),FONT_COLOR);
                    break;
                case 7:
                    //draw sudoku board
                    drawBoard(canvas, boardObj.getWord(),fontColor,REGULAR_COLOR);
                    drawTools(canvas);
                    drawAvailNum(canvas, boardObj.getWord(),FONT_COLOR);
                    drawDefinition(canvas, boardObj.getwType(),boardObj.getDefinition());
                    //drawBoard(canvas, "WAKE",Color.WHITE,Color.BLACK);

                    break;
                case 6:
                    //draw sudoku board
                    drawBoard(canvas, boardObj.getWord(),fontColor,REGULAR_COLOR);
                    drawTools(canvas);
                    drawAvailNum(canvas, boardObj.getWord(),FONT_COLOR);
                    drawDefinition(canvas, boardObj.getwType(),boardObj.getDefinition());
                    //drawBoard(canvas, "WAKE",Color.WHITE,Color.BLACK);

                    fadeIn(canvas);

                    break;
                case 5:
                    if(effectTimer==0){
                        boardObj = sceneObj.getThisBoard(boardSelect);
                        String tempS = boardObj.getWord();
                        int diff = boardObj.getDifficulty();
                        currBoard = generateBoard(boardObj.getWord(), boardObj.getDifficulty());
                        iprogFlag = false;
                    }

                    drawLine(canvas, 4);
                    drawLine(canvas, 3);
                    drawLine(canvas, 2);
                    drawLine(canvas, 1);
                    drawLine(canvas, 0);

                    fadeOut(canvas);

                    break;
                case 4:
                    drawLine(canvas,4);
                case 3:
                    drawLine(canvas,3);
                case 2:
                    drawLine(canvas,2);
                case 1:
                    drawLine(canvas,1);
                case 0:
                    drawLine(canvas,0);
                    break;
                case -1:

                    sceneObj = sceneManager.getScene(currScene);
                    lineList = sceneObj.getLines();
                    iProg++;
                    break;

                case -2: //dev flag? edit this later
                    iprogFlag = false;
                    break;
                    */

                case 6:
                    winState();
                    break;
                case 5:
                    if(lingerTimer < 45){
                        drawAvailNum(canvas, boardObj.getWord(),FONT_COLOR);
                    }
                    lingerTimer++;
                    if(lingerTimer > 60){
                        lingerTimer = 0;
                        iProg++;
                    }
                    break;
                case 4:
                    //draw sudoku board
                    drawBoard(canvas, boardObj.getWord(),fontColor,REGULAR_COLOR);
                    drawTools(canvas);
                    drawAvailNum(canvas, boardObj.getWord(),FONT_COLOR);
                    drawDefinition(canvas, boardObj.getwType(),boardObj.getDefinition());
                    //drawBoard(canvas, "WAKE",Color.WHITE,Color.BLACK);
                    fadeOut(canvas);
                    drawAvailNum(canvas, boardObj.getWord(),FONT_COLOR);
                    break;
                case 3:
                    //draw sudoku board
                    drawBoard(canvas, boardObj.getWord(),fontColor,REGULAR_COLOR);
                    drawTools(canvas);
                    drawAvailNum(canvas, boardObj.getWord(),FONT_COLOR);
                    drawDefinition(canvas, boardObj.getwType(),boardObj.getDefinition());
                    //drawBoard(canvas, "WAKE",Color.WHITE,Color.BLACK);

                    break;
                case 2:
                    //draw sudoku board
                    drawBoard(canvas, boardObj.getWord(),fontColor,REGULAR_COLOR);
                    drawTools(canvas);
                    drawAvailNum(canvas, boardObj.getWord(),FONT_COLOR);
                    drawDefinition(canvas, boardObj.getwType(),boardObj.getDefinition());
                    //drawBoard(canvas, "WAKE",Color.WHITE,Color.BLACK);

                    fadeIn(canvas);
                    break;
                case 1:
                    if(effectTimer==0){
                        if(boardType == 4){
                            boardObj = values.getRandomFourBoard();
                        }else{
                            boardObj = values.getRandomNineBoard();
                        }
                        //String tempS = boardObj.getWord();
                        //int diff = boardObj.getDifficulty();
                        currWord = boardObj.getWord();
                        currBoard = generateBoard(boardObj.getWord(), boardObj.getDifficulty());
                        iprogFlag = false;
                    }

                    drawSingleLine(canvas,"NINE",posTopMid);
                    drawSingleLine(canvas,"FOUR",posBotMid);

                    fadeOut(canvas);
                    break;
                case 0:
                    drawSingleLine(canvas,"NINE",posTopMid);
                    drawSingleLine(canvas,"FOUR",posBotMid);
                    break;
                case -1:
                    iProg++;
                    break;
            }


            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawBoard(Canvas c, String msg, int priColor, int secColor){
        int boardSize = msg.length();
        paint.setColor(priColor);
        paint.setTextSize(boardFont);
        switch(boardSize){
            case 9:
                bCoords = boardNine;
                break;
            case 6:
                bCoords = boardSix;
                break;
            case 4:
                bCoords = boardFour;
                break;
            default:
                bCoords = boardNine;
                break;
        }
        //draw primary background
        c.drawRect(bCoords[0],bCoords[1],bCoords[2],bCoords[3],paint);
        //draw individual blocks
        paint.setColor(secColor);

        //drawNineBoard(c,msg,priColor,secColor,bCoords);

        /* draw for specific board, implement once you have all board info set up
         */
        switch(boardSize){
            case 9:
                drawNineBoard(c,msg,priColor,secColor,bCoords);
                break;
            case 6:
                drawSixBoard(c,msg,priColor,secColor,bCoords);
                break;
            case 4:
                drawFourBoard(c,msg,priColor,secColor,bCoords);
                break;
            default:
                drawNineBoard(c,msg,priColor,secColor,bCoords);
                break;
        }
    }
    private void drawNineBoard(Canvas c, String msg, int priColor, int secColor, int[] bCoords){
        int xSpace = 0, ySpace = 0;
        for(int x = 0;x< board;x++){
            switch(x){
                case 0:
                    xSpace = 12;
                    break;
                case 3:
                case 6:
                    xSpace+=6;
                case 1:
                case 2:
                case 4:
                case 5:
                case 7:
                case 8:
                    xSpace+=2;
                    break;
            }
            for(int y = 0;y<board;y++){
                switch(y){
                    case 0:
                        ySpace = 12;
                        break;
                    case 3:
                    case 6:
                        ySpace+=6;
                    case 1:
                    case 2:
                    case 4:
                    case 5:
                    case 7:
                    case 8:
                        ySpace+=2;
                        break;
                }
                int hit = 0;
                boolean fixed = currBoard[x][y].getFixed();
                if(winFlag){
                    if(currBoard[x][y].getvState() == 0){
                        paint.setColor(FIXED_COLOR);
                    }else {
                        paint.setColor(HIGHLIGHT_COLOR);
                    }
                }else if(activeTouch[0]==x & activeTouch[1]==y){
                    hit = 1;
                    paint.setColor(HIGHLIGHT_COLOR);
                }else if(currBoard[x][y].getFixed()) {
                    hit = 3;
                    paint.setColor(FIXED_COLOR);
                }else if(activeTouch[0]==x || activeTouch[1]==y) {
                    hit = 2;
                    paint.setColor(NEIGHBOR_COLOR);
                }else{
                    hit = 4;
                    paint.setColor(secColor);
                }
                int tempX = bCoords[0]+xSpace+boxSize*x;
                int tempY = bCoords[1]+ySpace+boxSize*y;
                int color = paint.getColor();
                c.drawRect(tempX,tempY,tempX+boxSize,tempY+boxSize,paint);

                // draw nums from board, still testing
                if(currBoard[x][y].getError()){
                    paint.setColor(ERROR_COLOR);
                    System.out.print("error color " + ERROR_COLOR);
                }else{
                    paint.setColor(priColor);
                    System.out.print("normal color" + priColor);
                }
                paint.setTextSize(boardFont);
                paint.setTextAlign(Paint.Align.CENTER);
                int squareVal = currBoard[x][y].getNum();
                if(squareVal > 0){
                    String tempMsg =  String.valueOf(msg.charAt(squareVal - 1));
                    c.drawText(tempMsg,tempX+bigFont/2 + 4,tempY+(3*boxSize/4),paint);
                }
                paint.setTextAlign(Paint.Align.LEFT);
            }
        }
    }

    private void drawSixBoard(Canvas c, String msg, int priColor, int secColor, int[] bCoords){
        int xSpace = 0, ySpace = 0;
        for(int x = 0;x< board;x++){
            switch(x){
                case 0:
                    xSpace = 12;
                    break;
                case 3:
                    xSpace+=8;
                case 1:
                case 2:
                case 4:
                case 5:
                    xSpace+=3;
                    break;
            }
            for(int y = 0;y<board;y++){
                switch(y){
                    case 0:
                        ySpace = 12;
                        break;
                    case 2:
                    case 4:
                        ySpace+=8;
                    case 1:
                    case 3:
                    case 5:
                        ySpace+=2;
                        break;
                }
                paint.setColor(secColor);
                int tempX = bCoords[0]+xSpace+boxSize*x;
                int tempY = bCoords[1]+ySpace+boxSize*y;
                c.drawRect(tempX,tempY,tempX+boxSize,tempY+boxSize,paint);

                // draw nums from board, still testing
                paint.setColor(priColor);
                paint.setTextSize(medFont);
                int squareVal = currBoard[x][y].getNum();
                if(squareVal > 0){
                    String tempMsg =  String.valueOf(msg.charAt(squareVal - 1));
                    c.drawText(tempMsg,tempX,tempY+boxSize,paint);
                }
            }
        }
    }

    private void drawFourBoard(Canvas c, String msg, int priColor, int secColor, int[] bCoords){
        int xSpace = 0, ySpace = 0;
        for(int x = 0;x< board;x++){
            switch(x){
                case 0:
                    xSpace = 16;
                    break;
                case 2:
                    xSpace+=8;
                case 1:
                case 3:
                    xSpace+=3;
                    break;
            }
            for(int y = 0;y<board;y++){
                switch(y){
                    case 0:
                        ySpace = 16;
                        break;
                    case 2:
                        ySpace+=8;
                    case 1:
                    case 3:
                        ySpace+=3;
                        break;
                }
                int hit = 0;
                boolean fixed = currBoard[x][y].getFixed();

                if(winFlag){
                    if(currBoard[x][y].getvState() == 0){
                        paint.setColor(FIXED_COLOR);
                    } else{
                        paint.setColor(HIGHLIGHT_COLOR);
                    }
                }else if(activeTouch[0]==x & activeTouch[1]==y){
                    hit = 1;
                    paint.setColor(HIGHLIGHT_COLOR);
                }else if(currBoard[x][y].getFixed()) {
                    hit = 3;
                    paint.setColor(FIXED_COLOR);
                }else if(activeTouch[0]==x || activeTouch[1]==y) {
                    hit = 2;
                    paint.setColor(NEIGHBOR_COLOR);
                }else{
                    hit = 4;
                    paint.setColor(secColor);
                }
                int tempX = bCoords[0]+xSpace+boxSize*x;
                int tempY = bCoords[1]+ySpace+boxSize*y;
                int color = paint.getColor();
                c.drawRect(tempX,tempY,tempX+boxSize,tempY+boxSize,paint);

                // draw nums from board, still testing
                if(currBoard[x][y].getError()){
                    paint.setColor(ERROR_COLOR);
                    System.out.print("error color " + ERROR_COLOR);
                }else{
                    paint.setColor(priColor);
                    System.out.print("normal color" + priColor);
                }
                paint.setTextSize(boardFont);
                paint.setTextAlign(Paint.Align.CENTER);
                int squareVal = currBoard[x][y].getNum();
                if(squareVal > 0){
                    String tempMsg =  String.valueOf(msg.charAt(squareVal - 1));
                    c.drawText(tempMsg,tempX+bigFont/2 + 2,tempY+(3*boxSize/4),paint);
                }

                paint.setTextAlign(Paint.Align.LEFT);
            }
        }
    }

    private void drawAvailNum(Canvas c, String msg, int priColor) {
        int startY = selectStartY;

        numPaint.setColor(priColor);
        numPaint.setLetterSpacing((float)0.4);
        numPaint.setTextSize(bigFont);
        numPaint.setTextAlign(Paint.Align.CENTER);
        c.drawText(msg,screenX/2,startY,numPaint);
    }
    private void drawDefinition(Canvas c, String wType, String defi){
        int startY = selectStartY+(bigFont/4)+96;

        defPaint.setColor(fontColor);
        defPaint.setTextSize(medFont);

        //defPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        defPaint.setTextSkewX(-0.25f);
        c.drawText(wType,25,startY,defPaint);

        defPaint.setTextSkewX(0);
        //c.drawText(defi,25,startY+medFont+24,defPaint); //will probably have to account for mutliple lines


        int textWidth = Math.round(defPaint.measureText(defi));
        int lines = Math.round(textWidth / (((screenX-100)*6)/8));
        ArrayList<String> msgLine = new ArrayList<String>();
        int start = 0;
        int target = 0;
        int cutoff= 0;
        if(lines > 1){
            String temp = defi;
            boolean flag = true;
            while(flag){
                if(Math.round(defPaint.measureText(temp)) < (((screenX-100)*6)/8)){
                    flag = false;
                }
                target = Math.round(defi.length()/lines);
                if(target > temp.length()){
                    cutoff = temp.length();
                }else{
                    cutoff = temp.substring(0,target).lastIndexOf(" ")+1;
                }
                msgLine.add(temp.substring(0,cutoff));
                temp = temp.substring(cutoff);
            }
        }

        if(lines <=1){
            c.drawText(defi,25,startY+medFont+12,defPaint);
        }else{
            for(int i=0;i<msgLine.size();i++){
                String temp = msgLine.get(i);
                c.drawText(temp, 25,startY+medFont+12+(medFont*i),defPaint);
            }
        }
    }

    private void drawLine(Canvas c, int num){
        if(num < lineList.size()){
            dialogue(c,lineList.get(num).getLine(),
                    lineList.get(num).getPos(), num,lineList.get(num).getEffect(),
                    lineList.get(num).getSpeed(), lineList.get(num).getFontSize());
        }else if(iProg < 4){
            iProg++;
        }
    }

    private void drawTools(Canvas c){
        int boxSize = 108;
        int startY = toolStartY;
        int[] boxOne = {Math.round(1*(screenX/3)) - boxSize/2,startY};
        int[] boxTwo = {Math.round(2*(screenX/3)) - boxSize/2,startY};

        toolsPaint.setColor(fontColor); //you're seriously going to have to fix these colors at some point
        c.drawRect(boxOne[0],boxOne[1],boxOne[0]+boxSize,boxOne[1]+boxSize,toolsPaint);
        c.drawRect(boxTwo[0],boxTwo[1],boxTwo[0]+boxSize,boxTwo[1]+boxSize,toolsPaint);
    }

    private void dialogue(Canvas c, String msg, int pos, int timer, String effect, int textSpeed, int fontSize){
        if(fTimer[timer] != -1 & fTimer[timer]>textSpeed & dTimer[timer] < msg.length()){ //THESE FOUR IF STATEMENTS ARE A FUCKING MESS
            fTimer[timer] = 0;                                      //YOU'RE GOING TO HAVE TO CLEAN THIS
            dTimer[timer]++;
        }
        if(dTimer[timer] > msg.length()){
            dTimer[timer] = msg.length();
        }
        if(dTimer[timer] == msg.length() & fTimer[timer]==0){
            iprogFlag = true;
        }

        if(iprogFlag){
            fTimer[timer]=-1;
            if(iProg < 4){
                iProg++;
            }
            iprogFlag = false;
        }
        paint.setColor(fontColor);
        paint.setTextSize(fontSize);
        int textWidth = (int) Math.ceil(paint.measureText(msg));
        int lines = (int) Math.ceil( (double)textWidth / ((double)screenX-48.0));
        ArrayList<String> msgLine = new ArrayList<String>();
        int start = 0;
        int target = 0;
        int cutoff= 0;
        if(lines > 1){
            String temp = msg;
            boolean flag = true;
            while(flag){
                if(Math.ceil(paint.measureText(temp)) < Math.ceil( (double)textWidth / ((double)screenX-48.0))){
                    flag = false;
                }
                target = Math.round(msg.length()/lines);
                if(target > temp.length()){
                    cutoff = temp.length();
                }else{
                    cutoff = temp.substring(0,target).lastIndexOf(" ")+1;
                }
                msgLine.add(temp.substring(0,cutoff));
                temp = temp.substring(cutoff);
            }
        }

        int maxTime = msg.length();

        if(timer>=maxTime){
            timer = maxTime;
        }
        if(lines <=1){
            if(lineList.get(timer).isClickable()){
                paint.setLetterSpacing((float)0.4);
                textWidth = Math.round(paint.measureText(msg));
                c.drawText(msg.substring(0,dTimer[timer]),screenX/2-textWidth/2,200+((screenY/4)*pos)-fontSize/2,paint);
                paint.setLetterSpacing(0);
            }else{
                c.drawText(msg.substring(0,dTimer[timer]),screenX/2-textWidth/2,200+((screenY/4)*pos)-fontSize/2,paint);
            }

        }else{
            for(int i=0;i<msgLine.size();i++){
                String temp = msgLine.get(i);
                int prevCh = 0;
                if(i > 0){
                    for(int x=0;x<i;x++){
                        prevCh+=msgLine.get(x).length()-1;
                    }
                }
                int tempC = dTimer[timer] - prevCh;
                if(tempC < 0){
                    tempC = 0;
                }else if(tempC > msgLine.get(i).length()){
                    tempC = msgLine.get(i).length();
                }
                textWidth = Math.round(paint.measureText(msgLine.get(i)));
                c.drawText(temp.substring(0,tempC), screenX/2-textWidth/2,200+((screenY/4)*pos)+(fontSize*i+1),paint);
            }
        }
        if(dTimer[timer] < msg.length() & fTimer[timer] != -1){
            fTimer[timer]++;
        }
    }

    private void drawSingleLine(Canvas c, String msg, int pos){
        paint.setLetterSpacing((float)0.4);
        paint.setColor(fontColor);
        paint.setTextSize(bigFont);
        int textWidth = Math.round(paint.measureText(msg));
        c.drawText(msg,screenX/2-textWidth/2,200+((screenY/4)*pos)-bigFont/2,paint);


    }


    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void pointerLoc(int touchX, int touchY){
        int[] bCoords;
        switch(board){
            case 9:
                bCoords = boardNine;
                break;
            case 6:
                bCoords = boardSix;
                break;
            case 4:
                bCoords = boardFour;
                break;
            default:
                bCoords = boardNine;
                break;
        }

        switch(board){
            case 9:
                locNineBoard(touchX,touchY,bCoords);
                break;
            case 6:
                locSixBoard(touchX,touchY,bCoords);
                break;
            case 4:
                locFourBoard(touchX,touchY,bCoords);
                break;
            default:
                locNineBoard(touchX,touchY,bCoords);
                break;
        }
    }

    private void locNineBoard(int touchX, int touchY, int[] bCoords){
        int xSpace = 0, ySpace = 0;
        xloop:
        for(int x = 0;x< board;x++){
            switch(x){
                case 0:
                    xSpace = 12;
                    break;
                case 3:
                case 6:
                    xSpace+=6;
                case 1:
                case 2:
                case 4:
                case 5:
                case 7:
                case 8:
                    xSpace+=2;
                    break;
            }
            for(int y = 0;y<board;y++){
                switch(y){
                    case 0:
                        ySpace = 12;
                        break;
                    case 3:
                    case 6:
                        ySpace+=6;
                    case 1:
                    case 2:
                    case 4:
                    case 5:
                    case 7:
                    case 8:
                        ySpace+=2;
                        break;
                }

                int tempX = bCoords[0]+xSpace+boxSize*x;
                int tempY = bCoords[1]+ySpace+boxSize*y;
                //extrapolate position and dimension based off of below
                //c.drawRect(tempX,tempY,tempX+boxSize,tempY+boxSize,paint);

                if(touchX > tempX & touchY > tempY & touchX < tempX+boxSize & touchY < tempY+boxSize){
                    activeTouch[0]=x;
                    activeTouch[1]=y;
                    System.out.println("tap at touchX: " + touchX + " touchY: " + touchY);
                    System.out.println("touch identified at location x: " + x + " y: " + y);
                    break xloop;
                }
            }
        }
    }

    private void locSixBoard(int touchX, int touchY, int[] bCoords){ //THIS IS INCOMPLETE AND NEEDS TO BE EDITED
        int xSpace = 0, ySpace = 0;
        xloop:
        for(int x = 0;x< board;x++){
            switch(x){
                case 0:
                    xSpace = 12;
                    break;
                case 3:
                    xSpace+=8;
                case 1:
                case 2:
                case 4:
                case 5:
                    xSpace+=3;
                    break;
            }
            for(int y = 0;y<board;y++){
                switch(y){
                    case 0:
                        ySpace = 12;
                        break;
                    case 2:
                    case 4:
                        ySpace+=8;
                    case 1:
                    case 3:
                    case 5:
                        ySpace+=2;
                        break;
                }

                int tempX = bCoords[0]+xSpace+boxSize*x;
                int tempY = bCoords[1]+ySpace+boxSize*y;
                //extrapolate position and dimension based off of below
                //c.drawRect(tempX,tempY,tempX+boxSize,tempY+boxSize,paint);

                if(touchX > tempX & touchY > tempY & touchX < tempX+boxSize & touchY < tempY+boxSize){
                    activeTouch[0]=x;
                    activeTouch[1]=y;
                    System.out.println("tap at touchX: " + touchX + " touchY: " + touchY);
                    System.out.println("touch identified at location x: " + x + " y: " + y);
                    break xloop;
                }
            }
        }
    }

    private void locFourBoard(int touchX, int touchY, int[] bCoords){
        int xSpace = 0, ySpace = 0;
        xloop:
        for(int x = 0;x< board;x++){
            switch(x){
                case 0:
                    xSpace = 16;
                    break;
                case 2:
                    xSpace+=8;
                case 1:
                case 3:
                    xSpace+=3;
                    break;
            }
            for(int y = 0;y<board;y++){
                switch(y){
                    case 0:
                        ySpace = 16;
                        break;
                    case 2:
                        ySpace+=8;
                    case 1:
                    case 3:
                        ySpace+=3;
                        break;
                }

                int tempX = bCoords[0]+xSpace+boxSize*x;
                int tempY = bCoords[1]+ySpace+boxSize*y;
                //extrapolate position and dimension based off of below
                //c.drawRect(tempX,tempY,tempX+boxSize,tempY+boxSize,paint);

                if(touchX > tempX & touchY > tempY & touchX < tempX+boxSize & touchY < tempY+boxSize){
                    activeTouch[0]=x;
                    activeTouch[1]=y;
                    System.out.println("tap at touchX: " + touchX + " touchY: " + touchY);
                    System.out.println("touch identified at location x: " + x + " y: " + y);
                    break xloop;
                }
            }
        }
    }

    private void toolsLoc(int touchX, int touchY){
        int boxSize = 108;
        int startY = toolStartY;
        int[] boxOne = {Math.round(1*(screenX/3)) - boxSize/2,startY};
        int[] boxTwo = {Math.round(2*(screenX/3)) - boxSize/2,startY};

        if(activeTouch[0] > 0 & activeTouch[1] > 0){
            if(touchX > boxOne[0] & touchX < boxOne[0]+boxSize
                    & touchY > boxOne[1] & touchY < boxOne[1]+boxSize ){
                if(!currBoard[activeTouch[0]][activeTouch[1]].getFixed()){
                    currBoard[activeTouch[0]][activeTouch[1]].setNum(0);
                    checkBoard();
                }
            }
        }
        if(touchX > boxTwo[0] & touchX < boxTwo[0]+boxSize
                & touchY > boxTwo[1] & touchY < boxTwo[1]+boxSize
                & !refreshFlag){
            refreshFlag = true;
            //board Reset, this may be a mistake, definitely need a dialog window to pop up ask "are you sure"
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            // Add the buttons

            builder.setMessage("Refresh with new board?");
            builder.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    iProg = 5;
                    refreshFlag = false;
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    refreshFlag = false;
                }
            });
            // Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void selectLoc(int touchX,int touchY){

        int startY = selectStartY;
        numPaint.setTextSize(bigFont);
        numPaint.setLetterSpacing((float)0.4);
        numPaint.setTextAlign(Paint.Align.CENTER);
        int fullLength = Math.round(numPaint.measureText(currWord));
        System.out.println("line 925: "+fullLength);
        int startX = screenX/2 - (fullLength/2);
        int spacer =  (int) Math.round(0.4*bigFont);
        int[] chLength =  new int[currWord.length()];
        for(int ch = 0; ch<currWord.length();ch++){
            String tempString = Character.toString(currWord.charAt(ch));
            chLength[ch] = Math.round(numPaint.measureText(tempString));
        }
        for(int i=0;i<board;i++){
            int currLength = 0;
            for(int cc = 0; cc<i;cc++){
                currLength+=chLength[cc];
            }
            /* DEV TOOL - draws select boxes, VERY BUGGY, PRONE TO CRASHING, but that's fine if you can figure out the spacing
            devPaint.setColor(Color.WHITE);
            devPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect((startX+currLength), startY - bigFont, (startX+currLength)+(chLength[i]), startY + Math.round(bigFont/2), devPaint);
            */
            if(touchX > (startX+currLength)
                    & touchX < (startX+currLength)+(chLength[i])
                    & touchY > startY - bigFont & touchY < startY + Math.round(bigFont/2)
                    & activeTouch[0] >= 0 & activeTouch[1] >= 0){
                if(!currBoard[activeTouch[0]][activeTouch[1]].getFixed()){
                    currBoard[activeTouch[0]][activeTouch[1]].setNum(i+1);
                    checkBoard();
                    break;
                }
            }

        }

        /*
        int spaceX = 24;
        int letterX = bigFont;
        int startX = screenX/2 - (letterX*board+spaceX*(board-1))/2;
        if(startX < 0){ //this shouldn't happen?
            startX = 24;
            spaceX = 12;
        }
        int startY = Math.round(7*screenY/8);
        for(int i=0;i<board;i++){
            if(touchX > startX+letterX*i+spaceX*i-Math.round(bigFont/2) & touchX < startX+letterX*i+spaceX*i+Math.round(bigFont/2)
                    & touchY > startY - Math.round(bigFont/2) & touchY < startY + Math.round(bigFont/2)
                    & activeTouch[0] >= 0 & activeTouch[1] >= 0){
                    if(!currBoard[activeTouch[0]][activeTouch[1]].getFixed()){
                        currBoard[activeTouch[0]][activeTouch[1]].setNum(i+1);
                        checkBoard();
                        break;
                    }
            }

        }
        */
    }

    private void checkBoard(){
        ArrayList<Integer> tempBoard=new ArrayList<Integer>();

        //board break points
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

        int errorCount = 0;
        int zeroCount = 0;
        for(int x = 0; x<board;x++){
            for(int y = 0;y < board;y++){
                boolean errorFlag = false;
                //check every square for errors
                for(int xx = 0;xx<board;xx++){ // check row
                    if(xx != x & currBoard[x][y].getNum() != 0){
                        if(currBoard[xx][y].getNum() == currBoard[x][y].getNum()){
                            currBoard[xx][y].setError(true);
                            errorFlag = true;
                            errorCount++;
                        }
                    }
                }
                for(int yy = 0;yy<board;yy++){ // check col
                    if(yy != y & currBoard[x][y].getNum() != 0){
                        if(currBoard[x][yy].getNum() == currBoard[x][y].getNum()){
                            currBoard[x][yy].setError(true);
                            errorFlag = true;
                            errorCount++;
                        }
                    }
                }

                //check box
                ArrayList<int[]> breakPoints;
                int[] bPoint = new int[2];
                if(board == 9){
                    breakPoints = breakNine;
                }else if(board == 6){
                    breakPoints = breakSix;
                    bWidth = 3;
                    bHeight = 2;
                }else if(board == 4){
                    breakPoints = breakFour;
                    bWidth = 2;
                    bHeight = 2;
                }else{
                    breakPoints = breakNine;
                }
                for(int b = 0;b<breakPoints.size();b++){
                    if(x< breakPoints.get(b)[0] & y< breakPoints.get(b)[1]){
                        bPoint = breakPoints.get(b);
                        break;
                    }
                }
                for(int xx = bPoint[0]-bWidth;xx<bPoint[0];xx++){
                    for(int yy = bPoint[1]-bHeight;yy<bPoint[1];yy++){
                        if(xx != x & yy != y & currBoard[x][y].getNum() != 0){
                            if(currBoard[xx][yy].getNum() == currBoard[x][y].getNum()){
                                currBoard[xx][yy].setError(true);
                                errorFlag = true;
                                errorCount++;
                            }
                        }
                    }
                }
                if(currBoard[x][y].getNum() == 0){
                    zeroCount++;
                }
                if(errorFlag){
                    currBoard[x][y].setError(true);
                }else{
                    currBoard[x][y].setError(false);
                }
            }
        }
        if(zeroCount == 0 & errorCount == 0){
            winFlag = true;
            iProg++; //should advance to case 8
        }
    }

    private void winState(){
        System.out.println("win!");
        iProg = 0;

        //RESET ALL VALUES FOR NEW SCENE
        iprogFlag = false; bsFlag = false; effectFlag = false; winFlag = false;
        effectTimer = 0; winTimer = 0; lingerTimer = 0; winCounter = 0;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int touchX = (int) motionEvent.getX();
        int touchY = (int) motionEvent.getY();
        if(iProg == 0){
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    //System.out.println("touchY: "+touchY+ " screenY: "+screenY+" bigFont: "+bigFont);
                    if(touchY < 200+ 1 * (screenY/4) + bigFont
                            & touchY > 200+ 1 * (screenY/4) - bigFont){
                        boardType = 9;
                        iProg++;
                    }else if(touchY < 200+ 3 * (screenY/4) + bigFont
                            & touchY > 200+ 3 * (screenY/4) - bigFont) {
                        boardType = 4;
                        iProg++;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
        }else if(iProg == 2){ //handles touch events for board
            int action = motionEvent.getActionMasked();
            switch(action){
                case MotionEvent.ACTION_DOWN:
                    if(touchY >= bCoords[3]+64+108){ //this is sloppy
                        selectLoc(touchX,touchY);
                    }else if(touchY > bCoords[3]){
                        toolsLoc(touchX,touchY);
                    }else if(touchY > bCoords[1]){
                        pointerLoc(touchX,touchY);
                    }

                    if(touchY < 200){ //THIS IS A DEV SKIP, REMOVE LATER
                        //winState();
                        winFlag = true;
                        iProg++;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(touchY >= bCoords[3]+64+108){ //this is super fucking inelegant
                        selectLoc(touchX,touchY);
                    }else if(touchY > bCoords[3]){
                        toolsLoc(touchX,touchY);
                    }else if(touchY > bCoords[1]){
                        pointerLoc(touchX,touchY);
                    }
                    break;

            }
        }else if(iProg == 3){ //handles touch events for win board
            int action = motionEvent.getActionMasked();
            switch(action){
                case MotionEvent.ACTION_DOWN:
                    if(touchX>0 & touchY >0){
                        iProg++;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;


            }

        }
        return true;
    }

}