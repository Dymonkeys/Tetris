/*
 * File: Paramaters.java
 * Author: David Neufeld
 * Created Date: Sun Mar 23 2025 at 5:27:59 PM
 * E-mail: david.neufeld@maine.edu
 * Description:
 * Holds paramaters for the the heuristic scoring system
 */
package Tetris;

public class Paramaters {
    public int singleScore = -30;
    public int doubleScore = -20;
    public int tripleScore = 0;
    public int tetrisScore = 100;
    
    public int heightPenalty = 15; // Scaler for Penalty for each row above heightPenaltyThreshold
    public int heightPenaltyThreshold = 8; // Number of rows above which height penalty is applied
    public int horizontalGapPenalty = 10; // Scaler for Penalty for each horizontal gap
    public int verticalGapPenalty = 25; // Scaler for Penalty for each vertical gap

    public Paramaters
            (int singleScore, int doubleScore, int tripleScore, int tetrisScore, 
            int heightPenalty, int heightPenaltyThreshold,
            int horizontalGapPenalty, int verticalGapPenalty){
        this.singleScore = singleScore;
        this.doubleScore = doubleScore;
        this.tripleScore = tripleScore;
        this.tetrisScore = tetrisScore;
        this.heightPenalty = heightPenalty;
        this.heightPenaltyThreshold = heightPenaltyThreshold;
        this.horizontalGapPenalty = horizontalGapPenalty;
        this.verticalGapPenalty = verticalGapPenalty;
    }

    public Paramaters() {
        // Default constructor
    }

}