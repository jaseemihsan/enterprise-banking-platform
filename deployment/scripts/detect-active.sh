#!/bin/bash

set -e

NGINX_CONFIG="deployment/nginx/nginx.conf"

if grep -q "server banking-blue:8080" "$NGINX_CONFIG"; then
    echo "blue"
elif grep -q "server banking-green:8080" "$NGINX_CONFIG"; then
    echo "green"
else
    echo "unknown"
    exit 1
fi
