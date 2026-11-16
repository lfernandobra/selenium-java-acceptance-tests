package br.com.alura.leilao.leiloes;

import br.com.alura.leilao.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CadastroLeilaoPage extends PageObject {
    public static final String URL_CADASTRO_LEILAO = "http://localhost:8080/leiloes/new";

    public CadastroLeilaoPage(WebDriver browser) {
        super(browser);
    }

    public LeiloesPage cadastrarLeilao(String nome, String valorInicial, String dataAbertura) {
        WebDriverWait wait = new WebDriverWait(this.browser, 5);
        WebElement inputNome = wait.until(ExpectedConditions.elementToBeClickable(By.id("nome")));
        WebElement inputValor = this.browser.findElement(By.id("valorInicial"));
        WebElement inputData = this.browser.findElement(By.id("dataAbertura"));

        inputNome.clear();
        inputNome.sendKeys(nome);
        inputValor.clear();
        inputValor.sendKeys(valorInicial);
        inputData.clear();
        inputData.sendKeys(dataAbertura);

        Object domInfo = ((JavascriptExecutor) this.browser).executeScript(
                "return {url: window.location.href, readyState: document.readyState, hasFocus: document.hasFocus(), "
                        + "nome: document.getElementById('nome') ? document.getElementById('nome').value : null, "
                        + "valor: document.getElementById('valorInicial') ? document.getElementById('valorInicial').value : null, "
                        + "data: document.getElementById('dataAbertura') ? document.getElementById('dataAbertura').value : null, "
                        + "btnRect: document.getElementById('button-submit') ? JSON.stringify(document.getElementById('button-submit').getBoundingClientRect()) : null};");
        System.err.println("CadastroLeilaoPage DOM pre-submit: " + domInfo);

        wait.until(ExpectedConditions.elementToBeClickable(By.id("button-submit"))).click();
        aguardarSaidaDaUrl(URL_CADASTRO_LEILAO);

        return new LeiloesPage(browser);
    }

    public boolean isPaginaAtual() {
        return browser.getCurrentUrl().equals(URL_CADASTRO_LEILAO);
    }

    public boolean isMensagensDeValidacaoVisiveis() {
        String pageSource = browser.getPageSource();
        return pageSource.contains("minimo 3 caracteres") &&
                pageSource.contains("não deve estar em branco") &&
                pageSource.contains("deve ser um valor maior de 0.1") &&
                pageSource.contains("deve ser uma data no formato dd/MM/yyyy");
    }
}
