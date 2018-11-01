package com.jhchang.simplesudoku;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class OptionsActivity extends Activity implements View.OnClickListener {

    // play image button
    private Button buttonReset, buttonEmail, buttonBack;
    public static final String STORY_FILE = "SudokuStoryFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_options);

        //setting the orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //getting the button
        buttonReset = (Button) findViewById(R.id.buttonReset);
        buttonEmail = (Button) findViewById(R.id.buttonEmail);
        buttonBack = (Button) findViewById(R.id.buttonBack);

        //setting the on click listener to play now button
        buttonReset.setOnClickListener(this);
        buttonEmail.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
    }

    // the onclick methods
    @Override
    public void onClick(View v) {


        if (v == buttonReset) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setMessage("Completely reset story progress?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                            SharedPreferences prefs;
                            SharedPreferences.Editor editor;
                            prefs = getSharedPreferences(STORY_FILE, MODE_PRIVATE);
                            editor = prefs.edit();

                            editor.putInt("currScene",0);
                            editor.putBoolean("victorFlag",false);
                            editor.apply();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            android.support.v7.app.AlertDialog alert = builder.create();
            alert.show();

        }else if(v == buttonEmail){
            /* Create the Intent */
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

            /* Fill it with Data */
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"discenereet@gmail.com"});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Re: Sudoku Story");
            //emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");

            /* Send it off to the Activity-Chooser */
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        }else if(v == buttonBack){
            finish();
        }

    }

    /*
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int touchX = (int)motionEvent.getX();
        int touchY = (int)motionEvent.getY();
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }
    */
    @Override
    public void onBackPressed() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("Return to menu?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();

    }
}
