package com.gdsc.moa.domain.team.entity;

import com.gdsc.moa.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class TeamGifticonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "team_user_id")
    private TeamUserEntity teamUserEntity;
    
}
