ALTER TABLE equipment_categories
    ADD COLUMN icon VARCHAR(255) NULL AFTER parent_id;

UPDATE equipment_categories
SET icon = 'flask-conical'
WHERE slug = 'thiet-bi-cho-thi-nghiem' AND (icon IS NULL OR icon = '');

UPDATE equipment_categories
SET icon = 'ruler'
WHERE slug = 'thiet-bi-cho-trac-dac' AND (icon IS NULL OR icon = '');

UPDATE equipment_categories
SET icon = 'waves'
WHERE slug = 'thiet-bi-khao-sat-thuy-van' AND (icon IS NULL OR icon = '');

UPDATE equipment_categories
SET icon = 'pickaxe'
WHERE slug = 'thiet-bi-cho-khao-sat-dia-chat' AND (icon IS NULL OR icon = '');
