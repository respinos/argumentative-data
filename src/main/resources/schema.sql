DROP TABLE IF EXISTS possible_objects;

CREATE TABLE IF NOT EXISTS possible_objects (
    id SERIAL PRIMARY KEY,
    parent_id INTEGER NULL,
    identifier VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    version_number INTEGER NOT NULL DEFAULT 1
);

CREATE INDEX IF NOT EXISTS idx_possible_objects_id ON possible_objects(id);
CREATE INDEX IF NOT EXISTS idx_possible_objects_parent_id ON possible_objects(parent_id);
CREATE INDEX IF NOT EXISTS idx_possible_objects_identifier ON possible_objects(identifier);
CREATE INDEX IF NOT EXISTS idx_possible_objects_type ON possible_objects(type);
CREATE INDEX IF NOT EXISTS idx_possible_objects_identifier_version_number ON possible_objects(identifier, version_number);
