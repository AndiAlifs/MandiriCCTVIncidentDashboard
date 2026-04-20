-- Seed branches
INSERT INTO branches (name, region, area_group) VALUES
  ('KCP Taman Ismail Marzuki',       'Jakarta',   'Jakarta Pusat'),
  ('KCP Jakarta Pamulang',           'Jakarta',   'Jakarta Selatan'),
  ('KC Jakarta Daan Mogot',          'Jakarta',   'Jakarta Barat'),
  ('KCP Tangerang Villa Melati Mas', 'Tangerang', 'Tangerang Selatan'),
  ('KC Jakarta Pondok Kelapa',       'Jakarta',   'Jakarta Timur'),
  ('KCP Jakarta Tomang',             'Jakarta',   'Jakarta Barat'),
  ('KCP Jakarta R.S. Pelni',         'Jakarta',   'Jakarta Barat'),
  ('KC Jakarta Pluit Selatan',       'Jakarta',   'Jakarta Utara'),
  ('KCP Jakarta Kelapa Gading',      'Jakarta',   'Jakarta Utara'),
  ('KCP Jakarta Graha Rekso',        'Jakarta',   'Jakarta Pusat');

-- Seed devices (one per branch for now)
INSERT INTO devices (branch_id, location, status, last_ping) VALUES
  (1,  'CAM 1 - Lounge Area', 'ONLINE',  NOW()),
  (1,  'CAM 2 - Teller Area', 'ONLINE',  NOW()),
  (2,  'CAM 2 - Teller Area', 'ONLINE',  NOW()),
  (3,  'CAM 1 - ATM Area',    'ONLINE',  NOW()),
  (4,  'CAM 3 - Lounge Area', 'OFFLINE', NOW() - INTERVAL '2 hours'),
  (5,  'CAM 2 - Teller Area', 'ONLINE',  NOW()),
  (6,  'CAM 1 - Lounge Area', 'ONLINE',  NOW()),
  (7,  'CAM 2 - Teller Area', 'ONLINE',  NOW()),
  (8,  'CAM 1 - ATM Area',    'ONLINE',  NOW()),
  (9,  'CAM 3 - Lounge Area', 'ONLINE',  NOW()),
  (10, 'CAM 2 - Teller Area', 'ONLINE',  NOW());

-- Seed default admin user (password: Admin@1234 — bcrypt encoded)
INSERT INTO users (username, password, role) VALUES
  ('admin', '$2b$12$7fYeYGSCSS4.TGUmu08d9.yC1tMeA1b7DrALTKGuSJBOmW4U8mrzK', 'ADMIN');

-- Seed sample incidents
INSERT INTO incidents (device_id, type, severity, status, description, detected_at, cleared_at) VALUES
  (1,  'FIRE_SMOKE',           'HIGH',   'RESOLVED', 'Smoke or fire appears in the building.',          NOW() - INTERVAL '3 days 12h 30m', NOW() - INTERVAL '3 days 12h 10m'),
  (3,  'UNAUTHORIZED_ACCESS',  'MEDIUM', 'RESOLVED', 'A customer has entered the Teller area.',         NOW() - INTERVAL '3 days 13h 30m', NOW() - INTERVAL '3 days 13h 18m'),
  (4,  'ATM_LOITERING',        'MEDIUM', 'RESOLVED', 'Someone is at the ATM for an extended period.',   NOW() - INTERVAL '3 days 14h 30m', NOW() - INTERVAL '3 days 14h 25m'),
  (5,  'AFTER_HOURS',          'LOW',    'RESOLVED', 'Detecting activity at a closed branch.',          NOW() - INTERVAL '3 days 5h 30m',  NOW() - INTERVAL '3 days 5h 27m'),
  (6,  'FIRE_SMOKE',           'HIGH',   'RESOLVED', 'Smoke or fire appears in the building.',          NOW() - INTERVAL '3 days 15h 50m', NOW() - INTERVAL '3 days 15h 38m'),
  (7,  'FIRE_SMOKE',           'HIGH',   'OPEN',     'Smoke or fire appears in the building.',          NOW() - INTERVAL '1 hour',         NULL),
  (8,  'UNAUTHORIZED_ACCESS',  'MEDIUM', 'OPEN',     'A customer has entered the Teller area.',         NOW() - INTERVAL '2 hours',        NULL),
  (9,  'ATM_LOITERING',        'MEDIUM', 'OPEN',     'Someone is at the ATM for an extended period.',   NOW() - INTERVAL '30 minutes',     NULL),
  (10, 'AFTER_HOURS',          'LOW',    'RESOLVED', 'Detecting activity at a closed branch.',          NOW() - INTERVAL '4 days 16h 20m', NOW() - INTERVAL '4 days 16h'),
  (11, 'FIRE_SMOKE',           'HIGH',   'RESOLVED', 'Smoke or fire appears in the building.',          NOW() - INTERVAL '4 days 16h 40m', NOW() - INTERVAL '4 days 16h 20m');
