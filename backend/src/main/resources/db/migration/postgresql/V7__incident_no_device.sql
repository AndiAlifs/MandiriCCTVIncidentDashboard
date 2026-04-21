-- Drop old device FK constraints and columns from incidents
ALTER TABLE incidents DROP CONSTRAINT IF EXISTS incidents_device_id_fkey;
ALTER TABLE incidents DROP COLUMN IF EXISTS device_id;

ALTER TABLE incidents ADD COLUMN ip_address   VARCHAR(50);
ALTER TABLE incidents ADD COLUMN branch_name  VARCHAR(255);
ALTER TABLE incidents ADD COLUMN camera_name  VARCHAR(255);
ALTER TABLE incidents ADD COLUMN region       VARCHAR(100);
ALTER TABLE incidents ADD COLUMN area_group   VARCHAR(100);

DROP INDEX IF EXISTS idx_incidents_device;
CREATE INDEX idx_incidents_branch_name ON incidents(branch_name);
CREATE INDEX idx_incidents_type_branch ON incidents(type, branch_name);

-- Drop old device FK from incident_cameras
ALTER TABLE incident_cameras DROP CONSTRAINT IF EXISTS incident_cameras_device_id_fkey;
ALTER TABLE incident_cameras DROP COLUMN IF EXISTS device_id;

ALTER TABLE incident_cameras ADD COLUMN ip_address  VARCHAR(50);
ALTER TABLE incident_cameras ADD COLUMN camera_name VARCHAR(255);
