## Many To Many Relationship: Employee And Project

## 1. Requirement

- Employee can have multiple projects
- Project can have multiple employees

## 2. Database Tables

- employee
- project
- employee_project

## 3. Project Entity

- Create `Project` entity
- Add `id`
- Add `name`
- Add `employees`
- Use `@ManyToMany(mappedBy = "projects")`
- Use `@JsonIgnore`

## 4. Employee Entity

- Add `projects`
- Use `@ManyToMany`
- Use `@JoinTable`
- Add join table name: `employee_project`
- Add join column: `employee_id`
- Add inverse join column: `project_id`
- Use `Set<Project>`

## 5. Employee Helper Method

- Add `addProject(Project project)`
- Add project to employee
- Add employee to project

## 6. EmployeeDTO

- Add `projectIds`
- Add `projects`
- Make `projectIds` write only
- Use `projectIds` in request
- Use `projects` in response

## 7. EmployeeMapper

- Map employee projects to DTO projects

## 8. EmployeeService

- Load projects using project ids
- Add projects to employee
- Save employee
- Update employee

## 9. Project Repository

- Create `ProjectRepository`
- Extend `JpaRepository<Project, Long>`

## 10. Project Service

- Create `ProjectService`
- Create `ProjectServiceImpl`
- Add save
- Add update
- Add find by id
- Add find all
- Add delete by id

## 11. Project Controller

- Create `ProjectController`
- Add create project API
- Add update project API
- Add get project by id API
- Add get all projects API
- Add delete project API

## 12. API Testing Sequence

- Create project
- Create another project
- Create employee with project ids
- Get employee
- Update employee project ids
- Get employee again

## 13. Final Summary

- Many to many uses join table
- Employee owns the relationship
- Project is inverse side
- `projectIds` are used in request
- `projects` are shown in response
- Hibernate manages join table records
