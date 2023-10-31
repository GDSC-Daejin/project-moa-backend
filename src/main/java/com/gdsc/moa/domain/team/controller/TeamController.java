package com.gdsc.moa.domain.team.controller;

import com.gdsc.moa.domain.team.dto.request.TeamCreateRequestDto;
import com.gdsc.moa.domain.team.dto.request.TeamJoinRequestDto;
import com.gdsc.moa.domain.team.dto.response.TeamCreateResponseDto;
import com.gdsc.moa.domain.team.dto.response.TeamListResponseDto;
import com.gdsc.moa.domain.team.service.TeamService;
import com.gdsc.moa.global.dto.MoaApiResponse;
import com.gdsc.moa.global.exception.ApiException;
import com.gdsc.moa.global.jwt.oauth.UserInfo;
import com.gdsc.moa.global.message.TeamMessage;
import lombok.RequiredArgsConstructor;
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

    // TODO: 10/31/22 팀 탈퇴
    // TODO: 10/31/22 팀에 기프티콘 추가
    // TODO: 10/31/22 팀에 해당되는 기프티콘 가져오기

}
