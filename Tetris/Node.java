
package Tetris;
import Tetris.GameState.Instruction;
/*
 * File: Node.java
 * Author: David Neufeld
 * Created Date: Wed Sep 14 2022 at 3:56:22 PM
 * E-mail: david.neufeld@maine.edu
 * Description:
 * Used to organize game state in a tree structure . Contains a gamestate, 
 * an array of child nodes, and function to recursively generate child nodes.
 * Collaboration: None
 */
public class Node {
    GameState element;
    Node parent;
    Instruction[] path;//unused
    int placeX, placeY, placeR;
    Node[] children;
    int[][] debugGrid;
    public Node(GameState state){
        this.element = state;
    }
    public Node(GameState state, Instruction[] path, int x, int y, int r){
        this.element=state;
        this.path=path;
        this.placeX=x;
        this.placeY=y;
        this.placeR=r;
    }
    public void recursiveGenerate(int n){
        generateChildren();
        n--;
        if(n>0)
        for (Node node : children) {
            node.recursiveGenerate(n);
        }
    }
    public void weightedGenerate(){
        weightedGenerate(4);
    }
    //Generates n number of children, and recursively calls each child to generate a smaller number of children.
    public void weightedGenerate(int n){
        int count = Math.min( (4*(n-1))+2 ,14);
        generateChildren(count);
        //System.out.println(50-50/n);
        n--;
        if(n>0)
        for (Node node : children) {
            node.weightedGenerate(n);
        }
    }
    //Generates n children ffor this node based on placement options
    public void generateChildren(int n){
        children = element.placementOptions(n,this);
    }
    //Generates maximum possible number of children
    public void generateChildren(){
        generateChildren(-1);
    }
    public int evaluation=-999;
    public int evaluate(){
        if(evaluation!=-999) return evaluation;
        evaluation=element.evaluate();
        return evaluation;
    }
    //Returns the node which has the highest evaluating game state of all decendants
    public Node maxDecendant(){
        if(children==null) return this;
        int max=-999;
        Node maxNode = null;
        for (Node node : children) {
            Node thisNode = node.maxDecendant();
            int eval = thisNode.evaluate();
            if(eval>max) {
                max=eval;
                maxNode=thisNode;
            }
        }
        return maxNode;
    }
    //Highlights the placement of the block placed to create this node in white.
    public void highlightInParent(){
        TetrisShape shape = parent.element.activeShape().clone();
        shape.rotation=placeR;
        parent.element.placeShape(shape, placeX, placeY,-1);
    }

    public int depth(){
        if(children==null) return 0;
        return children[0].depth()+1;
    }
}