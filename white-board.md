## ✅ Step-by-step Flow

### 1️⃣ Add validation dependency
Add `spring-boot-starter-validation` to enable input validation in Spring Boot.

---

### 2️⃣ Create `EmployeeDTO`
Define a DTO class and apply validation annotations on the required fields.

---

### 3️⃣ Create a mapper
Create a mapper to convert between `EmployeeDTO` and `Employee` to keep layers clean.

---

### 4️⃣ Update controller and service
Use `EmployeeDTO` in controller and service layers instead of exposing the entity directly.

---

### 5️⃣ Test the application
Send both valid and invalid requests to verify validation and DTO mapping.
