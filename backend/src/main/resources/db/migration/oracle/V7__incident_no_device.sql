-- Drop old device FK constraints from incidents
DECLARE
BEGIN
  FOR c IN (SELECT constraint_name FROM user_constraints
            WHERE table_name = 'INCIDENTS' AND constraint_type = 'R'
              AND constraint_name IN (
                SELECT constraint_name FROM user_cons_columns
                WHERE table_name = 'INCIDENTS' AND column_name = 'DEVICE_ID')) LOOP
    EXECUTE IMMEDIATE 'ALTER TABLE incidents DROP CONSTRAINT ' || c.constraint_name;
  END LOOP;
END;
/

-- Drop device_id column from incidents (if present)
BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE incidents DROP COLUMN device_id';
EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE != -904 AND SQLCODE != -12983 THEN RAISE; END IF;  -- column does not exist
END;
/

-- Add new columns to incidents (skip if already present)
DECLARE
  PROCEDURE add_col(p_name VARCHAR2, p_type VARCHAR2) IS
  BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE incidents ADD ' || p_name || ' ' || p_type;
  EXCEPTION
    WHEN OTHERS THEN
      IF SQLCODE != -1430 THEN RAISE; END IF;  -- column already exists
  END;
BEGIN
  add_col('ip_address',  'VARCHAR2(50)');
  add_col('branch_name', 'VARCHAR2(255)');
  add_col('camera_name', 'VARCHAR2(255)');
  add_col('region',      'VARCHAR2(100)');
  add_col('area_group',  'VARCHAR2(100)');
END;
/

-- Drop old device index (if present)
BEGIN
  EXECUTE IMMEDIATE 'DROP INDEX idx_incidents_device';
EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE != -1418 THEN RAISE; END IF;  -- index does not exist
END;
/

-- Create new indexes (skip if already present)
DECLARE
  PROCEDURE create_idx(p_name VARCHAR2, p_def VARCHAR2) IS
  BEGIN
    EXECUTE IMMEDIATE 'CREATE INDEX ' || p_name || ' ON ' || p_def;
  EXCEPTION
    WHEN OTHERS THEN
      IF SQLCODE != -955 THEN RAISE; END IF;  -- name already used
  END;
BEGIN
  create_idx('idx_incidents_branch_name', 'incidents(branch_name)');
  create_idx('idx_incidents_type_branch', 'incidents(type, branch_name)');
END;
/

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

-- Drop device_id column from incident_cameras (if present)
BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE incident_cameras DROP COLUMN device_id';
EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE != -904 AND SQLCODE != -12983 THEN RAISE; END IF;
END;
/

-- Add new columns to incident_cameras (skip if already present)
DECLARE
  PROCEDURE add_col(p_name VARCHAR2, p_type VARCHAR2) IS
  BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLE incident_cameras ADD ' || p_name || ' ' || p_type;
  EXCEPTION
    WHEN OTHERS THEN
      IF SQLCODE != -1430 THEN RAISE; END IF;
  END;
BEGIN
  add_col('ip_address',  'VARCHAR2(50)');
  add_col('camera_name', 'VARCHAR2(255)');
END;
/
