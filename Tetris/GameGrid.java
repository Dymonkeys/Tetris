/*
 * File: gamegrid.java
 * Author: David Neufeld
 * Created Date: Wed Sep 14 2022 at 2:19:50 PM
 * E-mail: david.neufeld@maine.edu
 * Description:
 * Grid to display and remove pixels
 * Collaboration: 
 * 
 */
package Tetris;
import java.awt.*;

//import java.util.Timer;
//import java.util.TimerTask;\
import javax.swing.JPanel;
public class GameGrid extends JPanel{
    public GameState state;
    private int width;
    private int height;
    public boolean showQueue = true;
    public GameGrid(GameState state){
        this.state = state;
        this.width = 10;
        this.height = 20;
        repaint();
    }
    public void setState(GameState inp){
        state = inp;
    }
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
        pixelSize = Math.min(this.getBounds().height/(double)height,this.getBounds().width/(double)(width));
        //Draw the game
        for(int x=0;x<width;x++){
            for(int y=0;y<height;y++){
                g.setColor(getColor(drawGrid[x][y]));
                g.fillRect((int)(x*pixelSize), (int)(y*pixelSize), (int)Math.ceil(pixelSize), (int)Math.ceil(pixelSize));
                g.setColor(Color.BLACK);
                //g.drawRect(x*PixelWidth, y*PixelHeight, PixelWidth, PixelHeight);
            }   
        } 
    }
}