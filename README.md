# 🍽️ Tech Challenge Workspace (10ADJT)

Este repositório atua como o ambiente central (workspace) para a arquitetura de microsserviços do Tech Challenge. Ele utiliza submódulos do Git para centralizar o versionamento dos serviços independentes e gerencia a orquestração de toda a infraestrutura local utilizando o Docker Compose.

## 🏗️ Microsserviços

O projeto é composto pelos seguintes domínios, mantidos em repositórios separados e integrados aqui como submódulos:

* 💳 **[Chef Payment Service](https://github.com/10ADJT-Tech-Challenge/chef-payment-service):** Responsável pelo gerenciamento e processamento de pagamentos.
* ⚙️ **[Chef Manager API](https://github.com/10ADJT-Tech-Challenge/chef-manager-api):** Responsável pelo gerenciamento administrativo e de catálogo.
* 🛒 **[Chef Order Service](https://github.com/10ADJT-Tech-Challenge/chef-order-service):** Responsável pela orquestração e fluxo de pedidos.

## 📂 Estrutura do Projeto

```text
tech-challenge-workspace/
├── docker-compose.yml           # Orquestração da infraestrutura e microsserviços
├── .env                         # Variáveis de ambiente locais (bancos, portas, credenciais)
├── README.md                    # Documentação do workspace
├── chef-manager-api/            # (Submódulo) Código fonte do Manager API
├── chef-order-service/          # (Submódulo) Código fonte do Order Service
└── chef-payment-service/        # (Submódulo) Código fonte do Payment Service
```

## 🚀 Como começar

### Pré-requisitos
* [Git](https://git-scm.com/)
* [Docker](https://www.docker.com/)
* [Docker Compose](https://docs.docker.com/compose/)

### 1. Clonando o repositório

Para clonar o workspace e inicializar os submódulos na mesma execução, utilize a flag `--recurse-submodules`:

```bash
git clone --recurse-submodules <url-deste-repositorio>
```

> **Nota:** Se você já fez o clone normal e as pastas dos serviços estão vazias, rode o comando abaixo na raiz do projeto para forçar o download:
> ```bash
> git submodule update --init --recursive
> ```

### 2. Configurando as Variáveis de Ambiente (.env)

O projeto utiliza um arquivo `.env` na raiz do workspace para injetar configurações dinâmicas no `docker-compose.yml` (como senhas de banco de dados, chaves e portas).

1.  Crie o arquivo `.env` na raiz do projeto. Existe o arquivo de exemplo (`.env.example`), você pode copiá-lo:
    ```bash
    cp .env.example .env
    ```
2.  Abra o arquivo `.env` e preencha com as credenciais locais e configurações necessárias para rodar a infraestrutura.
> ⚠️ **Atenção:** O arquivo `.env` não deve ser versionado. Nunca comite senhas reais ou dados sensíveis no repositório!

### 3. Subindo a infraestrutura

Com os repositórios clonados e o `.env` configurado, levante toda a arquitetura com um único comando na raiz do projeto:

```bash
docker compose up -d --build
```
> O parâmetro `--build` garante que as imagens dos microsserviços sejam reconstruídas caso haja alterações recentes no código fonte dos submódulos.

Para acompanhar os logs de todos os serviços em tempo real:
```bash
docker compose logs -f
```

Para derrubar a infraestrutura (parar e remover os containers):
```bash
docker compose down
```

## 🔄 Gerenciando Submódulos

Para atualizar todos os submódulos com as últimas alterações dos seus respectivos repositórios remotos (buscando da branch principal), execute na raiz do workspace:

```bash
git submodule update --remote --merge
```

Caso alguém da equipe tenha alterado a URL de um submódulo no arquivo `.gitmodules`, sincronize as configurações locais com:
```bash
git submodule sync
git submodule update --init --recursive
```

## 📝 Fluxo de Trabalho (Importante)

Trabalhar com submódulos exige atenção para não gerar conflitos de versionamento. Siga este fluxo:

1.  **Não altere código diretamente pelo workspace:** As pastas `chef-*` são repositórios independentes.
2.  **Para desenvolver:** Entre na pasta do serviço (ex: `cd chef-payment-service`), crie sua branch, faça as alterações, os commits e o push diretamente para aquele repositório.
3.  **Atualize o Workspace:** Após o código do serviço ser validado e mergeado na branch principal (main/master), volte para a raiz do workspace, rode `git submodule update --remote` e faça um commit no workspace para atualizar o "ponteiro" (hash) do submódulo.