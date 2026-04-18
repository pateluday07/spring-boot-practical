# Spring Boot Transactional Atomicity Demo

This branch is focused on **transactional atomicity** using `Employee` and `IdCard`.

The demo shows:
- first database action succeeds (`Employee` save)
- second action fails (`IdCard` step)
- Spring rolls back the **entire** transaction

So either both entities are persisted, or none are.

## 1) Single demo API

Use one endpoint with a boolean request parameter:

```http
POST /api/v1/employees/transactional-demo?shouldFail=true
POST /api/v1/employees/transactional-demo?shouldFail=false
```

- `shouldFail=true` (default) -> throws exception after `Employee` save
- `shouldFail=false` -> saves both `Employee` and `IdCard`

## 2) What is demonstrated

The service method `createEmployeeAndIdCardForAtomicDemo(boolean shouldFail)` is `@Transactional` and does two steps:

1. Save `Employee` using `employeeRepository.save(...)`
2. Save `IdCard` using `idCardRepository.save(...)`

When `shouldFail=true`, an exception is intentionally thrown between these steps.
Because both steps are in one transaction, DB state is rolled back to the start of the method.

## 3) Why this is a strong atomicity example

This is clearer than a single cascade save for teaching:
- operations are visibly separate
- failure point is explicit
- rollback effect is easy to explain on camera

You can explain it as:
> "Even though Employee save happened first, the final transaction failed, so Employee is also rolled back."

## 4) Quick test flow for video

1. Call `POST /api/v1/employees/transactional-demo?shouldFail=false`
2. Verify record exists in `employee` and `id_card`
3. Call `POST /api/v1/employees/transactional-demo?shouldFail=true`
4. Verify no partial data was committed for failed request

## 5) SQL checks (optional for demo)

```sql
SELECT e.id, e.email FROM employee e ORDER BY e.id DESC;
SELECT c.id, c.card_number FROM id_card c ORDER BY c.id DESC;
```

For success mode, new rows appear in both tables with matching IDs.
For fail mode, no new partial row should remain.

## 6) Demo data note

The service currently uses fixed demo values:
- `firstName`: `Harry`
- `lastName`: `Potter`
- `email`: `harry@gmail.com`

Because `email` is unique, a second successful call may fail with conflict unless you delete/update that row first.

## 7) `@MapsId` context used in this branch

`IdCard` uses shared primary key mapping:

```java
@Id
private Long id;

@MapsId
@OneToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "id", nullable = false, unique = true)
private Employee employee;
```

This means `id_card.id` is both:
- primary key of `id_card`
- foreign key to `employee.id`

### Helpful links

[![YouTube](https://img.shields.io/badge/YouTube-ByteAndBeyondWithUday-red?logo=youtube&logoColor=white&style=flat-square)](https://www.youtube.com/@ByteAndBeyondWithUday)

[![Postman](https://img.shields.io/badge/Postman-Collection-orange?logo=postman&style=flat-square)](https://www.postman.com/planetary-water-884580/uday-s-public-workspace/folder/1581944-5479ea1d-631e-444b-8745-d1d2d8e2731e?action=share&source=copy-link&creator=1581944)