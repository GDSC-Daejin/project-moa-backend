package com.gdsc.moa.domain.team.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    AVAILABLE("사용가능"),
    UNAVAILABLE("사용불가");
    private final String status;
}
