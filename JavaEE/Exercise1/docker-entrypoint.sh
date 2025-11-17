#!/bin/bash

# Wait for database to be ready
echo "Waiting for database to be ready..."
until mysqladmin ping -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" --silent; do
  echo "Database not ready, waiting..."
  sleep 2
done

echo "Database is ready. Configuring WildFly datasource..."

# Start WildFly in background
/opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 &

# Wait for WildFly to start
echo "Waiting for WildFly to start..."
sleep 10

# Configure datasource
echo "Configuring datasource..."
/opt/jboss/wildfly/bin/jboss-cli.sh -c --file=/opt/jboss/wildfly/bin/configure.cli

echo "Configuration complete. WildFly is ready."

# Bring WildFly to foreground
wait
