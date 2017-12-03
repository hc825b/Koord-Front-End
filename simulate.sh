#!/bin/sh
python simulate.py $1
an=$(basename "$1" .krd) # FIXME This doesn't work if filename is not lowercase of program name

mvn compile 
mvn install
HOME_DIR=`echo "$HOME"`
mvn exec:java -Dexec.mainClass="testSim.apps.$an.Main"
