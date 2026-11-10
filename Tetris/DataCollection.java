/*
 * File: DataCollection.java
 * Author: David Neufeld
 * Created Date: Sun Mar 09 2025 at 6:34:13 PM
 * E-mail: david.neufeld@maine.edu
 * Description:
 * 
 */
package Tetris;
import java.util.ArrayList;
import javax.swing.JFrame;
import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.classfile.Instruction;

//import java.util.Arrays;
//import Tetris.GameState.Instruction;

public class DataCollection {
    private int lookahead; //Controls how many lookahead moves are generated
    private GameState startState;
    private boolean showNext = true; //Enables the white visualization of next placement
    private int stepSize;//Controls how many of the next moves are generated at once
    private GameDisplay display;
    private boolean displayState = true; //Enables the display of the game state

    //private Node head; //Head of tree and traversal Node
    public static void main(String[] args) {
        new DataCollection(10, 5);
    }
    public DataCollection(double speed, int lookahead){
        Paramaters paramaters = new Paramaters
        (0, 20, 50, 200,
         30, 6, 5, 50);
        this.lookahead=lookahead;
        startState = new GameState(paramaters);         
        long start_time = System.nanoTime();
        runDataCollection(32925);
        long end_time = System.nanoTime();
        System.out.printf("completed in %.2f seconds%n", (end_time-start_time)/1e9);
    }

    public void runDataCollection(int iterations){
        GameDisplay display = new GameDisplay(startState);
        if (displayState) {
            makeFrame(display);
            this.display=display;
        }
        Node head = new Node(startState);
        head.weightedGenerate(lookahead);
        nextNode=head;
        for (int i = 0; i < iterations; i++) {
            GameState gamestate = head.element;
            Node next = step();
            if(next.path!=null){
                GameState[] sequence = gamestate.followPath(next.path);
                logSequence(sequence, next.path);
            }
            head = next;
            if (displayState) {
                display.state=head.element;
                display.repaint();
            }
        }
    }

        // Global offset for sampling; this rotates across calls.
        private static int samplingOffset = 0;
        // Define how many moves to skip between samples.
        private static final int samplingInterval = 6; 
        public void logSequence(GameState[] sequence, GameState.Instruction[] path){
            //Depreciated: Log all of the GameStates in the sequence
            //for(int i=0;i<sequence.length;i++){
            //    logData(sequence[i], path[i].toString());
            //}
            //Log every samplingInterval move
            int len = sequence.length;
            for (int i = len - 1 - samplingOffset; i >=0; i-= samplingInterval) {
                // If this move is at the correct position according to the current offset...
                logData(sequence[i], path[i].toString());
            }
            // Increment the sampling offset    
            samplingOffset ++;
            if(samplingOffset >= samplingInterval) samplingOffset = 0;
        }


    /**
     * Logs the given GameState and instruction to a binary file.
     * 
     * The format of the binary file is as follows:
     * 
     * For each data entry:
     * - 10x20 booleans representing the current game grid.
     * - 10x20 booleans representing the active piece grid.
     * - An int representing the length of the shape queue.
     * - For each shape in the queue, an int representing the shape ID.
     * - An int representing the length of the one-hot encoded instruction.
     * - For each element of the one-hot encoded instruction, an int representing the element's value.
     * 
     * The file is appended to, rather than overwritten.
     * 
     * @param gamestate the GameState to log.
     * @param instruction the instruction associated with the given GameState.
     */
   public void logData(GameState gamestate, String instruction) {
        // Convert the game grid to a boolean grid.
        // true represents a filled cell; false represents an empty cell.
        boolean[][] grid = new boolean[10][20];
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 20; y++) {
                grid[x][y] = (gamestate.grid[x][y] != 0);
            }
        }
        
        // Convert the shape queue to an array of ints.
        ArrayList<TetrisShape> queueShapes = gamestate.queue;
        int[] queueInts = new int[queueShapes.size()];
        for (int i = 0; i < queueShapes.size(); i++) {
            TetrisShape shape = queueShapes.get(i);
            queueInts[i] = shape.id;
        }
        
        // Convert the active piece grid to booleans.
        boolean[][] activeGrid = new boolean[10][20];
        // Assuming gamestate.queue.get(0) is the active piece.
        for (int[] b : gamestate.queue.get(0).blockPlacements(gamestate.shapeX, gamestate.shapeY)) {
            if (b[0] >= 0 && b[0] < 10 && b[1] >= 0 && b[1] < 20) {
                activeGrid[b[0]][b[1]] = true;
            } else {
                System.out.println("Something is wrong with blockPlacements data");
            }
        }
        
        // One-hot conversion for the instruction (the output label).
        int[] instructionOneHot = oneHot(instruction);


        // Write the data entry to a file.
        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("tetris_dataset.bin", true)))) {
            // Write grid data (10x20 booleans).
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 20; y++) {
                    dos.writeBoolean(grid[x][y]);
                }
            }
            // Write active grid data (10x20 booleans).
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 20; y++) {
                    dos.writeBoolean(activeGrid[x][y]);
                }
            }
            // Write the queue.
            // First, write the length of the queue.
            dos.writeInt(queueInts.length);
            // Then, write each queue element as an int.
            for (int i = 0; i < queueInts.length; i++) {
                dos.writeInt(queueInts[i]);
            }
            // Write the one-hot encoded instruction.
            // Write the length (should be 7).
            dos.writeInt(instructionOneHot.length);
            // Then, write each element.
            for (int i = 0; i < instructionOneHot.length; i++) {
                dos.writeInt(instructionOneHot[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Define the possible moves
    private static final String[] MOVES = {"down", "left", "right", "drop", "rotateleft", "rotateright", "rotate180"};
    // Converts an instruction string to a one-hot encoded vector
    private int[] oneHot(String instruction) {
        int[] oneHotVector = new int[MOVES.length];
        // Initialize with 0's and set the matching move to 1 if found.
        for (int i = 0; i < MOVES.length; i++) {
            if (MOVES[i].equalsIgnoreCase(instruction)) {
                oneHotVector[i] = 1;
                break;
            } else {
                oneHotVector[i] = 0;
            }
        }
        return oneHotVector;
    }

    //Steps forward in the search, and generates more nodes. Returns the next step.
    private Node nextNode;
    public Node step(){
        Node n=nextNode;
        if(n.children==null) return null;
        //n.weightedGenerate(lookahead);
        Node head = n;
        n=n.maxDecendant();
        int count=0;
        while(n!=head){
            count++;
            nextNode=n;
            n=n.parent;
        }
        if (showNext) nextNode.highlightInParent();
        if (displayState){
            display.state=n.element;
            display.repaint();
        }
        if(lookahead-count>=stepSize) n.weightedGenerate(lookahead);
        n.parent=null;
        return n;
    }
    public JFrame makeFrame(GameDisplay display){
        JFrame GUI = new JFrame();
        int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        height-=80;
        System.out.println(height);
        int width = (int)((height-15)*0.675);
        GUI.setSize(width,height);
            GUI.setLayout(new GridLayout(1,1));
            GUI.setBackground(Color.BLACK);
            GUI.setTitle("Tetris");
            GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            GUI.getContentPane().add(display);
            GUI.setVisible(true);
            return GUI;
        }
}
