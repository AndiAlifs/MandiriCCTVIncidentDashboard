ALTER TABLE incidents ADD clear_token VARCHAR2(36);

UPDATE incidents SET clear_token = LOWER(
    REGEXP_REPLACE(
        RAWTOHEX(SYS_GUID()),
        '([A-F0-9]{8})([A-F0-9]{4})([A-F0-9]{4})([A-F0-9]{4})([A-F0-9]{12})',
        '\1-\2-\3-\4-\5'
    )
);

COMMIT;

ALTER TABLE incidents MODIFY clear_token VARCHAR2(36) NOT NULL;

CREATE UNIQUE INDEX idx_incidents_clear_token ON incidents(clear_token);
