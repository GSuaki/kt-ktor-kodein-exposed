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

find "./migrations" |
  sort -n |
  while read -r migration_file
  do
    if test -f "$migration_file"
    then
      cat "${migration_file}" |  docker exec -i "${DOCKER_NAME}" /bin/sh -c "
      mysql -hlocalhost -uroot -p${MYSQL_ROOT_PASSWORD} ${MYSQL_CENTRAL_DATABASE}
    "
    fi
  done