package com.jhchang.simplesudoku;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.res.TypedArrayUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class GameView extends SurfaceView implements Runnable {


    volatile boolean playing;
    private Thread gameThread = null;
    private SceneManager sceneManager;
    private Sudoku sudokuGen;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private boolean dprogFlag, bsFlag;
    //a screenX holder
    private int screenX, screenY, level, bgColor,fontColor, dProg, currScene, boxSize;
    //int fTimer1, fTimer2, fTimer3, fTimer4, dTimer1, dTimer2, dTimer3, dTimer4;
    private int[] fTimer = new int[5];
    private int[] dTimer = new int[5];;
    private int[] bsTimer = new int [9];
    private int[] targetX = new int[9];
    private int[] targetY = new int[9];
    private int[] activeTouch = {-1,-1};
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

    public GameView(Context context, int screenX, int screenY) {
        super(context);

        //initializing context
        this.context = context;
        //player = new Player(context, screenX, screenY);
        this.screenX = screenX;
        this.screenY = screenY;

        initValues();

        sceneManager = new SceneManager();

        //countMisses = 0;
        dprogFlag = false; bsFlag = false;
        level = 0;
        surfaceHolder = getHolder();
        paint = new Paint();
        bgColor = Color.BLACK;
        fontColor = Color.WHITE;
        //fTimer = 0; fTimer1 = 0; fTimer2 = 0; fTimer3 = 0; fTimer4 = 0;
        //dTimer = 0; dTimer1 = 0; dTimer2 = 0; dTimer3 = 0; dTimer4 = 0;
        for(int i = 0;i<5;i++){
            fTimer[i] = 0;
            dTimer[i] = 0;
        }
        //dProg = 0; //init should be 0
        dProg = -2;
        currScene = 0;
    }
    private void initValues(){  //This should account for all screen sizes
        //System.out.println(screenX);
        //System.out.println(screenY);
        //border sizes:
        //outer border, largest = 12
        //box border, medium = 6
        //inner border, smallest = 2
        //4 squares, border length is 34
        //6 squares, border length is 38
        //9 squares, border length is 48
        if(screenX < 1080){ //change boxsize as appropriately a la media queries
            boxSize = 96;  //maybe better way is to do it percentage based
        }else{             //but then letters need to scale as well
            boxSize = 108;  //probably doable, probably better, for now will stick with flat value
        }
        //figure out how big each square needs to be
        //
        boardNine[0] = Math.round(screenX/2) - (48+boxSize*9)/2;
        boardNine[1] = Math.round(screenY/2) - (48+boxSize*9)/2;
        boardNine[2] = boardNine[0] + (48+boxSize*9);
        boardNine[3] = boardNine[1] + (48+boxSize*9);
    }
    private Cell[][] generateBoard(String msg, int diff){
        int[] square = new int[4]; //input value 0-8, fixed num value 0-1, select value 0-1, highlight value 0-1
        int boardSize = msg.length();
        board = boardSize;
        //int boardSize = 4;


        sudokuGen = new Sudoku(boardSize, diff);

        return sudokuGen.getBoard();

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
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            canvas.drawColor(bgColor);
            /*
            paint.setColor(Color.WHITE);
            canvas.drawRect(0,0, screenX, screenY/2, paint);
            */
            switch(dProg){
                case 5:
                    //draw sudoku board
                    drawBoard(canvas, "DISMANTLE",Color.WHITE,Color.BLACK);
                    drawAvailNum(canvas, "DISMANTLE",Color.WHITE);
                    //drawBoard(canvas, "WAKE",Color.WHITE,Color.BLACK);
                    break;
                case 4:
                    //draw animation of button becoming sudoku board
                    if(!bsFlag){
                        bsFlag = true;  //make sure to set this false when animation is done
                        targetCoord("WAKE",posMid);
                    }
                    btnSpread(canvas, "W",0, posMid, bigFont);
                    btnSpread(canvas, "A",1, posMid, bigFont);
                    btnSpread(canvas, "K",2, posMid, bigFont);
                    btnSpread(canvas, "E",3, posMid, bigFont);
                    break;
                case 3:
                    //3 to -1 is scene text


                    //dProg = 5; //EDIT THIS OUT LATER
                    dprogFlag = false;
                case 2:
                    dialogue(canvas,"WAKE", posMid, 2,"none", normText, bigFont);
                case 1:
                    dialogue(canvas,"Maybe this time you're dead.",
                            posTopMid, 1,"none", fasterText, medFont);
                case 0:
                    dialogue(canvas,"Every night you wonder whether you're asleep or dead.",
                            posTop, 0,"none", fasterText, medFont);
                    break;
                case -1:
                    dprogFlag = false;
                    break;

                case -2: //dev flag? edit this later
                    currBoard = generateBoard("DISMANTLE", EASY_BOARD);
                    dProg = 5;
                    dprogFlag = false;
                    break;
            }


            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }


    /*
    LOTS OF THE SPACING IS OFF, FIX THIS WHEN YOU GET THE CHANCE
     */
    private void drawBoard(Canvas c, String msg, int priColor, int secColor){
        int boardSize = msg.length();
        paint.setColor(priColor);
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

        drawNineBoard(c,msg,priColor,secColor,bCoords);

        /* draw for specific board, implement once you have all board info set up
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
        */



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
                if(activeTouch[0]==x & activeTouch[1]==y){
                    hit = 1;
                    paint.setColor(HIGHLIGHT_COLOR);
                }else if(activeTouch[0]==x || activeTouch[1]==y) {
                    hit = 2;
                    paint.setColor(NEIGHBOR_COLOR);
                }else if(currBoard[x][y].getFixed()) {
                    hit = 3;
                    paint.setColor(FIXED_COLOR);
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
                paint.setTextSize(medFont);
                paint.setTextAlign(Paint.Align.CENTER);
                int squareVal = currBoard[x][y].getNum();
                if(squareVal > 0){
                    String tempMsg =  String.valueOf(msg.charAt(squareVal - 1));
                    c.drawText(tempMsg,tempX+medFont/2,tempY+boxSize,paint);
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
                    xSpace+=6;
                case 1:
                case 2:
                case 4:
                case 5:
                    xSpace+=2;
                    break;
            }
            for(int y = 0;y<board;y++){
                switch(y){
                    case 0:
                        ySpace = 12;
                        break;
                    case 2:
                    case 4:
                        ySpace+=6;
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
                    xSpace = 12;
                    break;
                case 2:
                    xSpace+=6;
                case 1:
                case 3:
                    xSpace+=2;
                    break;
            }
            for(int y = 0;y<board;y++){
                switch(y){
                    case 0:
                        ySpace = 12;
                        break;
                    case 2:
                        ySpace+=6;
                    case 1:
                    case 3:
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

    private void drawAvailNum(Canvas c, String msg, int priColor) {
        int spaceX = 24;
        int letterX = medFont;
        int startX = screenX/2 - (letterX*msg.length()+spaceX*(msg.length()-1))/2;
        if(startX < 0){ //this shouldn't happen?
            startX = 24;
            spaceX = 12;
        }
        int startY = Math.round(7*screenY/8);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(priColor);
        for(int i=0;i<msg.length();i++){
            String tempMsg =  String.valueOf(msg.charAt(i));
            c.drawText(tempMsg, startX+letterX*i+spaceX*i,startY,paint);
        }
        paint.setTextAlign(Paint.Align.LEFT);
    }

    private void dialogue(Canvas c, String msg, int pos, int timer, String effect, int textSpeed, int fontSize){
        if(fTimer[timer]>textSpeed & dTimer[timer] < msg.length()){
            fTimer[timer] = 0;
            ++dTimer[timer];
        }
        if(fTimer[timer] == 0 & dTimer[timer] == msg.length()-1 & !dprogFlag){
            dprogFlag = true;
        }
        if(dprogFlag){
            dprogFlag = false;
            dProg++;
        }
        paint.setColor(fontColor);
        paint.setTextSize(fontSize);
        int textWidth = Math.round(paint.measureText(msg));
        int lines = Math.round(textWidth / (((screenX-100)*6)/8));
        ArrayList<String> msgLine = new ArrayList<String>();
        int start = 0;
        int target = 0;
        int cutoff= 0;
        if(lines > 1){
            String temp = msg;
            boolean flag = true;
            while(flag){
                if(Math.round(paint.measureText(temp)) < (((screenX-100)*6)/8)){
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
            c.drawText(msg.substring(0,dTimer[timer]),screenX/2-textWidth/2,100+((screenY/4)*pos)-fontSize/2,paint);
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
                c.drawText(temp.substring(0,tempC), screenX/2-textWidth/2,100+(fontSize*i+1),paint);
            }
        }
        if(dTimer[timer] < msg.length()){
            fTimer[timer]++;
        }
    }
    private void targetCoord(String msg, int startPos){

    }
    private void btnSpread(Canvas c, String msg, int timer, int pos, int fontSize){

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

    private void wordTransition(int gLevel){
        switch(gLevel){
            case 0:

                level = 1;
                break;
        }
    }

    private void pointerLoc(int touchX, int touchY){
        int[] bCoords;
        int xSpace = 0, ySpace = 0;
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

        /* will have to set up board specific locator func since their spacing is different
        switch(board){
            case 9:
                locNineBoard(touchX,touchY,bCoords);
                break;
        }
        */
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

    private void selectLoc(int touchX,int touchY){

        int spaceX = 24;
        int letterX = medFont;
        int startX = screenX/2 - (letterX*board+spaceX*(board-1))/2;
        if(startX < 0){ //this shouldn't happen?
            startX = 24;
            spaceX = 12;
        }
        int startY = Math.round(7*screenY/8);
        for(int i=0;i<board;i++){
            if(touchX > startX+letterX*i+spaceX*i-Math.round(medFont/2) & touchX < startX+letterX*i+spaceX*i+Math.round(medFont/2)
                    & touchY > startY - Math.round(medFont/2) & touchY < startY + Math.round(medFont/2)
                    & !currBoard[activeTouch[0]][activeTouch[1]].getFixed()){
                currBoard[activeTouch[0]][activeTouch[1]].setNum(i+1);
                checkBoard();
                break;
            }

        }
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
                }else if(board == 4){
                    breakPoints = breakFour;
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
            System.out.println("win!");
            //INSERT BOARD WIN HERE
            //dProg = 6;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int touchX = (int)motionEvent.getX();
        int touchY = (int)motionEvent.getY();
        if(dProg <= 3){
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    System.out.println("touchY: "+touchY+ " screenY: "+screenY+" bigFont: "+bigFont);
                    if(touchY < screenY/2 + bigFont & touchY > screenY/2 - bigFont){
                        dProg = -2; //edit this make sure it's good
                        dprogFlag = false;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
        }else if(dProg == 5){
            int index = motionEvent.getActionIndex();
            int action = motionEvent.getActionMasked();
            int pointerId = motionEvent.getPointerId(index);
            switch(action){
                case MotionEvent.ACTION_DOWN:
                    if(touchY > bCoords[3]){
                        selectLoc(touchX,touchY);
                    }else if(touchY > bCoords[1]){
                        pointerLoc(touchX,touchY);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(touchY > bCoords[3]){
                        selectLoc(touchX,touchY);
                    }else if(touchY > bCoords[1]){
                        pointerLoc(touchX,touchY);
                    }
                    break;

            }
        }
        return true;
    }
}
