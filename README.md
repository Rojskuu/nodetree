# Node Management System (Take Home Exam)

## Overview
This project is a **Spring Boot RESTful API** that manages a hierarchical tree of nodes.  
Nodes are organized in a parent–child relationship, forming a tree structure:
- **Root node**: the top-most node with no parent.
- **Parent node**: a node that has one or more children.
- **Child node**: a node that belongs to exactly one parent.
- **Leaf node**: a node with no children.

The application starts with a predefined root node and supports full node management operations.

---

## Functionalities
1. **Add nodes**
   - Given a node, add a child to that node.
2. **Delete nodes**
   - Given a node, delete a child of that node (soft delete with cascading).
3. **Move nodes**
   - Transfer a node from one parent to another.
4. **List downline nodes**
   - Return all descendants of a given node.
   - Example:
     - Children of `A` → `[B, C, D, E, F, G, H, I, J, K, L, M, N]`
     - Children of `D` → `[I, J, K, L, M, N]`
     - Children of `C` → `[]`

---

## Technology
- Java 17
- Spring Boot (4.0.2)
- Maven
- MySQL (or any RDBMS/NoSQL database)
- Supports both **JSON** and **XML** content types

---

## How to Run
1. Extract the provided zip file.  
2. Configure your database in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/nodetree
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
3. Access Swagger UI through this link : http://localhost:8080/swagger-ui/index.html