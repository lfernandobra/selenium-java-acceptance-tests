package br.com.alura.leilao;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class PageObject {
    protected WebDriver browser;

    public PageObject(WebDriver browser) {
        if (browser == null) {
            ChromeOptions options = new ChromeOptions();
            if (Boolean.parseBoolean(System.getProperty("headless", "false"))
                    || "true".equalsIgnoreCase(System.getenv("CI"))) {
                options.addArguments(
                        "--headless=new",
                        "--no-sandbox",
                        "--disable-dev-shm-usage",
                        "--disable-gpu",
                        "--disable-extensions",
                        "--remote-allow-origins=*",
                        "--window-size=1920,1080"
                );
            }
            this.browser = new ChromeDriver(options);
        } else {
            this.browser = browser;
        }
        this.browser.manage().timeouts()
                .implicitlyWait(5, TimeUnit.SECONDS)
                .pageLoadTimeout(10, TimeUnit.SECONDS);
    }

    /**
     * Aguarda a navegacao sair da URL informada apos um submit/redirecionamento.
     */
    protected void aguardarSaidaDaUrl(String urlAnterior) {
        new WebDriverWait(this.browser, 5)
                .until(ExpectedConditions.not(ExpectedConditions.urlToBe(urlAnterior)));
    }

    public void fechar() {
        if (this.browser != null) {
            this.browser.quit();
        }
    }
}
