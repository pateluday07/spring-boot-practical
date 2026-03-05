## 1. Why `employee.setIdCard(...)` is important
This method sets both sides of the relation:
- `employee.idCard = idCard`
- `idCard.employee = employee`

If we do not set both sides, JPA can persist incomplete relationship data (for example, `employee_id` may be null or association may not behave as expected).

## 2. Cascade PERSIST
When `CascadeType.PERSIST` is active on `Employee.idCard`, saving an `Employee` also saves its new `IdCard`.

## 3. Cascade REMOVE
When `CascadeType.REMOVE` is active (or included via `CascadeType.ALL`), deleting an `Employee` also deletes its linked `IdCard`.

## 4. `orphanRemoval = true`
If `employee.setIdCard(null)` is called, the old `IdCard` becomes an orphan and is deleted automatically.
