package com.filk.entity;

public enum UserRole {
    USER("USER"),   // can manage product cart
    ADMIN("ADMIN"); // can manage products and users also

    private final String name;

    UserRole(String name) {
        this.name = name;
    }

    public static UserRole getByName(String name) {
        UserRole[] userRoles = UserRole.values();
        for (UserRole userRole : userRoles) {
            if (userRole.getName().equalsIgnoreCase(name)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("No role for name " + name);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
