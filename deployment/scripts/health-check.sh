#!/bin/bash

TARGET=$1

URL=http://$TARGET:8080

echo "Checking $URL"

for i in {1..30}
do

    STATUS=$(docker exec banking-nginx \
        wget -q --server-response \
        --spider \
        http://banking-$TARGET:8080 \
        2>&1 \
        | awk '/HTTP/{print $2}')

    if [ "$STATUS" = "200" ]
    then

        echo "Healthy"

        exit 0

    fi

    sleep 2

done

echo "FAILED"

exit 1
