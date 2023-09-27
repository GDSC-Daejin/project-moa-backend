package com.gdsc.moa.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    ADMIN("admin"),
    MEMBER("user");

    private final String value;
}
