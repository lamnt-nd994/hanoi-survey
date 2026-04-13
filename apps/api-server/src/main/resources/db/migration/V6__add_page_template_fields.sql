ALTER TABLE pages
    ADD COLUMN template_code VARCHAR(100) NOT NULL DEFAULT 'default' AFTER content,
    ADD COLUMN content_json LONGTEXT NULL AFTER template_code;

UPDATE pages
SET slug = 'gioi-thieu',
    title = 'Gioi thieu',
    template_code = 'about_company',
    content_json = '{"hero":{"title":"Ve Chung Toi","backgroundImagePath":""},"intro":{"heading":"Doi tac tin cay trong nganh khao sat xay dung","paragraphs":["Duoc thanh lap voi muc tieu mang lai nhung giai phap khao sat chinh xac va hieu qua, Hanoi Survey cung cap cac dich vu khao sat dia chat, dia hinh, thi nghiem, quan trac, tu van giam sat va xay lap.","Chung toi ap dung cong nghe hien dai cung doi ngu chuyen gia giau kinh nghiem de dam bao moi cong trinh deu co nen mong vung chac."]},"coreValues":{"sectionTitle":"Su Menh & Tam Nhin","items":[{"title":"Su Menh","icon":"bullseye","description":"Cung cap du lieu ky thuat chinh xac, minh bach, gop phan xay dung nhung cong trinh ben vung va an toan."},{"title":"Tam Nhin","icon":"eye","description":"Tro thanh thuong hieu khao sat va thi nghiem xay dung uy tin, dong hanh cung cac du an trong diem."},{"title":"Gia Tri Cot Loi","icon":"gem","description":"Chinh xac - Uy tin - Tan tam. Luon dat chat luong va loi ich khach hang len hang dau."}]},"timeline":{"sectionTitle":"Qua Trinh Phat Trien","items":[{"year":"2013","title":"Thanh lap cong ty","description":"Khoi dau voi doi ngu ky su nen mong va khao sat cho cac du an quy mo nho.","sortOrder":1},{"year":"2016","title":"Mo rong quy mo","description":"Dau tu them he thong may moc thi nghiem va mo rong mang luoi thi cong.","sortOrder":2},{"year":"2019","title":"Nang cao he thong quan ly","description":"Hoan thien quy trinh van hanh, tieu chuan hoa chat luong va nang luc doi ngu.","sortOrder":3},{"year":"2023 - Nay","title":"But pha manh me","description":"Tham gia cac du an trong diem va cung cap giai phap khao sat tron goi tren nhieu linh vuc.","sortOrder":4}]},"organization":{"sectionTitle":"Co Cau To Chuc","heading":"Su to chuc khoa hoc va hieu qua","description":"Cong ty duoc van hanh theo mo hinh ro rang giup trien khai du an nhanh, dong bo va kiem soat chat luong tot hon.","chartImagePath":"","chartCaption":"So do to chuc nhan su","departments":["Ban Giam doc & Hoi dong quan tri","Phong Ky thuat - Nghien cuu & Phat trien","Phong Thi nghiem & Kiem dinh chat luong","Phong Thiet ke & Xay lap","Phong Hanh chinh - Nhan su & Ke toan","Cac doi thi cong hien truong"]}}'
WHERE slug = 'gioi-thieu-cong-ty';

UPDATE pages
SET template_code = COALESCE(template_code, 'default')
WHERE template_code IS NULL;

UPDATE menu_items
SET url = '/gioi-thieu'
WHERE url = '/gioi-thieu-cong-ty';
