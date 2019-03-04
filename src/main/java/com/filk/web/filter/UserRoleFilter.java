package com.filk.web.filter;

import com.filk.entity.UserRole;

import java.util.EnumSet;

public class UserRoleFilter extends RoleFilter {
    public UserRoleFilter() {
        super();
        super.setUserRole(EnumSet.of(UserRole.USER, UserRole.ADMIN));
    }
}
