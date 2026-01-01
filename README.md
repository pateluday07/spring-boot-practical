# Validation in Spring Boot — Video Companion

A compact companion for the YouTube video about validation using the code in this repository.

## Main endpoints (see `src/.../controller/EmployeeController.java`)

- POST   /api/v1/employees       — create (validates request)
- PUT    /api/v1/employees       — update (validates request)
- GET    /api/v1/employees/{id}  — find by id
- DELETE /api/v1/employees/{id}  — delete by id
- GET    /api/v1/employees       — list all

## Validation (from `src/.../dto/EmployeeDTO.java`)

Fields and annotations used in this project:

```java
@NotBlank
private String firstName;

@Email
private String email;

@DecimalMin("8000.00")
private BigDecimal salary;
```

Controller validates incoming DTOs using `@Valid` (see `save` method):

```java
public ResponseEntity<HttpStatus> save(@Valid @RequestBody EmployeeDTO employeeDTO) { ... }
```

## Helpful links

[![YouTube](https://img.shields.io/badge/YouTube-ByteAndBeyondWithUday-red?logo=youtube&logoColor=white&style=flat-square)](https://www.youtube.com/@ByteAndBeyondWithUday)

[![Postman](https://img.shields.io/badge/Postman-Collection-orange?logo=postman&style=flat-square)](https://www.postman.com/planetary-water-884580/uday-s-public-workspace/folder/1581944-5aea2edb-e798-4eec-96a3-c0b8b6e1ce1e?action=share&source=copy-link&creator=1581944&ctx=documentation)

<!-- Stylish video card: clickable thumbnail + badge -->
<div align="center">

[![Custom Validation in Spring Boot (thumbnail)](https://img.youtube.com/vi/7nKaBNFVP6s/0.jpg)](https://youtu.be/7nKaBNFVP6s)

<br/>

[![▶ Watch on YouTube](https://img.shields.io/badge/▶%20Watch%20Video-Custom%20Validation%20in%20Spring%20Boot-red?logo=youtube&style=for-the-badge)](https://youtu.be/7nKaBNFVP6s)

<br/>

<sub>Custom Validation in Spring Boot — companion video by ByteAndBeyondWithUday</sub>

</div>
