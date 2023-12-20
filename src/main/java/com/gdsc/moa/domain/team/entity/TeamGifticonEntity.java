package com.gdsc.moa.domain.team.entity;

import com.gdsc.moa.domain.gifticon.entity.GifticonEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TeamGifticonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "team_user_id")
    private TeamUserEntity teamUserEntity;

    @ManyToOne
    @JoinColumn(name = "gifticon_id")
    private GifticonEntity gifticonEntity;


    public TeamGifticonEntity(TeamUserEntity teamUserEntity, GifticonEntity gifticonEntity) {
        this.teamUserEntity = teamUserEntity;
        this.gifticonEntity = new GifticonEntity(gifticonEntity);
    }
}
