package com.filk.web.filter;

import com.filk.entity.UserRole;

public class UserRoleFilter extends RoleFilter {
    public UserRoleFilter() {
        super();
        super.setUserRole(UserRole.USER);
    }
}
