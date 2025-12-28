# 🏥 Hospital Management System

A robust Fullstack application designed to streamline hospital administration. This project features a **Spring Boot REST API** backend and a desktop frontend built with **JavaFX**, demonstrating a clear separation of concerns and modern development practices.

## 🚀 Key Features

- **Dynamic Scheduling:** Advanced logic for booking and managing medical appointments.
- **Comprehensive CRUD:** Full management (Create, Read, Update, Delete) for Patients and Doctors.
- **Pharmacy & Inventory:** Real-time stock control and medicine dispensing system.
- **Financial Module:** Automated invoice generation and tracking for hospital services.
- **RESTful Integration:** Backend designed to be consumed by multiple clients via REST API.

## 🛠️ Tech Stack

### Backend
- **Java 17** & **Spring Boot**
- **Spring Data JPA / Hibernate** (ORM)
- **MySQL** (Database)
- **Maven** (Dependency Management)

### Frontend (Desktop)
- **JavaFX** with a custom UI (ButtonFactory, Scene Management)
- **REST Client** integration to consume the Spring Boot API.

## 🏗️ Architecture
The project follows a **Client-Server architecture**:
1. **Server:** Spring Boot handles the business logic, database persistence, and exposes endpoints.
2. **Client:** The JavaFX application provides an intuitive interface for hospital staff, communicating with the server via JSON.

## 🔧 How to Run

1. **Database:** Create a MySQL database and update `application.properties`.
2. **Server:** Run `HospitalManagementApplication.java` to start the API.
3. **Client:** Run `Launcher.java` from the Frontend module.
