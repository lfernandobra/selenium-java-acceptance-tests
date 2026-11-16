package br.com.alura.leilao;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

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

    public void fechar() {
        if (this.browser != null) {
            this.browser.quit();
        }
    }
}
