package com.gdsc.moa.domain.team.controller;

import com.gdsc.moa.domain.gifticon.dto.request.FilterListDto;
import com.gdsc.moa.domain.gifticon.dto.response.GifticonListResponse;
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
import com.gdsc.moa.global.message.GifticonMessage;
import com.gdsc.moa.global.message.TeamMessage;
import com.gdsc.moa.global.paging.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "팀 생성하기")
    @PostMapping("")
    public MoaApiResponse<TeamCreateResponseDto> createTeam(@RequestBody TeamCreateRequestDto teamCreateRequestDto, @AuthenticationPrincipal UserInfo userInfo) {
        TeamCreateResponseDto response = teamService.createTeam(teamCreateRequestDto, userInfo.getEmail());
        return MoaApiResponse.createResponse(response, TeamMessage.TEAM_CREATE_SUCCESS);
    }

    @Operation(summary = "팀 가입하기")
    @PostMapping("/join")
    public MoaApiResponse<TeamCreateResponseDto> joinTeam(@RequestBody TeamJoinRequestDto teamJoinRequestDto, @AuthenticationPrincipal UserInfo userInfo) {
        TeamCreateResponseDto response = teamService.joinTeam(teamJoinRequestDto, userInfo.getEmail());
        return MoaApiResponse.createResponse(response, TeamMessage.TEAM_JOIN_SUCCESS);
    }

    @Operation(summary = "팀 목록 가져오기")
    @GetMapping("/my_team")
    public MoaApiResponse<List<TeamListResponseDto>> getMyTeam(@AuthenticationPrincipal UserInfo userInfo) {
        List<TeamListResponseDto> responses = teamService.getMyTeams(userInfo.getEmail());
        return MoaApiResponse.createResponse(responses, TeamMessage.TEAM_GET_SUCCESS);
    }

    @Operation(summary = "팀 수정하기")
    @PutMapping("/{teamId}")
    public MoaApiResponse<TeamCreateResponseDto> updateTeam(@PathVariable Long teamId, @RequestBody TeamCreateRequestDto teamCreateRequestDto, @AuthenticationPrincipal UserInfo userInfo) {
        TeamCreateResponseDto response = teamService.updateTeam(teamId, teamCreateRequestDto, userInfo.getEmail());
        return MoaApiResponse.createResponse(response, TeamMessage.TEAM_UPDATE_SUCCESS);
    }

    @Operation(summary = "팀 초대코드 갱신")
    @PutMapping("/invite_code/{teamId}")
    public MoaApiResponse<TeamCreateResponseDto> updateTeamInviteCode(@PathVariable Long teamId, @AuthenticationPrincipal UserInfo userInfo) {
        TeamCreateResponseDto response = teamService.updateTeamInviteCode(teamId, userInfo.getEmail());
        return MoaApiResponse.createResponse(response, TeamMessage.TEAM_UPDATE_INVITE_CODE_SUCCESS);
    }

    @Operation(summary = "팀 탈퇴하기")
    @DeleteMapping("/{teamId}")
    public MoaApiResponse<TeamCreateResponseDto> leaveTeam(@PathVariable Long teamId, @AuthenticationPrincipal UserInfo userInfo) {
        teamService.leaveTeam(teamId, userInfo.getEmail());
        return MoaApiResponse.createResponse(null, TeamMessage.TEAM_LEAVE_SUCCESS);
    }

    @Operation(summary = "팀에 기프티콘 공유하기")
    @PostMapping("/gifticon")
    public MoaApiResponse<ShareTeamGifticonResponseDto> shareTeamGifticon(@RequestBody ShareTeamGifticonRequestDto shareTeamGifticonRequestDto, @AuthenticationPrincipal UserInfo userInfo) {
        ShareTeamGifticonResponseDto response = teamService.shareTeamGifticon(shareTeamGifticonRequestDto, userInfo.getEmail());
        return MoaApiResponse.createResponse(response, TeamMessage.TEAM_SHARE_GIFTICON_SUCCESS);
    }

    @Operation(summary = "팀에 속한 기프티곤 가져오기")
    @GetMapping("/gifticon/{teamId}")
    public MoaApiResponse<PageResponse<GifticonResponseDto>> getTeamGifticon(@PathVariable Long teamId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal UserInfo userInfo) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<GifticonResponseDto> response = teamService.getTeamGifticon(teamId, pageable, userInfo.getEmail());
        return MoaApiResponse.createResponse(response, TeamMessage.TEAM_GET_GIFTICON_SUCCESS);
    }

    @Operation(summary = "팀 기프티콘 각종 목록 조회(필터)")
    @GetMapping("/gifticon/{teamId}/{request}")
    public MoaApiResponse<PageResponse<GifticonListResponse>> getAllTeamGifticonList(@PathVariable FilterListDto request, @PathVariable Long teamId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal UserInfo user) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<GifticonListResponse> response = teamService.getAllTeamRequestGifticonList(teamId, request, pageable, user.getEmail());
        return MoaApiResponse.createResponse(response, TeamMessage.TEAM_GET_GIFTICON_SUCCESS);
    }

    @Operation(summary = "팀 에서 최근 사용한 기프티콘 리스트")
    @GetMapping("/gifticon/recent/{teamId}")
    public MoaApiResponse<PageResponse<GifticonListResponse>> getRecentTeamGifticonList(@PathVariable Long teamId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal UserInfo user) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<GifticonListResponse> response = teamService.getRecentTeamGifticonList(teamId, pageable, user.getEmail());
        return MoaApiResponse.createResponse(response, TeamMessage.TEAM_GET_GIFTICON_SUCCESS);
    }

}
