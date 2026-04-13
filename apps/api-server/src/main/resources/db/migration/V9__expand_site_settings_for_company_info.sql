ALTER TABLE site_settings ADD COLUMN short_name VARCHAR(255) NULL AFTER site_name;
ALTER TABLE site_settings ADD COLUMN slogan VARCHAR(500) NULL AFTER short_name;
ALTER TABLE site_settings ADD COLUMN logo_path VARCHAR(500) NULL AFTER slogan;
ALTER TABLE site_settings ADD COLUMN footer_logo_path VARCHAR(500) NULL AFTER logo_path;
ALTER TABLE site_settings ADD COLUMN hero_banner_path VARCHAR(500) NULL AFTER footer_logo_path;
ALTER TABLE site_settings ADD COLUMN company_intro TEXT NULL AFTER hero_banner_path;
ALTER TABLE site_settings ADD COLUMN tax_code VARCHAR(255) NULL AFTER company_intro;
ALTER TABLE site_settings ADD COLUMN business_license_path VARCHAR(500) NULL AFTER tax_code;
ALTER TABLE site_settings ADD COLUMN license_issued_by VARCHAR(255) NULL AFTER business_license_path;
ALTER TABLE site_settings ADD COLUMN license_issued_date DATE NULL AFTER license_issued_by;
ALTER TABLE site_settings ADD COLUMN office_address VARCHAR(500) NULL AFTER address;
ALTER TABLE site_settings ADD COLUMN branch_address VARCHAR(500) NULL AFTER office_address;
ALTER TABLE site_settings ADD COLUMN phone VARCHAR(100) NULL AFTER hotline;
ALTER TABLE site_settings ADD COLUMN website VARCHAR(255) NULL AFTER email;
ALTER TABLE site_settings ADD COLUMN working_hours VARCHAR(255) NULL AFTER map_embed;
ALTER TABLE site_settings ADD COLUMN facebook_url VARCHAR(500) NULL AFTER working_hours;
ALTER TABLE site_settings ADD COLUMN zalo_url VARCHAR(500) NULL AFTER facebook_url;
ALTER TABLE site_settings ADD COLUMN youtube_url VARCHAR(500) NULL AFTER zalo_url;
ALTER TABLE site_settings ADD COLUMN linkedin_url VARCHAR(500) NULL AFTER youtube_url;

UPDATE site_settings
SET office_address = address,
    short_name = COALESCE(short_name, site_name)
WHERE office_address IS NULL OR short_name IS NULL;
