package com.gdsc.moa.domain.team.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamMember {
    private Long id;
    private String nickname;
    private String profileImageUrl;
}
