#!/bin/bash

TARGET="$1"

METRIC_FILE="/var/lib/node_exporter/textfile_collector/banking_deployment.prom"

if [ "$TARGET" = "blue" ]; then
    BLUE=1
    GREEN=0
elif [ "$TARGET" = "green" ]; then
    BLUE=0
    GREEN=1
else
    echo "Usage: $0 blue|green"
    exit 1
fi

cat > "$METRIC_FILE" <<EOF
# HELP banking_deployment_active Currently active production deployment
# TYPE banking_deployment_active gauge
banking_deployment_active{environment="blue"} $BLUE
banking_deployment_active{environment="green"} $GREEN
EOF

echo "Active deployment metric updated: $TARGET"
