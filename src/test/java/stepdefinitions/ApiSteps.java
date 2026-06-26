package stepdefinitions;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

public class ApiSteps {

    private Response response;

    @Given("the base URI is {string}")
    public void setBaseUri(String uri) {
        RestAssured.baseURI = uri;
    }

    @When("I send a GET request to {string}")
    public void sendGet(String endpoint) {
        response = RestAssured.given().header("Accept", "application/json").get(endpoint);
    }

    @When("I send a POST request to {string} with body:")
    public void sendPost(String endpoint, String body) {
        response = RestAssured.given()
            .header("Content-Type", "application/json")
            .body(body)
            .post(endpoint);
    }

    @Then("the response status code should be {int}")
    public void verifyStatusCode(int expected) {
        Assertions.assertEquals(expected, response.getStatusCode());
    }

    @Then("the response field {string} should be {string}")
    public void verifyField(String field, String expected) {
        Assertions.assertEquals(expected, response.jsonPath().getString(field));
    }

    @Then("the response field {string} should not be empty")
    public void verifyFieldNotEmpty(String field) {
        String value = response.jsonPath().getString(field);
        Assertions.assertFalse(value == null || value.isEmpty());
    }

    @Then("the response should include headers {string}, {string}, and {string}")
    public void verifyHeaders(String h1, String h2, String h3) {
        var headers = response.jsonPath().getMap("headers");
        for (String name : new String[]{h1, h2, h3}) {
            boolean found = headers.keySet().stream()
                .anyMatch(key -> key.toString().equalsIgnoreCase(name));
            Assertions.assertTrue(found, "Header not found: " + name);
        }
    }
}
