#!/usr/bin/env bash

mkdir elasticsearch

mkdir elasticsearch/data

chmod 777 elasticsearch/data

mvn clean install -DskipTests

docker-compose -f docker-compose.yml up --build