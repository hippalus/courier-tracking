#!/usr/bin/env bash

mkdir elasticsearch

mkdir elasticsearch/data

chmod 777 elasticsearch/data

mvn clean install -DskipTests

# shellcheck disable=SC2164
cd courier-geo-locations/

docker build -f Dockerfile -t courier-geo-locations .

# shellcheck disable=SC2164
cd ../courier-event-processor

docker build -f Dockerfile -t courier-event-processor .

cd ../

docker-compose -f docker-compose.yml up --build


# shellcheck disable=SC2212
while [ ]; do
  sleep 1
  curl --request GET -sL \
    --url 'localhost:9200' && break || echo "ElasticSearch server is not responding"
done

curl -XPOST localhost:9200/stores/_bulk?pretty --data-binary @stores.json -H 'Content-Type: application/json'