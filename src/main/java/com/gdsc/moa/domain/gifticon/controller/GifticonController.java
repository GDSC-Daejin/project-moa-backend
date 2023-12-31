package com.gdsc.moa.domain.gifticon.controller;

import com.gdsc.moa.domain.gifticon.dto.request.FilterListDto;
import com.gdsc.moa.domain.gifticon.dto.request.GifticonRequestDto;
import com.gdsc.moa.domain.gifticon.dto.request.GifticonUpdateRequestDto;
import com.gdsc.moa.domain.gifticon.dto.request.UseMoneyRequestDto;
import com.gdsc.moa.domain.gifticon.dto.response.GifticonDetailResponseDto;
import com.gdsc.moa.domain.gifticon.dto.response.GifticonResponseDto;
import com.gdsc.moa.domain.gifticon.dto.response.GifticonListResponse;
import com.gdsc.moa.domain.gifticon.dto.response.UseMoneyResponseDto;
import com.gdsc.moa.domain.gifticon.entity.GifticonHistoryEntity;
import com.gdsc.moa.domain.gifticon.service.GifticonService;
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
@RequestMapping("/api/v1/gifticon")
public class GifticonController {
    private final GifticonService gifticonService;
    private final TeamService teamService;

    @Operation(summary = "기프티콘 생성")
    @PostMapping("")
    public MoaApiResponse<GifticonResponseDto> createGifticon(@RequestBody GifticonRequestDto gifticonRequestDto, @AuthenticationPrincipal UserInfo user) {

        GifticonResponseDto response = gifticonService.createGifticon(gifticonRequestDto, user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_CREATE_SUCCESS);
    }

    @Operation(summary = "기프티콘 조회(상세페이지)")
    @GetMapping("/{gifticonId}")
    public MoaApiResponse<GifticonDetailResponseDto> getGifticon(@PathVariable Long gifticonId, @AuthenticationPrincipal UserInfo user) {
        List<TeamListResponseDto> teamList = teamService.getTeamListByGifticon(gifticonId, user.getEmail());
        GifticonDetailResponseDto response = gifticonService.getGifticonDetail(gifticonId, user.getEmail(), teamList);
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_GET_SUCCESS);
    }

    @Operation(summary = "기프티콘 수정")
    @PutMapping("")
    public MoaApiResponse<GifticonResponseDto> updateGifticon(@RequestBody GifticonUpdateRequestDto gifticonUpdateRequestDto, @AuthenticationPrincipal UserInfo user) {
        GifticonResponseDto response = gifticonService.updateGifticon(gifticonUpdateRequestDto, user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_UPDATE_SUCCESS);
    }

    @Operation(summary = "기프티콘 삭제")
    @DeleteMapping("/{gifticonId}")
    public MoaApiResponse<GifticonResponseDto> deleteGifticon(@PathVariable Long gifticonId, @AuthenticationPrincipal UserInfo user) {
        gifticonService.deleteGifticon(gifticonId, user.getEmail());
        return MoaApiResponse.createResponse(null, GifticonMessage.GIFTICON_DELETE_SUCCESS);
    }

    @Operation(summary = "사용가능한 기프티콘 리스트")
    @GetMapping("/usable_list")
    public MoaApiResponse<PageResponse<GifticonListResponse>> getUsableGifticon(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal UserInfo user) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<GifticonListResponse> response = gifticonService.getUsableGifticon(pageable, user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_GET_SUCCESS);
    }

    @Operation(summary = "사용불가 기프티콘 리스트")
    @GetMapping("/disable_list")
    public MoaApiResponse<PageResponse<GifticonListResponse>> getDisableGifticon(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal UserInfo user) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<GifticonListResponse> response = gifticonService.getDisableGifticon(pageable, user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_GET_SUCCESS);
    }

    @Operation(summary = "전체 기프티콘 리스트")
    @GetMapping("/all_list")
    public MoaApiResponse<PageResponse<GifticonListResponse>> getAllGifticon(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal UserInfo user) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<GifticonListResponse> response = gifticonService.getAllGifticon(pageable, user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_GET_SUCCESS);
    }

    @Operation(summary = "카테고리별 기프티콘 리스트")
    @GetMapping("/category_list")
    public MoaApiResponse<PageResponse<GifticonListResponse>> getGifticonByCategory(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam Long categoryId, @AuthenticationPrincipal UserInfo user) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<GifticonListResponse> response = gifticonService.getGifticonByCategory(pageable, categoryId, user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_GET_SUCCESS);
    }

    @Operation(summary = "최근 기프티콘 리스트")
    @GetMapping("/recent_list")
    public MoaApiResponse<PageResponse<GifticonListResponse>> getRecentGifticon(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal UserInfo user) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<GifticonListResponse> response = gifticonService.getRecentGifticon(pageable, user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_GET_SUCCESS);
    }


    @Operation(summary = "내 기프티콘 개수")
    @GetMapping("/count")
    public MoaApiResponse<Long> getGifticonCount(@AuthenticationPrincipal UserInfo user) {
        Long response = gifticonService.getGifticonCount(user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_GET_SUCCESS);
    }

    @Operation(summary = "사용가능한 기프티콘 개수")
    @GetMapping("/usable_count")
    public MoaApiResponse<Long> getUsableGifticonCount(@AuthenticationPrincipal UserInfo user) {
        Long response = gifticonService.getUsableGifticonCount(user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_GET_SUCCESS);
    }

    @Operation(summary = "사용한 기프티콘 개수")
    @GetMapping("/used_count")
    public MoaApiResponse<Long> getUsedGifticonCount(@AuthenticationPrincipal UserInfo user) {
        Long response = gifticonService.getUsedGifticonCount(user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_GET_SUCCESS);
    }


    @Operation(summary = "금액권 사용금액 입력")
    @PostMapping("/money")
    public MoaApiResponse<UseMoneyResponseDto> addMoneyHistory(@RequestBody UseMoneyRequestDto useMoneyRequestDto, @AuthenticationPrincipal UserInfo user) {
        UseMoneyResponseDto response = gifticonService.addMoneyHistory(useMoneyRequestDto, user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.MONEY_HISTORY_CREATE_SUCCESS);

    }

    @Operation(summary = "금액권 사용기록 가져오기")
    @GetMapping("/money/{gifticonId}")
    public MoaApiResponse<List<UseMoneyResponseDto>> getMoneyHistory(@PathVariable Long gifticonId, @AuthenticationPrincipal UserInfo user) {
        List<UseMoneyResponseDto> responseDtos = gifticonService.getMoneyHistory(gifticonId, user.getEmail());
        return MoaApiResponse.createResponse(responseDtos, GifticonMessage.MONEY_HISTORY_GET_SUCCESS);

    }

    @Operation(summary = "기프티콘 사용완료버튼(다시누르면 취소)")
    @PutMapping("/use/{gifticonId}")
    public MoaApiResponse<GifticonDetailResponseDto> useGifticon(@PathVariable Long gifticonId, @AuthenticationPrincipal UserInfo user) {
        List<TeamListResponseDto> teamList = teamService.getTeamListByGifticon(gifticonId, user.getEmail());
        GifticonDetailResponseDto response = gifticonService.useGifticon(gifticonId, user.getEmail(),teamList);
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_USE_SUCCESS);
    }

    @Operation(summary = "개인 기프티콘 각종 목록 조회(필터)")
    @GetMapping("/list/{request}")
    public MoaApiResponse<PageResponse<GifticonListResponse>> getAllFilterGifticonList(@PathVariable FilterListDto request, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal UserInfo user) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<GifticonListResponse> response = gifticonService.getAllRequestGifticonList(request, pageable, user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_GET_SUCCESS);
    }
}
