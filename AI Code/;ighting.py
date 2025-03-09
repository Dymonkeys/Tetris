from PIL import Image, ImageDraw
import math

# Create a new image with a white background
width = 400
height = 400
img = Image.new('RGB', (width, height), color = (255, 255, 255))

# Draw a grid of pixels on the image
draw = ImageDraw.Draw(img)

# Set the size of the grid cells
cell_size = 20

# Define a light source at a specific position and intensity
light_x = 100
light_y = 100
light_intensity = 255

# Iterate over the rows and columns of the image
for i in range(0, width, cell_size):
    for j in range(0, height, cell_size):
        # Calculate the distance from the current cell to the light source
        distance = math.sqrt((i - light_x)**2 + (j - light_y)**2)

        # Use the distance to calculate the brightness of the pixel
        brightness = light_intensity / (distance + 1)

        # Draw a pixel with the calculated brightness at the current cell
        draw.point((i, j), fill=(brightness, brightness, brightness))

# Save the image
img.save('grid_with_lighting.png')
