CREATE TABLE incident_cameras (
    id          NUMBER(19)   GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    incident_id NUMBER(19)   NOT NULL,
    device_id   NUMBER(19)   NOT NULL,
    url         VARCHAR2(500),
    CONSTRAINT fk_ic_incident FOREIGN KEY (incident_id) REFERENCES incidents(id),
    CONSTRAINT fk_ic_device   FOREIGN KEY (device_id)   REFERENCES devices(id)
);

CREATE INDEX idx_incident_cameras_incident ON incident_cameras(incident_id);
