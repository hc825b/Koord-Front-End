#!/bin/sh
mvn clean install exec:java -Dexec.mainClass="testSim.apps.$1.Main"
