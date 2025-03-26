echo "Starting redis..."
docker run -d --name redis-stack-server-local -p 6379:6379 redis/redis-stack-server:latest
