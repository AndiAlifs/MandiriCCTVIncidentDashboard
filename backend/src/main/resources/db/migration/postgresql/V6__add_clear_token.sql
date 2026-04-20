ALTER TABLE incidents ADD COLUMN clear_token VARCHAR(36);

UPDATE incidents SET clear_token = gen_random_uuid()::text WHERE clear_token IS NULL;

ALTER TABLE incidents ALTER COLUMN clear_token SET NOT NULL;

CREATE UNIQUE INDEX idx_incidents_clear_token ON incidents(clear_token);
