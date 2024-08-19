# Enviro365 File Upload API

Enviro365 File Upload API is a RESTful API built with Spring Boot that allows clients to upload text files containing environmental data for analysis.
The API processes the data, stores it in an H2 in-memory database, and provides endpoints to retrieve processed results.

## Technologies Used
- **Java 17**: The core programming language used.
- **Spring Boot 3.3.2**: Framework for building RESTful services.
- **H2 Database**: In-memory database for testing.
- **Maven**: Dependency management and build tool.
- **Swagger** (via `springdoc-openapi`): API documentation tool.

## Features
- **File Upload**: Clients can upload text files containing environmental data.
- **Data Processing**: The API processes uploaded data, extracting environmental metrics.
- **Data Storage**: Processed data is stored in an H2 in-memory database.
- **Result Retrieval**: Clients can retrieve processed results, including summaries and metadata.
- **Error Logging**: Invalid lines and errors are logged for monitoring and troubleshooting.
- **API Documentation**: Swagger UI is available for interactive API documentation.

## Getting Started

### Prerequisites
- **Java 17** installed
- **Maven** installed

### Running the Application
1. **Clone the Repository**:
    ```bash
    git clone https://github.com/RNBaloyi/enviro365-file-upload-api.git
    ```
2. **Navigate to the Project Directory**:
    ```bash
    cd enviro365-file-upload-api
    ```
3. **Run the Application**:
    ```bash
    mvn spring-boot:run
    ```

### Accessing the API
Once the application is running, you can access the API at `http://localhost:8080`.

### Swagger Documentation
- Swagger UI: Access interactive API documentation at `http://localhost:8080/swagger-ui.html`.

### Manual Documentation
- Detailed API documentation is provided in the `docs` directory as a PDF file named `API Documentation.pdf`.

## API Endpoints
Here are some of the main API endpoints:

- POST /api/v1/files/upload: Upload a text file containing environmental data.
- GET /api/v1/processed-results/file/{fileUploadId}: Retrieve processed results for a specific file upload ID.
- GET /api/v1/files/{id}: Retrieve details of a file upload by its ID.
- PATCH /api/v1/files/{id}/status: Update the status of a file upload.
- GET /api/v1/files: Retrieve a list of all file uploads.


For a complete list of endpoints and detailed information, refer to the Swagger UI or the manual documentation.

## How It Works
1. Upload File: Clients upload a file with environmental data.
2. Process Data: The file is processed, and key metrics are extracted.
3. Store Data: The processed data is stored in the H2 in-memory database.
4. Retrieve Results: Clients can retrieve processed results via the API.

## Logging
The application includes logging to capture invalid lines and errors during file processing. This helps in monitoring and troubleshooting issues.

## Known Issues
- Currently, the API does not have automated tests.


