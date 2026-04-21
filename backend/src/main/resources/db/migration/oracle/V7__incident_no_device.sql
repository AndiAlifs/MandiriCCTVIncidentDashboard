-- Drop old device FK constraints and columns from incidents
DECLARE
  v_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_constraints
  WHERE constraint_name = 'SYS_C' AND table_name = 'INCIDENTS' AND constraint_type = 'R';
  -- Drop by finding FK on device_id
  FOR c IN (SELECT constraint_name FROM user_constraints
            WHERE table_name = 'INCIDENTS' AND constraint_type = 'R'
              AND constraint_name IN (
                SELECT constraint_name FROM user_cons_columns
                WHERE table_name = 'INCIDENTS' AND column_name = 'DEVICE_ID')) LOOP
    EXECUTE IMMEDIATE 'ALTER TABLE incidents DROP CONSTRAINT ' || c.constraint_name;
  END LOOP;
END;
/

ALTER TABLE incidents DROP COLUMN device_id;

ALTER TABLE incidents ADD ip_address   VARCHAR2(50);
ALTER TABLE incidents ADD branch_name  VARCHAR2(255);
ALTER TABLE incidents ADD camera_name  VARCHAR2(255);
ALTER TABLE incidents ADD region       VARCHAR2(100);
ALTER TABLE incidents ADD area_group   VARCHAR2(100);

DROP INDEX idx_incidents_device;
CREATE INDEX idx_incidents_branch_name ON incidents(branch_name);
CREATE INDEX idx_incidents_type_branch ON incidents(type, branch_name);

-- Drop old device FK from incident_cameras
DECLARE
BEGIN
  FOR c IN (SELECT constraint_name FROM user_constraints
            WHERE table_name = 'INCIDENT_CAMERAS' AND constraint_type = 'R'
              AND constraint_name IN (
                SELECT constraint_name FROM user_cons_columns
                WHERE table_name = 'INCIDENT_CAMERAS' AND column_name = 'DEVICE_ID')) LOOP
    EXECUTE IMMEDIATE 'ALTER TABLE incident_cameras DROP CONSTRAINT ' || c.constraint_name;
  END LOOP;
END;
/

ALTER TABLE incident_cameras DROP COLUMN device_id;

ALTER TABLE incident_cameras ADD ip_address  VARCHAR2(50);
ALTER TABLE incident_cameras ADD camera_name VARCHAR2(255);
