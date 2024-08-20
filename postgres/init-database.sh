#!/bin/bash

set -e
set -u

function create_databases() {
	echo "  Creating database '$1'"
		psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
		    SELECT 'CREATE DATABASE "$1"'
        WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '"$1"')\gexec
EOSQL
}

if [ -n "$CREATE_DATABASES" ]; then
	echo "Multiple database creation requested: $CREATE_DATABASES"
	for db in $(echo $CREATE_DATABASES | tr ',' ' '); do
		create_databases $db
	done
	echo "Multiple databases created"
fi
