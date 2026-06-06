# Nexus Agendamentos

O **Nexus Agendamentos** é um sistema focado no gerenciamento e na automatização de agendamentos de serviços. A aplicação conta com uma interface visual de página única (Single Page Application) integrada diretamente a uma API REST robusta desenvolvida em Java com Spring Boot e persistência em banco de dados PostgreSQL.

---

## 🔗 Link de Produção

A aplicação foi totalmente conteinerizada e está em execução em ambiente de produção na nuvem. O acesso à interface e à API ocorre de forma unificada pelo link:

> **Ambiente Online:** https://nexus-agendamentos.onrender.com/

---

## 🛠️ Matriz Tecnológica

| Camada | Tecnologia | Componente / Biblioteca |
| :--- | :--- | :--- |
| **Back-end** | Java 17 | Spring Boot 4.x (Web, Data JPA) |
| **Banco de Dados** | PostgreSQL | Driver Nativo JDBC |
| **Migrações** | Flyway | Evolução de Esquema Declarativa |
| **Front-end** | Web Nativo | HTML5, CSS3, JavaScript (ES6) |
| **Infraestrutura** | DevOps | Docker (Multi-stage Build), Render Cloud |

---

## 📋 Funcionalidades Principais

* **Persistência em Tempo Real:** Operações completas de CRUD salvas de forma segura em um servidor de banco de dados em nuvem.
* **Filtros Avançados:** Mecanismo de busca que permite segmentar os registros de agendamentos por nome de usuário diretamente na tela.
* **Fluxo de Estados:** Transição dinâmica do status do agendamento (Pendente, Concluído, Cancelado) através de requisições assíncronas.
* **Interface Monolítica Simplificada:** Arquivos estáticos servidos diretamente pelo servidor Tomcat embutido no Spring Boot, eliminando problemas com CORS.

---

## 📂 Arquitetura do Projeto

A organização de pastas adota o padrão de separação por responsabilidades recomendado pelo ecossistema Spring:


src/main/java/dev/rogerrdeveloper/miniagendamentoX/
├── controller/  # Exposição dos endpoints REST e tratamento de requisições HTTP
├── dto/         # Objetos de transferência de dados (Request e Response)
├── mapper/      # Classes de conversão entre entidades de banco e DTOs
├── model/       # Entidades mapeadas para tabelas relacionais e Enums
├── repository/  # Camada de abstração de dados e consultas SQL (Spring Data)
└── service/     # Isolamento de lógica de negócio e validações do sistema
[!NOTE]
Os arquivos de interface (index.html, style.css e app.js) ficam alocados em src/main/resources/static/. Isso faz com que o Spring Boot gerencie e sirva as telas automaticamente na rota raiz (/).

