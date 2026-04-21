-- =============================================================
-- FULL DDL  —  Oracle  (consolidated from V1 through V7)
-- Generated: 2026-04-21
-- =============================================================

-- ------------------------------------------------------------------
-- BRANCHES
-- ------------------------------------------------------------------
CREATE TABLE branches (
    id         NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR2(255) NOT NULL,
    region     VARCHAR2(100) NOT NULL,
    area_group VARCHAR2(100) NOT NULL,
    created_at TIMESTAMP     DEFAULT SYSTIMESTAMP NOT NULL
);

-- ------------------------------------------------------------------
-- DEVICES
-- ------------------------------------------------------------------
CREATE TABLE devices (
    id           NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    branch_id    NUMBER(19)    NOT NULL,
    ip_address   VARCHAR2(50),
    location     VARCHAR2(255) NOT NULL,
    device_type  VARCHAR2(10)  DEFAULT 'CCTV'   NOT NULL,
    status       VARCHAR2(20)  DEFAULT 'ONLINE'  NOT NULL,
    last_ping    TIMESTAMP,
    simulated_at TIMESTAMP,
    created_at   TIMESTAMP     DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT fk_device_branch  FOREIGN KEY (branch_id) REFERENCES branches(id),
    CONSTRAINT chk_device_status CHECK (status      IN ('ONLINE', 'OFFLINE')),
    CONSTRAINT chk_device_type   CHECK (device_type IN ('NVR', 'CCTV'))
);

-- ------------------------------------------------------------------
-- USERS
-- ------------------------------------------------------------------
CREATE TABLE users (
    id         NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username   VARCHAR2(100) NOT NULL,
    password   VARCHAR2(255) NOT NULL,
    role       VARCHAR2(20)  DEFAULT 'VIEWER' NOT NULL,
    created_at TIMESTAMP     DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT uq_users_username UNIQUE (username),
    CONSTRAINT chk_user_role CHECK (role IN ('ADMIN', 'VIEWER', 'BRANCH_MGR'))
);

-- ------------------------------------------------------------------
-- INCIDENTS
-- (device_id removed in V7; denormalised branch/camera fields added)
-- ------------------------------------------------------------------
CREATE TABLE incidents (
    id           NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type         VARCHAR2(50)  NOT NULL,
    severity     VARCHAR2(20)  DEFAULT 'MEDIUM' NOT NULL,
    status       VARCHAR2(20)  DEFAULT 'OPEN'   NOT NULL,
    activity     CLOB,
    evidence_url VARCHAR2(500),
    clear_token  VARCHAR2(36)  NOT NULL,
    ip_address   VARCHAR2(50),
    branch_name  VARCHAR2(255),
    camera_name  VARCHAR2(255),
    region       VARCHAR2(100),
    area_group   VARCHAR2(100),
    simulated_at TIMESTAMP,
    detected_at  TIMESTAMP     DEFAULT SYSTIMESTAMP NOT NULL,
    cleared_at   TIMESTAMP,
    CONSTRAINT chk_incident_type     CHECK (type     IN ('FIRE_SMOKE', 'UNAUTHORIZED_ACCESS', 'ATM_LOITERING', 'AFTER_HOURS')),
    CONSTRAINT chk_incident_severity CHECK (severity IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')),
    CONSTRAINT chk_incident_status   CHECK (status   IN ('OPEN', 'IN_PROGRESS', 'RESOLVED'))
);

-- ------------------------------------------------------------------
-- INCIDENT_CAMERAS
-- (device_id removed in V7; denormalised ip/camera fields added)
-- ------------------------------------------------------------------
CREATE TABLE incident_cameras (
    id          NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    incident_id NUMBER(19)    NOT NULL,
    ip_address  VARCHAR2(50),
    camera_name VARCHAR2(255),
    url         VARCHAR2(500),
    CONSTRAINT fk_ic_incident FOREIGN KEY (incident_id) REFERENCES incidents(id)
);

-- ------------------------------------------------------------------
-- INDEXES
-- ------------------------------------------------------------------
CREATE INDEX idx_devices_branch_id           ON devices(branch_id);
CREATE INDEX idx_devices_status              ON devices(status);

CREATE INDEX idx_incidents_status            ON incidents(status);
CREATE INDEX idx_incidents_detected          ON incidents(detected_at DESC);
CREATE UNIQUE INDEX idx_incidents_clear_token ON incidents(clear_token);
CREATE INDEX idx_incidents_branch_name       ON incidents(branch_name);
CREATE INDEX idx_incidents_type_branch       ON incidents(type, branch_name);

CREATE INDEX idx_incident_cameras_incident   ON incident_cameras(incident_id);
