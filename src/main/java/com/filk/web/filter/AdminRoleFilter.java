package com.filk.web.filter;

import com.filk.entity.UserRole;

public class AdminRoleFilter extends RoleFilter {
    public AdminRoleFilter() {
        super();
        super.setUserRole(UserRole.ADMIN);
    }
}
