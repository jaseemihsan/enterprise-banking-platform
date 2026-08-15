#!/bin/bash

set -e

ACTIVE=$(docker exec banking-nginx \
    nginx -T 2>/dev/null \
    | grep -E 'server banking-(blue|green):8080;' \
    | head -1)

if echo "$ACTIVE" | grep -q "banking-blue:8080"; then

    echo "blue"

elif echo "$ACTIVE" | grep -q "banking-green:8080"; then

    echo "green"

else

    echo "Unable to determine active environment" >&2
    exit 1

fi
