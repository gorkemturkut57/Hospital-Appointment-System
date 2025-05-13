# 🏥 Hospital Appointment & Patient Tracking System

A **Spring Boot-based web application** designed to manage hospital appointments and patient follow-up. It allows patients to schedule appointments and doctors to manage them with detailed tracking.

---

## 🚀 Features

### 👤 User Roles

#### 🧑‍⚕️ Doctor

* Secure login
* View assigned appointments
* Accept or reject appointments
* Add notes to appointments

#### 🧑 Patient

* Register and login
* Book appointments
* View past appointments
* Access doctor notes

---

### 📅 Appointment System

* Appointment date & time selection
* Prevents double booking (conflict detection)
* Appointment status management:

  * **Pending**
  * **Approved**
  * **Rejected**

---

### 🗂️ Patient Follow-up

* Doctors can add notes to each appointment
* Patients can view previous appointments & notes

---

## 🛠️ Tech Stack

| Layer             | Technology      |
| ----------------- | --------------- |
| Backend Framework | Spring Boot     |
| Security          | Spring Security |
| ORM               | Spring Data JPA |
| Database          | MySQL           |
| View Template     | Thymeleaf       |
| UI Styling        | Bootstrap       |

---

## ⚙️ Installation & Setup

### ✅ Requirements

* Java 17+
* Maven
* MySQL Server

### 🧭 Setup Steps

```bash
# 1. Clone the repository
git clone [repo-url]

# 2. Create a MySQL database (e.g., hospital_db)

# 3. Configure database settings
# → Edit src/main/resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# 4. Build the project
mvn clean install

# 5. Run the application
mvn spring-boot:run
# or
java -jar target/hospital-system-0.0.1-SNAPSHOT.jar

# 6. Open in browser
http://localhost:8080
```

---

## 📁 Project Structure

```
src
├── controller        # Handles HTTP requests
├── model             # Entity classes mapped to DB tables
├── repository        # Data access layer (DAO)
├── service           # Business logic
├── security          # Authentication & authorization setup
└── resources
    └── templates     # Thymeleaf HTML views
```

---

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch

   ```bash
   git checkout -b feature/your-feature-name
   ```
3. Commit your changes

   ```bash
   git commit -m "Add your feature"
   ```
4. Push to your branch

   ```bash
   git push origin feature/your-feature-name
   ```
5. Open a Pull Request

---

## 📌 Future Improvements

* Admin panel for hospital management
* Notification system (email/SMS)
* Appointment reminders
* Multi-language support

---

## 📬 Contact

For questions or suggestions, feel free to reach out via \[[gorkemturkut@hotmail.com)].
