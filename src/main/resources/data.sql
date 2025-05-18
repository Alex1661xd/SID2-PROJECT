
-- Clean up existing data
DELETE FROM GROUPS;
DELETE FROM SUBJECTS;
DELETE FROM PROGRAMS;
DELETE FROM AREAS;
DELETE FROM FACULTIES;
DELETE FROM EMPLOYEES;
DELETE FROM CONTRACT_TYPES;
DELETE FROM EMPLOYEE_TYPES;
DELETE FROM CAMPUSES;
DELETE FROM CITIES;
DELETE FROM DEPARTMENTS;
DELETE FROM COUNTRIES;

-- Insert Countries
INSERT INTO COUNTRIES (code, name) VALUES (1, 'Colombia');

-- Insert Departments
INSERT INTO DEPARTMENTS (code, name, country_code) VALUES
(1, 'Valle del Cauca', 1),
(2, 'Cundinamarca', 1),
(5, 'Antioquia', 1),
(8, 'Atlantico', 1),
(11, 'Bogota D.C.', 1);

-- Insert Cities
INSERT INTO CITIES (code, name, dept_code) VALUES
(101, 'Cali', 1),
(102, 'Bogota', 11),
(103, 'Medellin', 5),
(104, 'Barranquilla', 8),
(105, 'Barranquilla', 8);

-- Insert Campuses
INSERT INTO CAMPUSES (code, name, city_code) VALUES
(1, 'Campus Cali', 101),
(2, 'Campus Bogota', 102),
(3, 'Campus Medellin', 103),
(4, 'Campus Barranquilla', 104);

-- Insert Employee Types
INSERT INTO EMPLOYEE_TYPES (name) VALUES
('Docente'),
('Administrativo');

-- Insert Contract Types
INSERT INTO CONTRACT_TYPES (name) VALUES
('Planta'),
('Catedra');

-- Insert Faculties without dean_id to avoid circular FK constraint
INSERT INTO FACULTIES (code, name, location, phone_number, dean_id) VALUES
(1, 'Facultad de Ciencias Sociales', 'Cali', '555-1234', NULL),
(2, 'Facultad de Ingenieria', 'Cali', '555-5678', NULL);

-- Insert Employees
INSERT INTO EMPLOYEES (id, first_name, last_name, email, contract_type, employee_type, faculty_code, campus_code, birth_place_code) VALUES
('1001', 'Juan', 'Perez', 'juan.perez@univcali.edu.co', 'Planta', 'Docente', 1, 1, 101),
('1002', 'Maria', 'Gomez', 'maria.gomez@univcali.edu.co', 'Planta', 'Administrativo', 1, 2, 102),
('1003', 'Carlos', 'Lopez', 'carlos.lopez@univcali.edu.co', 'Catedra', 'Docente', 2, 1, 103),
('1004', 'Carlos', 'Mejia', 'carlos.mejia@univcali.edu.co', 'Planta', 'Docente', 1, 3, 103),
('1005', 'Sandra', 'Ortiz', 'sandra.ortiz@univcali.edu.co', 'Catedra', 'Docente', 2, 4, 104),
('1006', 'Julian', 'Reyes', 'julian.reyes@univcali.edu.co', 'Planta', 'Administrativo', 2, 1, 105);

-- Update Faculties to set dean_id after employees are inserted
UPDATE FACULTIES SET dean_id = '1001' WHERE code = 1;
UPDATE FACULTIES SET dean_id = '1002' WHERE code = 2;

-- Insert Areas
INSERT INTO AREAS (code, name, faculty_code, coordinator_id) VALUES
(1, 'Area de Ciencias Sociales', 1, '1001'),
(2, 'Area de Ingenieria', 2, '1003');

-- Insert Programs
INSERT INTO PROGRAMS (code, name, area_code) VALUES
(1, 'Psicologia', 1),
(2, 'Ingenieria de Sistemas', 2);

-- Insert Subjects
INSERT INTO SUBJECTS (code, name, program_code) VALUES
('S101', 'Psicologia General', 1),
('S102', 'Calculo I', 2),
('S103', 'Programacion', 2),
('S104', 'Estructuras de Datos', 2),
('S105', 'Bases de Datos', 2),
('S106', 'Redes de Computadores', 2),
('S107', 'Sistemas Operativos', 2),
('S108', 'Algoritmos Avanzados', 2);

-- Insert Groups
INSERT INTO GROUPS (number, semester, subject_code, professor_id) VALUES
(1, '2023-2', 'S101', '1001'),
(2, '2023-2', 'S102', '1003'),
(3, '2023-2', 'S103', '1004');
