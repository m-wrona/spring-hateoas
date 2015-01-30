#!/bin/bash

mvn clean package | grep -v DEBUG
cp target/hateoas-*.war target/hateoas.jar
java -jar target/hateoas.jar

