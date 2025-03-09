# File: words.py
# Author: David Neufeld
# Date: Thu Dec 08 2022
# Lab Section: 1003
# E-mail: david.neufeld@maine.edu
# Description:
# 
# Collaboration: 
# 
# define a function that takes a pair of words as input
def find_pairs_of_words(word1, word2):

  # convert the words to sets of characters, to make it easier to compare them
  set1 = set(word1)
  set2 = set(word2)

  # check if the sets of characters have any letters in common
  if set1.intersection(set2):
    # if the sets have any letters in common, return False
    return False
  else:
    # if the sets have no letters in common, return True
    return True


# define a list of 5-letter words
words = ["apple", "table", "flower", "river", "ocean", "mouse", "plane", "house", "horse", "train"]

# loop through the list of words, comparing each word to every other word
for i in range(len(words)):
  for j in range(i+1, len(words)):

    # for each pair of words, call the find_pairs_of_words function
    result = find_pairs_of_words(words[i], words[j])

    # if the function returns True, print the pair of words
    if result:
      print(words[i], words[j])