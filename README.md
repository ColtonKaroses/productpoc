### Sample Project

This is a sample web application written to test the capabilities of spring boot and docker.

The application is built with a basic three tiered architecture.  The database interaction is handled by hibernate using built in spring jpa capabilities.  Spring Data is autogenerating the sql statements based on the ProductRepository class.  The service layer handles the logic not involved in handling the web requests.  The controller handles the api level interactions, such as managing the correct status codes.

The database is either an in-memory h2 database in testing scenarios or postgres in deployment scenarios.  The database is create using liquibase, which provides structured changesets for database deployment.

There are four sets of tests.  Most of them are integration level tests and use the in-memory database.  The system level test is meant to be used to verify the full stack is working.  The system level test is intended to be written after running docker compose to deploy the application.