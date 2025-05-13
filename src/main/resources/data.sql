-- Roller ekleniyor (Eğer yoksa)
INSERT INTO roles (name) VALUES ('ADMIN') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO roles (name) VALUES ('DOCTOR') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO roles (name) VALUES ('PATIENT') ON DUPLICATE KEY UPDATE name=name;

-- Admin kullanıcısı ekleniyor (şifre: admin123)
INSERT INTO users (email, password, first_name, last_name)
SELECT 'admin@example.com', '$2a$10$vv9tMSiT0sXXODT9JvFEVeBdaQF0jiLcgjrAf8Kkx1VQwGzLbCWES', 'Admin', 'User'
FROM dual
WHERE NOT EXISTS (SELECT * FROM users WHERE email='admin@example.com');

-- Admin kullanıcısına ADMIN rolü atanıyor
INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.email = 'admin@example.com' AND r.name = 'ADMIN'
AND NOT EXISTS (
    SELECT * FROM users_roles ur 
    WHERE ur.user_id = u.id AND ur.role_id = r.id
);

-- Örnek doktor kullanıcısı ekleniyor (şifre: doctor123)
INSERT INTO users (email, password, first_name, last_name)
SELECT 'doctor@example.com', '$2a$10$1TrOWBs1Mnax5y9QsZhz2eWPUjYiH.0fRAa6YmyN6fbh.J37V4o6C', 'Örnek', 'Doktor'
FROM dual
WHERE NOT EXISTS (SELECT * FROM users WHERE email='doctor@example.com');

-- Doktor kullanıcısına DOCTOR rolü atanıyor
INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.email = 'doctor@example.com' AND r.name = 'DOCTOR'
AND NOT EXISTS (
    SELECT * FROM users_roles ur 
    WHERE ur.user_id = u.id AND ur.role_id = r.id
); 