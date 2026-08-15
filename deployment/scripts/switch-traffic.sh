#!/bin/bash

set -e

TARGET="$1"

if [ "$TARGET" = "blue" ]; then
    sed -i 's/banking-green/banking-blue/' deployment/nginx/nginx.conf
elif [ "$TARGET" = "green" ]; then
    sed -i 's/banking-blue/banking-green/' deployment/nginx/nginx.conf
else
    echo "Usage: $0 blue|green"
    exit 1
fi

echo "Switching traffic to $TARGET"

echo "Testing Nginx configuration..."

docker exec banking-nginx nginx -t

echo "Recreating Nginx..."

docker compose -p banking-app up -d \
    --force-recreate \
    --no-deps \
    nginx

echo "Updating deployment metric..."

./deployment/scripts/update-active-metric.sh "$TARGET"

echo "Traffic switched to $TARGET"
