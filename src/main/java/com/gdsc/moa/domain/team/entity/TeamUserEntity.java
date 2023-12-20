package com.gdsc.moa.domain.team.entity;

import com.gdsc.moa.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamEntity teamEntity;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "teamUserEntity", cascade = CascadeType.ALL)
    private List<TeamGifticonEntity> teamGifticonEntities;

    public TeamUserEntity(TeamEntity team, UserEntity user) {
        this.teamEntity = team;
        this.userEntity = user;
    }
}
