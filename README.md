# Currency Converter application

This Spring Boot application provides an API to calculate the payable amount of a bill after applying applicable discounts and converting the total amount from one currency to another using real-time exchange rate

**Features:**

1. Integrates with an external currency exchange API to get real-time exchange rates.
2. Applies various types of discounts based on user information and bill details.
3. Provides an API endpoint to calculate the payable amount after discounts and currency conversion

**Requirements:**
* JDK 17 
* Maven
* Spring Boot 3.2+

**Endpoints**

This API accepts a bill in one currency and returns the payable amount after applying the applicable discounts and converting the amount to the specified target currency.
The endpoint is exposed at port 8080 and can be accessed by API 
['http://localhost:8080/api/calculate']()

Request json :

`{
    "originalCurrency": "USD",
    "targetCurrency": "AED",
    "userType": {
        "employee": true,
        "affiliate": false,
        "customer": false,
        "tenure": 3
    },
    "items": [
        {
            "name": "Laptop",
            "price": 1500,
            "category": "NON_GROCERY"
        },
        {
            "name": "Groceries Item 1",
            "price": 50,
            "category": "GROCERY"
        }
    ]
}`

* `userType`: The type of user (e.g., "employee", "affiliate", or "customer").
* `tenure`: The number of years the customer has been associated with the store.
* `originalCurrency`: The original currency of the bill (e.g., "USD").
* `targetCurrency`: The currency to which the amount will be converted (e.g., "AED").
* `items`: A list of items in the bill. Each item includes:
* `name`: Name of the item.
* `category`: The category of the item (e.g., "non_grocery", "grocery").
* `price`: The price of the item in the original currency.

**Example response:**

`{
    "payableAmount": 3984.66250,
    "exchangeRate": 3.6725,
    "originalCurrency": "USD",
    "targetCurrency": "AED",
    "items": [
        {
            "name": "Laptop",
            "category": "NON_GROCERY",
            "price": 1500
        },
        {
            "name": "Groceries Item 1",
            "category": "GROCERY",
            "price": 50
        }
    ],
    "userType": {
        "tenure": 3,
        "employee": true,
        "affiliate": false,
        "customer": false
    }
}`

Here `payableAmount`: The total amount to be paid after applying the discounts

**Set up the Application** 

Clone the application from **github**
https://github.com/aleena-ch/SpringBoot-Currency-Converter

**How to start the application :**

using maven: ./mvnw spring-boot:run

**How to Test the endpoints:**
   You can use Postman or curl to interact with the API. Example:

Using **Postman**:
* POST http:localhost:8080/api/calculate:

* Set the Content-Type to application/json.
* Add the JSON request body as shown in the Request Body section.

Postman collection requests can be found in currencycalculator.postman_collection.json

**Sonarqube** 

To get a report of the code quality using sonarqube
https://sonarcloud.io/summary/new_code?id=aleena-ch_SpringBoot-Currency-Converter&branch=master

**UML Diagram**

UML diagram represents the classes and their relationships and can be found here

https://github.com/aleena-ch/SpringBoot-Currency-Converter/blob/master/UML.png