CREATE TABLE IF NOT EXISTS equipment_categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    parent_id BIGINT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    is_active BIT NOT NULL DEFAULT b'1',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_equipment_category_parent FOREIGN KEY (parent_id) REFERENCES equipment_categories(id)
);

INSERT INTO equipment_categories (name, slug, parent_id, sort_order, is_active)
SELECT 'Thiết bị cho thí nghiệm', 'thiet-bi-cho-thi-nghiem', NULL, 1, b'1'
WHERE NOT EXISTS (SELECT 1 FROM equipment_categories WHERE slug = 'thiet-bi-cho-thi-nghiem');

INSERT INTO equipment_categories (name, slug, parent_id, sort_order, is_active)
SELECT 'Thiết bị cho trắc đạc', 'thiet-bi-cho-trac-dac', NULL, 2, b'1'
WHERE NOT EXISTS (SELECT 1 FROM equipment_categories WHERE slug = 'thiet-bi-cho-trac-dac');

INSERT INTO equipment_categories (name, slug, parent_id, sort_order, is_active)
SELECT 'Thiết bị khảo sát thủy văn', 'thiet-bi-khao-sat-thuy-van', NULL, 3, b'1'
WHERE NOT EXISTS (SELECT 1 FROM equipment_categories WHERE slug = 'thiet-bi-khao-sat-thuy-van');

INSERT INTO equipment_categories (name, slug, parent_id, sort_order, is_active)
SELECT 'Thiết bị cho khảo sát địa chất', 'thiet-bi-cho-khao-sat-dia-chat', NULL, 4, b'1'
WHERE NOT EXISTS (SELECT 1 FROM equipment_categories WHERE slug = 'thiet-bi-cho-khao-sat-dia-chat');

ALTER TABLE equipments ADD COLUMN category_id BIGINT NULL AFTER id;
ALTER TABLE equipments ADD CONSTRAINT fk_equipments_category FOREIGN KEY (category_id) REFERENCES equipment_categories(id);

UPDATE equipments
SET category_id = (SELECT id FROM equipment_categories WHERE slug = 'thiet-bi-cho-khao-sat-dia-chat')
WHERE category_id IS NULL AND (slug LIKE '%khoan%' OR slug LIKE '%spt%' OR slug LIKE '%cpt%' OR slug LIKE '%dia-chat%');

UPDATE equipments
SET category_id = (SELECT id FROM equipment_categories WHERE slug = 'thiet-bi-cho-trac-dac')
WHERE category_id IS NULL AND (slug LIKE '%gps%' OR slug LIKE '%toan-dac%' OR slug LIKE '%drone%' OR slug LIKE '%do-ve%' OR slug LIKE '%trac-dac%');

UPDATE equipments
SET category_id = (SELECT id FROM equipment_categories WHERE slug = 'thiet-bi-cho-thi-nghiem')
WHERE category_id IS NULL AND (slug LIKE '%thi-nghiem%' OR slug LIKE '%nen-3-truc%' OR slug LIKE '%cbr%' OR slug LIKE '%atterberg%' OR slug LIKE '%cat-canh%');

UPDATE equipments
SET category_id = (SELECT id FROM equipment_categories WHERE slug = 'thiet-bi-khao-sat-thuy-van')
WHERE category_id IS NULL AND (slug LIKE '%nuoc-ngam%' OR slug LIKE '%thuy-van%' OR slug LIKE '%quan-trac%');

UPDATE equipments
SET category_id = (SELECT id FROM equipment_categories WHERE slug = 'thiet-bi-cho-khao-sat-dia-chat')
WHERE category_id IS NULL;

ALTER TABLE equipments MODIFY COLUMN category_id BIGINT NOT NULL;
CREATE INDEX idx_equipments_category_order ON equipments(category_id, sort_order);
