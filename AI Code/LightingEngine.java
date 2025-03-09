
public class LightingEngine {
  
  // The grid of cells, with each cell represented as a float value
  // representing its brightness
  private float[][] grid;
  
  // The x and y coordinates of the light sources
  private int[] lightX;
  private int[] lightY;
  
  // The maximum brightness of cells in the grid
  private float maxBrightness;
  
  public LightingEngine(int width, int height, int numLights) {
    this.grid = new float[width][height];
    this.lightX = new int[numLights];
    this.lightY = new int[numLights];
  }
  
  // Returns the width of the grid
  public int getWidth() {
    return grid.length;
  }
  
  // Returns the height of the grid
  public int getHeight() {
    return grid[0].length;
  }
  
  // Sets the position of the light with the given index
  public void setLightPosition(int lightIndex, int x, int y) {
    lightX[lightIndex] = x;
    lightY[lightIndex] = y;
  }
  
  // Returns the x coordinate of the light with the given index
  public int getLightX(int lightIndex) {
    return lightX[lightIndex];
  }
  
  // Returns the y coordinate of the light with the given index
  public int getLightY(int lightIndex) {
    return lightY[lightIndex];
  }
  
  // Sets the maximum brightness of cells in the grid
  public void setMaxBrightness(float maxBrightness) {
    this.maxBrightness = maxBrightness;
  }
  
  // Calculates the brightness of each cell in the grid
  public void update() {
    for (int x = 0; x < grid.length; x++) {
      for (int y = 0; y < grid[0].length; y++) {
        // Calculate the brightness of the current cell as the sum of the
        // brightnesses of all light sources
        float brightness = 0;
        for (int i = 0; i < lightX.length; i++) {
          // Calculate the distance of the current cell from the light source
          float distance = (float) Math.sqrt(Math.pow(lightX[i] - x, 2) + Math.pow(lightY[i] - y, 2));
          
          // Add the brightness of the current light source to the total brightness
          brightness += maxBrightness / (distance + 1);
        }
        
        // Set the brightness of the current cell
        grid[x][y] = brightness;
      }
    }
  }
  
  // Returns the brightness of the cell at the given coordinates
  public float getBrightness(int x, int y) {
    return grid[x][y];
  }
  
}