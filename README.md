<<<<<<< Updated upstream
## Docker related commands

Ensure target exists
mvn clean install

docker build -t autoboard .

docker run --rm -p 5000:8080 autoboard

You can access the API from port 5000 on your machine:
http://localhost:5000/api/misc/health

It is essentially the same as running the API locally, however we now have a way to package it into a container.
=======
# Auto Board

Auto Board is a project that provides a CLI application for managing tasks and interacting with Google OAuth2 for authentication.

## Getting Started

### Prerequisites

- Java 21
- Maven

### Running the CLI Application

1. Open a terminal or command prompt.
2. Navigate to the `cli` directory:

    ```sh
    cd cli
    ```

3. Run the CLI application:

    ```sh
    .\run.bat
    ```

### Features

- Google OAuth2 authentication
- Task management

### Project Structure

- `api/`: Contains the backend API for the project.
- `cli/`: Contains the CLI application for the project.

### License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE](LICENSE) file for details.

### Acknowledgments

- Spring Boot
- Spring Shell
- Google OAuth2
>>>>>>> Stashed changes
