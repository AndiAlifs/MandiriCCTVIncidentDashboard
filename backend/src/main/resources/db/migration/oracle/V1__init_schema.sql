CREATE TABLE branches (
    id         NUMBER(19)   GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR2(255) NOT NULL,
    region     VARCHAR2(100) NOT NULL,
    area_group VARCHAR2(100) NOT NULL,
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL
);

CREATE TABLE devices (
    id         NUMBER(19)   GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    branch_id  NUMBER(19)   NOT NULL,
    ip_address VARCHAR2(50),
    location   VARCHAR2(255) NOT NULL,
    status     VARCHAR2(20)  DEFAULT 'ONLINE' NOT NULL,
    last_ping  TIMESTAMP,
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT fk_device_branch FOREIGN KEY (branch_id) REFERENCES branches(id),
    CONSTRAINT chk_device_status CHECK (status IN ('ONLINE', 'OFFLINE'))
);

CREATE TABLE incidents (
    id           NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    device_id    NUMBER(19)    NOT NULL,
    type         VARCHAR2(50)  NOT NULL,
    severity     VARCHAR2(20)  DEFAULT 'MEDIUM' NOT NULL,
    status       VARCHAR2(20)  DEFAULT 'OPEN' NOT NULL,
    description  CLOB,
    evidence_url VARCHAR2(500),
    detected_at  TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    cleared_at   TIMESTAMP,
    CONSTRAINT fk_incident_device  FOREIGN KEY (device_id) REFERENCES devices(id),
    CONSTRAINT chk_incident_type     CHECK (type     IN ('FIRE_SMOKE','UNAUTHORIZED_ACCESS','ATM_LOITERING','AFTER_HOURS')),
    CONSTRAINT chk_incident_severity CHECK (severity IN ('LOW','MEDIUM','HIGH','CRITICAL')),
    CONSTRAINT chk_incident_status   CHECK (status   IN ('OPEN','IN_PROGRESS','RESOLVED'))
);

CREATE TABLE users (
    id         NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username   VARCHAR2(100) NOT NULL,
    password   VARCHAR2(255) NOT NULL,
    role       VARCHAR2(20)  DEFAULT 'VIEWER' NOT NULL,
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT uq_users_username UNIQUE (username),
    CONSTRAINT chk_user_role CHECK (role IN ('ADMIN','VIEWER','BRANCH_MGR'))
);

CREATE INDEX idx_devices_branch_id  ON devices(branch_id);
CREATE INDEX idx_devices_status     ON devices(status);
CREATE INDEX idx_incidents_device   ON incidents(device_id);
CREATE INDEX idx_incidents_status   ON incidents(status);
CREATE INDEX idx_incidents_detected ON incidents(detected_at DESC);
