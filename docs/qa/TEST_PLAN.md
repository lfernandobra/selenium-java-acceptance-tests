# Acceptance Test Plan — Leilão App (Selenium WebDriver + Java)

**Author:** Luis Fernando Brandão  
**Target System:** Spring Boot 2.3.1 MVC Application (`LeilaoApplication`) with Thymeleaf, Spring Security, and H2 In-Memory Database  
**Automation Stack:** Java, Selenium WebDriver, JUnit 5, Page Object Model, Maven Surefire, GitHub Actions  

---

## 1. Objective & Scope

Verify user authentication, Spring Security route protection, and auction form registration/validation flows end-to-end on the **Leilão** web application through a maintainable Page Object architecture.

### In Scope
* **Authentication & Access Control (`LoginTest.java`):** Valid form login (`fulano` / `pass` seeded in `data.sql`), invalid credentials handling (`/login?error`), and unauthenticated redirect protection on `/leiloes/2`.
* **Auction Creation & Bean Validation (`LeiloesTest.java`):** End-to-end creation of a new auction with dynamic date formatting (`dd/MM/yyyy`) and verification of Thymeleaf/Hibernate Validator error messages on empty submissions.
* **Page Object Base Lifecycle (`PageObject.java`):** Centralized WebDriver initialization, headless CI options (`-Dheadless=true` or `CI=true`), implicit/page-load timeouts (`5s` / `10s`), and teardown (`browser.quit()`).

### Out of Scope
* Bid placement flow (`POST /lances`) — `LancesPage` scaffolded; test suite not yet automated (tracked in `BUG-001`).
* External database engines (PostgreSQL/MySQL); application uses H2 `create-drop` with seeded `data.sql`.

---

## 2. Architecture & Engineering Decisions

1. **Base `PageObject` Inheritance:**  
   All page classes (`LoginPage`, `LeiloesPage`, `CadastroLeilaoPage`, `LancesPage`) extend [`PageObject`](../../src/test/java/br/com/alura/leilao/PageObject.java). Passing an existing `WebDriver` instance across page transitions (`LoginPage.efetuaLogin() -> new LeiloesPage(browser)`) preserves the authenticated Spring Security `JSESSIONID` cookie without re-authenticating inside tests.
2. **Environment-Agnostic Chrome Execution:**  
   The hardcoded OS-specific `drivers/chromedriver` binary was removed from version control. `PageObject` automatically enables `--headless=new --no-sandbox --disable-dev-shm-usage` when running in CI (`CI=true`) or via `-Dheadless=true`, using a matched `chromedriver` injected via `browser-actions/setup-chrome`.
3. **Deterministic Test Data (`data.sql` + `LocalDate.now()`):**  
   User accounts (`fulano`, `ciclano`, `beltrano`) and baseline auctions/bids are seeded cleanly on Spring Boot startup via `src/main/resources/data.sql`. Dynamic auction names concatenate `LocalDate.now()` to keep assertions readable in `#tabela-leiloes`.

---

## 3. Traceability Matrix

| Domain | BDD Feature | Test Class | Page Objects |
| :--- | :--- | :--- | :--- |
| Authentication & Security | [`specs/features/login.feature`](../../specs/features/login.feature) | `LoginTest.java` | `LoginPage`, `LeiloesPage` |
| Auctions & Validation | [`specs/features/leiloes.feature`](../../specs/features/leiloes.feature) | `LeiloesTest.java` | `LoginPage`, `LeiloesPage`, `CadastroLeilaoPage` |
| Bids (Scaffolded) | N/A | *(Pending — see `BUG-001`)* | `LancesPage` |

---

## 4. Entry & Exit Criteria

* **Entry:** Spring Boot application running on `http://localhost:8080` (`GET /login` returning `200 OK`) and matching Chrome/ChromeDriver available on `PATH` or `-Dwebdriver.chrome.driver`.
* **Exit:** All `5` JUnit acceptance tests in `LoginTest` and `LeiloesTest` pass via `mvn test` (`0` failures, `0` skipped) with `target/surefire-reports/` archived in CI.
