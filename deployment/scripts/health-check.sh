#!/bin/bash

TARGET="$1"

if [ "$TARGET" != "blue" ] && [ "$TARGET" != "green" ]; then
    echo "Usage: $0 blue|green"
    exit 1
fi

CONTAINER="banking-${TARGET}"
URL="http://${CONTAINER}:8080/"

echo "Checking ${URL} from banking-nginx"

for i in {1..30}
do
    if docker exec banking-nginx \
        wget -qO /dev/null \
        --timeout=5 \
        "$URL"
    then
        echo "Healthy"
        exit 0
    fi

    echo "Attempt ${i}/30 failed. Waiting..."
    sleep 2
done

echo "FAILED"
exit 1
