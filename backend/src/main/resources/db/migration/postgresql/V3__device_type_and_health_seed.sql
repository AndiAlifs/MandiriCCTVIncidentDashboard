-- Add device type and simulation tracking columns
ALTER TABLE devices ADD COLUMN device_type VARCHAR(10) NOT NULL DEFAULT 'CCTV'
    CONSTRAINT chk_device_type CHECK (device_type IN ('NVR', 'CCTV'));

ALTER TABLE devices ADD COLUMN simulated_at TIMESTAMP;
ALTER TABLE incidents ADD COLUMN simulated_at TIMESTAMP;

-- Add additional branches for health monitoring coverage
INSERT INTO branches (name, region, area_group) VALUES
  ('KCP Ciledug Tangerang',    'Tangerang', 'Tangerang Kota'),
  ('KCP Jakarta Taman Surya',  'Jakarta',   'Jakarta Barat'),
  ('KC Jakarta Cengkareng',    'Jakarta',   'Jakarta Barat'),
  ('KC Jakarta Kebayoran Baru','Jakarta',   'Jakarta Selatan'),
  ('KC Jakarta Senen',         'Jakarta',   'Jakarta Pusat'),
  ('KCP Plaza Semanggi',       'Jakarta',   'Jakarta Selatan'),
  ('KCP Senayan City',         'Jakarta',   'Jakarta Selatan'),
  ('KCP AEON Mall BSD City',   'Tangerang', 'Tangerang Selatan'),
  ('KCP Kebon Jeruk',          'Jakarta',   'Jakarta Barat'),
  ('KCP Pondok Gede',          'Jakarta',   'Jakarta Timur');

-- Promote existing devices 1 & 2 to NVR (branch 1: KCP Taman Ismail Marzuki)
UPDATE devices SET device_type = 'NVR', ip_address = '192.168.0.45' WHERE id = 1;
UPDATE devices SET device_type = 'NVR', ip_address = '172.16.254.3' WHERE id = 2;

-- Add NVR devices for new branches (OFFLINE - recent, within 60 min)
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES
  (11, '203.0.113.9',   'NVR Main Unit', 'NVR', 'OFFLINE', NOW() - INTERVAL '50 minutes'),
  (12, '198.51.100.27', 'NVR Main Unit', 'NVR', 'OFFLINE', NOW() - INTERVAL '55 minutes'),
  (13, '10.20.30.40',   'NVR Main Unit', 'NVR', 'ONLINE',  NOW() - INTERVAL '5 minutes'),
  (14, '10.20.30.41',   'NVR Main Unit', 'NVR', 'ONLINE',  NOW() - INTERVAL '3 minutes');

-- Set existing NVR-promoted devices to OFFLINE (recent issues)
UPDATE devices SET status = 'OFFLINE', last_ping = NOW() - INTERVAL '2 minutes'  WHERE id = 1;
UPDATE devices SET status = 'OFFLINE', last_ping = NOW() - INTERVAL '40 minutes' WHERE id = 2;

-- Add NVR unresolved (offline > 1 day)
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES
  (1,  '10.125.1.220', 'NVR Backup Unit', 'NVR', 'OFFLINE', NOW() - INTERVAL '3 days 2 hours'),
  (15, '10.125.1.211', 'NVR Main Unit',   'NVR', 'OFFLINE', NOW() - INTERVAL '2 days 1 hour'),
  (16, '10.125.1.212', 'NVR Main Unit',   'NVR', 'OFFLINE', NOW() - INTERVAL '2 days 3 hours'),
  (17, '10.125.1.213', 'NVR Main Unit',   'NVR', 'OFFLINE', NOW() - INTERVAL '1 day 4 hours');

-- Add CCTV offline devices (recent, within 60 min)
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES
  (13, '192.168.45.12',  'CAM 1 - Main Hall',   'CCTV', 'OFFLINE', NOW() - INTERVAL '5 minutes'),
  (14, '172.16.254.1',   'CAM 1 - Lobby',       'CCTV', 'OFFLINE', NOW() - INTERVAL '10 minutes'),
  (15, '10.0.0.99',      'CAM 2 - Teller Area', 'CCTV', 'OFFLINE', NOW() - INTERVAL '15 minutes'),
  (16, '192.0.2.146',    'CAM 1 - ATM Area',    'CCTV', 'OFFLINE', NOW() - INTERVAL '20 minutes'),
  (17, '203.0.113.76',   'CAM 2 - Lounge Area', 'CCTV', 'OFFLINE', NOW() - INTERVAL '25 minutes'),
  (18, '198.51.100.42',  'CAM 1 - Main Hall',   'CCTV', 'OFFLINE', NOW() - INTERVAL '30 minutes'),
  (19, '10.10.20.30',    'CAM 1 - Lobby',       'CCTV', 'OFFLINE', NOW() - INTERVAL '35 minutes'),
  (20, '172.31.255.255', 'CAM 2 - ATM Area',    'CCTV', 'OFFLINE', NOW() - INTERVAL '40 minutes'),
  (1,  '192.168.0.200',  'CAM 3 - Parking',     'CCTV', 'OFFLINE', NOW() - INTERVAL '45 minutes'),
  (2,  '192.168.0.201',  'CAM 3 - Parking',     'CCTV', 'OFFLINE', NOW() - INTERVAL '50 minutes');

-- Add CCTV unresolved (offline > 1 day) for branch rate calculation
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES
  (1,  '10.125.2.100', 'CAM 4 - Storage', 'CCTV', 'OFFLINE', NOW() - INTERVAL '3 days'),
  (15, '10.125.2.101', 'CAM 2 - Lobby',   'CCTV', 'OFFLINE', NOW() - INTERVAL '2 days'),
  (16, '10.125.2.102', 'CAM 2 - Lobby',   'CCTV', 'OFFLINE', NOW() - INTERVAL '2 days 6 hours'),
  (17, '10.125.2.103', 'CAM 2 - Lobby',   'CCTV', 'OFFLINE', NOW() - INTERVAL '1 day 2 hours');

-- Add extra CCTV online devices to simulate realistic totals
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES
  (18, '10.200.1.1', 'CAM 1 - Lobby',    'CCTV', 'ONLINE', NOW()),
  (19, '10.200.1.2', 'CAM 1 - ATM Area', 'CCTV', 'ONLINE', NOW()),
  (20, '10.200.1.3', 'CAM 1 - Teller',   'CCTV', 'ONLINE', NOW());
