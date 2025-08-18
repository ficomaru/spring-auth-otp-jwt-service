# Spring Boot JWT OTP Authentication API

**Secure authentication and authorization API built with Spring Boot**  
Includes **JWT access & refresh tokens**, **OTP verification**, **SMTP4dev for email testing**, and **role-based permissions** following security best practices.

![Spring Boot Authentication Banner](docs/images/banner.png)

---

## 🚀 Features

- **User Authentication** with **JWT** (access + refresh tokens)
- **OTP Verification** (One-Time Password for 2FA) sent via email
- **SMTP4dev Integration** for email testing in development
- **Role & Permission Management** (RBAC - Role-Based Access Control)
- **Secure Password Hashing** with BCrypt
- **Refresh Token Flow** for session renewal
- **Spring Security Integration** with custom filters
- **Database Persistence** with Spring Data JPA
- **Exception Handling** with standardized API responses

---

## 🛠 Technologies

- **Java 17+**
- **Spring Boot 3**
- **Spring Security**
- **Spring Data JPA**
- **JWT (JSON Web Token)**
- **BCrypt**
- **SMTP4dev (Docker)** for email testing
- **PostgreSQL**
- **Swagger/OpenApi**

---

## 📦 Installation

### 1️⃣ Clone the repository
```bash
git clone https://github.com/<your-username>/spring-boot-jwt-otp-auth.git
cd spring-boot-jwt-otp-auth
```
### 2️⃣ Run SMTP4dev with Docker
```bash
docker run --rm -p 5000:25 -p 5000:25 rnwood/smtp4dev
Web UI: http://localhost:5000
SMTP Server: localhost:25
```
### 3️⃣ Configure environment
Edit `application.yml` or `application.properties`
```bash
spring:
  mail:
    host: localhost
    port: 25
    username: ""
    password: ""
    properties:
      mail:
        smtp:
          auth: false
          starttls:
            enable: false

jwt:
  secret: your-secret-key
  expiration: 900 # seconds
```
### 4️⃣ Build and run
```bash
mvn clean install
mvn spring-boot:run
```


