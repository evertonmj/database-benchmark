# Database Benchmarking: Redis vs PostgreSQL Full-Text Search

This project performs a benchmarking comparison between **Redis** and **PostgreSQL** for full-text search capabilities. By using sample data and executing search queries against both databases, it provides insights into performance differences based on factors such as dataset size and query complexity.

---

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Setup Instructions](#setup-instructions)
- [Usage](#usage)
- [Benchmark Framework](#benchmark-framework)
- [Planned Improvements](#planned-improvements)
- [License](#license)

---

## Features

- **Full-Text Search**: Benchmarks full-text search functionality in Redis and PostgreSQL.
- **Dataset Loading**: Dynamically generates data or loads it directly into Redis and PostgreSQL.
- **Performance Measurement**: Compares execution time for search queries on both databases.
- **Multi-threaded Execution** (future improvement): Analyze performance under concurrent query loads.

---

## Technologies Used

This project leverages the following libraries and technologies:

- **Java 21**: Core programming language for application logic.
- **Redis**: Used for full-text search with Redis search capabilities.
    - Library: [Jedis](https://github.com/redis/jedis) for Redis CLI interaction.
- **PostgreSQL**: Used as a relational database with built-in full-text search features.
    - Library: JDBC Driver for database connectivity.
- **Loggers**: Java's `java.util.logging` for logging messages (recommendation: use SLF4J/Logback for advanced features).

---

## Setup Instructions

### Prerequisites

1. **Java Development Kit (JDK)**: Install JDK 21 or higher.
    - [Download JDK](https://www.oracle.com/java/technologies/javase-downloads.html)
2. **Redis**: Ensure Redis is installed and running.
    - Default Host: `127.0.0.1`
    - Default Port: `6379`
3. **PostgreSQL**: Ensure PostgreSQL is installed and accessible.
    - Default Host: `127.0.0.1`
    - Default Port: `5432`
    - Set up a database and user:
      ```sql
      CREATE DATABASE benchmark_db;
      CREATE USER postgres WITH PASSWORD 'Postgres2022!';
      GRANT ALL PRIVILEGES ON DATABASE benchmark_db TO postgres;
      ```
4. **Maven** (optional): Required if the project uses Maven to manage dependencies.

---

### Configuration

Update the database and Redis connection parameters in your code as follows:

- **PostgreSQL Configuration**:
  ```java
  private static final String POSTGRES_URL = "jdbc:postgresql://127.0.0.1:5432/benchmark_db";
  private static final String POSTGRES_USER = "postgres";
  private static final String POSTGRES_PASSWORD = "Postgres2022!";
  ```
- **Redis Configuration**:
  ```java
  private static final String REDIS_HOST = "127.0.0.1";
  private static final int REDIS_PORT = 6379;
  ```

Alternatively, externalize these configurations to a `config.properties` file or environment variables for better maintainability.

---

## Usage

### 1. Load Data

Run the application, and it will generate your dataset and load it into both Redis and PostgreSQL databases.

```bash
java -jar database-benchmark.jar
```

### 2. Perform Benchmarking

The program will execute and measure the following for each database:
- Full-text searches using sample queries.
- Query execution time comparison between Redis and PostgreSQL.

Sample usage in the code:
```java
FullTextSearchTest.performFullTextSearch("sample search term");
```

### 3. View Results

Results are printed in the console. Optionally, you can enhance this to write to a log file or an external analytics dashboard.

---

## Benchmark Framework

The benchmarking process involves the following steps:

1. **Data Loading**: Populate databases with randomly generated entries.
    - Redis: Uses UnifiedJedis to store and search data.
    - PostgreSQL: Inserts data into relational tables with full-text search enabled.
2. **Index Creation**:
    - Redis: Creates Redis-specific full-text indexes for search.
    - PostgreSQL: Uses built-in indexing mechanisms like GIN or GiST for full-text search.
3. **Query Execution**: Measure query execution times for both databases using sample search terms.

---

## Planned Improvements

The following improvements are under consideration:

1. **Multi-threaded Concurrency Testing**:
    - Simulate multiple concurrent search queries to analyze performance under load.
2. **Connection Pooling**:
    - Integrate connection pools (e.g., HikariCP for PostgreSQL) into database connection logic.
3. **Benchmark Visualization**:
    - Create a visual representation (charts/tables) of benchmark results.
4. **Refactoring**:
    - Split responsibilities across multiple classes (e.g., RedisUtility, PostgresUtility) to align with clean code practices.
5. **Testing Framework**:
    - Add unit and integration tests to validate functionality.
6. **External Configuration Management**:
    - Support configuration files or environment variables for database connection settings.

---

## License

This project is licensed under the MIT License. See the `LICENSE` file for more details.

---