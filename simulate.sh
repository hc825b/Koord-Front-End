#!/bin/sh
cd appCode
cp $1 ../
cd ..
cp $1 KoordFE/
cd KoordFE
python parser.py $1
an="$(echo $1 | cut -d '.' -f 1)"

mkdir ../src/main/java/testSim/apps/$an
mv *.java ../src/main/java/testSim/apps/$an
cd ..
mvn compile 
mvn install
HOME_DIR=`echo "$HOME"`
mvn exec:java -Dexec.mainClass="testSim.$an.Main"
rm $1
