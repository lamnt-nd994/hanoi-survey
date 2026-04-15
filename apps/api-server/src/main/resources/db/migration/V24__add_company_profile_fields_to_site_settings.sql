ALTER TABLE site_settings ADD COLUMN company_trade_name VARCHAR(255) NULL AFTER company_name_en;
ALTER TABLE site_settings ADD COLUMN representative_name VARCHAR(255) NULL AFTER website;
ALTER TABLE site_settings ADD COLUMN representative_title VARCHAR(255) NULL AFTER representative_name;
ALTER TABLE site_settings ADD COLUMN establishment_info TEXT NULL AFTER representative_title;
