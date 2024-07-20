# Plivo SMS Automation

## Setup

1. Clone the repository.
2. Navigate to the project directory.
   3. Create a `.env` file in the root of your project with the following content:
       ```
       PLIVO_AUTH_ID=your_auth_id_here
       PLIVO_AUTH_TOKEN=your_auth_token_here
       OPENWEATHER_API_KEY=your_openweather_api_key_here
       ```

## Build and Run

1. Build the project using Maven.
    ```sh
    mvn clean install
    ```
2. Run the tests using TestNG.
    ```sh
    mvn test
    ```

## Usage

1. Create `customer_message.csv` by running the `CreateCustomerCSV` class.
2. Get customer IDs by running the `GetCustomerIDs` class.
3. Read customer data by running the `ReadCustomerData` class.
4. Send SMS by running the `SendSMS` class.

## Running Tests

To run the tests, execute the `PlivoSMSTest` class using TestNG in your IDE or via command line.
To run the tests, execute the `WeatherAutomationTests` class using TestNG in your IDE or via command line.

# PlivoAssignment
