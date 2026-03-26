# 🎓 Sistema de Gestão Escolar — API REST

> API REST desenvolvida com **Spring Boot 3** para gerenciar um mini-sistema escolar, contemplando estudantes, professores, cursos, disciplinas e matrículas.

<br>

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
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

Este projeto é uma **API REST** construída como prática de desenvolvimento com **Spring Boot**, **Git** e **GitHub**. O sistema simula o back-end de uma instituição de ensino, permitindo gerenciar o cadastro de pessoas, organização acadêmica e matrículas.

O projeto explora na prática conceitos como:

- Relacionamentos **OneToMany** e **ManyToOne** com JPA/Hibernate
- Herança entre entidades com estratégia `JOINED`
- Upload e armazenamento de imagens de perfil
- Criptografia de senhas com PBKDF2 + salt
- Tratamento global e centralizado de exceções
- Logging estruturado por arquivo e por nível
- Documentação automática com SpringDoc/OpenAPI

---

## 🏗 Arquitetura

O projeto segue o padrão em camadas tradicional do ecossistema Spring:

```
Controller  ──►  Service  ──►  Repository  ──►  Database
    │                │
    │         ManipuladorExcecoesGlobais
    │                │
    └──────  DTOs / Entities
```

| Camada | Responsabilidade |
|---|---|
| **Controller** | Receber requisições HTTP, validar entrada, delegar ao Service |
| **Service** | Regras de negócio, mapeamento DTO ↔ Entity, orquestração |
| **Repository** | Acesso ao banco via Spring Data JPA |
| **DTO** | Contrato da API — o que entra e o que sai |
| **Entity** | Mapeamento objeto-relacional com JPA |
| **GerenciamentoErros** | Tratamento centralizado de todas as exceções do sistema |

---

## ✨ Funcionalidades

### 👨‍🎓 Estudantes
- Cadastro completo com dados pessoais e número de matrícula
- Upload opcional de foto de perfil (multipart/form-data)
- Validação de CPF (formato e unicidade)
- Atualização e remoção

### 👨‍🏫 Professores
- Cadastro com especialidade acadêmica
- Upload opcional de foto de perfil (multipart/form-data)
- Validação de CPF (formato e unicidade)
- Atualização e remoção

### 📚 Cursos
- Cadastro com código único, nome e descrição
- Associação com múltiplas disciplinas (OneToMany)

### 📖 Disciplinas
- Associação obrigatória a um curso e a um professor
- Controle de carga horária (mínimo 20h, máximo 400h)
- Código único por disciplina

### 📝 Matrículas
- Vínculo entre estudante e disciplina por semestre
- Controle de frequência (0–100%) e nota final (0–10)
- Status da matrícula: `ATIVA`, `TRANCADA`, `CANCELADA`, `CONCLUIDA`
- Unicidade garantida por estudante + disciplina + semestre

---

## 🔌 Endpoints da API

### Estudantes — `/api/estudantes`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/estudantes` | Cadastrar estudante (JSON) |
| `POST` | `/api/estudantes` | Cadastrar estudante com foto (multipart) |
| `GET` | `/api/estudantes` | Listar todos os estudantes |
| `GET` | `/api/estudantes/{id}` | Buscar estudante por ID |
| `PUT` | `/api/estudantes/{id}` | Atualizar estudante (JSON) |
| `PUT` | `/api/estudantes/{id}` | Atualizar estudante com foto (multipart) |
| `DELETE` | `/api/estudantes/{id}` | Remover estudante |

### Professores — `/api/professores`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/professores` | Cadastrar professor (JSON) |
| `POST` | `/api/professores` | Cadastrar professor com foto (multipart) |
| `GET` | `/api/professores` | Listar todos os professores |
| `GET` | `/api/professores/{id}` | Buscar professor por ID |
| `PUT` | `/api/professores/{id}` | Atualizar professor (JSON) |
| `PUT` | `/api/professores/{id}` | Atualizar professor com foto (multipart) |
| `DELETE` | `/api/professores/{id}` | Remover professor |

### Cursos — `/api/cursos`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/cursos` | Criar curso |
| `GET` | `/api/cursos` | Listar todos os cursos |
| `GET` | `/api/cursos/{id}` | Buscar curso por ID |
| `PUT` | `/api/cursos/{id}` | Atualizar curso |
| `DELETE` | `/api/cursos/{id}` | Remover curso |

### Disciplinas — `/api/disciplinas`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/disciplinas` | Criar disciplina |
| `GET` | `/api/disciplinas` | Listar todas as disciplinas |
| `GET` | `/api/disciplinas/{id}` | Buscar disciplina por ID |
| `PUT` | `/api/disciplinas/{id}` | Atualizar disciplina |
| `DELETE` | `/api/disciplinas/{id}` | Remover disciplina |

### Matrículas — `/api/matriculas`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/matriculas` | Criar matrícula |
| `GET` | `/api/matriculas` | Listar todas as matrículas |
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
  │  Estudante │        │ curso (FK)   │
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

**Herança de Usuario:** `InheritanceType.JOINED` — a tabela `usuario` armazena os campos comuns; `estudante` e `professor` possuem tabelas próprias com suas colunas específicas e uma FK para `usuario`.

---

## ⚠️ Tratamento de Erros

Todas as exceções são interceptadas e tratadas de forma centralizada pela classe `ManipuladorExcecoesGlobais`, que retorna respostas padronizadas no seguinte formato:

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
| `400` | Validação de campos, JSON inválido, parâmetro de URL errado |
| `404` | Recurso ou rota não encontrada |
| `409` | CPF duplicado, violação de constraint única no banco |
| `413` | Imagem de perfil acima de 5 MB |
| `422` | Regra de negócio violada |
| `500` | Erro interno inesperado |

---

## 🔒 Segurança

As senhas **nunca são armazenadas em texto puro**. O sistema utiliza o algoritmo **PBKDF2 com HMAC-SHA256**, com salt aleatório de 16 bytes e 65.536 iterações, garantindo resistência a ataques de força bruta e rainbow tables.

```
senha em texto  ──►  [salt aleatório + PBKDF2/SHA256 x 65536]  ──►  "base64(salt):base64(hash)"
```

---

## 📊 Logging

O sistema possui três arquivos de log com rotação diária:

| Arquivo | Conteúdo |
|---|---|
| `logs/app.log` | Log geral da aplicação (INFO+) |
| `logs/error.log` | Apenas erros (ERROR) |
| `logs/api-access.log` | Todas as requisições HTTP com IP, método, rota, status e tempo de resposta |

Exemplo de linha no `api-access.log`:
```
2026-03-24 14:35:10 IP=127.0.0.1 METHOD=POST URI=/api/estudantes STATUS=201 TIME=87ms
```

O interceptor de acesso é registrado via `WebMvcConfigurer` e registra cada requisição automaticamente ao término, sem impacto nas respostas da API.

---

## 🧪 Testes Unitários

O projeto possui testes automatizados cobrindo as principais camadas da aplicação:

- **Services**: validação de regras de negócio, persistência e exceções esperadas
- **Controllers**: validação das rotas, status HTTP, retorno de JSON e tratamento de erros

Os testes foram escritos com **JUnit 5**, **Mockito** e **MockMvc**. Para executar tudo localmente:

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
CREATE DATABASE pratica_springboot_one_to_one;
```

### Variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto (já ignorado pelo `.gitignore`):

```env
DB_URL=jdbc:mysql://localhost:3306/pratica_springboot_one_to_one
DB_USERNAME=root
DB_PASSWORD=sua_senha
```

> ⚠️ Nunca comite credenciais no `application.properties`. Use variáveis de ambiente em todos os ambientes.

### Executando

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/Pratica_SpringBoot.git
cd Pratica_SpringBoot

# Execute com Maven Wrapper
./mvnw spring-boot:run
```

A aplicação sobe na porta `8080` por padrão.

---

## 📖 Documentação Swagger

Com a aplicação rodando, acesse a documentação interativa:

```
http://localhost:8080/swagger-ui/index.html
```

A documentação lista todos os endpoints, schemas de entrada e saída, e permite testar as requisições diretamente pelo navegador.

---

## 📁 Estrutura do Projeto

```
src/main/java/org/Pratica_SpringBoot/
│
├── Config/
│   ├── ConfigSwagger/        # Configuração do OpenAPI/Swagger
│   └── ConfigWebApp/         # Registro de interceptors (WebMvcConfigurer)
│
├── Controllers/              # Camada REST — recebe e responde requisições HTTP
│   ├── CursoController
│   ├── DisciplinaController
│   ├── EstudanteController
│   ├── MatriculaController
│   └── ProfessorController
│
├── Docs/
│   └── ProblemResponse       # Schema padronizado de resposta de erro
│
├── GerenciamentoErros/       # Tratamento centralizado de exceções
│   ├── ManipuladorExcecoesGlobais   # @RestControllerAdvice + CpfDuplicadoException (inner class)
│   └── RecursosNaoEncontradosException
│
├── Loggings/
│   └── InterceptorLoggingApi # Interceptor HTTP para log de acesso
│
├── Models/
│   ├── DTOs/                 # Objetos de transferência de dados (contrato da API)
│   │   ├── UsuarioDTO (abstrato)
│   │   ├── EstudanteDTO
│   │   ├── ProfessorDTO
│   │   ├── CursoDTO
│   │   ├── DisciplinaDTO
│   │   └── MatriculaDTO
│   ├── Entities/             # Entidades JPA (mapeamento objeto-relacional)
│   │   ├── Usuario (abstrato, JOINED)
│   │   ├── Estudante
│   │   ├── Professor
│   │   ├── Curso
│   │   ├── Disciplina
│   │   └── Matricula
│   └── Enums/
│       └── StatusMatricula   # ATIVA | TRANCADA | CANCELADA | CONCLUIDA
│
├── Repositories/             # Interfaces Spring Data JPA
│   ├── CursoRepository
│   ├── DisciplinaRepository
│   ├── EstudanteRepository
│   ├── MatriculaRepository
│   └── ProfessorRepository
│
└── Services/                 # Regras de negócio e orquestração
    ├── CursoService
    ├── DisciplinaService
    ├── EstudanteService
    ├── MatriculaService
    ├── ProfessorService
    ├── SenhaCriptografiaService      # PBKDF2 + salt
    └── UsuarioImagemStorageService   # Upload e persistência de imagens
```

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Consulte o arquivo [CONTRIBUTING.md](CONTRIBUTING.md) para entender o fluxo sugerido, padrões de código e boas práticas adotadas no projeto.

---

<div align="center">

Desenvolvido como projeto de prática com **Spring Boot**, **JPA** e **Git**

**[⬆ Voltar ao topo](#-sistema-de-gestão-escolar--api-rest)**

</div>