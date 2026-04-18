# Spring Boot JPA `@MapsId` - Practical Notes

This project demonstrates a one-to-one mapping using a **shared primary key** with `@MapsId` between `Employee` and `IdCard`.

## 1) What `@MapsId` means here

`@MapsId` tells JPA that `IdCard` should reuse the primary key of `Employee`.

- Parent entity: `Employee` (`id` is generated with `IDENTITY`)
- Child entity: `IdCard` (`id` is not generated separately)
- Relationship key: `id_card.id` is both:
  - Primary key of `id_card`
  - Foreign key referencing `employee.id`

So there is no separate `employee_id` column in `id_card` for this mapping style.

## 2) Entity mapping in this project

`IdCard` (owning side, shared PK):

```java
@Id
private Long id;

@MapsId
@OneToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "id", nullable = false, unique = true)
private Employee employee;
```

`Employee` (inverse side):

```java
@OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
private IdCard idCard;
```

## 3) Persist behavior with `@MapsId`

When persisting, set both sides through `employee.setIdCard(idCard)` and save `Employee`.

Because of `@MapsId`:
- `Employee` is inserted first and gets generated `id`
- `IdCard.id` is automatically mapped to that same `Employee.id`
- `IdCard` row uses the same key value as its parent

## 4) Expected table shape

```sql
DESCRIBE employee;
DESCRIBE id_card;
```

You should see:
- `employee.id` as PK
- `id_card.id` as PK and FK to `employee.id`

## 5) Verify shared primary key data

```sql
SELECT e.id AS employee_id, c.id AS id_card_id, c.card_number
FROM employee e
LEFT JOIN id_card c ON c.id = e.id;
```

For linked rows, `employee_id` and `id_card_id` must be equal.

## 6) Notes for this codebase

- `cascade = CascadeType.ALL` allows persisting/removing `IdCard` through `Employee`
- `orphanRemoval = true` removes the old `IdCard` row when detached from `Employee`
- Both sides use `FetchType.LAZY`

### Helpful links

[![YouTube](https://img.shields.io/badge/YouTube-ByteAndBeyondWithUday-red?logo=youtube&logoColor=white&style=flat-square)](https://www.youtube.com/@ByteAndBeyondWithUday)

[![Postman](https://img.shields.io/badge/Postman-Collection-orange?logo=postman&style=flat-square)](https://www.postman.com/planetary-water-884580/uday-s-public-workspace/folder/1581944-5479ea1d-631e-444b-8745-d1d2d8e2731e?action=share&source=copy-link&creator=1581944)