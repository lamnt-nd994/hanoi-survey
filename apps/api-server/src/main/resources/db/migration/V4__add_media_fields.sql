ALTER TABLE services ADD COLUMN cover_image_path VARCHAR(600) NULL AFTER icon;
ALTER TABLE projects ADD COLUMN cover_image_path VARCHAR(600) NULL AFTER scale_text;
ALTER TABLE projects ADD COLUMN gallery_json LONGTEXT NULL AFTER cover_image_path;
ALTER TABLE equipments ADD COLUMN cover_image_path VARCHAR(600) NULL AFTER description;
