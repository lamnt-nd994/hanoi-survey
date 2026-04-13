DELETE FROM menu_items
WHERE url = '/bao-gia'
   OR LOWER(title) LIKE '%bao gia%'
   OR LOWER(title) LIKE '%bao-gia%'
   OR (
        item_type = 'PAGE'
        AND ref_id IN (SELECT id FROM pages WHERE slug = 'bao-gia')
   );

DELETE FROM pages
WHERE slug = 'bao-gia';

DROP TABLE IF EXISTS quote_requests;
