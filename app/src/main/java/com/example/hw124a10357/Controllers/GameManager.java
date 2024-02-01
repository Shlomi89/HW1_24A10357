package com.example.hw124a10357.Controllers;


import android.view.View;

import java.util.Arrays;

public class GameManager {


    private int score = 0;
    private int questionIndex = 0;
    private int hits = 0;
    private int life;

    private int rows = 5;
    private int cols = 3;

    private int playerPos = 1;

    private boolean isBlank = false;

    private static final int COYOTE = View.VISIBLE;
    private static final int BLANK = View.INVISIBLE;

    private int[][] obsPos= new int[rows][cols];

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public GameManager(int life) {
        this.life = life;
    }


    public int getScore() {
        return score;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }

    public int getHits() {
        return hits;
    }

    public int getLife() {
        return life;
    }

    public int[][] getObsPos() {
        return obsPos;
    }


    //    public boolean isGameEnded(){
//        return getQuestionIndex() == getAllCountry().size();
//    }

    public boolean isGameLost() {
        if (getLife() == getHits()){
            hits=0;
            return true;
        }
        return false; // Game Run In Endless Mode
//        return getLife() == getHits();
    }

    public int getPlayerPos() {
        return playerPos;
    }

    public void setPlayerNewPos(int playerPos) {
        this.playerPos = this.playerPos + playerPos;
    }

    public void changeDirection(int direction) {
        if (getPlayerPos() + direction < cols && getPlayerPos() + direction >= 0)
            setPlayerNewPos(direction);
    }

    public boolean checkIfCrashed(){
        if(obsPos[rows-1][playerPos] == COYOTE){
            hits++;
            return true;
        }
        return false;
    }


    public void initObs(){
        for(int i=0; i<rows-1; i++){
            Arrays.fill(obsPos[i],View.INVISIBLE);
        }
    }

    private int[] createNewObsLine(){


        int[] line = new int[cols];
        for(int i=0; i<cols; i++)
        {
            if (getRandomBollean())
                line[i] = COYOTE;
            else
                line[i]=BLANK;
        }
        if(Arrays.stream(line).sum() == COYOTE*3)
            return createNewObsLine();
        return line;
    }
    private int[] createNewBlankLine(){
        int[] line = new int[cols];
        for (int i =0; i<cols; i++) {
            line[i] = View.INVISIBLE;
        }
        return line;
    }


    public void obsStepFroward(){
        for(int i=rows-2; i>=0; i--){
            for (int j= 0; j<cols; j++){
                obsPos[i+1][j] = obsPos[i][j];
            }
        }
        if (isBlank) {
            obsPos[0] = createNewBlankLine();
            triggerisBlank();
        }
        else {
            obsPos[0] = createNewObsLine();
            triggerisBlank();
        }

    }

    private void triggerisBlank(){
        if (isBlank)
            isBlank=false;
        else
            isBlank=true;
    }


    public boolean getRandomBollean(){
        return Math.random() < 0.5;
    }


    public void setCols(int cols) {
        this.cols = cols;
    }
}

