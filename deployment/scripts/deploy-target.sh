#!/bin/bash

if grep -q "banking-blue" deployment/nginx/nginx.conf
then
    echo "ACTIVE=blue"
    echo "TARGET=green"
else
    echo "ACTIVE=green"
    echo "TARGET=blue"
fi
