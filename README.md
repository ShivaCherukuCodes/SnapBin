# 📦 SnapBin - Secure File Sharing with Expiry & JWT Authentication

**SnapBin** is a lightweight and secure file-sharing application that allows users to upload files and share download links that auto-expire after a specified duration. Built with Spring Boot, MongoDB, and JWT-based authentication.

---

## 🚀 Features

- ✅ Upload and download files via REST APIs
- 🔒 JWT-based authentication (register + login)
- ⏱️ File expiry logic (customizable per upload)
- 🧹 Automatic cleanup of expired files (daily scheduler)
- 📦 MongoDB backend with file metadata storage
- 💡 Easy-to-use and extensible architecture

---

## 🛠️ Tech Stack

- Java 17+
- Spring Boot 3.x
- MongoDB (NoSQL)
- JWT (JSON Web Token) for authentication
- Maven / Gradle

---

## ⚙️ MongoDB Setup (Local or Cloud)

You can use:
- 🖥️ Local MongoDB instance (default port: `27017`)
- ☁️ MongoDB Atlas (free cloud database)

**Sample `application.properties`:**
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/snapbin
file.storage.local-path=uploads
file.access.base-url=http://localhost:8080
```

---

## 🔐 Auth API - JWT Enabled

### 📝 Register a new user

`POST /api/auth/register`

**Request Body:**
```json
{
  "username": "shivacodes",
  "email": "shiva@example.com",
  "password": "yourSecurePassword"
}
```

---

### 🔓 Login & Get JWT Token

`POST /api/auth/login`

**Request Body:**
```json
{
  "username": "shivacodes",
  "password": "yourSecurePassword"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR..."
}
```

Use this token in all **protected endpoints** as:
```
Authorization: Bearer <your_token_here>
```

---

## 📤 Upload File

`POST /api/files/upload`

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
```

**Form Data:**
- `file`: (binary file)
- `expiry`: Expiry time in minutes (default: 10)

**Example:**
```
curl -X POST http://localhost:8080/api/files/upload   -H "Authorization: Bearer <JWT_TOKEN>"   -F "file=@/path/to/image.png"   -F "expiry=30"
```

**Response:**
```
File uploaded successfully. Access it here: http://localhost:8080/api/files/download/<filename>
```

---

## 📥 Download File

`GET /api/files/download/{filename}`

**Example:**
```
curl -O http://localhost:8080/api/files/download/bf72322e_image.png
```

- ❌ Returns `File has expired` if expired
- ✅ Automatically sets content type (JPG, PNG, PDF, etc.)

---

## 🧹 File Expiry & Auto Cleanup

- Expiry is customizable at upload (in **minutes**).
- Files and metadata are:
  - Rejected if expired at download
  - Deleted automatically every day at `2 AM` (using Spring `@Scheduled`).

---

## 📁 Allowed File Types

- ✅ `.jpg`, `.jpeg`, `.png`
- ✅ `.pdf`
- ✅ `.txt`, `.docx`, `.xlsx`
- ✅ Other common binary files (`application/octet-stream` fallback)

*You can customize allowed types in the service logic.*

---

## 🧪 Test Manually

You can test cleanup by setting expiry to 1 minute and manually calling:

```java
@Scheduled(fixedRate = 60000) // every 1 min (for dev/testing only)
```

Or expose a temporary test endpoint to trigger cleanup manually.

---

## 💡 Roadmap

- [x] File expiry logic ✅
- [x] JWT security ✅
- [x] MongoDB integration ✅
- [x] Auto-clean expired files ✅
- [ ] AWS S3 Integration (Future)
- [ ] Web-based UI (React)

---

## 👨‍💻 Developer

**Shiva Cheruku**  
[GitHub](https://github.com/ShivaCherukuCodes) | Passionate Java + Spring Boot Developer 🚀

---

## 📄 License

This project is licensed under the MIT License.