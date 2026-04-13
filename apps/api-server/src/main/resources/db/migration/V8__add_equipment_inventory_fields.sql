ALTER TABLE equipments ADD COLUMN origin VARCHAR(255) NULL AFTER manufacturer;
ALTER TABLE equipments ADD COLUMN unit VARCHAR(100) NULL AFTER origin;
ALTER TABLE equipments ADD COLUMN quantity INT NULL AFTER unit;

UPDATE equipments SET unit = 'bo' WHERE unit IS NULL;
