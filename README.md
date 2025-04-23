# Final Project – CS-601-01

## Overview

This project is a full-stack web application developed as part of the CS-601-01 course. It includes both Release 1 and Release 2, with functionality ranging from user registration and login to dynamic review management and frontend validation.

---

## 🔹 Release 1 – Core Functionality

### Features:
- User registration and login
- Redirect to `/register` if a user already exists
- Server setup using Jetty and Java Servlets
- Dynamic HTML rendering using Thymeleaf
- Modular code with table creation logic encapsulated in a helper method

---

## 🔹 Release 2 – Feature Enhancements

### Enhancements:
- Frontend password validation using JavaScript
- Display of average rating rounded to two decimal places using `String.format("%.2f")`
- New user reviews appear at the top of the list
- Shared Thymeleaf configuration and renderer created once in `JettyHotelServer`
  - Passed as an attribute in `ServletContextHandler` for servlet access
- Cleaner and more maintainable structure with consistent coding practices

🚀 How to Run the Project

1. Clone the Repository
git clone https://github.com/ruchamaslekar/WanderVista.git

2. Set Up the Project
Make sure you have Java (JDK 11+) and a compatible IDE like IntelliJ IDEA or Eclipse.

3. Run the Jetty Server
java -cp . JettyHotelServer

4. Access the App
http://localhost:8080/
