
# Library Management

Web app for maintenance of library.

## Getting Started

These instructions will guide you on how to set up and run the project locally.

### Prerequisites

To run the project locally, you will need the following software installed on your machine:

-   Java 11 or higher
-   PostgreSQL 9.6 or higher

### Installing

1.  Clone the repository to your local machine.
    
2.  Create a new PostgreSQL database with the name `your-database-name`. You can use the following command to create a database:
    
    `createdb your-database-name` 
    
3.  Set the following environment variables in your machine:
    `spring.datasource.url=jdbc:postgresql://localhost:5432/your-database-name`

    `spring.datasource.username=your-database-username`

    `spring.datasource.password=your-database-password`

    `spring.datasource.hikari.connectionTimeout=20000`

    `spring.datasource.hikari.maximumPoolSize=5`

    `spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQL95Dialect`

    `spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true`

    `spring.jpa.hibernate.ddl-auto=update`

    `spring.jpa.show-sql=true`

    `spring.flyway.enabled=true`

    `spring.flyway.baseline-on-migrate=true`
    
5.  Navigate to the project directory and run the following command to start the application:
    
    `./mvnw spring-boot:run` 
    
6.  The application should now be running on `http://localhost:8080`.
    

## Running the tests

To run the automated tests for this system, navigate to the project directory and run the following command:

    `./mvnw test` 

## Deployment

To deploy the application to a live system, you will need to do the following:

1.  Build the application using the following command:
    
    `./mvnw clean package` 
    
2.  Deploy the generated `jar` file to your preferred deployment environment.
    

## Built With

-   [Spring Boot](https://spring.io/projects/spring-boot) - The web framework used
-   [PostgreSQL](https://www.postgresql.org/) - The database management system used
-   [Maven](https://maven.apache.org/) - Dependency Management

## Authors

Ninh Văn Hải