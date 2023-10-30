package com.gdsc.moa.domain.team.entity;

import com.gdsc.moa.domain.team.dto.request.TeamCreateRequestDto;
import com.gdsc.moa.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;
    private String teamCode;
    private String teamName;
    @Enumerated(EnumType.STRING)
    private Status status;


    public TeamEntity(TeamCreateRequestDto teamCreateRequestDto, UserEntity user, String inviteCode) {
        this.teamName = teamCreateRequestDto.getTeamName();
        this.teamCode = inviteCode;
        this.status = Status.AVAILABLE;
    }
}
