package com.gdsc.moa.domain.team.dto.response;

import com.gdsc.moa.domain.gifticon.entity.GifticonEntity;
import com.gdsc.moa.domain.team.entity.TeamGifticonEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ShareTeamGifticonResponseDto {
    private Long id;
    private String teamName;
    private String teamLeaderNickname;
    private GifticonEntity gifticonEntity;
    // TODO: 10/31/23 이부분 수정하

    public ShareTeamGifticonResponseDto(Long id, String teamName, String teamLeaderNickname) {
        this.id = id;
        this.teamName = teamName;
        this.teamLeaderNickname = teamLeaderNickname;
    }

    public ShareTeamGifticonResponseDto(TeamGifticonEntity teamGifticonEntity) {
        this.id = teamGifticonEntity.getId();
        this.teamName = teamGifticonEntity.getTeamUserEntity().getTeamEntity().getTeamName();
        this.teamLeaderNickname = teamGifticonEntity.getTeamUserEntity().getTeamEntity().getUser().getNickname();
        this.gifticonEntity = teamGifticonEntity.getGifticonEntity();
    }
}
