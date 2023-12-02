#!/bin/bash

# check if directory arg is provided
if [ $# -eq 0 ]; then
  echo "Please provide a directory as an argument."
  exit 1
fi

echo "Running main.kt in $1"

# change directory to provided arg
cd "$1" || exit 1

# compile main.kt to JAR
kotlinc main.kt -include-runtime -d main.jar

# run JAR
java -jar main.jar

# clean up
rm main.jar
