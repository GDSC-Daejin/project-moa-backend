package com.gdsc.moa.domain.team.controller;

import com.gdsc.moa.domain.gifticon.dto.response.GifticonResponseDto;
import com.gdsc.moa.domain.team.dto.request.ShareTeamGifticonRequestDto;
import com.gdsc.moa.domain.team.dto.request.TeamCreateRequestDto;
import com.gdsc.moa.domain.team.dto.request.TeamJoinRequestDto;
import com.gdsc.moa.domain.team.dto.response.ShareTeamGifticonResponseDto;
import com.gdsc.moa.domain.team.dto.response.TeamCreateResponseDto;
import com.gdsc.moa.domain.team.dto.response.TeamListResponseDto;
import com.gdsc.moa.domain.team.service.TeamService;
import com.gdsc.moa.global.dto.MoaApiResponse;
import com.gdsc.moa.global.jwt.oauth.UserInfo;
import com.gdsc.moa.global.message.TeamMessage;
import com.gdsc.moa.global.paging.PagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/team")

public class TeamController {
    private final TeamService teamService;
    @PostMapping("")
    public MoaApiResponse<TeamCreateResponseDto> createTeam(@RequestBody TeamCreateRequestDto teamCreateRequestDto, @AuthenticationPrincipal UserInfo userInfo) {
        TeamCreateResponseDto response = teamService.createTeam(teamCreateRequestDto, userInfo.getEmail());
        return MoaApiResponse.createResponse(response, TeamMessage.TEAM_CREATE_SUCCESS);
    }

    @PostMapping("/join")
    public MoaApiResponse<TeamCreateResponseDto> joinTeam(@RequestBody TeamJoinRequestDto teamJoinRequestDto, @AuthenticationPrincipal UserInfo userInfo) {
        TeamCreateResponseDto response = teamService.joinTeam(teamJoinRequestDto, userInfo.getEmail());
        return MoaApiResponse.createResponse(response, TeamMessage.TEAM_JOIN_SUCCESS);
    }

    @GetMapping("/my_team")
    public MoaApiResponse<List<TeamListResponseDto>> getMyTeam(@AuthenticationPrincipal UserInfo userInfo) {
        List<TeamListResponseDto> responses = teamService.getMyTeams(userInfo.getEmail());
        return MoaApiResponse.createResponse(responses, TeamMessage.TEAM_GET_SUCCESS);
    }

    @DeleteMapping("/{teamId}")
    public MoaApiResponse<TeamCreateResponseDto> leaveTeam(@PathVariable Long teamId, @AuthenticationPrincipal UserInfo userInfo) {
        teamService.leaveTeam(teamId, userInfo.getEmail());
        return MoaApiResponse.createResponse(null, TeamMessage.TEAM_LEAVE_SUCCESS);
    }
    // TODO: 10/31/22 팀에 기프티콘 추가
    @PostMapping("/gifticon")
    public MoaApiResponse<ShareTeamGifticonResponseDto> shareTeamGifticon(@RequestBody ShareTeamGifticonRequestDto shareTeamGifticonRequestDto, @AuthenticationPrincipal UserInfo userInfo) {
        ShareTeamGifticonResponseDto response = teamService.shareTeamGifticon(shareTeamGifticonRequestDto, userInfo.getEmail());
        return MoaApiResponse.createResponse(response, TeamMessage.TEAM_SHARE_GIFTICON_SUCCESS);
    }
    // TODO: 10/31/22 팀에 해당되는 기프티콘 가져오기
    @GetMapping("/gifticon/{teamId}")
    public MoaApiResponse<PagingResponse<GifticonResponseDto>> getTeamGifticon(@PathVariable Long teamId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal UserInfo userInfo) {
        Pageable pageable = PageRequest.of(page, size);
        PagingResponse<GifticonResponseDto> response = teamService.getTeamGifticon(teamId, pageable, userInfo.getEmail());
        return MoaApiResponse.createResponse(response, TeamMessage.TEAM_GET_GIFTICON_SUCCESS);
    }

}
