@ui
Feature: LabCorp Career Job Search

  Scenario Outline: Search QA job and verify details
    Given I am on the LabCorp careers site
    When I search and open the first job for "<searchKeyword>"
    Then I should see job "<jobTitle>" in "<locationKeyword>" with id "<jobId>"
    And the job page should mention "<pageText1>" and "<pageText2>" and "<pageText3>"
    When I apply and check Workday for "<applyPageText>" in "<workdayLocation>"
    Then I should return to the job search page

    Examples:
      | searchKeyword      | jobTitle                      | locationKeyword | jobId   | pageText1                                                                         | pageText2                                                          | pageText3 | applyPageText  | workdayLocation |
      | QA Test Automation | Lead Test Automation Engineer | locations       | 2617100 | Labcorp is a global leader in diagnostic testing and drug development solutions | Develop robust automated tests for backend, frontend, UI or mobile | Selenium  | Apply Manually | Durham          |
