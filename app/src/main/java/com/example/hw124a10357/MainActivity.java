package com.example.hw124a10357;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hw124a10357.Controllers.GameManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity {


    private MaterialButton main_BTN_left;
    private MaterialButton main_BTN_right;
    private ShapeableImageView[][] main_IMG_Obstacles;
    private ShapeableImageView[] main_IMG_Player;
    private ShapeableImageView[] main_IMG_hearts;
    private GameManager gameManager;

    private ShapeableImageView main_IMG_background;

    private static final int DELAY = 1000;

    final Handler handler = new Handler();
    Runnable runnable  = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this,DELAY);
            updateObs();
        }
    };

    private void updateObs() {
        gameManager.obsStepFroward();
        refreshUI();
    }


    private static final int RIGHT = 1;
    private static final int LEFT = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        gameManager = new GameManager(main_IMG_hearts.length);
//        refreshUI();
        gameManager.initObs();
        Glide.with(this).load(R.drawable.background).centerCrop().placeholder(R.drawable.ic_launcher_background).into(main_IMG_background);
        main_BTN_right.setOnClickListener(view -> directionClicked(RIGHT));
        main_BTN_left.setOnClickListener(view -> directionClicked(LEFT));
        handler.postDelayed(runnable, 0);
    }

    private void directionClicked(int Direction) {
        gameManager.changeDirection(Direction);
        refreshUI();
    }

    private void refreshUI() {
        // lost:
        if (gameManager.isGameLost())
            for (int i=0; i< main_IMG_hearts.length; i++){
                main_IMG_hearts[i].setVisibility(View.VISIBLE);
            }
//            changeActivity("GAME OVER", gameManager.getScore());

        else {
//            main_IMG_flag.setImageResource(gameManager.getCurrentCountry().getFlagImage());
            refreshObsPosition();
            refreshPlayerPosition();
            if (gameManager.checkIfCrashed()){
                toastAndVibrate("BOOM");
                if (gameManager.getHits() != 0 && gameManager.getHits()<=3)
                    main_IMG_hearts[main_IMG_hearts.length - gameManager.getHits()].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void changeActivity(String status, int score) {
//        Intent scoreIntent = new Intent(this, ScoreActivity.class);
//        scoreIntent.putExtra(ScoreActivity.KEY_STATUS, status);
//        scoreIntent.putExtra(ScoreActivity.KEY_SCORE, score);
//        startActivity(scoreIntent);
//        finish();
    }

    private void refreshPlayerPosition() {
        for (int i = 0; i < gameManager.getCols(); i++) {
            if (gameManager.getPlayerPos() == i)
                main_IMG_Player[i].setVisibility(View.VISIBLE);
            else
                main_IMG_Player[i].setVisibility(View.INVISIBLE);
        }
    }


    private void refreshObsPosition() {
        for (int i=0; i<gameManager.getRows(); i++){
            for (int j=0; j<gameManager.getCols(); j++){
                main_IMG_Obstacles[i][j].setVisibility(gameManager.getObsPos()[i][j]);
            }
        }
    }

    private void toastAndVibrate(String st){
        toast(st);
        vibrate();
    }



    private void toast(String st) {
        Toast.makeText(this,st,Toast.LENGTH_SHORT).show();
    }



    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    private void findViews() {
        main_BTN_left = findViewById(R.id.main_BTN_Left);
        main_BTN_right = findViewById(R.id.main_BTN_Right);
        main_IMG_background = findViewById(R.id.main_IMG_background);
        main_IMG_Obstacles = new ShapeableImageView[][]{
                //ROW 1
                {findViewById(R.id.imageView_0_0),
                        findViewById(R.id.imageView_0_1),
                        findViewById(R.id.imageView_0_2)}
                ,
                //ROW 2
                {findViewById(R.id.imageView_1_0),
                        findViewById(R.id.imageView_1_1),
                        findViewById(R.id.imageView_1_2)}
                ,
                //ROW 3
                {findViewById(R.id.imageView_2_0),
                        findViewById(R.id.imageView_2_1),
                        findViewById(R.id.imageView_2_2)}

                ,
                //ROW 4
                {findViewById(R.id.imageView_3_0),
                        findViewById(R.id.imageView_3_1),
                        findViewById(R.id.imageView_3_2)}

                ,
                //Player Row
                {
                        findViewById(R.id.imageView_Last_0),
                        findViewById(R.id.imageView_Last_1),
                        findViewById(R.id.imageView_Last_2)}
        };

        main_IMG_Player = new ShapeableImageView[]{
                findViewById(R.id.imageView_Player_0),
                findViewById(R.id.imageView_Player_1),
                findViewById(R.id.imageView_Player_2)};


        main_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };
    }
}