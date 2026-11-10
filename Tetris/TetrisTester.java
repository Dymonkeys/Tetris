package Tetris;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;
public class TetrisTester {
    public static void main(String[] args) {
        GameState startState = new GameState();
        createShapeQueue(startState, 20);
        randomDropShapes(startState, 7);
        Node head = new Node(startState);
        head.generateChildren(6);
        head.generateChildren(12);
        //displayStates(head.children);

        //displayStates(startState);
        //System.out.println(startState.evaluate());

        //Switch
        weightedGenerateDemo();
        tetrisDemo(30,10000,5,true);
    }
    public static GameGrid displayStates(GameState state){
        GameState[] states = {state};
        return displayStates(states);
    }
    public static GameGrid displayStates(Node node){
        GameState[] states = {node.element};
        return displayStates(states);
    }
    public static void displayStates(Node[] nodes){
        GameState[] states = new GameState[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            states[i]=nodes[i].element;
        }
        displayStates(states);
    }
    public static GameGrid displayStates(ArrayList<GameState> states){
        GameState[] s = new GameState[states.size()];
        states.toArray(s);
        return displayStates(s);
    }

    public static GameGrid displayStates(GameState[] states){
        return displayStates(states,"Tetris");
    }
    public static GameGrid displayStates(GameState[] states, String title){
        int count = states.length;
        JFrame GUI = new JFrame();
        //GUI.setSize(Math.min(400*count,1600),800);
        //calculate layout
        int rows=1;
        int columns=1;
        while(rows*columns<count){
            columns++;
            if(columns/6.0>rows) {
                columns=(rows*columns)/(rows+1);
                rows++;
            }
        }
        int height = 800;
        int screenWidth=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int width = Math.min(((height-25)/rows/2)*columns,screenWidth);
        //int width = 1500;
        if(width==screenWidth) height=screenWidth/columns*rows*2+25;
        GUI.setSize(width,height);
        GUI.setLayout(new GridLayout(rows,columns));
        GUI.setBackground(Color.BLACK);
        GUI.setTitle(title);
        GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container pane = GUI.getContentPane();
        if(states.length==1){
            GameGrid grid = new GameGrid(states[0]);
            pane.add(grid);
            GUI.setVisible(true);
            return grid;
        }
        for (GameState state : states) {
            pane.add(new GameGrid(state));
        }
        GUI.setVisible(true);
        return null;
    }
    public static void createShapeQueue(GameState state,int length){
        for (int i = 0; i < length; i++) {
            state.addToQueue(new TetrisShape());
        }
    }
    public static void randomDropShapes(GameState state,int count){
        for (int i = 0; i < count; i++) {
            state.randomizeX();
            state.dropShape();
        }
    }
    public static void waitForInput(){
        Scanner s = new Scanner(System.in);
        System.out.print("Waiting for input...");
        s.nextLine();
        s.close();
    }
    public static void weightedGenerateDemo(){
        GameState startState = new GameState();
        randomDropShapes(startState, 7);
        Node head = new Node(startState);
        head.weightedGenerate(5);
        displayStates(head);
        Node displayNode = head;
        long mark = System.nanoTime();
        //displayStates(displayNode.children);
        while(displayNode.children!=null){
            if(System.nanoTime()-mark>500000000){
                displayStates(displayNode.children);
                System.out.println(displayNode.children.length);
                mark = System.nanoTime();
                displayNode=displayNode.children[0];
            }
        }
    }
    public static void tetrisDemo(int speed){
        tetrisDemo(speed,10000,5,true);
    }
    public static void tetrisDemo(int speed,int total,int lookahead,boolean showNext){
        GameState startState = new GameState();
        Node head = new Node(startState);
        long mark = System.nanoTime();
        head.recursiveGenerate(4);
        head=head.maxDecendant();
        head.parent=null;
        mark=System.nanoTime();
        GameGrid grid = displayStates(head);
        Node next=head;
        for(int i=0;i<total;i+=0){
            if(System.nanoTime()-mark>=1000000000/speed){
                mark=System.nanoTime();
                i++;
                head.parent=null;
                head=next;
                head.weightedGenerate(lookahead);
                //head.recursiveGenerate(lookahead);
                head=head.maxDecendant();
                for(int j=1;j<lookahead;j++) head=head.parent;
                next = head;
                head=head.parent;
                if (showNext) next.highlightInParent();
                grid.state=head.element;
                grid.repaint();
                //System.out.println("Value: "+head.evaluate());
            }
        }
        head=null;
        System.out.println("Done");
    }
}
