# Labcorp Coding Test

Selenium + Cucumber UI test and REST Assured API test.

## Run

```bash
mvn test
mvn test -Dcucumber.filter.tags="@ui"
mvn test -Dcucumber.filter.tags="@api"
```

## Files

| File | What it does |
|------|----------------|
| `LabCorpCareers.feature` | UI test steps + test data (Examples table) |
| `ApiTests.feature` | API test steps + POST JSON body |
| `LabCorpSteps.java` | All UI Selenium code |
| `ApiSteps.java` | All API code |
| `DriverManager.java` | Opens and closes Chrome |
| `TestRunner.java` | Starts the tests |

Test data is in the `Examples` table at the bottom of `LabCorpCareers.feature`.
