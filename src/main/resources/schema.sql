DROP VIEW IF EXISTS current_possible_objects;

DROP TABLE IF EXISTS possible_objects CASCADE;

CREATE TABLE IF NOT EXISTS possible_objects (
    id SERIAL PRIMARY KEY,
    parent_id INTEGER NULL,
    identifier VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    version_number INTEGER NOT NULL DEFAULT 1,
    bin_identifier VARCHAR(255),
    CONSTRAINT fk_possible_objects_parent
        FOREIGN KEY (parent_id)
        REFERENCES possible_objects (id)
        ON DELETE SET NULL  -- Options: CASCADE, SET NULL, or RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_possible_objects_id ON possible_objects(id);
CREATE INDEX IF NOT EXISTS idx_possible_objects_parent_id ON possible_objects(parent_id);
CREATE INDEX IF NOT EXISTS idx_possible_objects_identifier ON possible_objects(identifier);
CREATE INDEX IF NOT EXISTS idx_possible_objects_type ON possible_objects(type);
CREATE INDEX IF NOT EXISTS idx_possible_objects_identifier_version_number ON possible_objects(identifier, version_number);
CREATE INDEX IF NOT EXISTS idx_possible_objects_bin_identifier ON possible_objects(bin_identifier);

CREATE OR REPLACE VIEW current_possible_objects AS
SELECT po.*
FROM possible_objects po
         INNER JOIN (
    SELECT identifier, MAX(version_number) AS max_version
    FROM possible_objects
    GROUP BY identifier
) grouped_po
ON po.identifier = grouped_po.identifier AND po.version_number = grouped_po.max_version;
