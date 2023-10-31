package com.gdsc.moa.domain.team.dto.response;

import com.gdsc.moa.domain.team.dto.TeamMember;
import com.gdsc.moa.domain.team.entity.TeamEntity;
import com.gdsc.moa.domain.team.entity.TeamUserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamListResponseDto {
    private Long id;
    private String teamCode;
    private String teamName;
    private TeamMember teamMember;

    @Builder
    public TeamListResponseDto(Long id, String teamCode, String teamName, TeamUserEntity teamMember) {
        this.id = id;
        this.teamCode = teamCode;
        this.teamName = teamName;
        this.teamMember = TeamMember.builder()
                .id(teamMember.getId())
                .nickname(teamMember.getUserEntity().getNickname())
                .profileImageUrl(teamMember.getUserEntity().getProfileImageUrl())
                .build();
    }

    @Builder
    public TeamListResponseDto(TeamEntity teamEntity, TeamUserEntity teamMember) {
        this.id = teamEntity.getId();
        this.teamCode = teamEntity.getTeamCode();
        this.teamName = teamEntity.getTeamName();
        this.teamMember = TeamMember.builder()
                .id(teamMember.getId())
                .nickname(teamMember.getUserEntity().getNickname())
                .profileImageUrl(teamMember.getUserEntity().getProfileImageUrl())
                .build();

    }
}
