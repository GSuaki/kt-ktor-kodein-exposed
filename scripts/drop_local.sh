#!/usr/bin/env sh

MYSQL_INVOICE_DATA="$HOME/.databases/mysql/invoices"
MYSQL_CENTRAL_DATABASE="invoices"
DOCKER_NAME="invoices_db"
TIMEOUT_AVAILABLE=30
MYSQL_ROOT_PASSWORD="123456"

echo "\033[1m> Checking if MySQL is up\033[0m..."
if
  ! docker exec -ti "${DOCKER_NAME}" /bin/sh -c "
    mysql -hlocalhost -uroot -p${MYSQL_ROOT_PASSWORD} ${MYSQL_CENTRAL_DATABASE} -e SELECT\ 1
  " 2>&1 >/dev/null
then
  echo "\033[1m> MySQL is not available. Please run mysql_run.sh to start it.\033[0m"
  exit
fi
echo "\033[1m> Running migration scripts locally\033[0m..."

PARENT_FOLDER_LOCATION="$(cd "$(dirname $0)/.."; pwd)"

echo "$(
  cat <<FIND_TABLES | docker exec -i "${DOCKER_NAME}" /bin/sh -c "mysql --skip-column-names -hlocalhost -uroot -p${MYSQL_ROOT_PASSWORD} ${MYSQL_CENTRAL_DATABASE}"
  SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='${MYSQL_CENTRAL_DATABASE}'
FIND_TABLES
)" |
  while read -r table_name
  do
    if test -z "$table_name"
    then
      continue
    fi
    echo "DROP TABLE IF EXISTS $table_name" | docker exec -i "${DOCKER_NAME}" /bin/sh -c "mysql  -hlocalhost -uroot -p${MYSQL_ROOT_PASSWORD} ${MYSQL_CENTRAL_DATABASE}"
  done