package com.gdsc.moa.domain.team.dto.response;

import com.gdsc.moa.domain.team.dto.TeamMember;
import com.gdsc.moa.domain.team.entity.TeamEntity;
import com.gdsc.moa.domain.team.entity.TeamUserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class TeamListResponseDto {
    private Long id;
    private String teamCode;
    private String teamName;
    private String teamImage;
    private String teamLeaderNickname;
    private List<TeamMember> teamMembers; // 여러 개의 TeamMember를 담을 리스트

    @Builder
    public TeamListResponseDto(Long id, String teamCode, String teamName, List<TeamUserEntity> teamMembers) {
        this.id = id;
        this.teamCode = teamCode;
        this.teamName = teamName;
        this.teamMembers = teamMembers.stream()
                .map(teamMember -> TeamMember.builder()
                        .id(teamMember.getId())
                        .nickname(teamMember.getUserEntity().getNickname())
                        .profileImageUrl(teamMember.getUserEntity().getProfileImageUrl())
                        .build())
                .collect(Collectors.toList());
    }

    @Builder
    public TeamListResponseDto(TeamEntity teamEntity, List<TeamUserEntity> teamMembers) {
        this.id = teamEntity.getId();
        this.teamCode = teamEntity.getTeamCode();
        this.teamName = teamEntity.getTeamName();
        this.teamImage = teamEntity.getTeamImage();
        this.teamLeaderNickname = teamEntity.getUser().getNickname();
        this.teamMembers = teamMembers.stream()
                .map(teamMember -> TeamMember.builder()
                        .id(teamMember.getId())
                        .nickname(teamMember.getUserEntity().getNickname())
                        .profileImageUrl(teamMember.getUserEntity().getProfileImageUrl())
                        .build())
                .collect(Collectors.toList());
    }
}
