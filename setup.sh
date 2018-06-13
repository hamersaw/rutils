#!/bin/sh
BASEDIR=$(dirname $0)

# clone okde-java repository
mkdir $BASEDIR/libs
git clone https://github.com/joluet/okde-java.git $BASEDIR/libs/okde-java

# build project 
DIR=`pwd`
cd $BASEDIR/libs/okde-java
gradle build
cd $DIR

# copy jar file to libs
cp $BASEDIR/libs/okde-java/build/libs/okde-java-classes.jar $BASEDIR/libs

# remove git repository
rm -rf $BASEDIR/libs/okde-java
