/*
 * File: RunNeuralNetwork.java
 * Author: David Neufeld
 * Created Date: Mon Mar 10 2025 at 6:10:32 PM
 * E-mail: david.neufeld@maine.edu
 * Description:
 * 
 * Collaboration: 
 * 
 */
// Add the maven dependencies
package Tetris;

import ai.djl.Device;
import ai.djl.Model;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.NoopTranslator;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;

import Tetris.GameState.Instruction;
import Tetris.GameDisplay.*;
import Tetris.TetrisMain;
public class RunNeuralNetwork {
    // Create an NDManager to manage NDArrays.
    NDManager manager = NDManager.newBaseManager(Device.cpu());
    
    // Create a model instance. The name "TetrisNet" is just an identifier.
    Model model = Model.newInstance("TetrisNet");

    // Create a GameDisplay instance.
    private GameDisplay display;
    private GameState gamestate;
    float speed = 5;

    public static void main(String[] args) throws Exception {
        new RunNeuralNetwork();
    }

    public RunNeuralNetwork(){
        // Create an instance and call the inference method.
        gamestate = new GameState();
        gamestate.shapeX=5;
        display = new GameDisplay(gamestate);
        display.state=gamestate;
        TetrisMain.makeFrame(display);
        try {
            runModel();
            //dummyInference();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runModel() throws Exception {
        model.load(Paths.get("tetris_net.pt"));
        Predictor<NDList, NDList> predictor = model.newPredictor(new NoopTranslator());
        long mark = System.nanoTime();

        Instruction[] recentInstructions = new Instruction[5];
        int index = 0;
        while(true) {
            if(System.nanoTime() - mark >= 1000000000/speed) {
                mark = System.nanoTime();
                NDList input = transformData(gamestate);
                NDList output = predictor.predict(input);
                Instruction instruction = getInstruction(output);
                if (instruction == Instruction.drop) instruction = Instruction.down;
                recentInstructions[index] = instruction;
                index = (index + 1) % 5;
                if (!Arrays.asList(recentInstructions).contains(Instruction.down)) instruction = Instruction.down;
                gamestate.followInstruction(instruction);
                display.state=gamestate;
                display.repaint();
            }
        }
    }

    public NDList transformData(GameState gamestate) {
        // Convert the game grid to a float grid.
        // true represents a filled cell; false represents an empty cell.
        NDArray grid = manager.zeros(new Shape(1, 10, 20));
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 20; y++) {
                grid.set(new NDIndex(0, x, y), gamestate.grid[x][y] != 0 ? 1.0f : 0.0f);
            }
        }
        
        // Convert the active piece grid to a float grid.
        NDArray activeGrid = manager.zeros(new Shape(1, 10, 20));
        // Assuming gamestate.queue.get(0) is the active piece.
        for (int[] b : gamestate.queue.get(0).blockPlacements(gamestate.shapeX, gamestate.shapeY)) {
            if (b[0] >= 0 && b[0] < 10 && b[1] >= 0 && b[1] < 20) {
                activeGrid.set(new NDIndex(0, b[0], b[1]), 1.0f);
            } else {
                System.out.println("Something is wrong with blockPlacements data");
            }
        }
        
        // Convert the shape queue to a onehot array
        ArrayList<TetrisShape> queueShapes = gamestate.queue;
        float[][] queue_onehot = new float[1][64];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                queue_onehot[0][i*8+j] = 0;
            }
            if (i < queueShapes.size()) {
                int index = queueShapes.get(i).id;
                queue_onehot[0][i*8+index] = 1.0f;
            }
        }
        
        // Convert to NDArray
        NDArray queue = manager.create(queue_onehot);
        
        //return results
        return new NDList(grid, activeGrid, queue);
    }

    public Instruction getInstruction(NDList output) {
        float[] arr = output.get(0).toFloatArray();
        //Get max index of output array
        int maxIndex = 0;
        float maxValue = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > maxValue) {
                maxValue = arr[i];
                maxIndex = i;
            }
        }
        //Convert max index to instruction
        switch (maxIndex) {
            case 0:
                return Instruction.down;
            case 1:
                return Instruction.left;
            case 2:
                return Instruction.right;
            case 3:
                return Instruction.drop;
            case 4:
                return Instruction.rotateleft;
            case 5:
                return Instruction.rotateright;
            case 6:
                return Instruction.rotate180;
            default:
                return Instruction.down;
        }
    }
    
    public void dummyInference() throws Exception {
        // Load the TorchScript model from the file.
        model.load(Paths.get("tetris_net.pt"));

        // Create a Predictor using a NoopTranslator, which means
        // the NDList is passed through without conversion.
        Predictor<NDList, NDList> predictor = model.newPredictor(new NoopTranslator());

        // Create dummy input NDArrays:
        // 1. grid: shape (1, 10, 20)
        NDArray grid = manager.zeros(new Shape(1, 10, 20));
        // 2. activeGrid: shape (1, 10, 20)
        NDArray activeGrid = manager.zeros(new Shape(1, 10, 20));
        // 3. queue: shape (1, 8, 8) (8 queue elements, each one-hot encoded as an 8-dimensional vector)
        NDArray queue = manager.zeros(new Shape(1, 64));

        // Pack the inputs into an NDList in the order expected by your model.
        NDList input = new NDList(grid, activeGrid, queue);

        // Run inference.
        NDList output = predictor.predict(input);

        // Print the output.
        System.out.println("Model output:");
        for (NDArray arr : output) {
            System.out.println(arr);
        }

        System.out.println("Instruction: " + getInstruction(output));

        // Clean up: close the predictor, model, and manager.
        predictor.close();
        model.close();
        manager.close();
    }
}
