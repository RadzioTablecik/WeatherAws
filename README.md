# WeatherAWS - Spring Cloud Function & AWS Lambda

An application utilizing **Spring Cloud Function** to create an **AWS Lambda** function that connects to the **Open-Meteo API** to retrieve the current temperature for Wrocław.

Using Spring to build this solution allowed for the leverage of standard Java application development tools, significantly improving code readability and scalability.

## Project Structure
The project is divided into independent modules that work together to provide the full functionality:

*   **WeatherClient**: Responsible solely for executing API calls.
*   **TemperatureCategory**: Handles the categorization of the received weather data.
*   **CoordinatesWGS84**: A **Value Object** that ensures data integrity and prevents wasting computational power on invalid queries.
*   **WeatherService**: Manages the logic of preparing the final response for the user.
*   **Lambda Function**: Acts as a thin entry point, delegating tasks to the service and returning the response to the client.

I utilized **Java Records** for DTOs (Data Transfer Objects) to ensure clear and concise communication between layers.

---

## Strategy Pattern & Scalability (Task-4 Branch)
During development, I identified that `WeatherService` was too tightly coupled with a single provider, which limited scalability.

To resolve this, on the **Task-4** branch, I implemented the **Strategy Pattern**. I also introduced an intermediate weather response model, ensuring that results are consistent and type-safe, regardless of the external API provider used.

> [!IMPORTANT]  
> The **Task-4** branch contains a more refined and cleaner implementation of the project, showcasing better architectural practices compared to the initial versions.
---

## Testing Approach
The project includes a comprehensive testing suite to ensure reliability:

*   **Functional Testing**: The core logic can be tested without an active internet connection using **Mockito** to mock API responses.
*   **Integration Tests**: I included an integration test to verify the actual connection with the API. *Note: This test is disabled by default as it was used primarily for development and debugging.*
*   **Unit Tests**: Standard unit tests cover most of the major functionalities of the application.

---

### Screenshots
All screenshots located in the `doc/` directory were obtained using the **AWS Lambda "Test"** feature, showing the successful execution and response format of the function.