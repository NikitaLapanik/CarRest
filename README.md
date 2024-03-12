# Car Rest service
This is a demonstrative Java Spring Boot project which is built in a microservice style. The main idea is to provide CRUD operations access to the car database through the REST interface.
## Project Description
The Car Rest Microservice is designed to provide a flexible API for any size Car database. It allows users to easily make any CRUD operation. Also, Microservice architecture allows easily separate main duties and guarantees the reliability of the whole system.
## Technology Stack
- Backend:
    - Java - A general-purpose programming language that is class-based, object-oriented, and designed to have as few implementation dependencies as possible.
    - Spring Framework - An application framework and inversion of control container for the Java platform.
        - Spring Boot - An extension of the Spring framework that simplifies the process of building production-ready applications.
        - Spring Data JPA - Provides a simple and consistent programming model for data access.
    - PostgreSQL - An open-source relational database system.
    - SpringDoc - Library helps to automate the generation of API documentation using spring boot projects
- Testing:
    - Mockito - Mockito is a mocking framework that tastes really good.
    - JUnit 5 - A programming and testing model for Java applications.
    - Testcontainers - Provides throwaway instances of common databases, Selenium web browsers, or anything else that can run in a Docker container.
    - Flyway - Database migration tool.
- Development Tools:
    - Postman - Postman is an API platform for building and using APIs.
- Containerization and Deployment:
    - Docker - A platform for developing, shipping, and running applications.

## How to Install and Run the Project
### Prerequisites:
- Installed Docker
### Installing
- Clone the repo
```
git.clong bla blab lablablabla
```
### Running
- Run out the box by Docker Compose
```
docker-compose up
```
### The application will be accessible at
http://localhost:8080/ or <br>
http://localhost:8080/swagger-ui/index.html
# Credits
Lapanyk Nikita - developer