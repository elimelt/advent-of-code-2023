#!/bin/bash

# Check if directory argument is provided
if [ $# -eq 0 ]; then
  echo "Please provide a directory as an argument."
  exit 1
fi

echo "Running main.kt in ./$1"

# Change to the specified directory
cd "$1" || exit 1

# Compile the Kotlin file and create a JAR file
kotlinc main.kt -include-runtime -d main.jar

# Run the JAR file
java -jar main.jar

# Remove the JAR file
rm main.jar
