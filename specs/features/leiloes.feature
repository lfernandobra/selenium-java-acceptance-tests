@leiloes @selenium @ui
Feature: Auction Registration & Validation
  As an authenticated Leilão user
  I want to register new auctions and receive validation feedback on invalid inputs
  So that only valid auctions are listed on the platform

  Background:
    Given I am authenticated as "fulano" ("pass")
    And I navigate to the auction creation form ("/leiloes/new")

  @positive @create
  Scenario: Register a new auction with valid attributes
    When I submit an auction with today's date ("dd/MM/yyyy"), name "Leilão do dia <today>", and initial value "500.00"
    Then the last row in "#tabela-leiloes tbody" should match the submitted name, date, and value

  @negative @validation
  Scenario: Display validation errors when submitting empty fields
    When I submit the auction form with empty name, initial value, and opening date
    Then the browser URL should remain at "/leiloes" (not "/leiloes/new")
    And the form should display all 4 Bean Validation errors:
      | expected_message                                   |
      | minimo 3 caracteres                                |
      | não deve estar em branco                           |
      | deve ser um valor maior de 0.1                     |
      | deve ser uma data no formato dd/MM/yyyy            |
