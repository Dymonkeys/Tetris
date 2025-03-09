/*
 * File: TetrisMain.java
 * Author: David Neufeld
 * Created Date: Mon Dec 05 2022 at 10:50:05 AM
 * E-mail: david.neufeld@maine.edu
 * Description:
 * The TetrisMain class is the main class of a program that plays Tetris using 
 * a search tree. It contains the main run loop, generates new nodes, 
 * and determines the next move. It also has methods to create a JFrame for the 
 * game display and step forward in the search tree. It takes parameters for 
 * game speed, number of moves to consider per step, and number of moves to 
 * generate at once.
 * Collaboration: None
 */
package Tetris;

import javax.swing.JFrame;
import java.awt.*;

public class TetrisMain {
    private double speed;
    private int lookahead;
    private GameState startState;
    private boolean showNext = true; //Enables the white visualization of next placement
    private int stepSize;//Controls how many of the next moves are generated at once
    private GameDisplay display;
    //private Node head; //Head of tree and traversal Node
    public static void main(String[] args) {
        new TetrisMain(10, 5,1);
        //new TetrisMain(1, 6,5);
        //new TetrisMain(30, 5,2);
    }
    public TetrisMain(double speed, int lookahead, int stepSize){
        this.speed=speed;
        this.lookahead=lookahead;
        this.stepSize=stepSize;
        this.nextNodes = new Node[stepSize];
        //if(stepSize!=1) showNext = false;
        startState = new GameState();
        GameDisplay display = new GameDisplay(startState);
        makeFrame(display);
        run(display);
    }
    //Creates a JFrame for a GameDisplay object,
    //and automatically sizes it to fit the screen.
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
    //contains the main run loop
    public void run(GameDisplay display){
        this.display=display;
        Node head = new Node(startState);
        head.weightedGenerate(lookahead);
        nextNode=head;
        long mark=System.nanoTime();
        while (true) {
            if(System.nanoTime()-mark>=1000000000/speed){
                mark=System.nanoTime();
                //head = threadedStep();
                head = step();
                display.state=head.element;
                display.repaint();
            }
        }
        
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
        display.state=n.element;
        display.repaint();
        if(lookahead-count>=stepSize) n.weightedGenerate(lookahead);
        n.parent=null;
        return n;
    }
    boolean generating = false;
    int count=0;
    
    
    //Queue nextNodes;
    Node[] nextNodes = new Node[stepSize];
    Thread generate = new Thread(() -> weightedGenerate(nextNodes[nextNodes.length-1], lookahead));
    public Node threadedStep(){
        Node n = nextNodes[0];
        if(n==null) n=nextNode;
        n.parent=null;
        display.state=n.element;
        display.repaint();
        //shift queue
        for(int i=1;i<nextNodes.length;i++) nextNodes[i-1]=nextNodes[i];
        nextNodes[nextNodes.length-1]=null;
        //If there are nodes on the queue, then return first in queue
        if(nextNodes[0]!=null) return n;
        //If there are no nodes on the queue, wait for generating to finish
        while(generating) System.out.println("waiting...");
        //Add the new nodes to the queue
        Node head = n;
        n=n.maxDecendant();
        int count=lookahead;
        while(n!=head){
            count--;
            nextNode=n;
            n=n.parent;
            if(count<stepSize) nextNodes[count]=n;
        }
        //Start generating more nodes
        //generate.dumpStack();
        Thread generate = new Thread(() -> 
            weightedGenerate(nextNodes[nextNodes.length-1], lookahead));
        generate.start();
        return n;
    }
    public void weightedGenerate(Node n, int lookahead){
        generating=true;
        System.out.println("Start");
        n.weightedGenerate(lookahead);
        generating=false;
        count=0;
        System.out.println("done");
    }
}
