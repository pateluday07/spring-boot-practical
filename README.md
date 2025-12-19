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

[![Postman](https://img.shields.io/badge/Postman-Collection-orange?logo=postman&style=flat-square)](https://www.postman.com/planetary-water-884580/workspace/uday-s-public-workspace/folder/1581944-dd76519e-bc76-454b-a95e-da8a925cb2ea?action=share&source=copy-link&creator=1581944&ctx=documentation)

## Notes

- This README only references code already present in the repo.
- For full examples and error-handling (ControllerAdvice), see the project source files referenced above.
