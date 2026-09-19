# EverGreen: Online Grocery Order Management System

**EverGreen** is a professional, web-based grocery management solution designed with a **Minimal Luxury** aesthetic. Built using the **Spring Boot** framework, the system addresses real-world inventory challenges like "Ghost Stock" through real-time synchronization and supports dynamic pricing for variable-weight items.

## Tech Stack

*   **Backend:** Java 17+, Spring Boot, Spring Data JPA.
*   **Frontend:** HTML5, CSS3 (Custom Glassmorphism), Bootstrap 5.
*   **Database:** MySQL (Managed via PHPMyAdmin).
*   **Version Control:** GitHub with professional branch protection rules.

## Key Features
*   **User Management:** Secure registration, login, and profile handling for Customers and Admins.
*   **Product & Inventory:** Real-time stock updates and stock reconciliation to prevent inventory mismatches.
*   **Order Processing:** Automatic total price calculation with support for actual weight input during packing.
*   **Payment Integration:** Management of transaction history and secure payment status tracking.
*   **Admin Dashboard:** Comprehensive tools for managing users, products, and generating performance reports.

## OOP Implementation
This project serves as a practical application of core **Object-Oriented Programming** principles:
*   **Encapsulation:** All model attributes (User, Product, Order) are private with public getters/setters to ensure data integrity.
*   **Inheritance & Polymorphism:** Used in service layers to handle different user roles and payment methods.
*   **Abstraction:** Implemented via Repository interfaces to separate business logic from data access.

## Project Structure
```text
src/main/java/lk/evergreen/grocery/
├── config/      # Database & Security configurations
├── controller/  # Web Request Handlers (Routes)
├── dto/         # Data Transfer Objects for secure data handling
├── entity/      # OOP Models (User, Product, Order, Payment)
├── exception/   # Custom error handling for the system
├── repository/  # Database Access Layer (Spring Data JPA)
├── service/     # Core Business Logic & Calculations
└── util/        # Utility classes (Helpers)

src/main/resources/
├── static/css      # Global CSS (style.css), JS, and Images
└── templates/admin  # HTML User Interfaces (User & Admin views)
```

## Getting Started
1.  **Clone the repository:** `git clone https://github.com/N-Amasha/evergreen-grocery-system.git`
2.  **Database Setup:** Import the provided SQL schema into PHPMyAdmin.
3.  **Run Application:** Execute `EvergreenApplication.java` via IntelliJ IDEA.
4.  **Access UI:** Navigate to `http://localhost:8080` in your browser.

