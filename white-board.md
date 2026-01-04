## ✅ Step-by-Step Flow

### 1️⃣ Show the problem first
Call the APIs *without* any custom exception handling and observe the default error response.

---

### 2️⃣ Create custom exceptions and a common error response model
Define your own exception classes and a standard error response structure.

---

### 3️⃣ Add a global exception handler
Use `@RestControllerAdvice` to catch and return custom error responses.

---

### 4️⃣ Update the service layer
Throw your custom exceptions instead of generic exceptions.

---

### 5️⃣ Test everything again
Verify that the APIs now return clean and consistent error responses.
