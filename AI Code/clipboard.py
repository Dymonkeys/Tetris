import pyperclip

# Path to the text file where the clipboard contents will be saved
file_path = "clipboard.txt"

# Read the current clipboard contents
clipboard_text = pyperclip.paste()

# Save the clipboard contents to the text file
with open(file_path, "w") as file:
    file.write(clipboard_text)

# Set the previous clipboard contents to the current clipboard contents
# This will be used to compare the current clipboard contents with the next clipboard contents
prev_clipboard_text = clipboard_text

# Continue checking the clipboard contents until the program is interrupted
while True:
    # Read the current clipboard contents
    clipboard_text = pyperclip.paste()

    # If the clipboard contents have changed since the last time they were checked,
    # save the new clipboard contents to the text file
    if clipboard_text != prev_clipboard_text:
        with open(file_path, "w") as file:
            file.write(clipboard_text)

        # Set the previous clipboard contents to the current clipboard contents
        prev_clipboard_text = clipboard_text
