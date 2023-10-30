package com.gdsc.moa.domain.team.controller;

import com.gdsc.moa.domain.team.dto.request.TeamCreateRequestDto;
import com.gdsc.moa.domain.team.dto.request.TeamJoinRequestDto;
import com.gdsc.moa.domain.team.dto.response.TeamCreateResponseDto;
import com.gdsc.moa.domain.team.service.TeamService;
import com.gdsc.moa.global.dto.MoaApiResponse;
import com.gdsc.moa.global.jwt.oauth.UserInfo;
import com.gdsc.moa.global.message.TeamMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
