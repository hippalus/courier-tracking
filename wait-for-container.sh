#!/usr/bin/env bash

# shellcheck disable=SC2160
while [ ]; do
  sleep 1
  curl --request GET -sL \
    --url 'localhost:9200' && break || echo "ElasticSearch server is not responding"
done

curl -XPOST localhost:9200/stores/_bulk?pretty --data-binary @stores.json -H 'Content-Type: application/json'