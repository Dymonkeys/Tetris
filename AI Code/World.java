import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
public class World {
    // Represents a light source in a 2D scene
class Light {
    // The position of the light source
    public Point position;
  
    // The intensity of the light
    public float intensity;
  
    // Constructs a new light with the given position and intensity
    public Light(Point position, float intensity) {
      this.position = position;
      this.intensity = intensity;
    }
  }
  // Represents a two-dimensional vector
class Vector extends Point2D.Float {
    // Constructs a new vector with the given x and y components
    public Vector(float x, float y) {
      super(x, y);
    }
  
    // Adds the given vector to this vector and returns the result
    public Vector add(Vector other) {
      return new Vector(this.x + other.x, this.y + other.y);
    }
  
    // Subtracts the given vector from this vector and returns the result
    public Vector subtract(Vector other) {
      return new Vector(this.x - other.x, this.y - other.y);
    }
  
    // Multiplies this vector by the given scalar and returns the result
    public Vector multiply(float scalar) {
      return new Vector(this.x * scalar, this.y * scalar);
    }
  
    // Divides this vector by the given scalar and returns the result
    public Vector divide(float scalar) {
      return new Vector(this.x / scalar, this.y / scalar);
    }
  
    // Returns the magnitude of this vector
    public float magnitude() {
      return (float)Math.sqrt(this.x * this.x + this.y * this.y);
    }
  
    // Normalizes this vector and returns the result
    public Vector normalize() {
      float magnitude = this.magnitude();
      return new Vector(this.x / magnitude, this.y / magnitude);
    }
  
  // Represents an object in a 2D scene
  class SceneObject {
    // The position of the object
    public Point position;
  
    // The velocity of the object
    public Vector velocity;
  
    // The mass of the object
    public float mass;
  
    // The color of the object
    public Color color;
  
    // The ambient light level at the object's position
    public float ambientLight;
  
    // Constructs a new object with the given position, velocity, mass, color, and ambient light level
    public SceneObject(Point position, Vector velocity, float mass, Color color, float ambientLight) {
      this.position = position;
      this.velocity = velocity;
      this.mass = mass;
      this.color = color;
      this.ambientLight = ambientLight;
    }
  
    // Updates the object's position based on its velocity and the time elapsed since the last update
    public void update(float elapsedTime) {
      this.position = this.position.add(this.velocity.multiply(elapsedTime));
    }
  
    // Returns the color of the object, taking into account the effects of lighting
    public Color getColor() {
      // TODO: Compute the color of the object based on the light sources in the scene
  
      return this.color;
    }
  }
  
  // Represents a 2D scene containing objects and light sources
  class Scene {
    // The objects in the scene
    public List<SceneObject> objects;
  
    // The light sources in the scene
    public List<Light> lights;
  
    // Constructs a new scene with the given objects and lights
    public Scene(List<SceneObject> objects, List<Light> lights) {
      this.objects = objects;
      this.lights = lights;
    }
  
    // Updates the positions of all objects in the scene based on their velocities and the time elapsed since the last update
    public void update(float elapsedTime) {
      for (SceneObject object : this.objects) {
        object.update(elapsedTime);
      }
    }
  
    // Renders the scene and returns the resulting image
    public BufferedImage render() {
      // TODO: Render the scene and return the resulting image
  
      return null;
    }
  }
  
}
