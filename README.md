# Spring Boot JPA One-to-Many - Practical Notes

This README is focused on the `Employee` -> `Address` One-to-Many mapping in this project (`spring_boot_practical`, MySQL).

## 1) What is One-to-Many

A One-to-Many relationship means:
- One row in parent table can be linked to many rows in child table.
- Each child row belongs to exactly one parent row.

In this project:
- One `Employee` can have multiple `Address` records.
- One `Address` belongs to one `Employee`.

## 2) Database Design

The relevant tables are:
- `employee` (parent)
- `address` (child)

Foreign key:
- `address.employee_id` references `employee.id`
- `NOT NULL` on `employee_id` ensures every address is tied to an employee

Unlike One-to-One, `employee_id` is **not unique** here, so many addresses can reference the same employee.

## 3) Entity Mapping

### Parent side (`Employee`)

```java
@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Address> addresses = new ArrayList<>();
```

### Child side (`Address`)

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "employee_id", nullable = false)
private Employee employee;
```

## 4) Owning vs Inverse Side

- `Address` is the owning side because it has `@JoinColumn`.
- `Employee` is the inverse side because it uses `mappedBy = "employee"`.

`mappedBy` prevents JPA from creating an extra join table or duplicate foreign key mapping.

## 5) Helper Methods in `Employee`

The `Employee` entity includes helper methods to keep both sides in sync:
- `addAddress(Address address)` adds address and sets `address.setEmployee(this)`
- `setAddresses(List<Address> addresses)` clears old list and re-attaches new items through `addAddress(...)`

This is important to avoid incomplete relationship state during persist/merge.

## 6) Cascade and Orphan Removal

Configured on parent side:

```java
cascade = CascadeType.ALL
orphanRemoval = true
```

Effect:
- Save employee -> addresses are saved automatically.
- Delete employee -> addresses are deleted automatically.
- Remove an address from `employee.getAddresses()` and save -> that address row is deleted as orphan.

## 7) Fetch Strategy

`Address -> Employee` is `LAZY`:
- Loading an address does not immediately load employee.
- Employee is loaded only when accessed inside an active persistence context.

## Helpful Links

[![YouTube](https://img.shields.io/badge/YouTube-ByteAndBeyondWithUday-red?logo=youtube&logoColor=white&style=flat-square)](https://www.youtube.com/@ByteAndBeyondWithUday)

[![Postman](https://img.shields.io/badge/Postman-Collection-orange?logo=postman&style=flat-square)](https://www.postman.com/planetary-water-884580/workspace/uday-s-public-workspace/folder/1581944-42e718e1-ae1a-410b-960d-0c4266b26fde?action=share&source=copy-link&creator=1581944)