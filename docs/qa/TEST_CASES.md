# Test Case Inventory — Leilão Acceptance Suite

Total automated JUnit + Selenium acceptance tests: **`5`** across `2` test classes.

---

## 1. Login & Access Control (`LoginTest.java`)

| ID | Test Method | Precondition / Input | Expected Result | Status |
| :--- | :--- | :--- | :--- | :---: |
| `TC-LOGIN-01` | `deveriaEfetuarLoginComDadosValidos` | Seeded user `fulano` / `pass` | Redirects away from `/login`; `#usuario-logado` displays `"fulano"` | Automated |
| `TC-LOGIN-02` | `naoDeveriaLogarComDadosInvalidos` | Invalid user `invalido` / `123123` | URL equals `/login?error`; `#usuario-logado` is `null`; shows `"Usuário e senha inválidos."` | Automated |
| `TC-LOGIN-03` | `naoDeveriaAcessarPaginaRestritaSemEstarLogado` | Unauthenticated session; navigate to `/leiloes/2` | Redirected to `/login`; `"Dados do Leilão"` is not rendered | Automated |

---

## 2. Auction Management (`LeiloesTest.java`)

| ID | Test Method | Precondition / Input | Expected Result | Status |
| :--- | :--- | :--- | :--- | :---: |
| `TC-LEILAO-01` | `deveriaCadastrarLeilao` | Logged in as `fulano`; `"Leilão do dia <dd/MM/yyyy>"`, `"500.00"`, today's date | Last row in `#tabela-leiloes tbody` matches submitted name, date, and value | Automated |
| `TC-LEILAO-02` | `deveriaValidarCadastroDeLeilao` | Logged in as `fulano`; empty `nome`, `valorInicial`, `dataAbertura` | URL equals `/leiloes`; renders all 4 validation error messages (`minimo 3 caracteres`, `não deve estar em branco`, `deve ser um valor maior de 0.1`, `deve ser uma data no formato dd/MM/yyyy`) | Automated |
