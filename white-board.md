# ✅ One-To-One Step-by-Step Flow

## 1. What is One-To-One
A One-To-One relationship means one record in one table is linked to exactly one record in another table.

## 2. Database design
Create two tables (`employee` and `id_card`) where each employee can have only one id card.

## 3. Owning side
The owning side is `IdCard` because it contains `@JoinColumn` and controls the foreign key value.

## 4. mappedBy
`mappedBy` marks the inverse side (`Employee`) and tells JPA that the relationship is managed by the owning side field.

## 5. Foreign key
The foreign key `id_card.employee_id` references `employee.id` and should be `UNIQUE` to enforce one-to-one.

## 6. Cascade
`CascadeType.ALL` lets operations on `Employee` automatically propagate to related `IdCard`.

## 7. orphanRemoval
`orphanRemoval = true` deletes the `IdCard` row when it is detached from its parent `Employee`.

## 8. LAZY vs EAGER
`LAZY` loads related data only when accessed, while `EAGER` loads it immediately with the parent entity.

## 9. Show table creation
Display generated schema (`employee` and `id_card`) to explain primary key, foreign key, and unique constraints.

## 10. Insert sample data
Insert sample `Employee` and `IdCard` data.
