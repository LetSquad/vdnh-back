ALTER TABLE place DROP COLUMN schedule_id;
ALTER TABLE place ADD COLUMN schedule JSONB;
ALTER TABLE event DROP COLUMN schedule_id;
ALTER TABLE event ADD COLUMN schedule JSONB;

DROP TABLE schedule;
