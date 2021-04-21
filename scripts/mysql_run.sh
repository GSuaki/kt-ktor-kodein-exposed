#!/usr/bin/env sh

MYSQL_INVOICE_DATA="$HOME/.databases/mysql/invoices"
MYSQL_CENTRAL_DATABASE="invoices"
DOCKER_NAME="invoices_db"
TIMEOUT_AVAILABLE=30
MYSQL_ROOT_PASSWORD="123456"

if ! test -d "$MYSQL_INVOICE_DATA"
then
  mkdir -p ${MYSQL_INVOICE_DATA}
fi

echo "\033[1m> Initializing database ${MYSQL_CENTRAL_DATABASE}\033[0m..."

echo "\033[1m> Stopping any previous instances\033[0m..."
docker stop ${DOCKER_NAME} 2>/dev/null || true
echo "\033[1m> Removing any previous instances\033[0m..."
docker rm ${DOCKER_NAME} 2>/dev/null || true

echo "\033[1m> Starting the database (this might take a while)\033[0m..."
docker run --name "${DOCKER_NAME}" \
  -p "3311:3306" \
  -v "${MYSQL_INVOICE_DATA}:/var/lib/mysql" \
  -e "MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}" \
  -e "MYSQL_DATABASE=${MYSQL_CENTRAL_DATABASE}" \
  -d "mysql:8.0.22"

printf "\033[1m"
printf %s "> Waiting for the instance to be available"
printf "\033[0m"
while
  test ${TIMEOUT_AVAILABLE} -gt 0 &&
  ! docker exec -ti "${DOCKER_NAME}" /bin/sh -c "
    mysql -hlocalhost -uroot -p${MYSQL_ROOT_PASSWORD} ${MYSQL_CENTRAL_DATABASE} -e SELECT\ 1
  " 2>&1 >/dev/null
do
  printf %s "."
  TIMEOUT_AVAILABLE=$((TIMEOUT_AVAILABLE - 1))
  sleep 1
done

if test ${TIMEOUT_AVAILABLE} -lt 1
then
  echo "\033[1m> Waiting for MySQL to be available has timed out\033[0m"
  exit 1
fi

printf \\n
echo "\033[1m> Done!\033[0m"


# Em caso de erro de "/var/lib/mysql: Operation not permitted" verificar as permissoes e acessos de user | sudo chown -R $USER "$HOME/.databases"