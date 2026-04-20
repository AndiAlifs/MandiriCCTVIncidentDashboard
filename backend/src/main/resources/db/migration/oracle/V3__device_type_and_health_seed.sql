-- Add device type and simulation tracking columns
ALTER TABLE devices ADD device_type VARCHAR2(10) DEFAULT 'CCTV' NOT NULL;
ALTER TABLE devices ADD CONSTRAINT chk_device_type CHECK (device_type IN ('NVR','CCTV'));
ALTER TABLE devices ADD simulated_at TIMESTAMP;
ALTER TABLE incidents ADD simulated_at TIMESTAMP;

-- Add additional branches
INSERT INTO branches (name, region, area_group) VALUES ('KCP Ciledug Tangerang',    'Tangerang', 'Tangerang Kota');
INSERT INTO branches (name, region, area_group) VALUES ('KCP Jakarta Taman Surya',  'Jakarta',   'Jakarta Barat');
INSERT INTO branches (name, region, area_group) VALUES ('KC Jakarta Cengkareng',    'Jakarta',   'Jakarta Barat');
INSERT INTO branches (name, region, area_group) VALUES ('KC Jakarta Kebayoran Baru','Jakarta',   'Jakarta Selatan');
INSERT INTO branches (name, region, area_group) VALUES ('KC Jakarta Senen',         'Jakarta',   'Jakarta Pusat');
INSERT INTO branches (name, region, area_group) VALUES ('KCP Plaza Semanggi',       'Jakarta',   'Jakarta Selatan');
INSERT INTO branches (name, region, area_group) VALUES ('KCP Senayan City',         'Jakarta',   'Jakarta Selatan');
INSERT INTO branches (name, region, area_group) VALUES ('KCP AEON Mall BSD City',   'Tangerang', 'Tangerang Selatan');
INSERT INTO branches (name, region, area_group) VALUES ('KCP Kebon Jeruk',          'Jakarta',   'Jakarta Barat');
INSERT INTO branches (name, region, area_group) VALUES ('KCP Pondok Gede',          'Jakarta',   'Jakarta Timur');

-- Promote existing devices to NVR
UPDATE devices SET device_type = 'NVR', ip_address = '192.168.0.45' WHERE id = 1;
UPDATE devices SET device_type = 'NVR', ip_address = '172.16.254.3' WHERE id = 2;

-- NVR devices (offline recent)
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (11,'203.0.113.9','NVR Main Unit','NVR','OFFLINE',SYSTIMESTAMP - INTERVAL '50' MINUTE);
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (12,'198.51.100.27','NVR Main Unit','NVR','OFFLINE',SYSTIMESTAMP - INTERVAL '55' MINUTE);
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (13,'10.20.30.40','NVR Main Unit','NVR','ONLINE',SYSTIMESTAMP - INTERVAL '5' MINUTE);
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (14,'10.20.30.41','NVR Main Unit','NVR','ONLINE',SYSTIMESTAMP - INTERVAL '3' MINUTE);

-- Set NVR-promoted devices offline
UPDATE devices SET status = 'OFFLINE', last_ping = SYSTIMESTAMP - INTERVAL '2' MINUTE WHERE id = 1;
UPDATE devices SET status = 'OFFLINE', last_ping = SYSTIMESTAMP - INTERVAL '40' MINUTE WHERE id = 2;

-- NVR unresolved
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (1,'10.125.1.220','NVR Backup Unit','NVR','OFFLINE',SYSTIMESTAMP - NUMTODSINTERVAL(3,'DAY') - NUMTODSINTERVAL(2,'HOUR'));
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (15,'10.125.1.211','NVR Main Unit','NVR','OFFLINE',SYSTIMESTAMP - NUMTODSINTERVAL(2,'DAY') - NUMTODSINTERVAL(1,'HOUR'));
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (16,'10.125.1.212','NVR Main Unit','NVR','OFFLINE',SYSTIMESTAMP - NUMTODSINTERVAL(2,'DAY') - NUMTODSINTERVAL(3,'HOUR'));
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (17,'10.125.1.213','NVR Main Unit','NVR','OFFLINE',SYSTIMESTAMP - NUMTODSINTERVAL(1,'DAY') - NUMTODSINTERVAL(4,'HOUR'));

-- CCTV offline (recent)
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (13,'192.168.45.12','CAM 1 - Main Hall','CCTV','OFFLINE',SYSTIMESTAMP - INTERVAL '5' MINUTE);
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (14,'172.16.254.1','CAM 1 - Lobby','CCTV','OFFLINE',SYSTIMESTAMP - INTERVAL '10' MINUTE);
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (15,'10.0.0.99','CAM 2 - Teller Area','CCTV','OFFLINE',SYSTIMESTAMP - INTERVAL '15' MINUTE);
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (16,'192.0.2.146','CAM 1 - ATM Area','CCTV','OFFLINE',SYSTIMESTAMP - INTERVAL '20' MINUTE);
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (17,'203.0.113.76','CAM 2 - Lounge Area','CCTV','OFFLINE',SYSTIMESTAMP - INTERVAL '25' MINUTE);
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (18,'198.51.100.42','CAM 1 - Main Hall','CCTV','OFFLINE',SYSTIMESTAMP - INTERVAL '30' MINUTE);
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (19,'10.10.20.30','CAM 1 - Lobby','CCTV','OFFLINE',SYSTIMESTAMP - INTERVAL '35' MINUTE);
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (20,'172.31.255.255','CAM 2 - ATM Area','CCTV','OFFLINE',SYSTIMESTAMP - INTERVAL '40' MINUTE);
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (1,'192.168.0.200','CAM 3 - Parking','CCTV','OFFLINE',SYSTIMESTAMP - INTERVAL '45' MINUTE);
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (2,'192.168.0.201','CAM 3 - Parking','CCTV','OFFLINE',SYSTIMESTAMP - INTERVAL '50' MINUTE);

-- CCTV unresolved
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (1,'10.125.2.100','CAM 4 - Storage','CCTV','OFFLINE',SYSTIMESTAMP - NUMTODSINTERVAL(3,'DAY'));
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (15,'10.125.2.101','CAM 2 - Lobby','CCTV','OFFLINE',SYSTIMESTAMP - NUMTODSINTERVAL(2,'DAY'));
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (16,'10.125.2.102','CAM 2 - Lobby','CCTV','OFFLINE',SYSTIMESTAMP - NUMTODSINTERVAL(2,'DAY') - NUMTODSINTERVAL(6,'HOUR'));
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (17,'10.125.2.103','CAM 2 - Lobby','CCTV','OFFLINE',SYSTIMESTAMP - NUMTODSINTERVAL(1,'DAY') - NUMTODSINTERVAL(2,'HOUR'));

-- Extra CCTV online
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (18,'10.200.1.1','CAM 1 - Lobby','CCTV','ONLINE',SYSTIMESTAMP);
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (19,'10.200.1.2','CAM 1 - ATM Area','CCTV','ONLINE',SYSTIMESTAMP);
INSERT INTO devices (branch_id, ip_address, location, device_type, status, last_ping) VALUES (20,'10.200.1.3','CAM 1 - Teller','CCTV','ONLINE',SYSTIMESTAMP);

COMMIT;
