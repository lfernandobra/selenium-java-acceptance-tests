package br.com.alura.leilao;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PageObject {
    protected WebDriver browser;

    public PageObject(WebDriver browser) {
        if (browser == null) {
            ChromeOptions options = new ChromeOptions();
            boolean isCi = "true".equalsIgnoreCase(System.getenv("CI"));
            String headlessProp = System.getProperty("headless");
            boolean isHeadless = headlessProp != null
                    ? Boolean.parseBoolean(headlessProp)
                    : isCi && System.getenv("DISPLAY") == null;

            if (isHeadless) {
                options.addArguments("--headless=new", "--disable-extensions");
            }
            if (isHeadless || isCi) {
                options.addArguments(
                        "--no-sandbox",
                        "--disable-dev-shm-usage",
                        "--disable-gpu",
                        "--remote-allow-origins=*",
                        "--window-size=1920,1080"
                );
            }
            this.browser = new ChromeDriver(options);
        } else {
            this.browser = browser;
        }
        this.browser.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(5))
                .pageLoadTimeout(Duration.ofSeconds(10));
    }

    /**
     * Cria uma instancia padrao de espera explicita para interacoes de UI.
     */
    protected WebDriverWait esperaPadrao() {
        return new WebDriverWait(this.browser, Duration.ofSeconds(5));
    }

    /**
     * Aguarda a navegacao sair da URL informada e concluir o carregamento do novo documento.
     */
    protected void aguardarSaidaDaUrl(String urlAnterior) {
        WebDriverWait wait = esperaPadrao();
        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(urlAnterior)));
        wait.until(driver -> "complete".equals(
                ((JavascriptExecutor) driver).executeScript("return document.readyState")));
    }

    public void fechar() {
        if (this.browser != null) {
            this.browser.quit();
        }
    }
}
