# Spring Boot JPA Many-to-Many - Practical Notes

This README is focused on the `Employee` <-> `Project` many-to-many mapping in this project.

## 1. What is Many-to-Many

A many-to-many relationship means:

- One `Employee` can work on multiple `Project`s.
- One `Project` can have multiple `Employee`s.

Example:

- Employee 1 works on Project 1 and Project 2.
- Project 1 has Employee 1 and Employee 2.

## 2. Database Design

Many-to-many needs 3 tables:

- `employee`
- `project`
- `employee_project`

The third table is called the join table.

```text
employee_project
----------------
employee_id
project_id
```

`employee_project.employee_id` points to `employee.id`.

`employee_project.project_id` points to `project.id`.

## 3. Project Entity

```java
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "projects")
    @JsonIgnore
    private Set<Employee> employees = new HashSet<>();
}
```

Important points:

- `Project` is the inverse side.
- `mappedBy = "projects"` means the relationship is already mapped in `Employee`.
- `@JsonIgnore` prevents infinite JSON recursion.

## 4. Employee Entity

```java
@ManyToMany
@JoinTable(
        name = "employee_project",
        joinColumns = @JoinColumn(name = "employee_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
)
private Set<Project> projects = new HashSet<>();
```

Important points:

- `Employee` is the owning side.
- `@JoinTable` tells Hibernate to create/use the join table.
- `joinColumns` represents the current entity: `employee_id`.
- `inverseJoinColumns` represents the other entity: `project_id`.
- `Set` helps avoid duplicate project assignments.

## 5. Helper Method in Employee

```java
public void addProject(Project project) {
    if (project == null) {
        return;
    }
    projects.add(project);
    project.getEmployees().add(this);
}
```

This method keeps both sides of the relationship in sync.

## 6. EmployeeDTO Design

The request should use project ids:

```java
private Set<Long> projectIds = new HashSet<>();
```

The response should return project details:

```java
private Set<Project> projects = new HashSet<>();
```

`projectIds` is write-only:

```java
@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
public Set<Long> getProjectIds() {
    return projectIds;
}
```

This means:

- `projectIds` can be sent in request JSON.
- `projectIds` will not be returned in response JSON.
- `projects` will be returned in response JSON.

## 7. EmployeeMapper

When converting `Employee` to `EmployeeDTO`, map projects into the response:

```java
employeeDTO.setProjects(employee.getProjects());
```

## 8. EmployeeService

In employee create/update flow:

```java
private void mapEmployeeToProjects(EmployeeDTO employeeDTO, Employee employee) {
    employeeDTO.getProjectIds().stream()
            .map(projectService::findById)
            .forEach(employee::addProject);
}
```

Flow:

- Get project ids from request.
- Load existing projects from database.
- Add projects to employee.
- Save employee.
- Hibernate inserts records into `employee_project`.

Projects are created separately. The employee request only connects existing projects with the employee.

## 9. Project Repository

```java
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
```

This gives basic CRUD methods for `Project`.

## 10. Project Service

Project service methods:

- `save(Project project)`
- `update(Project project)`
- `findById(Long id)`
- `findAll()`
- `deleteById(Long id)`

The controller talks to the service, and the service talks to the repository.

## 11. Project Controller

Project APIs:

```text
POST   /api/v1/projects
PUT    /api/v1/projects
GET    /api/v1/projects/{id}
GET    /api/v1/projects
DELETE /api/v1/projects/{id}
```

## 12. API Testing Flow

### Step 1: Create Project

```http
POST /api/v1/projects
```

```json
{
  "name": "Spring Boot App"
}
```

### Step 2: Create Another Project

```http
POST /api/v1/projects
```

```json
{
  "name": "CRM System"
}
```

### Step 3: Create Employee With Project IDs

```http
POST /api/v1/employees
```

```json
{
  "firstName": "Uday",
  "lastName": "Kumar",
  "email": "uday@test.com",
  "salary": 10000,
  "projectIds": [1, 2]
}
```

### Step 4: Get Employee

```http
GET /api/v1/employees/1
```

Expected response:

```json
{
  "employeeId": 1,
  "firstName": "Uday",
  "lastName": "Kumar",
  "email": "uday@test.com",
  "salary": 10000,
  "idCard": null,
  "addresses": [],
  "projects": [
    {
      "id": 1,
      "name": "Spring Boot App"
    },
    {
      "id": 2,
      "name": "CRM System"
    }
  ]
}
```

### Step 5: Update Employee Projects

Use the existing employee update API:

```http
PUT /api/v1/employees
```

```json
{
  "employeeId": 1,
  "firstName": "Uday",
  "lastName": "Kumar",
  "email": "uday@test.com",
  "salary": 10000,
  "projectIds": [1]
}
```

Now Employee 1 is linked only with Project 1.

## 13. Final Summary

- Many-to-many uses a join table.
- `Employee` owns the relationship.
- `Project` is the inverse side.
- `projectIds` are used in request.
- `projects` are shown in response.
- Hibernate manages the join table records.

## Helpful Links

[![YouTube](https://img.shields.io/badge/YouTube-ByteAndBeyondWithUday-red?logo=youtube&logoColor=white&style=flat-square)](https://www.youtube.com/@ByteAndBeyondWithUday)

[![Postman](https://img.shields.io/badge/Postman-Collection-orange?logo=postman&style=flat-square)](https://www.postman.com/planetary-water-884580/workspace/uday-s-public-workspace/folder/1581944-42e718e1-ae1a-410b-960d-0c4266b26fde?action=share&source=copy-link&creator=1581944)
