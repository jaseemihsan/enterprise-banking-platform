#!/bin/bash

ACTIVE=$1

echo "Rolling back to $ACTIVE"

sed -i "s/server banking-.*/server banking-$ACTIVE:8080;/" \
deployment/nginx/nginx.conf

docker exec banking-nginx nginx -t || exit 1

docker compose up -d --force-recreate --no-deps nginx

echo "Rollback Completed"
