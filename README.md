# LibroNova

# 📚 LibroNova – Library Management System

## 📌 Overview  
LibroNova is an internal Java SE desktop application designed to manage books, members (socios), users, and loans for a library network. Built with a clean layered architecture, it replaces error-prone spreadsheets and paper forms with a reliable, consistent, and traceable system using a simple GUI powered by `JOptionPane`.

The system enforces business rules (e.g., unique ISBNs, stock validation), supports role-based access (`ADMIN` / `ASISTENTE`), logs all operations, exports data to CSV, and uses JDBC transactions to ensure data integrity during loan and return operations.

---

## 🛠️ Requirements

Before running the application, ensure you have the following installed:

- **Java 17** or higher  
- **Apache Maven 3.6+**  
- **MySQL 8+** (or compatible)  
- A MySQL user with privileges to create and manage the `libronova` database

---

## ⚙️ Setup & Execution

### 1. Database Initialization
Run the provided SQL script to create the database and tables:

```sql
-- Execute libronova.sql in your MySQL client
SOURCE libronova.sql;

💡 The script creates the libronova database, tables (libros, socios, usuarios, prestamos), indexes, and inserts a default admin user. 

2. Configure Database Credentials
Edit the file:
src/main/resources/config.properties

properties


1
2
3
4
5
db.url=jdbc:mysql://localhost:3306/libronova?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=root
db.password=your_password_here
diasPrestamo=7
multaPorDia=1500
🔐 Note: In production, never store plain-text passwords. This is acceptable for academic purposes. 

3. Build and Run
From the project root directory:

bash


1
2
mvn clean compile
mvn exec:java -Dexec.mainClass="com.libronova.Main"
Or package and run:

bash


1
2
mvn package
java -cp target/libronova-1.0.jar com.libronova.Main
4. Login Credentials (Default)
After startup, use these credentials to log in:

Email: admin@libronova.com
Password: admin123
You can create additional users once logged in as ADMIN. 

🖼️ Screenshots
(Add actual screenshots here after running the app. Example:)

Login dialog
Book registration form
Catalog table output
📁 Place screenshots in a folder like screenshots/ and reference them if needed (optional for NetBeans local README). 

🧩 Architecture Diagrams
Class Diagram: Shows model, dao, service, controller, and util layers.
Use Case Diagram: Includes actors (ADMIN, ASISTENTE) and core use cases:
Manage Catalog
Manage Members
Manage Users
Process Loans & Returns
Export Data
📝 (You can generate these diagrams externally and attach them when submitting the ZIP or GitHub repo. For NetBeans local README, description is sufficient.) 

✅ Features Implemented
✅ Unique ISBN validation
✅ Book CRUD + filtering (author/category)
✅ Role-based login (ADMIN / ASISTENTE)
✅ Decorator pattern for user creation (default role, status, timestamp)
✅ JDBC transactions for loans/returns
✅ CSV export (libros_export.csv, prestamos_vencidos.csv)
✅ Logging via java.util.logging → app.log
✅ Custom exceptions (ISBNAlreadyExistsException, InsufficientStockException, etc.)
✅ Unit tests with JUnit 5
✅ JOptionPane-based GUI with formatted text tables
🧪 Testing
Run unit tests with:

bash


1
mvn test
Tests cover ISBN uniqueness, stock validation, and loan logic.

📬 Author Information
Name: Andersson Moises Vargas Anillo
Clan: caiman
Email: anderssoninedjas@gmail.com
Document ID: 1046692389
