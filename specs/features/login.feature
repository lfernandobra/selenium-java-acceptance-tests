@auth @selenium @ui
Feature: Leilão Authentication & Route Access Control
  As a registered Leilão user
  I want to authenticate through the login form
  So that I can access restricted auction pages and manage bids

  @positive @login
  Scenario: Authenticate with seeded credentials
    Given I am on the login page ("/login")
    When I submit username "fulano" and password "pass"
    Then I should be redirected away from "/login"
    And the header element "#usuario-logado" should display "fulano"

  @negative @login
  Scenario: Reject authentication with invalid credentials
    Given I am on the login page ("/login")
    When I submit username "invalido" and password "123123"
    Then the browser URL should be "/login?error"
    And the header element "#usuario-logado" should not be present
    And the page should display "Usuário e senha inválidos."

  @security @access-control
  Scenario: Block unauthenticated access to restricted auction details
    Given I am not authenticated
    When I navigate directly to "/leiloes/2"
    Then Spring Security should redirect me to "/login"
    And the page should not contain "Dados do Leilão"
