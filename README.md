# Wallet App

Features:
- Perform transactions to credit bitcoins 
- Find the wallet balance at each hour between a specific time interval

## Assumptions made

- 1000 Bitcoin is credited at application start time as stated in the requirement doc. Check `db_migration/2-insert_1000_bitcoins.sql`
- The credit transactions can come from different timezones, and the transaction datetime needs to be parsed considering the zone info.
- The response to find wallet balance would present all datetimes in UTC. 
- While fetching wallet balance, the time interval should be an hour apart to give a valid response.

 Example Request:
      
      http://localhost:8090/wallet/balance 
       
      {
      "startDatetime": "2015-08-03T20:30:00+00:00",
      "endDatetime": "2015-08-03T20:45:00+00:00"
      }
      
 Response:
 
      {
          "message": "No valid hour between the start and end time range"
      }

## Design 
The solution contains the standard controller, service and data access layers. 
Dependency injection is followed at each point to promote testability.

### Perform transactions to credit bitcoins 

The transactions are recorded in Postgres. 
The current wallet balance is also calculated and stored at the time of the transaction.

### Find the wallet balance at each hour between a specific time interval

The wallet balance is fetched from Postgres at each time recorded before the end time interval.
The recorded wallet balance is then mapped at each hour between the start and end time interval.

## Technologies used 

Java - programming language.
Spring boot - a micro framework to develop REST API's.
Postgres - used as a persistent storage for recording transactions and inferring wallet details.
Gradle - for dependency resolution, building and testing the application
Lombok - for reducing Java source code verbosity.

## DB migration  

The schema is present in `db_migration/2-insert_1000_bitcoins.sql`. 
It is executed as part of docker entrypoint when the postgres container is started. 

## API docs

Swagger is used for API documentation.
Check [Swagger UI](http://localhost:8090/swagger-ui.html) 

## Configurations

Application config to load Postgres connection details, etc are loaded from `src/main/resources/application.properties`.

### Multithreading in Spring boot 

By default, spring boot allocates 200 threads (i.e. `server.tomcat.max-threads=200`) to deal with the service requests, for simultaneous executions.
A few other Spring boot concurrency configs are mentioned [here](https://learningbook.in/thread-pool-in-spring-boot-application/). 
These configs can be tuned based on the requirements. 

## Logging 

The application logs are written both to disk and console.
The log configurations can be found at `src/main/resources/logback.xml`.
Slf4j and Logback are used to implement logging.

## Testing 

Unit Tests is written for the main business logic, i.e. `WalletControllerTest.java`.

Integration test is added at the controller layer, i.e. `WalletBalanceCalculatorTest.java`.

The tests can be executed by running 
    
    ./gradlew clean test


## Packaging 

The application is packaged in a docker image.

## Building and Running the solution 

The solution works on Linux and Mac. There might be small tweaks required for running it on Windows.

You must have `docker` and `docker-compose` installed on your machine.
    
Run the following to start the Postgres and the Wallet app docker containers    
    
    ./run.sh 
    

You can stop the containers using:
    
     docker-compose down


## Scaling the application 

Wallet App
- Scale horizontally by adding more instances or Auto scaling strategies.

Wallet DB 
- Scale vertically when using a RDBMS like Postgres that is used here. 
- Scale horizontally using a NoSQL Database.

## Future improvements 
- Improve unit and integration test coverage, add load tests.
- Use HTTPs for secure access of data.
- Move input validation to service layer 
- Clean up - introduce a generic error handling in WalletController.
- API versioning
- DB schema versioning - use a tool like Flyway.
- Implement Auth 
- Rate limiting / Circuit breaker for app resilience
- Improve inline java docs
- Add metrics for monitoring application

