#!/bin/bash

set -e

ACTIVE="$1"

if [ "$ACTIVE" != "blue" ] && [ "$ACTIVE" != "green" ]; then
    echo "Usage: $0 blue|green"
    exit 1
fi

NGINX_CONFIG="deployment/nginx/nginx.conf"

echo "====================================="
echo "Rolling back to $ACTIVE"
echo "====================================="

echo "Updating Nginx configuration..."

sed -i "s/server banking-.*/server banking-$ACTIVE:8080;/" \
"$NGINX_CONFIG"

echo "Testing Nginx configuration..."

docker exec banking-nginx nginx -t

echo "Recreating Nginx..."

docker compose -p banking-app up -d \
    --force-recreate \
    --no-deps \
    nginx

echo "Updating deployment metric..."

./deployment/scripts/update-active-metric.sh "$ACTIVE"

echo "====================================="
echo "Rollback completed"
echo "Active deployment: $ACTIVE"
echo "====================================="
