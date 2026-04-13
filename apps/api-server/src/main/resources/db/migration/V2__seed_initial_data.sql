INSERT INTO menus(code, name, position)
VALUES
    ('HEADER_MAIN', 'Main Header', 'HEADER'),
    ('FOOTER_MAIN', 'Main Footer', 'FOOTER');

INSERT INTO service_categories(name, slug, sort_order, is_active)
VALUES
    ('Khảo sát địa chất công trình', 'khao-sat-dia-chat-cong-trinh', 1, b'1'),
    ('Khảo sát địa hình', 'khao-sat-dia-hinh', 2, b'1'),
    ('Khảo sát địa chất thủy văn', 'khao-sat-dia-chat-thuy-van', 3, b'1'),
    ('Thí nghiệm chuyên ngành xây dựng', 'thi-nghiem-chuyen-nganh-xay-dung', 4, b'1'),
    ('Quan trắc công trình', 'quan-trac-cong-trinh', 5, b'1'),
    ('Tư vấn giám sát khảo sát', 'tu-van-giam-sat-khao-sat', 6, b'1'),
    ('Xây lắp', 'xay-lap', 7, b'1');

INSERT INTO project_categories(name, slug, sort_order, is_active)
VALUES
    ('Khảo sát địa chất công trình', 'khao-sat-dia-chat-cong-trinh', 1, b'1'),
    ('Khảo sát địa hình', 'khao-sat-dia-hinh', 2, b'1'),
    ('Khảo sát địa chất thủy văn', 'khao-sat-dia-chat-thuy-van', 3, b'1'),
    ('Thí nghiệm công trình', 'thi-nghiem-cong-trinh', 4, b'1');

INSERT INTO post_categories(name, slug, sort_order, is_active)
VALUES
    ('Tin tức', 'tin-tuc', 1, b'1'),
    ('Kiến thức', 'kien-thuc', 2, b'1');

INSERT INTO site_settings(site_name, hotline, email, address, footer_text)
VALUES
    ('Hanoi Survey', '0900000000', 'contact@hanoisurvey.vn', 'Hà Nội, Việt Nam', '© Hanoi Survey - Geological Engineering');
