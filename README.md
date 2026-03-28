# JPA DETACH — Practical Notes (`Employee` ↔ `IdCard`)

This branch is **only** about **DETACH** in JPA: what it means, how `entityManager.detach()` behaves, why updates can stop being saved, and how **`CascadeType.DETACH`** affects a parent–child graph.

---

## Why your entity changes might not save

If you change an entity in Spring Boot but nothing updates in the database, one common reason is that the instance is **no longer managed**—for example, after a **detach**.

**Takeaway**

- **Managed** entity → persistence context **tracks** changes; they can be flushed on commit.
- **Detached** entity → changes are **not** saved automatically; the provider is no longer managing that instance.

---

## What DETACH means

**Detach** removes an entity (and, when configured, its associations) from the **persistence context**. After that, JPA does not treat that object as part of the current unit of work, so ordinary dirty checking will not persist those changes.

---

## `entityManager.detach(entity)`

Calling `detach` on the `EntityManager` explicitly removes the given entity from the persistence context. Any pending changes on that instance are **not** written when the transaction commits **unless** you merge or otherwise re-attach the state appropriately.

---

## `CascadeType.DETACH` on the association

On the inverse side, `Employee` declares cascade including **`CascadeType.DETACH`**:

```java
@OneToOne(mappedBy = "employee",
        cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE, CascadeType.DETACH},
        orphanRemoval = true, fetch = FetchType.LAZY)
private IdCard idCard;
```

When a detach operation **cascades** along this mapping, detaching the **`Employee`** also detaches the related **`IdCard`** from the same persistence context (per the cascade rules).

---

## DETACH vs “Cascade DETACH”

| Idea | Meaning |
|------|--------|
| **Detach** | The operation: remove instance(s) from the persistence context (e.g. `entityManager.detach(employee)`). |
| **`CascadeType.DETACH`** | Configuration on the association: when detach is applied to the parent, **propagate** detach to the linked `IdCard` according to the mapping. |

So: **detach** is what you *do*; **`CascadeType.DETACH`** declares whether **child** entities in this relationship should follow when the **parent** is detached.

---

## Example in this project: `EmployeeServiceImpl.findById`

`findById` loads an `Employee`, updates fields on the employee and on the loaded `IdCard`, then calls **`entityManager.detach(employee)`** before mapping to a DTO. The mutations happen **while** the graph is still managed; **`detach`** runs **before** commit, so those changes are **not** flushed. The response still reflects the **in-memory** `-UPDATED` values on the detached objects, while the database rows stay unchanged—illustrating **managed vs detached** behavior and cascade detach on **`Employee` → `IdCard`**.

```java
@Override
public EmployeeDTO findById(Long id) {
    Optional<Employee> optionalEmployee = employeeRepository.findById(id);
    if(optionalEmployee.isEmpty()) {
        throw new ResourceNotFoundException(ErrorMessage.EMPLOYEE_NOT_FOUND.formatMessage(id));
    }
    Employee employee = optionalEmployee.get();

    // Demonstrate DETACH: mutate while managed, then detach — these changes are not persisted on flush.
    employee.setFirstName(employee.getFirstName().concat("-UPDATED"));

    // IdCard follows on cascade detach; its changes are not persisted either.
    IdCard idCard = employee.getIdCard();
    idCard.setCardNumber(idCard.getCardNumber().concat("-UPDATED"));

    entityManager.detach(employee);

    return employeeMapper.toDTO(employee);
}
```

---

## Video: *Why Your JPA Entity Changes Are Not Saving? (DETACH Explained)*

This repo pairs with a walkthrough that covers:

- What **DETACH** really means in JPA
- How **`entityManager.detach()`** works
- Why changes are **not** saved after detaching an entity
- What **`CascadeType.DETACH`** is
- How detaching a **parent** can detach the **child** when cascade is set
- The difference between **detach** and **cascade detach**
- A step-by-step **Employee → IdCard** example

[![YouTube — ByteAndBeyondWithUday](https://img.shields.io/badge/YouTube-ByteAndBeyondWithUday-red?logo=youtube&logoColor=white&style=flat-square)](https://www.youtube.com/@ByteAndBeyondWithUday)

[![Postman collection](https://img.shields.io/badge/Postman-Collection-orange?logo=postman&style=flat-square)](https://www.postman.com/planetary-water-884580/uday-s-public-workspace/folder/1581944-5479ea1d-631e-444b-8745-d1d2d8e2731e?action=share&source=copy-link&creator=1581944)
