package Tetris;
import java.util.ArrayList;
/*
 * File: gamestate.java
 * Author: David Neufeld
 * Created Date: Wed Sep 14 2022 at 3:38:10 PM
 * E-mail: david.neufeld@maine.edu
 * Description:
 * An instance of tetris, which can be copied and orginized to create an AI
 * Collaboration: 
 * None
 */
public class GameState {
    public  int[][] grid;
    public ArrayList<TetrisShape> queue;
    public TetrisShape hold;//unused
    public int shapeX;
    public int shapeY=3;
    public int points = 0;
    final int spawnX = 4;
    final int spawnY =1;
    int score=0;
    final boolean debug = false;
    CopyableRandom r;
    
    public GameState(){
        this.grid=new int[10][20];
        r = new CopyableRandom();
        queue = new ArrayList<>();
        queue.add(new TetrisShape(r.nextInt(7)+1));
    }
    public GameState(int[][] grid,ArrayList<TetrisShape> queue,TetrisShape hold, int shapeX, int shapeY,int points, ArrayList<Integer> shapeBag,CopyableRandom random){
        this.queue = new ArrayList<>();
        this.points=points;
        r=random.copy();
        for(int i=0;i<queue.size();i++){
            this.queue.add(queue.get(i).clone());
        }
        this.grid = new int[10][20];
        for (int x = 0; x < 10; x++) {
            for(int y = 0; y<20;y++){
                this.grid[x][y]=grid[x][y];
            }
        }
        this.hold=hold;
        this.shapeBag = shapeBag;
        this.shapeX=shapeX;
        this.shapeY=shapeY;
    }
    public void addToQueue(TetrisShape t){
        queue.add(t);
    }
    //The active tetris shape (the one peing placed) is the shape in position 0 of the queue.
    public TetrisShape activeShape(){
        return queue.get(0);
    }
    //places a shape on the grid at x and y coordinates
    public void placeShape(TetrisShape shape, int x, int y){
        int[][] blocks = shape.blockPlacements(x, y);
        for (int[] b : blocks) {
            if(b[0]>=0 &&b[0]<10&&b[1]>=0&&b[1]<20)
            grid[b[0]][b[1]]=shape.id;
            else System.out.println("blocked in placeshape");
        }
    }
    public void placeShape(TetrisShape shape, int x, int y, int color){
        int[][] blocks = shape.blockPlacements(x, y);
        for (int[] b : blocks) {
            if(b[0]>=0 &&b[0]<10&&b[1]>=0&&b[1]<20)
            grid[b[0]][b[1]]=color;
            //else System.out.println("blocked in placeshape");
        }
    }
    //places active shape at shapeX shapeY and removes it from queue
    public void placeActiveShape(){
        placeShape(queue.get(0), shapeX, shapeY);
        queue.remove(0);
        shapeX=spawnX;
        shapeY=spawnY;
        updateGame();
    }
    public void placeActiveShape(int x, int y, int r){
        shapeX=x;
        shapeY=y;
        queue.get(0).rotation=r;
        //if(checkShape(queue.get(0), x, y))
        placeActiveShape();
    }
    //outputs the grid, including the active shape (queue[0])
    public int[][] drawGrid(){
        int[][] out = new int[grid.length][];
        for(int i=0;i<grid.length;i++){
            out[i]=grid[i].clone();
        }
        for (int[] b : queue.get(0).blockPlacements(shapeX, shapeY)) {
            if(b[0]>=0 &&b[0]<10&&b[1]>=0&&b[1]<20)
            out[b[0]][b[1]]=queue.get(0).id;
            else System.out.println("blocked in drawgrid");
        }
        return out;
    }
    //sets shapeX to a random (possible) value
    public void randomizeX(){
        queue.get(0).rotation=r.nextInt(queue.get(0).totalRotations);
        int x = r.nextInt(9);
        if(checkShape(queue.get(0), x, shapeY)){
            shapeX=x;
        } else randomizeX();
    }
    public boolean isRowEmpty(int y){
        for(int x=0;x<10;x++){
            if(grid[x][y]>0) return false;
        }
        return true;
    }
    public boolean isRowFull(int y){
        for(int x=0;x<10;x++){
            if(grid[x][y]<=0) return false;
        }
        return true;
    }
    public boolean checkShape(TetrisShape shape, int x, int y){
        int[][] blocks = shape.blockPlacements(x, y);
        for (int[] b : blocks) {
            if(b[0]<0||b[0]>=10 || b[1]<0||b[1]>=20) return false;
            if(grid[b[0]][b[1]]>0) return false;
        }
            return true;

    }
    //moves the active shape by an x and y value, only if possible
    public boolean moveShape(int mx, int my){
        if(checkShape(queue.get(0), shapeX+mx, shapeY+my)){
            shapeX+=mx;
            shapeY+=my;
            return true;
        } else return false;
    }
    //rotates the shape by a specific amount, only if possible.
    public boolean rotateShape(int mr){
        TetrisShape activeShape = queue.get(0);
        int oldR = activeShape.rotation;
        activeShape.rotation+=mr;
        if(activeShape.rotation>=activeShape.totalRotations) activeShape.rotation-=activeShape.totalRotations;
        if(activeShape.rotation<0) activeShape.rotation+=activeShape.totalRotations;
        if(checkShape(activeShape, shapeX, shapeY)){
            return true;
        } else{
            activeShape.rotation=oldR;
            return false;
        }

    }
    //Move the shape down one and solidifies it if hit
    //returns true if the shape hit something
    public boolean shapeFall(){
        if(checkShape(queue.get(0), shapeX, shapeY+1)){
            shapeY++;
            return false;
        } else {
            placeActiveShape();
            return true;
        }
    }
    //moves the shape down until it hits something
    public void dropShape(){
        while(!shapeFall()){}
    }
    //Returns a clone of this gamestate, copying the queue, points, rng etc.
    public GameState clone(){
        GameState c = new GameState(grid,queue,hold,shapeX,shapeY,points,shapeBag,r);
        return c;
    }
    //Returns n elements of the queue, Generating them as needed.
    public TetrisShape[] getQueue(int n){
        TetrisShape[] out = new TetrisShape[n];
        for(int i = queue.size(); i<n; i++){
            queue.add(new TetrisShape(r.nextInt(7)+1));
        }
        for(int i = 0; i < n; i++){
            out[i] = queue.get(i);
        }
        return out;
    }
    //clears full rows and updates points

    //this method generates a new tetris piece (represented by a number)
    //according to the official method used in tetris
    public ArrayList<Integer> shapeBag = new ArrayList<Integer>();
    public TetrisShape getNextPiece(){
        //if shape bag is empty, fill it with numbers 1 through 7
        if (shapeBag.isEmpty()) {
            for (int i = 1; i <= 7; i++) {
                shapeBag.add(i);
            }
        }
        //return random element of shapeBag
        return new TetrisShape(shapeBag.remove(r.nextInt(shapeBag.size())));
    }

    public void updateGame(){
        //Add to queue if needed
        //if(queue.size()==0) queue.add(new TetrisShape(r.nextInt(7)+1));
        if(queue.size()==0) queue.add(getNextPiece());
        int rowsCleared=0;
        for(int y=19;y>0;y--){
            if(isRowFull(y)){
                //shifts every block above this row down to clear the row.
                for(int x=0;x<10;x++){
                    for(int i=y;i>1;i--){
                        grid[x][i]=grid[x][i-1];
                    }
                    grid[x][0]=0;
                }
                y++;
                rowsCleared++;
            }
        }
        //Points are awarded or subtracted based on number of rows cleared. 
        //Sense I want my AI to get frequent tetrises, it is punished for only 
        //clearing one or two rows at a time
        if(rowsCleared==1) points+=-30;
        else if(rowsCleared==2) points+=-20;
        else if(rowsCleared==3) points+=0;
        else if(rowsCleared==4) points+=100;
    }
    //Evaluate returns the heuristic for a game state, which considers apects like:
    //Height of the blocks, empty space under blocks, gaps inbetween blocks horizontally,
    //and point value
    public int evaluate(){
        int value=points;
        //Detracts value for each empty tile with blocks above
        for(int x=0;x<10;x++){
            boolean last = false;
            for(int y=0;y<20;y++){
                if(!last&&grid[x][y]>0){
                    last=true;
                }
                else if(last&&grid[x][y]<=0){
                    //last=false;
                    value-=25;
                }
            }
        }
        //Detracts value for each gap between blocks horizontally
        //(ignoring size of gap)
        for(int y=0;y<20;y++){
            boolean last=grid[0][y]>0;
            for(int x=0;x<10;x++){
                if(!last&&grid[x][y]>0){
                    last=true;
                }
                else if(last&&grid[x][y]<=0){
                    last=false;
                    value-=10;
                }
            }
        }
        for(int y=0;y<20;y++){
            if(isRowEmpty(y)) value+=Math.max((12-y),0)*15;
        }
        return value;
    }

    //Generates all placement options and returns them represented as an array of nodes.
    public Node[] placementOptions(){
        return placementOptions(-1,null);
    }
    //quota determines how many nodes are generated.
    //The placements are generated from bottom to top, so that a good fit is more likely to be found.
    //Able to be called multiple times with an increased quota, and it will only generate the additional
    //needed placements rather than 
    public Node[] placementOptions(int quota,Node parent){
        GameState debugGrid; if(debug) debugGrid = clone();
        if(debug)
        if(parent.children==null){
            for(int x=0;x<10;x++){
                for(int y=0;y<20;y++){
                    if(grid[x][y]<0) debugGrid.grid[x][y]=0;
                }
            }
        }
        TetrisShape activeShape = queue.get(0); //Like how I dont use the methods I write?
        //list of move options
        ArrayList<Node> options = new ArrayList<>();
        int scanStartX=0;
        int scanStartY=19;
        int scanStartR=0;
        //check if list is partially generated
        boolean enabled = true;
        if(parent.children!=null){
            enabled=false;
            if(quota>0&&parent.children.length>=quota) return parent.children;
            //set the start of the scan to be the position of last path in list
            //get last
            Node lastState = parent.children[parent.children.length-1];
            scanStartX=lastState.placeX;
            scanStartY=lastState.placeY;
            scanStartR=lastState.placeR;
            
            //System.out.println("Continuing from.. "+scanStartX+" "+scanStartY+" r="+scanStartR);
            //Add all previously generated children
            for (Node child : parent.children) {
                options.add(child);
            }
            //if(scanStartY==19) scanStartY=18;
        }
        activeShape.rotation=0;
        //lastcheck keeps track of the last time checkshape was called with a specific r and x value (regardless of y)
        boolean[][] lastCheck = new boolean[4][10];
        //Checks from the bottom up so the list is naturally ordered from least to most worth
        //(for each y(for each rotation(for each x)))
        for(int y=scanStartY+1;y>=spawnY;y--){
            for(int x=0;x<10;x++){
                for(int r=0;r<activeShape.totalRotations;r++){
                    activeShape.rotation=r;
                    boolean check = checkShape(activeShape, x, y);
                    if(enabled)
                    if(check &&!lastCheck[r][x]&&check){
                        //Validate spot with createpath
                        Instruction[] path = createPath(x, y);
                        if(path!=null){
                            //Valid spot with is found, create node and add it to list
                            GameState s = clone();
                            s.placeActiveShape(x,y,r);
                            Node n = new Node(s, path,x,y,r);
                            n.parent=parent;
                            options.add(n);
                            //System.out.println("Found! x="+x+" y="+y+ " r="+r);
                            //draw to debugGrid
                            //if(debug) debugGrid.placeShape(activeShape, x, y,-1);
                        }else if(debug) debugGrid.placeShape(activeShape, x, y,-2);
                    }
                    if(!enabled&&x==scanStartX&&y==scanStartY&&r==scanStartR) enabled = true;
                    if(options.size()==quota) break;
                    lastCheck[r][x] = check;
                }
                //break if an empty row is reached
                if(y<17&&isRowEmpty(y+2)) break;
                if(options.size()==quota) break;
            }
            if(options.size()==quota) break;
        }
        if(debug) this.grid=debugGrid.grid;
        Node[] out = new Node[options.size()];
        options.toArray(out);
        return out;
    }

    //Create path from start to position. Returns null if no path is possible
    //(In the form of an array of the Instruction enum)
    public Instruction[] createPath(int x, int y){
        ArrayList<Instruction> path = new ArrayList<>();
        TetrisShape shape = queue.get(0);
        int initialRotation = shape.rotation;
        shapeX=x;
        shapeY=y;
        Instruction lastmove=Instruction.down;
        while(shapeY>spawnY){
            //System.out.println(lastmove);
            if(moveShape(0,-1)){path.add(Instruction.down);}
            else if((lastmove!=Instruction.right)&&moveShape(1, 0)){path.add(Instruction.left);}
            else if((lastmove!=Instruction.left)&&moveShape(-1, 0)){path.add(Instruction.right);}
            else if((lastmove!=Instruction.anyrotate)&&rotateShape(-1)){path.add(Instruction.rotateleft);}
            else if((lastmove!=Instruction.anyrotate)&&rotateShape(1)){path.add(Instruction.rotateright);}
            else if((lastmove!=Instruction.anyrotate)&&rotateShape(2)){path.add(Instruction.rotate180);}
            else{
                //placeShape(shape, shapeX, shapeY,9);
                shapeY=spawnY;
                shapeX=spawnX;
                shape.rotation=initialRotation;
                return null;
            }
            if(path.size()>30)return null;
            lastmove = path.get(path.size()-1);
            if(lastmove==Instruction.rotate180||lastmove==Instruction.rotateleft||lastmove==Instruction.rotateright)
            lastmove=Instruction.anyrotate;
        }
        while(shapeX>spawnX){
            shapeX--;
            path.add(Instruction.right);
        }
        while(shapeX<spawnX){
            shapeX++;
            path.add(Instruction.left);
        }
        if(shape.rotation==1) path.add(Instruction.rotateleft);
        else if(shape.rotation==3) path.add(Instruction.rotateright);
        else if(shape.rotation==2) path.add(Instruction.rotate180);
        shapeX=spawnX;
        shapeY=spawnY;
        shape.rotation=initialRotation;
        Instruction[] p = new Instruction[path.size()];
        for(int i=0;i<path.size();i++){
            p[i]=path.get(path.size()-i-1);
        }
        return p;
    }
    
    public enum Instruction {
        down,
        left,
        right,
        rotateleft,
        rotateright,
        rotate180,
        anyrotate;
    }
}
