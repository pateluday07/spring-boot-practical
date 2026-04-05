# JPA REFRESH — Practical Notes (`Employee` ↔ `IdCard`)

This branch is **only** about **REFRESH** in JPA: what it means, how `entityManager.refresh()` behaves, why in-memory changes can disappear, and how **`CascadeType.REFRESH`** affects a parent–child graph.

---

## Why your in-memory changes might “vanish”

If you set fields on a managed entity but the API response (or later state) shows the **old** values from the database, one common reason is a **refresh**: the persistence provider **reloads** the entity from the database and **overwrites** the current instance with that state.

**Takeaway**

- **Managed** entity → the context can **flush** your changes on commit **unless** something replaces that state first (for example **`refresh`**).
- **`refresh`** → JPA **discards** pending in-memory values for that instance (per the spec/implementation) and **re-hydrates** it from the database; with **cascade refresh**, the linked **`IdCard`** can be refreshed too.

---

## What REFRESH means

**Refresh** tells the provider to **load the current row(s) from the database** into the given managed entity (and, when configured, its associations). After a refresh, the object usually reflects **what is stored**, not the edits you had only in memory.

---

## `entityManager.refresh(entity)`

Calling `refresh` on the `EntityManager` **reloads** the entity’s state from the database. Uncommitted changes that existed only on that instance are **not** what you keep after the operation—the instance is aligned with the **database snapshot** used for the refresh (still within the same persistence context and transaction rules).

---

## `CascadeType.REFRESH` on the association

On the inverse side, `Employee` declares cascade including **`CascadeType.REFRESH`**:

```java
@OneToOne(mappedBy = "employee",
        cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH},
        orphanRemoval = true, fetch = FetchType.LAZY)
private IdCard idCard;
```

When a refresh operation **cascades** along this mapping, refreshing the **`Employee`** also refreshes the related **`IdCard`** from the database (per the cascade rules), so both sides of the graph can be reset together.

---

## REFRESH vs “Cascade REFRESH”

| Idea | Meaning |
|------|--------|
| **Refresh** | The operation: reload entity state from the database (e.g. `entityManager.refresh(employee)`). |
| **`CascadeType.REFRESH`** | Configuration on the association: when refresh is applied to the parent, **propagate** refresh to the linked `IdCard` according to the mapping. |

So: **refresh** is what you *do*; **`CascadeType.REFRESH`** declares whether the **child** should be refreshed when the **parent** is refreshed.

---

## Example in this project: `EmployeeServiceImpl.findById`

`findById` loads an `Employee`, appends `-UPDATED` to the employee’s first name and to the loaded `IdCard`’s card number, then calls **`entityManager.refresh(employee)`** before mapping to a DTO. The refresh **reloads** both the employee and (via cascade) the id card from the database, so the **`-UPDATED` suffixes are lost** in the returned DTO—the response matches **persisted** data, illustrating **refresh** and **cascade refresh** on **`Employee` → `IdCard`**.

```java
@Override
public EmployeeDTO findById(Long id) {
    Optional<Employee> optionalEmployee = employeeRepository.findById(id);
    if(optionalEmployee.isEmpty()) {
        throw new ResourceNotFoundException(ErrorMessage.EMPLOYEE_NOT_FOUND.formatMessage(id));
    }
    Employee employee = optionalEmployee.get();

    // Demonstrate REFRESH behavior: changes made to the entity will be overwritten by the database state after refresh.
    employee.setFirstName(employee.getFirstName().concat("-UPDATED"));

    // Accessing the associated IdCard to demonstrate that it will be refreshed as well.
    IdCard idCard = employee.getIdCard();
    idCard.setCardNumber(idCard.getCardNumber().concat("-UPDATED"));

    entityManager.refresh(employee);

    return employeeMapper.toDTO(employee);
}
```

---

## Video: *JPA REFRESH Explained (`entityManager.refresh` & `CascadeType.REFRESH`)*

This repo pairs with a walkthrough that covers:

- What **REFRESH** really means in JPA
- How **`entityManager.refresh()`** works
- Why in-memory changes can **disappear** after a refresh
- What **`CascadeType.REFRESH`** is
- How refreshing a **parent** can refresh the **child** when cascade is set
- The difference between **refresh** and **cascade refresh**
- A step-by-step **Employee → IdCard** example

[![YouTube — ByteAndBeyondWithUday](https://img.shields.io/badge/YouTube-ByteAndBeyondWithUday-red?logo=youtube&logoColor=white&style=flat-square)](https://www.youtube.com/@ByteAndBeyondWithUday)

[![Postman collection](https://img.shields.io/badge/Postman-Collection-orange?logo=postman&style=flat-square)](https://www.postman.com/planetary-water-884580/uday-s-public-workspace/folder/1581944-5479ea1d-631e-444b-8745-d1d2d8e2731e?action=share&source=copy-link&creator=1581944)
