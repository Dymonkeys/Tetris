import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Main {
  
  public static void main(String[] args) {
    // Create a JFrame to hold the JPanel
    JFrame frame = new JFrame("Lighting Engine");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    // Create a JPanel to draw the grid of cells
    JPanel panel = new JPanel() {
      public void paintComponent(Graphics g) {
        // Draw the grid of cells
        drawGrid(engine, g);
      }
    };
    
    // Add the JPanel to the JFrame
    frame.add(panel);
    frame.pack();
    frame.setVisible(true);
    
    // Create a lighting engine with a 100x100 grid and three light sources
    final LightingEngine engine = new LightingEngine(100, 100, 3);
    
    // Set the initial positions of the light sources
    engine.setLightPosition(0, 10, 10);
    engine.setLightPosition(1, 90, 10);
    engine.setLightPosition(2, 50, 50);
    
    // Set the maximum brightness of cells in the grid
    engine.setMaxBrightness(10);
    
    // Create a timer that updates the lighting engine 30 times per second
    Timer timer = new Timer(1000 / 30, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // Update the positions of the light sources
        engine.setLightPosition(0, engine.getLightX(0) + 1, engine.getLightY(0) + 1);
        engine.setLightPosition(1, engine.getLightX(1) - 1, engine.getLightY(1) + 1);
        engine.setLightPosition(2, engine.getLightX(2) + 1, engine.getLightY(2) - 1);
        
        // Update the brightness of each cell in the grid
        engine.update();
        
        // Redraw the grid of cells
        panel.repaint();
      }
    });
    
    // Start the timer
    timer.start();
  }
  
  // Draws the grid of cells using the given graphics object and lighting
  public static void drawGrid(LightingEngine engine, Graphics g) {
    // Get the dimensions of the grid
    int width = engine.getWidth();
    int height = engine.getHeight();
    
    // Draw each cell in the grid as a colored square
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        // Get the brightness of the current cell
        float brightness = engine.getBrightness(x, y);
        
        // Calculate the color of the square based on the brightness
        Color color = new Color(brightness, brightness, brightness);
        
        // Set the color of the graphics object
        g.setColor(color);
        
        // Draw the square at the given coordinates
        g.fillRect(x * 10, y * 10, 10, 10);
      }
    }
  }
  final static LightingEngine engine = new LightingEngine(100, 100, 3);
    
  }
  