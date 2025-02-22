echo "Starting Postgres..."
docker run --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -d postgres