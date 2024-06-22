### README.md

```markdown
# tinyBank

## Overview

tinyBank is a simple web application designed to manage user registrations, logins, and basic financial transactions such as adding money, withdrawing money, and transferring money between users. The application is built using Spring Boot and Thymeleaf for the frontend.

## Features

- User registration with password and transaction password.
- User login.
- View account details including balance.
- Add money to account.
- Withdraw money from account.
- Transfer money to another user.
- View recent transaction history.
- Delete user account.

## Requirements

- Java 8 or later
- Maven
- A web browser

## Project Structure

```
tinyBank/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── tinyBank/
│   │   │       ├── web/
│   │   │       │   ├── controller/
│   │   │       │   │   └── TinyBankController.java
│   │   │       │   ├── model/
│   │   │       │   │   ├── History.java
│   │   │       │   │   ├── TransactionHistory.java
│   │   │       │   │   ├── TypeTransaction.java
│   │   │       │   │   └── User.java
│   │   │       │   └── TinyBankApplication.java
│   │   │       └── resources/
│   │   │           ├── static/
│   │   │           │   └── images/
│   │   │           │       ├── 352432_info_icon.png
│   │   │           │       ├── hidden-icon-eye.jpg
│   │   │           │       └── vecteezy_eye-icon-hidden-icon-visible-invisible-icon-look-and_22782485.jpg
│   │   │           ├── templates/
│   │   │           │   ├── index.html
│   │   │           │   ├── register.html
│   │   │           │   ├── registration_success.html
│   │   │           │   ├── user_data.html
│   │   │           │   └── fragments/
│   │   │           │       └── transactionHistory.html
│   │   │           └── application.properties
│   │   └── resources/
│   └── test/
│       └── java/
└── pom.xml
```

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/yourusername/tinyBank.git
cd tinyBank
```

### Build the Project

Use Maven to build the project:

```bash
mvn clean install
```

### Run the Application

Run the application using Maven:

```bash
mvn spring-boot:run
```

Alternatively, you can run the application by executing the generated JAR file:

```bash
java -jar target/tinyBank-0.0.1-SNAPSHOT.jar
```

### Access the Application

Open your web browser and navigate to:

```
http://localhost:8080
```

### Usage

1. **Register a New User**:
    - Navigate to the registration page by clicking on the "Register New User" button on the login page.
    - Fill in the required fields including name, ID, password, confirm password, and transaction password. Click "Register".

2. **Login**:
    - Use your registered ID and password to log in.

3. **View Account Details**:
    - After logging in, you will be redirected to the account details page where you can see your balance and perform various actions.

4. **Add Money**:
    - Click on "Add Money", enter the amount and your password, then click "Add Money".

5. **Withdraw Money**:
    - Click on "Withdraw Money", enter the amount and your password, then click "Withdraw Money".

6. **Transfer Money**:
    - Click on "Transfer to Another User", enter the recipient's ID, amount, and your password, then click "Transfer".

7. **View Recent History**:
    - Click on "View Recent History" to see your recent transactions.

8. **Delete User**:
    - Click on "Delete User", enter your password, and confirm to delete your account.

9. **Logout**:
    - Click on "Logout & Home" to log out and return to the home page.

## Error Handling

If an error occurs during any transaction, an error modal will be displayed with the appropriate message.