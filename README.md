# Account-service

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/account-service-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplify your persistence code for Hibernate ORM via the active record or the repository pattern
- SmallRye Health ([guide](https://quarkus.io/guides/smallrye-health)): Monitor service health
- Hibernate Validator ([guide](https://quarkus.io/guides/validation)): Validate object properties (field, getter) and method parameters for your beans (REST, CDI, Jakarta Persistence)
- SmallRye JWT ([guide](https://quarkus.io/guides/security-jwt)): Secure your applications with JSON Web Token
- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC
- SmallRye Metrics ([guide](https://quarkus.io/guides/smallrye-metrics)): Expose metrics for your services

## Architecture

This microservice is part of a distributed banking system. The architecture consists of three independent microservices:

- **customer-service**: Manages customer data and exposes endpoints for customer queries.
- **account-service** (this service): Manages bank accounts, balances, and exposes endpoints for account queries and updates. It consumes Kafka events to update balances and audit logs asynchronously.
- **transaction-service**: Orchestrates money transfers, publishes transaction events to Kafka, and records transaction history.

## Available Endpoints

- `GET /accounts/{accountNumber}`: Get account details by account number.
- `GET /accounts?customerId={uuid}`: List all accounts for a specific customer.
- `GET /accounts/{accountNumber}/balance`: Get the balance of an account.
- `POST /accounts`: Create a new account (admin only).
- `PUT /accounts/{accountNumber}`: Update account type or status (admin only).
- `PATCH /accounts/{accountNumber}/status?active={true|false}`: Change account status (admin only).

## Usage Examples

### Create Account
**Request:**
```json
POST /accounts
{
  "accountNumber": "1234567890",
  "customerId": "4f080edd-8ce2-4813-b453-9b2f268ef195",
  "accountType": "C",
  "balance": 1000.00
}
```
**Response:**
```json
{
  "accountNumber": "1234567890",
  "customerId": "4f080edd-8ce2-4813-b453-9b2f268ef195",
  "balance": 1000.00,
  "accountType": "C",
  "createdAt": "2025-12-22T22:53:57.7575655",
  "updatedAt": "2025-12-22T22:53:57.7575655",
  "active": true
}
```

### Update Account
**Request:**
```json
PUT /accounts/1234567890
{
  "accountType": "M",
  "active": true
}
```
**Response:**
```json
{
  "accountNumber": "1234567890",
  "customerId": "4f080edd-8ce2-4813-b453-9b2f268ef195",
  "balance": 1000.00,
  "accountType": "M",
  "createdAt": "2025-12-22T22:53:57.7575655",
  "updatedAt": "2025-12-22T23:00:00.0000000",
  "active": true
}
```

### Get Account Balance
**Request:**
```
GET /accounts/1234567890/balance
```
**Response:**
```json
1000.00
```

## Test Credentials

Use these JWT users for testing:
- **user1** (role: USER)
- **admin1** (role: ADMIN)

You must include a valid JWT token in the `Authorization` header for all requests. Example:
```
Authorization: Bearer <jwt_token>
```
