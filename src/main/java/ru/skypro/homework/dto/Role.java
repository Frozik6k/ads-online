package ru.skypro.homework.dto;

import java.util.Optional;

public enum Role {
    USER, ADMIN;

    public static Role toRole(String roleString) {
        return parse(roleString).orElseThrow(() ->
                new IllegalArgumentException("Неизвестная роль: " + roleString));
    }

    public static Optional<Role> parse(String roleString) {
        if (roleString == null) return Optional.empty();
        String role = roleString.trim();
        if (role.isEmpty()) return Optional.empty();

        role = role.toUpperCase();
        if (role.startsWith("ROLE_")) role = role.substring(5);

        try {
            return Optional.of(Role.valueOf(role));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }
}
