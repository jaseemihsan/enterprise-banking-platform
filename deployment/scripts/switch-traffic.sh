#! /bin/bash

TARGET=$1

if [ "$TARGET" = "blue" ]
then
	sed -i \
"s/server banking-.*/server banking-$TARGET:8080;/" \
deployment/nginx/nginx.conf

else
    sed -i 's/banking-blue/banking-green/' deployment/nginx/nginx.conf
fi

docker exec banking-nginx nginx -t

docker compose up -d --force-recreate --no-deps nginx
