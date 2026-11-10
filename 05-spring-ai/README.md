# 💰 API de Orçamento Inteligente com Spring Boot & Spring AI

Desafio final de consolidação arquitetural e integração com inteligência artificial da trilha Spring Boot da DIO. O projeto consiste em uma API inteligente de gerenciamento de orçamento (_budgeting_) capaz de processar comandos financeiros por voz, interpretar intenções utilizando LLMs (OpenAI) e disparar funções de domínio reais de forma acoplada utilizando a arquitetura em camadas (DDD).

## 🚀 O que o projeto faz (Fluxo Principal)

1. **Reconhecimento de Fala (Speech-to-Text):** O cliente envia um arquivo de áudio (`.m4a`) contendo uma instrução financeira por comando de voz.
2. **Transcrição Dinâmica:** A API usa o `TranscriptionModel` do Spring AI para converter o áudio em texto literal.
3. **Orquestração por Inteligência Artificial (Tool Calling):** Através do `ChatClient` integrado à LLM, o modelo interpreta o texto e aciona dinamicamente métodos anotados com `@Tool`, mapeando a intenção do usuário para um caso de uso real da aplicação.
4. **Persistência das Transações:** O caso de uso aciona o domínio, valida a transação e persiste as informações no banco de dados MySQL rodando no Docker.
5. **Sintetização de Voz (Text-to-Speech):** A API gera uma resposta de confirmação contextualizada, converte o texto final para áudio (`.mp3`) via `TextToSpeechModel` e devolve a resposta sonora para o usuário.

## ⚙️ Tecnologias Utilizadas

- **Java 17** & **Spring Boot 3.x**
- **Spring AI** (ChatModel, Transcription API e Speech API)
- **Lombok** (Redução de boilerplate de código)
- **Spring Data JPA** & **MySQL** (Camada de persistência)
- **Docker & Docker Compose** (Containerização do banco de dados relacional)
- **Gradle** (Gerenciador de build e dependências)

## 🛠️ Minha Evolução Técnica (Melhoria Implementada)

**Proteção de Invariantes de Domínio contra Alucinações de IA:**
Em sistemas orientados por IA Generativa, os modelos de linguagem podem interpretar valores de forma incorreta ou sofrer alucinações (ex: tentar cadastrar uma transação com valor negativo ou zerado a partir de um áudio ambíguo).

Para blindar o sistema seguindo os padrões do **Domain-Driven Design (DDD)**, implementei uma **validação defensiva diretamente no construtor da Entidade de Domínio (`Transaction.java`)**. Isso garante que a entidade nunca exista em um estado inválido no ecossistema:

```java
public Transaction(String description, long amount, Category category) {
    if (amount <= 0) {
        throw new IllegalArgumentException("O valor da transação deve ser obrigatoriamente maior que zero!");
    }
    if (category == null) {
        throw new IllegalArgumentException("A transação precisa pertencer a uma categoria válida!");
    }

    this.id = new TransactionId();
    this.description = (description == null || description.isBlank()) ? "Gasto sem descrição especificada" : description;
    this.amount = amount;
    this.category = category;
}
```

Mesmo que a LLM envie dados corrompidos via _Tool Calling_, a regra de negócio do núcleo da aplicação barra a operação antes que ela chegue à infraestrutura de persistência.

## 🏃 Como Executar a Aplicação

1. Certifique-se de possuir o Docker ativo e suba a infraestrutura do banco de dados na raiz do módulo `05-spring-ai`:

   ```bash
   docker compose up -d
   ```

2. Configure as suas credenciais no arquivo `application.properties` ou exporte a sua chave de API da OpenAI no terminal:

   ```bash
   export OPENAI_API_KEY="sua_chave_da_openai_aqui"
   ```

3. Compile e execute a aplicação utilizando o Gradle Wrapper:
   ```bash
   ./gradlew bootRun
   ```

## 🧠 O que aprendi neste desafio

- Como acoplar Inteligência Artificial Generativa em sistemas corporativos sem violar as fronteiras arquiteturais do DDD.
- O poder do **Tool Calling** para permitir que uma LLM execute métodos Java reais com base na fala do usuário.
- Gerenciamento e controle rigoroso de tipos complexos e primitivos (`long` para tratamento de centavos em finanças).
- Utilização de fluxos de áudio assíncronos e tratamento de permissões de infraestrutura no Linux (Docker Socket).
