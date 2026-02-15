# Spring Boot JPA One-to-One - Practical Notes

This README is based on the `Employee` and `IdCard` entities in this project.

## 1) What is One-to-One

A One-to-One relationship means one row in table A is linked to exactly one row in table B.

In this project:
- One `Employee` has one `IdCard`.
- One `IdCard` belongs to one `Employee`.

## 2) Database Design

The project uses two tables:
- `employee`
- `id_card`

`id_card.employee_id` references `employee.id` and is unique, which enforces one-to-one.

## 3) Owning vs Inverse

`IdCard` is the owning side because it contains `@JoinColumn`.

```java
@OneToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "employee_id", nullable = false, unique = true)
private Employee employee;
```

`Employee` is the inverse side because it uses `mappedBy = "employee"`.

```java
@OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
private IdCard idCard;
```

## 4) mappedBy

`mappedBy = "employee"` tells JPA:
- Do not create another foreign key column on `employee`.
- The relationship is controlled by `IdCard.employee`.

## 5) Foreign key

Foreign key is:
- Column: `id_card.employee_id`
- References: `employee.id`
- Constraint behavior: `NOT NULL` + `UNIQUE`

This ensures each ID card is tied to exactly one employee, and an employee can have only one ID card.

## 6) Cascade

On `Employee` side:

```java
cascade = CascadeType.ALL
```

Effect:
- Persist employee -> id card is persisted.
- Merge/remove operations propagate to id card.

## 7) orphanRemoval

On `Employee` side:

```java
orphanRemoval = true
```

Effect:
- If `employee.setIdCard(null)` is called and saved, the old `id_card` row is deleted as an orphan.

## 8) LAZY vs EAGER

Both sides are configured as `FetchType.LAZY`.

Meaning:
- `Employee` loads without `IdCard` initially.
- `IdCard` loads without `Employee` initially.
- Related object loads only when accessed (inside active persistence context/session).

## 9) Show tables + insert sample

### Show table structure

```sql
USE your_database_name;

SHOW TABLES;
DESCRIBE employee;
DESCRIBE id_card;
```

### Insert sample data

Use existing files:
- `src/main/resources/sql/insert_employee.sql`
- `src/main/resources/sql/insert_id_card.sql`

Or run manually:

```sql
USE your_database_name;

INSERT INTO employee (first_name, last_name, email, salary)
VALUES ('John', 'Miller', 'john.miller@example.com', 95000.00);

INSERT INTO id_card (card_number, issue_date, expiry_date, employee_id)
VALUES ('EMP-1001', '2026-01-01', '2028-12-31',
        (SELECT id FROM employee WHERE email = 'john.miller@example.com'));
```

### Verify relationship

```sql
SELECT e.id, e.first_name, e.email, c.card_number
FROM employee e
LEFT JOIN id_card c ON c.employee_id = e.id;
```

### Helpful links

[![YouTube](https://img.shields.io/badge/YouTube-ByteAndBeyondWithUday-red?logo=youtube&logoColor=white&style=flat-square)](https://www.youtube.com/@ByteAndBeyondWithUday)

[![Postman](https://img.shields.io/badge/Postman-Collection-orange?logo=postman&style=flat-square)](https://www.postman.com/planetary-water-884580/workspace/uday-s-public-workspace/folder/1581944-1ba8e64f-4ff8-4f48-bad9-4c22e3fae205?action=share&source=copy-link&creator=1581944&ctx=documentation)