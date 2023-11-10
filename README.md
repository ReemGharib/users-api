users-api README

Overview

The Users API is a Java-based backend API that provides endpoints for managing user data. This documentation serves as a guide to help you understand how to interact with the API effectively. The API documentation is also available in Swagger for interactive exploration and testing.

**Table of Contents**

[1. Getting Started]()

[2. API Endpoints]()

[3. Error Handling]()

[4. Interactive Documentation]()



**Getting Started**

Before you begin using the users-api, make sure you have the following prerequisites in place:

    -Java Development Kit (JDK) 17.
    -Apache Maven for building the project.
    -Install Postgresql and Liquibase
    -A database setup and configured for user data storage.


    
The API should now be running on a local server (default: http://localhost:9999).

BASE URL: http://localhost:9999/users/api
    
Usage:
You can make HTTP requests to its endpoints to perform various operations like creating, updating, and retrieving user data.

**API Endpoints**

    GET /users
    GET /{uid}
    POST /users
    PUT /{uid}
    GET /emails/{uid}

**Error Handling**

The API uses standard HTTP status codes to indicate the success or failure of a request. Detailed error messages are provided in the response body for better understanding of the issue.

**Interactive Documentation**

To interactively explore and test the API, you can access the Swagger documentation at http://localhost:9999/users-api/swagger-ui.html. It provides a user-friendly interface for testing the API endpoints and understanding request and response formats
