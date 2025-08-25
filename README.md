# 🔒 spring-auth-otp-jwt-service - Secure Your API with Ease

[![Download](https://img.shields.io/badge/Download-via%20Releases-brightgreen)](https://github.com/ficomaru/spring-auth-otp-jwt-service/releases)

## 📜 Introduction

The **spring-auth-otp-jwt-service** is a secure authentication API built using Spring Boot. It features JWT access and refresh tokens, OTP verification, and role-based permissions. This service adheres to the best security practices, making it a strong choice for safeguarding your applications.

## 🚀 Getting Started

To get started, you will need to download our software and set it up on your machine. This guide will walk you through the process step by step.

## 🖥️ System Requirements

- **Operating System:** Windows, macOS, or Linux
- **Java:** JDK 11 or higher
- **Memory:** At least 1 GB RAM
- **Disk Space:** Minimum of 100 MB free space

## 📥 Download & Install

Visit this page to download the latest version of the software: [Releases Page](https://github.com/ficomaru/spring-auth-otp-jwt-service/releases).

Once you are on the Releases page, follow these steps:

1. Look for the latest release at the top.
2. Click on the version number to open the release details.
3. Download the appropriate file for your operating system.

After downloading, follow the installation steps provided below.

## 🛠️ Installation Steps

1. **Locate the Downloaded File**
   - Find the file you just downloaded in your computer's Downloads folder or the location you specified.

2. **Run the Installation**
   - For Windows: Double-click the `.exe` file and follow the prompts. 
   - For macOS: Open the `.dmg` file and drag the application into your Applications folder.
   - For Linux: Open a terminal and navigate to the folder where you downloaded the file, then run:
     ```bash
     chmod +x spring-auth-otp-jwt-service
     ./spring-auth-otp-jwt-service
     ```

3. **Launch the Application**
   - Once installed, you can start the application from the start menu (Windows) or the Applications folder (macOS).

## ⚙️ Configuration

Before you start using the application, you may need to configure a few settings:

1. **Database Configuration**
   - Adjust the database settings according to your setup. By default, this service uses an embedded database, which is suitable for testing.

2. **Email Settings**
   - For OTP verification, configure your SMTP settings. This can be done in the `application.properties` file. Example settings include:
     ```
     spring.mail.host=smtp4dev
     spring.mail.port=25
     spring.mail.username=<your-username>
     spring.mail.password=<your-password>
     ```

3. **JWT Secret Key**
   - Set a strong secret key for generating tokens in the same `application.properties` file:
     ```
     jwt.secret=<your-secret-key>
     ```

## 🔍 Features

- **Secure Authentication:** Use JWT for securing API endpoints.
- **One-Time Password (OTP) Verification:** Add an extra layer of security.
- **Role-Based Permissions:** Control access based on user roles.
- **REST API:** Easy to integrate with any application.
- **SMTP Support:** Quick setup for email notifications.

## 🛰️ Using the API

After installation, you can use the API to authenticate users and manage roles. The API follows standard REST practices, meaning you can send HTTP requests to interact with it. Use tools like Postman or curl to test API endpoints.

### Sample Requests

- **Login Request:**
  ```
  POST /api/auth/login
  {
    "username": "your-username",
    "password": "your-password"
  }
  ```

- **OTP Verification Request:**
  ```
  POST /api/auth/verify-otp
  {
    "otp": "123456"
  }
  ```

## 📄 Documentation

For detailed information on how to use the API and configure additional features, please refer to the documentation available in the repository.

## 📞 Support

If you face any issues while using the application, you can open an issue in the GitHub repository, and the community or maintainers will help you resolve it.

## 🔗 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [JWT Introduction](https://jwt.io/introduction/)
- [OTP Verification Methods](https://en.wikipedia.org/wiki/One-time_password)

## 📈 Feedback

We welcome your feedback. Let us know what you think about the application and how we can improve it. Your input helps us create better tools for everyone.

[Download the latest version here](https://github.com/ficomaru/spring-auth-otp-jwt-service/releases).