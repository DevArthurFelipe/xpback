# Sistema de Gerenciamento de Restaurante (Backend)

Este projeto é o backend de um sistema de gerenciamento para restaurantes, desenvolvido como parte de um trabalho acadêmico. A API permite gerenciar comandas, funcionários, cardápio e gerar relatórios de vendas.

---

## Tecnologias Utilizadas

* **Java 17**
* **Spring Boot 3.2.5**: Framework principal para a construção da API REST.
* **Spring Data JPA**: Para persistência de dados e comunicação com o banco de dados.
* **MySQL**: Banco de dados relacional utilizado.
* **Spring WebSockets**: Para comunicação em tempo real (notificar sobre atualização de comandas).
* **Maven**: Gerenciador de dependências e build do projeto.
* **Lombok**: Para reduzir código boilerplate em modelos e DTOs.

---

## Principais Endpoints da API

* `POST /api/auth/login`: Autentica um funcionário.
* `POST /api/registrar`: Registra um novo funcionário (requer chave mestra).
* `GET /api/comandas`: Lista todas as comandas com status "ABERTA".
* `POST /api/comandas`: Cria uma nova comanda.
* `PUT /api/comandas/fechar/{id}`: Fecha uma comanda existente.
* `GET /api/cardapio`: Lista todos os itens do cardápio.
* `GET /api/relatorio?data=AAAA-MM-DD`: Gera um relatório de vendas para a data especificada.