CREATE TABLE branches (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    region     VARCHAR(100) NOT NULL,
    area_group VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE devices (
    id         BIGSERIAL PRIMARY KEY,
    branch_id  BIGINT NOT NULL REFERENCES branches(id),
    ip_address VARCHAR(50),
    location   VARCHAR(255) NOT NULL,
    status     VARCHAR(20) NOT NULL DEFAULT 'ONLINE',
    last_ping  TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_device_status CHECK (status IN ('ONLINE', 'OFFLINE'))
);

CREATE TABLE incidents (
    id           BIGSERIAL PRIMARY KEY,
    device_id    BIGINT NOT NULL REFERENCES devices(id),
    type         VARCHAR(50) NOT NULL,
    severity     VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    status       VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    description  TEXT,
    evidence_url VARCHAR(500),
    detected_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    cleared_at   TIMESTAMP,
    CONSTRAINT chk_incident_type     CHECK (type IN ('FIRE_SMOKE','UNAUTHORIZED_ACCESS','ATM_LOITERING','AFTER_HOURS')),
    CONSTRAINT chk_incident_severity CHECK (severity IN ('LOW','MEDIUM','HIGH','CRITICAL')),
    CONSTRAINT chk_incident_status   CHECK (status IN ('OPEN','IN_PROGRESS','RESOLVED'))
);

CREATE TABLE users (
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(20)  NOT NULL DEFAULT 'VIEWER',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_user_role CHECK (role IN ('ADMIN','VIEWER','BRANCH_MGR'))
);

-- Indexes for common query patterns
CREATE INDEX idx_devices_branch_id  ON devices(branch_id);
CREATE INDEX idx_devices_status     ON devices(status);
CREATE INDEX idx_incidents_device   ON incidents(device_id);
CREATE INDEX idx_incidents_status   ON incidents(status);
CREATE INDEX idx_incidents_detected ON incidents(detected_at DESC);
