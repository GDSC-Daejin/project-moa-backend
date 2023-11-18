package com.gdsc.moa.domain.team.dto.response;

import com.gdsc.moa.domain.team.dto.TeamMember;
import com.gdsc.moa.domain.team.entity.Status;
import com.gdsc.moa.domain.team.entity.TeamEntity;
import com.gdsc.moa.domain.team.entity.TeamUserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TeamJoinResponseDto {
    private Long teamId;
    private String teamCode;
    private String teamName;
    private Status status;
    private String teamLeaderNickname;
    private String teamImage;
    private List<TeamMember> teamMembers;


    public TeamJoinResponseDto(TeamEntity teamEntity, List<TeamUserEntity> teamMembers) {
        this.teamId = teamEntity.getId();
        this.teamCode = teamEntity.getTeamCode();
        this.teamName = teamEntity.getTeamName();
        this.status = Status.AVAILABLE;
        this.teamLeaderNickname = teamEntity.getUser().getNickname();
        this.teamImage = teamEntity.getTeamImage();
        this.teamMembers = teamMembers.stream()
                .map(teamMember -> TeamMember.builder()
                        .id(teamMember.getId())
                        .nickname(teamMember.getUserEntity().getNickname())
                        .profileImageUrl(teamMember.getUserEntity().getProfileImageUrl())
                        .build())
                .collect(Collectors.toList());
    }
}
