package br.com.alura.leilao.leiloes;

import br.com.alura.leilao.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LeiloesPage extends PageObject {
    public static final String URL_CADASTRO_LEILAO = "http://localhost:8080/leiloes/new";
    public static final String URL_LEILOES = "http://localhost:8080/leiloes";

    public LeiloesPage(WebDriver browser) {
        super(browser);
        //this.browser.navigate().to(URL_LEILOES);
    }

    public CadastroLeilaoPage carregaFormulario() {
        this.browser.navigate().to(URL_CADASTRO_LEILAO);
        return new CadastroLeilaoPage(browser);
    }

    public boolean isLeilaoCadastrado(String nome, String valor, String data) {
        WebElement linhaDaTabela;
        try {
            linhaDaTabela = this.browser.findElement(By.cssSelector("#tabela-leiloes tbody tr:last-child"));
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(
                    "Tabela de leiloes nao encontrada em URL=" + this.browser.getCurrentUrl()
                            + " | title=" + this.browser.getTitle()
                            + " | snippet=" + this.browser.findElement(By.tagName("body")).getText(),
                    e);
        }

        WebElement colunaNome = linhaDaTabela.findElement(By.cssSelector("td:nth-child(1)"));
        WebElement colunaDataAbertura = linhaDaTabela.findElement(By.cssSelector("td:nth-child(2)"));
        WebElement colunaValorInicial = linhaDaTabela.findElement(By.cssSelector("td:nth-child(3)"));

        boolean matches = colunaNome.getText().equals(nome) &&
                colunaDataAbertura.getText().equals(data) &&
                colunaValorInicial.getText().equals(valor);

        if (!matches) {
            System.err.printf(
                    "Leilao divergente na tabela. Esperado=[%s, %s, %s] Obtido=[%s, %s, %s]%n",
                    nome, data, valor,
                    colunaNome.getText(), colunaDataAbertura.getText(), colunaValorInicial.getText());
        }

        return matches;
    }

    public boolean isPaginaAtual() {
        return browser.getCurrentUrl().contentEquals(URL_LEILOES);
    }
}
