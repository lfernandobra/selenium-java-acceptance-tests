# Leilão — Selenium WebDriver + Java + JUnit Acceptance Testing

[![Selenium Java Acceptance CI](https://github.com/lfernandobra/selenium-java-acceptance-tests/actions/workflows/ci.yml/badge.svg)](https://github.com/lfernandobra/selenium-java-acceptance-tests/actions/workflows/ci.yml)
[![Java](https://img.shields.io/badge/Java-11%2B-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Selenium](https://img.shields.io/badge/Selenium-WebDriver-43B02A?style=flat-square&logo=selenium&logoColor=white)](https://www.selenium.dev/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.3.1-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![JUnit 5](https://img.shields.io/badge/JUnit-5-25A162?style=flat-square&logo=junit5&logoColor=white)](https://junit.org/junit5/)

Automated acceptance test suite for the **Leilão** Spring Boot MVC application (Thymeleaf + Spring Security + H2 in-memory database), implemented in **Java** using **Selenium WebDriver**, **JUnit 5**, and the **Page Object Model (POM)** pattern, with automated headless execution in **GitHub Actions**.

---

## 1. QA Strategy & Documentation

| Artifact | Purpose | Location |
| :--- | :--- | :--- |
| **Test Plan** | Acceptance strategy, POM lifecycle, H2 seeding, and CI setup | [`docs/qa/TEST_PLAN.md`](docs/qa/TEST_PLAN.md) |
| **Test Case Inventory** | 5 automated JUnit + Selenium cases across login & auction flows | [`docs/qa/TEST_CASES.md`](docs/qa/TEST_CASES.md) |
| **BDD Specifications** | Gherkin scenarios for authentication, security, and auction forms | [`specs/features/`](specs/features/) |
| **Bug & Audit Report** | `LancesPage` route bug, driver decoupling, and coverage gap | [`BUG-001`](docs/qa/bug-reports/BUG-001-driver-management-and-lances-url.md) |

---

## 2. Repository Structure

```text
.
├── .github/workflows/
│   └── ci.yml                          # CI: Java 11 + ChromeDriver + Spring Boot + mvn test
├── docs/qa/
│   ├── bug-reports/
│   │   └── BUG-001-driver-management-and-lances-url.md
│   ├── TEST_CASES.md
│   └── TEST_PLAN.md
├── specs/features/
│   ├── leiloes.feature                 # BDD scenarios for auction creation & validation
│   └── login.feature                   # BDD scenarios for login & route protection
├── src/
│   ├── main/                           # Spring Boot Leilão app (MVC, Spring Security, H2 + data.sql)
│   └── test/java/br/com/alura/leilao/
│       ├── lances/
│       │   └── LancesPage.java         # Bid details Page Object
│       ├── leiloes/
│       │   ├── CadastroLeilaoPage.java # Auction form Page Object & validation checks
│       │   ├── LeiloesPage.java        # Auction table Page Object
│       │   └── LeiloesTest.java        # Auction registration & Bean Validation tests
│       ├── login/
│       │   ├── LoginPage.java          # Authentication Page Object
│       │   └── LoginTest.java          # Login, invalid credentials & access control tests
│       └── PageObject.java             # Base WebDriver lifecycle, timeouts & headless ChromeOptions
└── pom.xml
```

---

## 3. Technical Highlights

* **Shared `PageObject` Lifecycle:** [`PageObject.java`](src/test/java/br/com/alura/leilao/PageObject.java) centralizes browser instantiation, `5s` implicit wait / `10s` page-load timeouts, headless Chrome flags (`-Dheadless=true` or `CI=true`), and session hand-off between pages while preserving Spring Security session cookies.
* **Spring Security & Form Validation Coverage:** Verifies restricted route redirects (`/leiloes/2` -> `/login`), invalid login feedback (`/login?error`), and server-side Bean Validation messages (`minimo 3 caracteres`, `não deve estar em branco`, `deve ser um valor maior de 0.1`, `deve ser uma data no formato dd/MM/yyyy`).
* **Self-Contained In-Memory Database:** Uses H2 (`create-drop`) seeded on startup via [`data.sql`](src/main/resources/data.sql) (`fulano` / `pass`), requiring no external database setup.

---

## 4. Local Setup & Execution

### Prerequisites
* **Java 11+** (Selenium `4.27.0` with Spring Boot `2.3.1.RELEASE`)
* **Apache Maven 3.6+**
* **Google Chrome** + matching `chromedriver` on `PATH` (or passed via `-Dwebdriver.chrome.binary` / `-Dwebdriver.chrome.driver`)

### 1. Start the Spring Boot Application (port `8080`)

```bash
mvn spring-boot:run
```

### 2. Execute the Selenium Acceptance Suite

In a second terminal:

```bash
# Run with visible browser
mvn test

# Run in headless mode
mvn test -Dheadless=true
```

---

## 5. CI/CD Pipeline (GitHub Actions)

Workflow [`ci.yml`](.github/workflows/ci.yml) executes on every `push` and `pull_request` to `main`:
1. Configures Java 11 (Temurin) with Maven caching and installs Chrome `131` + matched ChromeDriver via `browser-actions/setup-chrome@v2`.
2. Compiles main and test sources (`mvn -B test-compile`).
3. Boots the Spring Boot application on port `8080` with `spring-devtools` live-restart disabled (`SPRING_DEVTOOLS_RESTART_ENABLED=false`) and polls `GET /login` readiness.
4. Runs `mvn -B test -Dheadless=true` passing `-Dwebdriver.chrome.binary` and `-Dwebdriver.chrome.driver`, archiving `target/surefire-reports/` for 14 days.

---

## 6. Author

**Luis Fernando Brandão**  
QA Automation Engineer  
* [LinkedIn](https://linkedin.com/in/lfernandobra)  
* [GitHub](https://github.com/lfernandobra)  
* lfernandobra@gmail.com
