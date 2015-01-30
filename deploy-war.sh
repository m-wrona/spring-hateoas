#!/bin/bash

if [ -z $CATALINA_HOME ]
then
	echo "\$CATALINA_HOME not set"
	exit 1
fi

mvn clean package

rm -rf $CATALINA_HOME/webapps/hatoeas*

cp target/hatoeas*.war $CATALINA_HOME/webapps
