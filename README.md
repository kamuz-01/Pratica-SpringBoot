# 🎓 Sistema de Gestão Escolar — API REST

> API REST desenvolvida com **Spring Boot 3.5** para simular e gerenciar um mini-sistema escolar com estudantes, professores, cursos, disciplinas e matrículas.

<br>

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.13-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MapStruct](https://img.shields.io/badge/MapStruct-1.6.3-8A2BE2?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-UI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

---

## 📋 Índice

- [🎓 Sistema de Gestão Escolar — API REST](#-sistema-de-gestão-escolar--api-rest)
  - [📋 Índice](#-índice)
  - [💡 Sobre o Projeto](#-sobre-o-projeto)
  - [🏗 Arquitetura](#-arquitetura)
  - [✨ Funcionalidades](#-funcionalidades)
    - [👨‍🎓 Estudantes](#-estudantes)
    - [👨‍🏫 Professores](#-professores)
    - [📚 Cursos](#-cursos)
    - [📖 Disciplinas](#-disciplinas)
    - [📝 Matrículas](#-matrículas)
  - [🔌 Endpoints da API](#-endpoints-da-api)
    - [Estudantes — `/api/estudantes`](#estudantes--apiestudantes)
    - [Professores — `/api/professores`](#professores--apiprofessores)
    - [Cursos — `/api/cursos`](#cursos--apicursos)
    - [Disciplinas — `/api/disciplinas`](#disciplinas--apidisciplinas)
    - [Matrículas — `/api/matriculas`](#matrículas--apimatriculas)
  - [🗄 Modelo de Dados](#-modelo-de-dados)
  - [⚠️ Tratamento de Erros](#️-tratamento-de-erros)
  - [🔒 Segurança](#-segurança)
  - [📊 Logging](#-logging)
  - [🧪 Testes Unitários](#-testes-unitários)
  - [🚀 Como Executar](#-como-executar)
    - [Pré-requisitos](#pré-requisitos)
    - [Configuração do banco](#configuração-do-banco)
    - [Variáveis de ambiente](#variáveis-de-ambiente)
    - [Executando](#executando)
  - [📖 Documentação Swagger](#-documentação-swagger)
  - [📁 Estrutura do Projeto](#-estrutura-do-projeto)
  - [🤝 Contribuindo](#-contribuindo)

---

## 💡 Sobre o Projeto

Este projeto é uma API REST construída como prática de desenvolvimento com **Spring Boot**, **JPA/Hibernate**, **MapStruct**, **Git** e **GitHub**. A proposta é representar o back-end de uma instituição de ensino e exercitar, de forma objetiva, organização em camadas, validação, mapeamento entre objetos e automação de testes.

A versão atual do projeto consolida melhorias importantes:

- Mapeamento DTO ↔ Entity com **MapStruct**
- Listagens paginadas e ordenadas com `Page<T>` e `Pageable`
- Criptografia de senha com **BCryptPasswordEncoder**
- Tratamento global e padronizado de exceções
- Upload e armazenamento de imagem de perfil
- Logging em arquivo com separação entre log geral, erros e acesso HTTP
- Documentação interativa com **SpringDoc/OpenAPI**
- Testes unitários e de integração cobrindo services, controllers e o carregamento do contexto

---

## 🏗 Arquitetura

O projeto segue um modelo em camadas tradicional do ecossistema Spring:

```
Controller  ──►  Service  ──►  Repository  ──►  Database
    │                │
    │         ManipuladorExcecoesGlobais
    │                │
    └──────  DTOs / Entities / Mappers
```

| Camada | Responsabilidade |
|---|---|
| **Controller** | Receber requisições HTTP, validar entrada e delegar ao service |
| **Service** | Regras de negócio, relacionamentos e orquestração |
| **Repository** | Acesso ao banco com Spring Data JPA |
| **DTO** | Contrato da API para entrada e saída |
| **Entity** | Persistência e relacionamento com JPA/Hibernate |
| **Mapper** | Conversão entre DTO e entidade com MapStruct |
| **GerenciamentoErros** | Tratamento centralizado de exceções do sistema |

---

## ✨ Funcionalidades

### 👨‍🎓 Estudantes
- Cadastro com dados pessoais, matrícula e senha
- Upload opcional de foto de perfil em `multipart/form-data`
- Validação de CPF e unicidade no banco
- Atualização, busca, listagem paginada e remoção

### 👨‍🏫 Professores
- Cadastro com especialidade acadêmica e senha
- Upload opcional de foto de perfil em `multipart/form-data`
- Validação de CPF e unicidade no banco
- Atualização, busca, listagem paginada e remoção

### 📚 Cursos
- Cadastro com código único, nome e descrição
- Atualização, busca, listagem paginada e remoção
- Relação com disciplinas via `OneToMany`

### 📖 Disciplinas
- Associação obrigatória a um curso e a um professor
- Código único por disciplina
- Controle de carga horária com validação de faixa
- Atualização, busca, listagem paginada e remoção

### 📝 Matrículas
- Vínculo entre estudante e disciplina por semestre
- Controle de frequência, nota final e status da matrícula
- Unicidade garantida por combinação de estudante, disciplina e semestre
- Atualização, busca, listagem paginada e remoção

---

## 🔌 Endpoints da API

Os endpoints de listagem suportam paginação e ordenação via `page`, `size` e `sort`.

### Estudantes — `/api/estudantes`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/estudantes` | Cadastrar estudante (JSON) |
| `POST` | `/api/estudantes` | Cadastrar estudante com foto (multipart) |
| `GET` | `/api/estudantes` | Listar estudantes com paginação |
| `GET` | `/api/estudantes/{id}` | Buscar estudante por ID |
| `PUT` | `/api/estudantes/{id}` | Atualizar estudante (JSON) |
| `PUT` | `/api/estudantes/{id}` | Atualizar estudante com foto (multipart) |
| `DELETE` | `/api/estudantes/{id}` | Remover estudante |

### Professores — `/api/professores`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/professores` | Cadastrar professor (JSON) |
| `POST` | `/api/professores` | Cadastrar professor com foto (multipart) |
| `GET` | `/api/professores` | Listar professores com paginação |
| `GET` | `/api/professores/{id}` | Buscar professor por ID |
| `PUT` | `/api/professores/{id}` | Atualizar professor (JSON) |
| `PUT` | `/api/professores/{id}` | Atualizar professor com foto (multipart) |
| `DELETE` | `/api/professores/{id}` | Remover professor |

### Cursos — `/api/cursos`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/cursos` | Criar curso |
| `GET` | `/api/cursos` | Listar cursos com paginação |
| `GET` | `/api/cursos/{id}` | Buscar curso por ID |
| `PUT` | `/api/cursos/{id}` | Atualizar curso |
| `DELETE` | `/api/cursos/{id}` | Remover curso |

### Disciplinas — `/api/disciplinas`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/disciplinas` | Criar disciplina |
| `GET` | `/api/disciplinas` | Listar disciplinas com paginação |
| `GET` | `/api/disciplinas/{id}` | Buscar disciplina por ID |
| `PUT` | `/api/disciplinas/{id}` | Atualizar disciplina |
| `DELETE` | `/api/disciplinas/{id}` | Remover disciplina |

### Matrículas — `/api/matriculas`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/matriculas` | Criar matrícula |
| `GET` | `/api/matriculas` | Listar matrículas com paginação |
| `GET` | `/api/matriculas/{id}` | Buscar matrícula por ID |
| `PUT` | `/api/matriculas/{id}` | Atualizar matrícula |
| `DELETE` | `/api/matriculas/{id}` | Remover matrícula |

---

## 🗄 Modelo de Dados

```
┌─────────────┐         ┌──────────────┐
│   Usuario   │         │    Curso     │
│  (abstrata) │         │              │
│─────────────│         │──────────────│
│ id_usuario  │         │ idCurso      │
│ nome        │         │ codigoCurso  │
│ sobrenome   │         │ nomeCurso    │
│ cpf (único) │         │ descricao    │
│ dataNasc.   │         └──────┬───────┘
│ cidade      │                │ 1
│ estado      │                │
│ paisOrigem  │                │ N
│ telefone    │         ┌──────▼───────┐       ┌─────────────┐
│ email       │         │  Disciplina  │       │  Professor  │
│ senha       │         │              │       │  (Usuario)  │
│ imagemPerfil│         │──────────────│   N   │─────────────│
└──────┬──────┘    N    │ id           ├───────┤ especialid. │
       │    ┌──────────►│ nome         │       └─────────────┘
       │    │           │ codigo       │
  ┌────▼────┴──┐        │ cargaHoraria │
  │  Estudante │        │ idCurso (FK) │
  │  (Usuario) │        │ professor(FK)│
  │────────────│        └──────┬───────┘
  │ matricula  │               │ N
  └─────┬──────┘               │
        │ 1                    │ N
        │    ┌─────────────────┘
        │    │
        │  N │ N
        └────▼────────┐
          ┌───────────┴──┐
          │   Matricula  │
          │──────────────│
          │ id           │
          │ estudante(FK)│
          │ disciplina(FK│
          │ semestre     │
          │ dataMatricula│
          │ frequencia   │
          │ notaFinal    │
          │ status       │
          └──────────────┘
```

**Herança de `Usuario`:** `InheritanceType.JOINED`. A tabela `usuario` concentra os campos comuns e `estudante`/`professor` mantêm os dados específicos.

---

## ⚠️ Tratamento de Erros

As exceções da aplicação são tratadas centralmente pela classe `ManipuladorExcecoesGlobais`, que devolve respostas padronizadas no formato `ProblemResponse`.

Exemplo de resposta:

```json
{
  "status": 404,
  "titulo": "Recurso não encontrado",
  "detalhe": "Estudante não encontrado: 42",
  "instancia": "/api/estudantes/42",
  "timestamp": "24/03/2026 14:35:10",
  "metodo": "GET"
}
```

| Status | Situação |
|---|---|
| `400` | Validação de campos, JSON inválido ou parâmetro incompatível |
| `404` | Recurso ou rota não encontrada |
| `409` | CPF duplicado ou violação de constraint única |
| `413` | Imagem de perfil acima do limite permitido |
| `422` | Regra de negócio violada |
| `500` | Erro interno inesperado |

---

## 🔒 Segurança

As senhas não são armazenadas em texto puro. O projeto utiliza **BCryptPasswordEncoder** com strength 10, garantindo hash seguro e resistência a ataques de força bruta.

```
senha em texto  ──►  BCrypt (strength 10)  ──►  hash armazenado
```

---

## 📊 Logging

O sistema registra logs em arquivo e em console com rotação diária.

| Arquivo | Conteúdo |
|---|---|
| `logs/app.log` | Log geral da aplicação |
| `logs/error.log` | Apenas erros |
| `logs/api-access.log` | Requisições HTTP com método, rota, IP, status e tempo de resposta |

Exemplo de linha no `api-access.log`:

```text
2026-03-24 14:35:10 IP=127.0.0.1 METHOD=POST URI=/api/estudantes STATUS=201 TIME=87ms
```

A aplicação usa `logback-spring.xml` para separar o log geral, os erros e o acesso HTTP, com retenção por 30 dias.

---

## 🧪 Testes Unitários

A base de testes cobre a aplicação em dois níveis:

- **Testes unitários de service** com **JUnit 5** e **Mockito**
- **Testes de controller** com **MockMvc**
- **Testes de integração** com `@SpringBootTest`, `@AutoConfigureMockMvc` e banco **H2** em memória

As principais garantias exercitadas pelos testes são:

- validação de regras de negócio
- criação, atualização, listagem e exclusão
- tratamento de erros e status HTTP
- carregamento do contexto completo da aplicação
- paginação e ordenação nas listagens

Para executar localmente:

```bash
# Linux / macOS
./mvnw test

# Windows
.\mvnw.cmd test
```

---

## 🚀 Como Executar

### Pré-requisitos

- Java 21+
- Maven 3.9+
- MySQL 8.0+

### Configuração do banco

Crie o banco de dados no MySQL:

```sql
CREATE DATABASE pratica_springboot;
```

Depois, ajuste as credenciais em `src/main/resources/application.properties` conforme o seu ambiente local.

### Variáveis de ambiente

O projeto pode ser adaptado para variáveis de ambiente, mas a configuração padrão atual está em `application.properties`.

Se preferir externalizar os dados sensíveis, defina as propriedades equivalentes no seu ambiente de execução e mantenha o banco apontando para a mesma URL.

### Executando

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/Pratica_SpringBoot.git
cd Pratica_SpringBoot

# Execute a aplicação
./mvnw spring-boot:run
```

No Windows, use:

```bash
.\mvnw.cmd spring-boot:run
```

A aplicação sobe na porta `8080` por padrão.

---

## 📖 Documentação Swagger

Com a aplicação em execução, acesse:

```text
http://localhost:8080/swagger-ui/index.html
```

A documentação exibe os endpoints, schemas, contratos de entrada e saída e permite testar as rotas diretamente pelo navegador.

---

## 📁 Estrutura do Projeto

```
src/main/java/org/Pratica_SpringBoot/
│
├── Config/
│   └── ConfigSwagger/            # Configuração do OpenAPI/Swagger
│
├── Controllers/                  # Camada REST
│   ├── CursoController
│   ├── DisciplinaController
│   ├── EstudanteController
│   ├── MatriculaController
│   └── ProfessorController
│
├── Docs/
│   └── ProblemResponse           # Estrutura padronizada de erro
│
├── GerenciamentoErros/           # Tratamento centralizado de exceções
│   └── ManipuladorExcecoesGlobais
│
├── Loggings/
│   └── InterceptorLoggingApi     # Interceptor de acesso HTTP
│
├── Models/
│   ├── DTOs/                     # Contratos de entrada/saída
│   │   ├── CursoDTO
│   │   ├── DisciplinaDTO
│   │   ├── EstudanteDTO
│   │   ├── MatriculaDTO
│   │   ├── ProfessorDTO
│   │   └── UsuarioDTO
│   ├── Entities/                 # Entidades JPA
│   │   ├── Curso
│   │   ├── Disciplina
│   │   ├── Estudante
│   │   ├── Matricula
│   │   ├── Professor
│   │   └── Usuario
│   ├── Enums/
│   │   └── StatusMatricula
│   └── Mappers/                 # Mapeamento DTO ↔ Entity com MapStruct
│       ├── CursoMapper
│       ├── DisciplinaMapper
│       ├── EstudanteMapper
│       ├── MatriculaMapper
│       └── ProfessorMapper
│
├── Repositories/                 # Spring Data JPA
│   ├── CursoRepository
│   ├── DisciplinaRepository
│   ├── EstudanteRepository
│   ├── MatriculaRepository
│   └── ProfessorRepository
│
└── Services/                     # Regras de negócio
    ├── CursoService
    ├── DisciplinaService
    ├── EstudanteService
    ├── MatriculaService
    ├── ProfessorService
    ├── SenhaCriptografiaService
    └── UsuarioImagemStorageService
```

---

## 🤝 Contribuindo

Contribuições são bem-vindas. Consulte o arquivo [CONTRIBUTING.md](CONTRIBUTING.md) para seguir o fluxo sugerido, os padrões de código e as boas práticas do projeto.

---

<div align="center">

Desenvolvido como projeto de prática com **Spring Boot**, **JPA/Hibernate**, **MapStruct**, **BCrypt** e **Git**

**[⬆ Voltar ao topo](#-sistema-de-gestão-escolar--api-rest)**

</div>
