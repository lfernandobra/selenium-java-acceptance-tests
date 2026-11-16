# BUG-001: Broken Route Constant in `LancesPage.java` & Binary Driver Coupling

| Field | Value |
| :--- | :--- |
| **ID** | `BUG-001` |
| **Severity** | Medium |
| **Priority** | P2 |
| **Status** | Partially Fixed (`LancesPage` route + CI driver setup fixed; `LancesTest` pending) |
| **Component** | `LancesPage.java`, `PageObject.java`, and `drivers/chromedriver` |
| **Target App** | Spring Boot MVC (`LeilaoController.java` / `LanceController.java`) |

---

## Findings Identified During QA Audit

### 1. Wrong Endpoint Path in `LancesPage.java` (Fixed)
`LeilaoController.java` (`@RequestMapping("/leiloes")`) exposes the auction detail & bidding screen at:
```java
@GetMapping("/{id}") // -> /leiloes/{id}
```
However, [`LancesPage.java`](../../../src/test/java/br/com/alura/leilao/lances/LancesPage.java) previously asserted:
```java
private static final String URL_LANCES = "http://localhost:8080/leilao/2"; // 404 Not Found
```
Meanwhile, `LoginPage.java` (line 10) correctly used `http://localhost:8080/leiloes/2`.  
* **Fix applied:** Updated `LancesPage.URL_LANCES` to `http://localhost:8080/leiloes/2`.

### 2. OS-Locked `drivers/chromedriver` Binary & Missing Headless Support (Fixed)
The repository previously tracked a macOS `chromedriver` binary under `drivers/` and instantiated `new ChromeDriver()` without headless options, breaking execution on Linux CI runners (`ubuntu-latest`) and newer Chrome versions.  
* **Fix applied:** Removed `drivers/chromedriver` from Git, added `ChromeOptions` (`--headless=new`, `--no-sandbox`, `--disable-dev-shm-usage`) triggered via `-Dheadless=true` or `CI=true` in [`PageObject.java`](../../../src/test/java/br/com/alura/leilao/PageObject.java), and wired `browser-actions/setup-chrome@v2` in GitHub Actions.

### 3. Open Coverage Gap: Bidding Flow (`POST /lances`)
`LanceController.java` implements business rules for bid proposals (`"Lance adicionado com sucesso!"` vs `"Lance invalido!"`), and `LancesPage.java` exists, but no `LancesTest.java` automates bid submission or duplicate-bid rejection yet.
