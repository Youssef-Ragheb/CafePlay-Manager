# CafePlay Manager

## Overview
CafePlay Manager is a management system for cafes and PlayStation areas.  
It allows you to manage table reservations, PlayStation game sessions, orders (drinks, snacks, and meals), and generate electronic invoices.  
The system also provides debt tracking for customers who regularly visit without immediate payment, allowing you to transfer table or PlayStation invoices to their debt account.  

## Features
- Manage PlayStation and table bookings.
- Add, update, and delete menu items (drinks, snacks, meals).
- Generate electronic invoices for orders and game sessions.
- Track customer debts and payments.
- Record daily expenses and income.
- View detailed analytics and reports on orders, expenses, and debts.
- Multi-user access with role-based permissions using Spring Security.

## Technology Stack
- **Backend:** Spring Boot (Java)
- **Security:** Spring Security with role-based authentication
- **Database:** MySQL
- **Build Tool:** Maven
- **Version Control:** Git & GitHub

## Installation
1. Clone the repository:
  git clone https://github.com/Youssef-Ragheb/CafePlay-Manager.git
2. Open the project in your preferred IDE (IntelliJ, Eclipse, etc.).
3. Configure the `application.properties` file with your MySQL credentials.
4. Run the project using Maven or your IDE's run configuration.

## Usage
- Log in with an **admin** account to manage menu items, game sessions, and user roles.
- Staff accounts can handle orders, table reservations, and record payments.
- The system automatically calculates total prices based on time and items ordered.

## Security
- Spring Security ensures only authorized users can access certain features.
- Passwords are encrypted using BCrypt before storing in the database.

## License
This project is licensed under the MIT License.
