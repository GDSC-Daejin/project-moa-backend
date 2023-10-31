package com.gdsc.moa.domain.team.dto.response;

import com.gdsc.moa.domain.team.entity.Status;
import com.gdsc.moa.domain.team.entity.TeamEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TeamCreateResponseDto {
    private Long id;
    private String teamCode;
    private String teamName;
    private Status status;
    private String teamLeaderNickname;

    public TeamCreateResponseDto(TeamEntity teamEntity) {
        this.id = teamEntity.getId();
        this.teamCode = teamEntity.getTeamCode();
        this.teamName = teamEntity.getTeamName();
        this.status = Status.AVAILABLE;
        this.teamLeaderNickname = teamEntity.getUser().getNickname();
    }
}
