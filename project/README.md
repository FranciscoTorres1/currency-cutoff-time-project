
# Nordea currency cut-off project

A java spring boot service that finds the earliest cut-off time between two currencies.

## API Reference

#### Get the deadline cut-off time between 2 currencies

```http
  GET cutoffTime/{currency1}/{currency2}?dateOfTrade
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `currency1` | `string` | **Required**. The ISO of the first currency |
| `currency2` | `string` | **Required**. The ISO of the second currency |
| `dateOfTrade` | `string` | **Required**. The given date of the trade between the currencies with the format yyyy-mm-dd|

An example:
```http
  http://localhost:8080/cutoffTime/AED/USD?dateOfTrade=2023-03-21
```

### Other Documentation
OpenAPI documentation as YAML:

``` yaml
openapi: 3.0.1
info:
  title: Currency cut-off time project
  version: v1
servers:
- url: http://localhost:8080
  description: Generated server url
paths:
  /cutoffTime/{currency1}/{currency2}:
    get:
      tags:
      - cutoff-time-controller
      operationId: getCutoffTime
      parameters:
      - name: dateOfTrade
        in: query
        required: true
        style: form
        explode: true
        schema:
          type: string
          format: date
      - name: currency1
        in: path
        required: true
        style: simple
        explode: false
        schema:
          type: string
      - name: currency2
        in: path
        required: true
        style: simple
        explode: false
        schema:
          type: string
      responses:
        "200":
          description: OK
          content:
            '*/*':
              schema:
                type: string
components:
  schemas: {}

```
### Curl

```bash
  curl -X 'GET' \
  'http://localhost:8080/cutoffTime/AED/USD?dateOfTrade=2023-03-21' \
  -H 'accept: */*'
```
## Running the project
There is a runnable jar in the project `project-0.0.1-SNAPSHOT.jar`which can be run be this command

```bash
$ ./mvnw clean package
$ java -jar target/project-0.0.1-SNAPSHOT.jar
```
### Docker
The project also has `Dockerfile` and `docker-compose.yml` files. The project can then be run as Docker container either by the Dockerfile:
```bash
$ docker build -t nordea/projectapp .
$ docker run -p 8080:8080 nordea/projectapp
```
or with docker compose:
```bash
$ docker compose up
```
## Extra Info
### Database
The database is an in-memory H2 database. When running the project the content of the database can be seen with H2 console at:

```http
  http://localhost:8080/h2-console
```
The username is `sa` and the password is `password`.

The file `data.sql` contains the whole currency table as INSERT statements given by the assignment. The script also populates the data for some of the tests.
The file is located at:

project\src\main\resources\data.sql

The actual database file is ``demodb.mv.db`
### Swagger
Also Swagger is utilized for some API documentation while running the project, which can be seen at:
```http
  http://localhost:8080/swagger-ui/index.html
```