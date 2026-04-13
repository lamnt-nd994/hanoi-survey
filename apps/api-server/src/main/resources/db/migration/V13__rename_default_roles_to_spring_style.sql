UPDATE roles
SET code = 'ROLE_ADMIN',
    name = 'ROLE_ADMIN',
    is_admin_access = b'1'
WHERE code = 'SUPER_ADMIN';

UPDATE roles
SET code = 'ROLE_EDITOR',
    name = 'ROLE_EDITOR',
    is_admin_access = b'1'
WHERE code = 'EDITOR';

UPDATE roles
SET code = 'ROLE_STAFF',
    name = 'ROLE_STAFF',
    is_admin_access = b'1'
WHERE code = 'CONTENT_STAFF';

UPDATE roles
SET is_admin_access = b'1'
WHERE code IN ('ROLE_ADMIN', 'ROLE_EDITOR', 'ROLE_STAFF', 'ROLE_VIEWER');
