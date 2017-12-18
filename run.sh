#!/bin/sh
mvn compile exec:java -Dexec.mainClass="testSim.apps.$1.Main"
