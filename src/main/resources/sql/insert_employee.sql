-- Idempotent employee seed data
INSERT INTO employee (first_name, last_name, email, salary)
SELECT 'John', 'Miller', 'john.miller@example.com', 95000.00
WHERE NOT EXISTS (
    SELECT 1 FROM employee WHERE email = 'john.miller@example.com'
);

INSERT INTO employee (first_name, last_name, email, salary)
SELECT 'Emma', 'Wilson', 'emma.wilson@example.com', 88000.00
WHERE NOT EXISTS (
    SELECT 1 FROM employee WHERE email = 'emma.wilson@example.com'
);

INSERT INTO employee (first_name, last_name, email, salary)
SELECT 'Liam', 'Anderson', 'liam.anderson@example.com', 91000.00
WHERE NOT EXISTS (
    SELECT 1 FROM employee WHERE email = 'liam.anderson@example.com'
);
