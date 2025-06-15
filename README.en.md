# Restaurant Management System (Backend)

This project is the backend for a restaurant management system, developed as part of an academic assignment. The API allows for managing customer orders, employees, menus, and generating sales reports.

---

## Technologies Used

* **Java 17**
* **Spring Boot 3.2.5**: Main framework for building the REST API.
* **Spring Data JPA**: For data persistence and communication with the database.
* **MySQL**: Relational database used.
* **Spring WebSockets**: For real-time communication (to notify about order updates).
* **Maven**: Dependency management and project build tool.
* **Lombok**: To reduce boilerplate code in models and DTOs.

---

## Main API Endpoints

* `POST /api/auth/login`: Authenticates an employee.
* `POST /api/registrar`: Registers a new employee (requires a master key).
* `GET /api/comandas`: Lists all orders with "OPEN" status.
* `POST /api/comandas`: Creates a new order.
* `PUT /api/comandas/fechar/{id}`: Closes an existing order.
* `GET /api/cardapio`: Lists all menu items.
* `GET /api/relatorio?data=YYYY-MM-DD`: Generates a sales report for the specified date.