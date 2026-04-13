CREATE TABLE IF NOT EXISTS service_images (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    service_id BIGINT NOT NULL,
    image_path VARCHAR(600) NOT NULL,
    alt_text VARCHAR(255) NULL,
    caption VARCHAR(500) NULL,
    sort_order INT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_service_images_service FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE
);

CREATE INDEX idx_service_images_service_sort ON service_images(service_id, sort_order, id);
