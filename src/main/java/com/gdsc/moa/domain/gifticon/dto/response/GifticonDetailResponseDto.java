package com.gdsc.moa.domain.gifticon.dto.response;

import com.gdsc.moa.domain.team.dto.response.TeamListResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class GifticonDetailResponseDto {
    private GifticonResponseDto gifticon;
    private List<TeamListResponseDto> teamList;

    public GifticonDetailResponseDto(GifticonResponseDto gifticonResponseDto, List<TeamListResponseDto> teamList) {
        this.gifticon = gifticonResponseDto;
        this.teamList = teamList;
    }
}
