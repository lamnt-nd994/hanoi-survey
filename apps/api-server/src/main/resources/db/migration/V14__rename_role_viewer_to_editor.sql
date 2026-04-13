UPDATE roles
SET code = 'ROLE_EDITOR',
    name = 'ROLE_EDITOR',
    is_admin_access = b'1'
WHERE code = 'ROLE_VIEWER';
