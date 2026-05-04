# WeatherAWS - Spring Cloud Function & AWS Lambda (Refined Version)

This project is a **Spring Cloud Function** application deployed as an **AWS Lambda**. It retrieves current weather data for a specified city using multiple external providers.

The version on this branch (**Task-4**) represents a cleaner, more decoupled architectural approach using the **Strategy Pattern**.

## Possible improvements
*   Better error handling and custom error classes
*   Better AWS Lambda configuration (memory & cold starts)
*   Better test coverage for edge cases
*   Caching responses for specific cities (approx. 1h)
*   Fault tolerance and external service fallback
*   Improved application logging and observability
*   Comprehensive Javadoc documentation

## Adding New Weather Providers
*   **Strategy Pattern**: Implemented via the `WeatherProvider` interface. This allows the application to switch between different weather APIs seamlessly.
*   **Multi-Provider Support**: Added integration for the **PirateWeather API** alongside the existing **Open-Meteo** integration.
*   **Unified Data Model**: Introduced an intermediate response model to ensure that weather data is consistent regardless of which provider is used.

## Usage
The function is exposed via a public Lambda URL and can be tested using **GET** requests.

**Endpoint URL:**  
`https://iai5cwb7kezsr65fplhmouibxy0rlkgh.lambda-url.eu-north-1.on.aws/?city={City}&provider={openmeteo|pirateweather}`

### Query Parameters
*   `city` (Required): The name of the city (e.g., `Wroclaw`).
*   `provider` (Optional): Choose between `openmeteo` or `pirateweather`.
    *   *Note: If the provider is not specified, the system defaults to **Open-Meteo**.*

---

## Live API Examples (Testing)
The screenshots provided in the `doc/` directory were obtained using the following queries:
1.  **Default Provider (Open-Meteo):**  
    [https://iai5cwb7kezsr65fplhmouibxy0rlkgh.lambda-url.eu-north-1.on.aws/?city=Wroclaw](https://iai5cwb7kezsr65fplhmouibxy0rlkgh.lambda-url.eu-north-1.on.aws/?city=Wroclaw)
2.  **Specific Provider (PirateWeather):**  
    [https://iai5cwb7kezsr65fplhmouibxy0rlkgh.lambda-url.eu-north-1.on.aws/?city=Wroclaw&provider=pirateweather](https://iai5cwb7kezsr65fplhmouibxy0rlkgh.lambda-url.eu-north-1.on.aws/?city=Wroclaw&provider=pirateweather)
3.  **Specific Provider (Open-Meteo):**  
    [https://iai5cwb7kezsr65fplhmouibxy0rlkgh.lambda-url.eu-north-1.on.aws/?city=Wroclaw&provider=openmeteo](https://iai5cwb7kezsr65fplhmouibxy0rlkgh.lambda-url.eu-north-1.on.aws/?city=Wroclaw&provider=openmeteo)

---


