@api
Feature: Beeceptor Echo API Tests

  Scenario: GET request validates path ip and headers
    Given the base URI is "https://echo.free.beeceptor.com"
    When I send a GET request to "/sample-request?author=beeceptor"
    Then the response status code should be 200
    And the response field "path" should be "/sample-request?author=beeceptor"
    And the response field "ip" should not be empty
    And the response should include headers "Host", "User-Agent", and "Accept"

  Scenario: POST request validates order payload is echoed correctly
    Given the base URI is "http://echo.free.beeceptor.com"
    When I send a POST request to "/sample-request?author=beeceptor" with body:
      """
      {
        "order_id": "12345",
        "customer": {
          "name": "Jane Smith",
          "email": "janesmith@example.com",
          "phone": "1-987-654-3210",
          "address": {
            "street": "456 Oak Street",
            "city": "Metropolis",
            "state": "NY",
            "zipcode": "10001",
            "country": "USA"
          }
        },
        "items": [
          { "product_id": "A101", "name": "Wireless Headphones", "quantity": 1, "price": 79.99 },
          { "product_id": "B202", "name": "Smartphone Case", "quantity": 2, "price": 15.99 }
        ],
        "payment": {
          "method": "credit_card",
          "transaction_id": "txn_67890",
          "amount": 111.97,
          "currency": "USD"
        },
        "shipping": {
          "method": "standard",
          "cost": 5.99,
          "estimated_delivery": "2024-11-15"
        },
        "order_status": "processing",
        "created_at": "2024-11-07T12:00:00Z"
      }
      """
    Then the response status code should be 200
    And the response field "parsedBody.order_id" should be "12345"
    And the response field "parsedBody.customer.name" should be "Jane Smith"
    And the response field "parsedBody.customer.email" should be "janesmith@example.com"
    And the response field "parsedBody.customer.phone" should be "1-987-654-3210"
    And the response field "parsedBody.customer.address.city" should be "Metropolis"
    And the response field "parsedBody.items[0].product_id" should be "A101"
    And the response field "parsedBody.items[0].name" should be "Wireless Headphones"
    And the response field "parsedBody.items[1].product_id" should be "B202"
    And the response field "parsedBody.payment.method" should be "credit_card"
    And the response field "parsedBody.payment.transaction_id" should be "txn_67890"
    And the response field "parsedBody.payment.currency" should be "USD"
    And the response field "parsedBody.order_status" should be "processing"
