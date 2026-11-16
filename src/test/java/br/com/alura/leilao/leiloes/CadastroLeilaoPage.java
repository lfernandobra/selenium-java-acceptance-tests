package br.com.alura.leilao.leiloes;

import br.com.alura.leilao.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CadastroLeilaoPage extends PageObject {
    public static final String URL_CADASTRO_LEILAO = "http://localhost:8080/leiloes/new";

    public CadastroLeilaoPage(WebDriver browser) {
        super(browser);
    }

    public LeiloesPage cadastrarLeilao(String nome, String valorInicial, String dataAbertura) {
        WebElement inputNome = esperaPadrao().until(ExpectedConditions.elementToBeClickable(By.id("nome")));
        WebElement inputValor = this.browser.findElement(By.id("valorInicial"));
        WebElement inputData = this.browser.findElement(By.id("dataAbertura"));
        JavascriptExecutor js = (JavascriptExecutor) this.browser;

        js.executeScript(
                "window.__dbg = {key:0, input:0, click:0};"
                        + "document.getElementById('nome').addEventListener('keydown', function(){window.__dbg.key++;});"
                        + "document.getElementById('nome').addEventListener('input', function(){window.__dbg.input++;});"
                        + "document.getElementById('button-submit').addEventListener('click', function(){window.__dbg.click++;});");

        inputNome.clear();
        inputNome.sendKeys(nome);
        inputValor.clear();
        inputValor.sendKeys(valorInicial);
        inputData.clear();
        inputData.sendKeys(dataAbertura);

        Object domInfo = js.executeScript(
                "return {url: window.location.href, dbg: window.__dbg, "
                        + "nome: document.getElementById('nome').value, "
                        + "valor: document.getElementById('valorInicial').value, "
                        + "data: document.getElementById('dataAbertura').value};");
        System.err.println("CadastroLeilaoPage DOM pre-submit: " + domInfo);

        esperaPadrao().until(ExpectedConditions.elementToBeClickable(By.id("button-submit"))).click();
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
