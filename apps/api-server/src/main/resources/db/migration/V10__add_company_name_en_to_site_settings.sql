ALTER TABLE site_settings ADD COLUMN company_name_en VARCHAR(255) NULL AFTER site_name;

UPDATE site_settings
SET company_name_en = COALESCE(company_name_en, site_name)
WHERE company_name_en IS NULL;
