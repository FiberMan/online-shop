package com.filk.web.filter;

import com.filk.entity.UserRole;

import java.util.EnumSet;

public class AdminRoleFilter extends RoleFilter {
    public AdminRoleFilter() {
        super();
        super.setUserRole(EnumSet.of(UserRole.ADMIN));
    }
}
