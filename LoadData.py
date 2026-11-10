import struct
import numpy as np

def load_tetris_dataset(filename):
    entries = []
    with open(filename, 'rb') as f:
        while True:
            # Read the grid: 10x20 booleans (200 bytes)
            grid_bytes = f.read(200)
            if len(grid_bytes) < 200:
                break  # End of file
            # Convert to a 10x20 boolean array. DataOutputStream writes booleans as one byte (0 or 1).
            grid = np.frombuffer(grid_bytes, dtype=np.uint8).reshape((10, 20)).astype(bool)
            
            # Read the active grid: 10x20 booleans (200 bytes)
            active_grid_bytes = f.read(200)
            if len(active_grid_bytes) < 200:
                break
            active_grid = np.frombuffer(active_grid_bytes, dtype=np.uint8).reshape((10, 20)).astype(bool)
            
            # Read the queue length (an int, 4 bytes, big-endian)
            queue_length_bytes = f.read(4)
            if len(queue_length_bytes) < 4:
                break
            queue_length = struct.unpack(">i", queue_length_bytes)[0]
            
            # Read the queue integers (each 4 bytes, big-endian)
            queue_ints = []
            for _ in range(queue_length):
                int_bytes = f.read(4)
                if len(int_bytes) < 4:
                    break
                queue_int = struct.unpack(">i", int_bytes)[0]
                queue_ints.append(queue_int)
            queue_ints = np.array(queue_ints, dtype=int)
            
            # Read the one-hot instruction vector length (should be 7, 4 bytes, big-endian)
            onehot_length_bytes = f.read(4)
            if len(onehot_length_bytes) < 4:
                break
            onehot_length = struct.unpack(">i", onehot_length_bytes)[0]
            
            # Read the one-hot vector (onehot_length ints, each 4 bytes)
            onehot_vector = []
            for _ in range(onehot_length):
                int_bytes = f.read(4)
                if len(int_bytes) < 4:
                    break
                val = struct.unpack(">i", int_bytes)[0]
                onehot_vector.append(val)
            onehot_vector = np.array(onehot_vector, dtype=int)
            
            entries.append({
                "grid": grid,
                "active_grid": active_grid,
                "queue": queue_ints,
                "instruction": onehot_vector
            })
    return entries

# Example usage
#dataset = load_tetris_dataset("Datasets/tetris_dataset.bin")
#print("Loaded", len(dataset), "entries.")
#
#if dataset:
#    # Display the first entry as an example.
#    first = dataset[50]
#    print("Grid:")
#    print(first["grid"])
#    print("\nActive Grid:")
#    print(first["active_grid"])
#    print("\nQueue:", first["queue"])
#    print("Instruction (one-hot):", first["instruction"])
