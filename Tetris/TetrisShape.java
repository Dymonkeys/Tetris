package Tetris;
import java.util.Random;

/*
 * File: tetromino.java
 * Author: David Neufeld
 * Created Date: Wed Sep 14 2022 at 3:40:17 PM
 * E-mail: david.neufeld@maine.edu
 * Description:
 * This is a class that represents the tetrominoes in the game of Tetris. 
 * It includes attributes and methods related to the shape of the tetromino 
 * and its rotations.
 * Collaboration: None
 */
public class TetrisShape {
    //[rotation][blocknum][x/y]
    public int blocks[][][];// = new int[4][][];
    public int id;
    public String shapename;
    public int rotation = 0;
    public int totalRotations;
    public TetrisShape(int id){
        this.id=id;
        assignBlocks();
    }
    public TetrisShape(int id, int rotation){
        this.rotation=rotation;
        this.id = id;
        assignBlocks();
    }
    public TetrisShape(){
        Random r = new Random();
        this.id = r.nextInt(7)+1;
        assignBlocks();
    }
    public int[][] blockPlacements(int x, int y){
        int[][] out = new int[4][2];
        for (int i = 0; i < 4; i++) {
            out[i][0] = blocks[rotation][i][0]+x;
            out[i][1] = blocks[rotation][i][1]+y;
        }
        return out;
    }
    //Generates the correct list of tetrino rotations corisponding to the shape id
    private void assignBlocks(){
        if(id==1)
        blocks = new int[][][]{
            {{0, 0}, {1, 0}, {0,  1}, {1,  1}}
        };
        else if(id==2)
        blocks = new int[][][]{
            {{-1,  0}, {0 ,  0}, {1,  0}, {1,  1}},
            {{0 , -1}, {0 ,  0}, {0,  1}, {-1,  1}},
            {{-1, -1}, {-1,  0}, {0,  0}, {1,  0}},
            {{0 , -1}, {1 , -1}, {0,  0}, {0,  1}}
        };
        else if(id==3)
        blocks = new int[][][]{
            {{-1,  0}, {0 ,  0}, {1,  0}, {-1,  1}},
            {{-1, -1}, {0 , -1}, {0,  0}, {0 ,  1}},
            {{1 , -1}, {-1,  0}, {0,  0}, {1 ,  0}},
            {{0 , -1}, {0 ,  0}, {0,  1}, {1 ,  1}}
        };
        else if(id==4)
        blocks = new int[][][]{
            {{0,  0}, {1,  0}, {1,  1}, {2,  1}},
            {{1,  0}, {0,  1}, {1,  1}, {0,  2}}
        };
        else if(id==5)
        blocks = new int[][][]{
            {{1,  0}, {2,  0}, {0,  1}, {1,  1}},
            {{0,  0}, {0,  1}, {1,  1}, {1,  2}}
        };
        else if(id==6)
        blocks = new int[][][]{
            {{0 ,  0}, {-1,  1}, {0,  1}, {1,  1}},
            {{0 ,  0}, {0 ,  1}, {1,  1}, {0,  2}},
            {{-1,  1}, {0 ,  1}, {1,  1}, {0,  2}},
            {{0 ,  0}, {-1,  1}, {0,  1}, {0,  2}}
        };
        else if(id==7)
        blocks = new int[][][]{
            {{-1,  0}, {0,  0}, {1,  0}, {2,  0}},
            {{0, -1}, {0,  0}, {0,  1}, {0,  2}}
        };
        totalRotations=blocks.length;
    }
    public void setRotation(int inp){
        rotation = inp;
    }
    public TetrisShape clone(){
        TetrisShape c = new TetrisShape(id);
        c.rotation=rotation;
        return c;
    }
}