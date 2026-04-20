-- Seed branches
INSERT INTO branches (name, region, area_group) VALUES ('KCP Taman Ismail Marzuki',       'Jakarta',   'Jakarta Pusat');
INSERT INTO branches (name, region, area_group) VALUES ('KCP Jakarta Pamulang',           'Jakarta',   'Jakarta Selatan');
INSERT INTO branches (name, region, area_group) VALUES ('KC Jakarta Daan Mogot',          'Jakarta',   'Jakarta Barat');
INSERT INTO branches (name, region, area_group) VALUES ('KCP Tangerang Villa Melati Mas', 'Tangerang', 'Tangerang Selatan');
INSERT INTO branches (name, region, area_group) VALUES ('KC Jakarta Pondok Kelapa',       'Jakarta',   'Jakarta Timur');
INSERT INTO branches (name, region, area_group) VALUES ('KCP Jakarta Tomang',             'Jakarta',   'Jakarta Barat');
INSERT INTO branches (name, region, area_group) VALUES ('KCP Jakarta R.S. Pelni',         'Jakarta',   'Jakarta Barat');
INSERT INTO branches (name, region, area_group) VALUES ('KC Jakarta Pluit Selatan',       'Jakarta',   'Jakarta Utara');
INSERT INTO branches (name, region, area_group) VALUES ('KCP Jakarta Kelapa Gading',      'Jakarta',   'Jakarta Utara');
INSERT INTO branches (name, region, area_group) VALUES ('KCP Jakarta Graha Rekso',        'Jakarta',   'Jakarta Pusat');

-- Seed devices
INSERT INTO devices (branch_id, location, status, last_ping) VALUES (1,  'CAM 1 - Lounge Area', 'ONLINE',  SYSTIMESTAMP);
INSERT INTO devices (branch_id, location, status, last_ping) VALUES (1,  'CAM 2 - Teller Area', 'ONLINE',  SYSTIMESTAMP);
INSERT INTO devices (branch_id, location, status, last_ping) VALUES (2,  'CAM 2 - Teller Area', 'ONLINE',  SYSTIMESTAMP);
INSERT INTO devices (branch_id, location, status, last_ping) VALUES (3,  'CAM 1 - ATM Area',    'ONLINE',  SYSTIMESTAMP);
INSERT INTO devices (branch_id, location, status, last_ping) VALUES (4,  'CAM 3 - Lounge Area', 'OFFLINE', SYSTIMESTAMP - INTERVAL '2' HOUR);
INSERT INTO devices (branch_id, location, status, last_ping) VALUES (5,  'CAM 2 - Teller Area', 'ONLINE',  SYSTIMESTAMP);
INSERT INTO devices (branch_id, location, status, last_ping) VALUES (6,  'CAM 1 - Lounge Area', 'ONLINE',  SYSTIMESTAMP);
INSERT INTO devices (branch_id, location, status, last_ping) VALUES (7,  'CAM 2 - Teller Area', 'ONLINE',  SYSTIMESTAMP);
INSERT INTO devices (branch_id, location, status, last_ping) VALUES (8,  'CAM 1 - ATM Area',    'ONLINE',  SYSTIMESTAMP);
INSERT INTO devices (branch_id, location, status, last_ping) VALUES (9,  'CAM 3 - Lounge Area', 'ONLINE',  SYSTIMESTAMP);
INSERT INTO devices (branch_id, location, status, last_ping) VALUES (10, 'CAM 2 - Teller Area', 'ONLINE',  SYSTIMESTAMP);

-- Seed default admin user (password: Admin@1234 — bcrypt encoded)
INSERT INTO users (username, password, role)
VALUES ('admin', '$2a$12$XdlJrk9h5w6Kd9KQ9lFKP.uZJq2bL1VZXFk5wKb7lRxYQ2hV/PJqC', 'ADMIN');

-- Seed sample incidents
INSERT INTO incidents (device_id, type, severity, status, description, detected_at, cleared_at)
VALUES (1,  'FIRE_SMOKE',          'HIGH',   'RESOLVED', 'Smoke or fire appears in the building.',        SYSTIMESTAMP - INTERVAL '3' DAY - INTERVAL '12' HOUR - INTERVAL '30' MINUTE, SYSTIMESTAMP - INTERVAL '3' DAY - INTERVAL '12' HOUR - INTERVAL '10' MINUTE);
INSERT INTO incidents (device_id, type, severity, status, description, detected_at, cleared_at)
VALUES (3,  'UNAUTHORIZED_ACCESS', 'MEDIUM', 'RESOLVED', 'A customer has entered the Teller area.',       SYSTIMESTAMP - INTERVAL '3' DAY - INTERVAL '13' HOUR - INTERVAL '30' MINUTE, SYSTIMESTAMP - INTERVAL '3' DAY - INTERVAL '13' HOUR - INTERVAL '18' MINUTE);
INSERT INTO incidents (device_id, type, severity, status, description, detected_at, cleared_at)
VALUES (4,  'ATM_LOITERING',       'MEDIUM', 'RESOLVED', 'Someone is at the ATM for an extended period.', SYSTIMESTAMP - INTERVAL '3' DAY - INTERVAL '14' HOUR - INTERVAL '30' MINUTE, SYSTIMESTAMP - INTERVAL '3' DAY - INTERVAL '14' HOUR - INTERVAL '25' MINUTE);
INSERT INTO incidents (device_id, type, severity, status, description, detected_at, cleared_at)
VALUES (5,  'AFTER_HOURS',         'LOW',    'RESOLVED', 'Detecting activity at a closed branch.',        SYSTIMESTAMP - INTERVAL '3' DAY - INTERVAL '5' HOUR - INTERVAL '30' MINUTE,  SYSTIMESTAMP - INTERVAL '3' DAY - INTERVAL '5' HOUR - INTERVAL '27' MINUTE);
INSERT INTO incidents (device_id, type, severity, status, description, detected_at, cleared_at)
VALUES (6,  'FIRE_SMOKE',          'HIGH',   'RESOLVED', 'Smoke or fire appears in the building.',        SYSTIMESTAMP - INTERVAL '3' DAY - INTERVAL '15' HOUR - INTERVAL '50' MINUTE, SYSTIMESTAMP - INTERVAL '3' DAY - INTERVAL '15' HOUR - INTERVAL '38' MINUTE);
INSERT INTO incidents (device_id, type, severity, status, description, detected_at, cleared_at)
VALUES (7,  'FIRE_SMOKE',          'HIGH',   'OPEN',     'Smoke or fire appears in the building.',        SYSTIMESTAMP - INTERVAL '1' HOUR, NULL);
INSERT INTO incidents (device_id, type, severity, status, description, detected_at, cleared_at)
VALUES (8,  'UNAUTHORIZED_ACCESS', 'MEDIUM', 'OPEN',     'A customer has entered the Teller area.',       SYSTIMESTAMP - INTERVAL '2' HOUR, NULL);
INSERT INTO incidents (device_id, type, severity, status, description, detected_at, cleared_at)
VALUES (9,  'ATM_LOITERING',       'MEDIUM', 'OPEN',     'Someone is at the ATM for an extended period.', SYSTIMESTAMP - INTERVAL '30' MINUTE, NULL);
INSERT INTO incidents (device_id, type, severity, status, description, detected_at, cleared_at)
VALUES (10, 'AFTER_HOURS',         'LOW',    'RESOLVED', 'Detecting activity at a closed branch.',        SYSTIMESTAMP - INTERVAL '4' DAY - INTERVAL '16' HOUR - INTERVAL '20' MINUTE, SYSTIMESTAMP - INTERVAL '4' DAY - INTERVAL '16' HOUR);
INSERT INTO incidents (device_id, type, severity, status, description, detected_at, cleared_at)
VALUES (11, 'FIRE_SMOKE',          'HIGH',   'RESOLVED', 'Smoke or fire appears in the building.',        SYSTIMESTAMP - INTERVAL '4' DAY - INTERVAL '16' HOUR - INTERVAL '40' MINUTE, SYSTIMESTAMP - INTERVAL '4' DAY - INTERVAL '16' HOUR - INTERVAL '20' MINUTE);
