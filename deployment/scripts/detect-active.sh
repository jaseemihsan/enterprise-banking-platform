#!/bin/bash

if grep -q "banking-blue" deployment/nginx/nginx.conf
then
	echo "blue"
else
	echo "green"
fi
