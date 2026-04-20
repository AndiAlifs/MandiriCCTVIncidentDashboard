CREATE TABLE incident_cameras (
    id          BIGSERIAL PRIMARY KEY,
    incident_id BIGINT NOT NULL REFERENCES incidents(id),
    device_id   BIGINT NOT NULL REFERENCES devices(id),
    url         VARCHAR(500)
);

CREATE INDEX idx_incident_cameras_incident ON incident_cameras(incident_id);
