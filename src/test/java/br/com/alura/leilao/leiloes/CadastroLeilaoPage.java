package br.com.alura.leilao.leiloes;

import br.com.alura.leilao.PageObject;
import org.openqa.selenium.By;
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

        inputNome.clear();
        inputNome.sendKeys(nome);
        inputValor.clear();
        inputValor.sendKeys(valorInicial);
        inputData.clear();
        inputData.sendKeys(dataAbertura);

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
