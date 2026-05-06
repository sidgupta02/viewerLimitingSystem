# 🎬 Viewer Limiter System (Netflix-style)

A backend system to restrict simultaneous device logins per user.

## 🚀 Features
- User Registration
- Login with device tracking
- Device limit enforcement
- Session management
- Logout functionality
- Session expiry (auto cleanup)

## 🛠️ Tech Stack
- Java
- Spring Boot
- PostgreSQL
- JPA / Hibernate

## 📌 How it works
- Each login creates a session
- Active sessions are counted
- If limit reached → login blocked
- Sessions expire after fixed time
- Logout marks session inactive

## 🔮 Future Improvements
- JWT Authentication
- Redis for session storage
- Distributed system support
- Replace oldest session logic
- Email verification
