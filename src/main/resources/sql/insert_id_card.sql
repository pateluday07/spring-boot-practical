-- Idempotent id_card seed data
-- Run this after insert_employee.sql
INSERT INTO id_card (card_number, issue_date, expiry_date, employee_id)
SELECT 'EMP-1001', '2026-01-01', '2028-12-31', e.id
FROM employee e
WHERE e.email = 'john.miller@example.com'
  AND NOT EXISTS (
      SELECT 1 FROM id_card c WHERE c.card_number = 'EMP-1001'
  );

INSERT INTO id_card (card_number, issue_date, expiry_date, employee_id)
SELECT 'EMP-1002', '2026-01-10', '2028-12-31', e.id
FROM employee e
WHERE e.email = 'emma.wilson@example.com'
  AND NOT EXISTS (
      SELECT 1 FROM id_card c WHERE c.card_number = 'EMP-1002'
  );

INSERT INTO id_card (card_number, issue_date, expiry_date, employee_id)
SELECT 'EMP-1003', '2026-01-15', '2028-12-31', e.id
FROM employee e
WHERE e.email = 'liam.anderson@example.com'
  AND NOT EXISTS (
      SELECT 1 FROM id_card c WHERE c.card_number = 'EMP-1003'
  );
