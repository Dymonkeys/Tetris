/*
 * File: GameDisplay.java
 * Author: David Neufeld
 * Created Date: Mon Dec 05 2022 at 9:17:10 AM
 * E-mail: david.neufeld@maine.edu
 * Description:
 * Provides the Jframe to display the visuals of a tetris game state including 
 * It's shape queue.
 * Collaboration: None
 */
package Tetris;
import java.awt.*;
import javax.swing.JPanel;
public class GameDisplay extends JPanel{
    public GameState state;
    private int width;
    private int height;
    //Convert integer value from game grid to awt color.
    public GameDisplay(GameState state){
        this.state = state;
        this.width = 10;
        this.height = 20;
        repaint();
    }
    //Converts the block ID value of eact tile on the grid to a awt color
    public Color getColor(int color){
        if(color==0) return Color.black;
        if(color==1) return Color.yellow;
        if(color==2) return Color.blue;
        if(color==3) return Color.orange;
        if(color==4) return Color.red;
        if(color==5) return Color.green;
        if(color==6) return new Color(255,0,255);
        if(color==7) return Color.cyan;
        if(color==-1) return Color.white;
        if(color==-2) return Color.gray;
        if(color==-3) return Color.darkGray;
        return Color.lightGray;
    }

    @Override
    protected void paintComponent(Graphics g) {
        //Generate draw grid
        int[][] drawGrid = state.drawGrid();
        super.paintComponent(g);
        double pixelSize;
        pixelSize = Math.min(this.getBounds().height/(double)height,this.getBounds().width/(double)(width+3.5));
        //Draw the game
        for(int x=0;x<width;x++){
            for(int y=0;y<height;y++){
                g.setColor(getColor(drawGrid[x][y]));
                g.fillRect((int)(x*pixelSize), (int)(y*pixelSize), (int)Math.ceil(pixelSize), (int)Math.ceil(pixelSize));
                g.setColor(Color.BLACK);
                //g.drawRect(x*PixelWidth, y*PixelHeight, PixelWidth, PixelHeight);
            }   
        } 
        //Draw the queue
        g.setColor(Color.black);
        int margin = (int)Math.ceil(pixelSize*((double)width+1));
        g.fillRect((int)(pixelSize*(double)width),0,(int)Math.ceil(pixelSize*3.5),(int)Math.ceil(pixelSize*height));
        g.setColor(Color.darkGray);
        g.drawRect((int)(pixelSize*(double)width),0,(int)Math.ceil(pixelSize*3.5),(int)Math.ceil(pixelSize*height));
        g.setColor(Color.white);
        //int textStart = (int)(pixelSize*(double)width)+(int)Math.ceil(pixelSize*3.5)-100;
        int textStart = (int)(pixelSize*((double)width+3.5))-100;
        g.drawString("Score: "+state.points, textStart,25);
        g.drawString("Heuristic: "+(state.evaluate()-state.points), textStart,40);
        g.drawString("Next:", textStart,55);
        TetrisShape[] queue = state.getQueue(8);
        for(int i = 1; i < 8; i++){
            g.setColor(getColor(queue[i].id));
            for(int[] block : queue[i].blocks[queue[i].rotation]){
                int x = margin + (int)(block[0]*pixelSize*0.7);
                int y = (int)((i*2.5)*pixelSize) + (int)(block[1]*pixelSize*0.7);
                g.fillRect(x, y, (int)Math.ceil(pixelSize*0.7), (int)Math.ceil(pixelSize*0.7));
            }
        }
    }
}
