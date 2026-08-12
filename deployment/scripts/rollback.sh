#!/bin/bash

set -e

ACTIVE="$1"

if [ "$ACTIVE" != "blue" ] && [ "$ACTIVE" != "green" ]; then
    echo "Usage: $0 blue|green"
    exit 1
fi

echo "Rolling back to $ACTIVE"

sed -i "s/server banking-.*/server banking-$ACTIVE:8080;/" \
deployment/nginx/nginx.conf

docker exec banking-nginx nginx -t

docker compose up -d --force-recreate --no-deps nginx

./deployment/scripts/update-active-metric.sh "$ACTIVE"

echo "Rollback completed. Active deployment: $ACTIVE"
