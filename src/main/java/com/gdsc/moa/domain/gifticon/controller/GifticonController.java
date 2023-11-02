package com.gdsc.moa.domain.gifticon.controller;

import com.gdsc.moa.domain.gifticon.dto.request.GifticonRequestDto;
import com.gdsc.moa.domain.gifticon.dto.request.GifticonUpdateRequestDto;
import com.gdsc.moa.domain.gifticon.dto.response.GifticonResponseDto;
import com.gdsc.moa.domain.gifticon.dto.response.GifticonListResponse;
import com.gdsc.moa.domain.gifticon.service.GifticonService;
import com.gdsc.moa.global.dto.MoaApiResponse;
import com.gdsc.moa.global.jwt.oauth.UserInfo;
import com.gdsc.moa.global.message.GifticonMessage;
import com.gdsc.moa.global.paging.PagingResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gifticon")
public class GifticonController {
    private final GifticonService gifticonService;

    @Operation(summary = "기프티콘 생성")
    @PostMapping("")
    public MoaApiResponse<GifticonResponseDto> createGifticon(@RequestBody GifticonRequestDto gifticonRequestDto, @AuthenticationPrincipal UserInfo user) {

        GifticonResponseDto response = gifticonService.createGifticon(gifticonRequestDto, user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_CREATE_SUCCESS);
    }

    @Operation(summary = "기프티콘 조회(상세페이지)")
    @GetMapping("/{gifticonId}")
    public MoaApiResponse<GifticonResponseDto> getGifticon(@PathVariable Long gifticonId, @AuthenticationPrincipal UserInfo user) {
        GifticonResponseDto response = gifticonService.getGifticonDetail(gifticonId, user.getEmail());
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
    public MoaApiResponse<PagingResponse<GifticonListResponse>> getUsableGifticon(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal UserInfo user) {
        Pageable pageable = PageRequest.of(page, size);
        PagingResponse<GifticonListResponse> response = gifticonService.getUsableGifticon(pageable, user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_GET_SUCCESS);
    }

    @Operation(summary = "사용불가 기프티콘 리스트")
    @GetMapping("/disable_list")
    public MoaApiResponse<PagingResponse<GifticonListResponse>> getDisableGifticon(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal UserInfo user) {
        Pageable pageable = PageRequest.of(page, size);
        PagingResponse<GifticonListResponse> response = gifticonService.getDisableGifticon(pageable, user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_GET_SUCCESS);
    }

    @Operation(summary = "전체 기프티콘 리스트")
    @GetMapping("/all_list")
    public MoaApiResponse<PagingResponse<GifticonListResponse>> getAllGifticon(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal UserInfo user) {
        Pageable pageable = PageRequest.of(page, size);
        PagingResponse<GifticonListResponse> response = gifticonService.getAllGifticon(pageable, user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_GET_SUCCESS);
    }

    @Operation(summary = "카테고리별 기프티콘 리스트")
    @GetMapping("/category_list")
    public MoaApiResponse<PagingResponse<GifticonListResponse>> getGifticonByCategory(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam Long categoryId, @AuthenticationPrincipal UserInfo user) {
        Pageable pageable = PageRequest.of(page, size);
        PagingResponse<GifticonListResponse> response = gifticonService.getGifticonByCategory(pageable, categoryId, user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_GET_SUCCESS);
    }

    @Operation(summary = "최근 기프티콘 리스트")
    @GetMapping("/recent_list")
    public MoaApiResponse<PagingResponse<GifticonListResponse>> getRecentGifticon(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal UserInfo user) {
        Pageable pageable = PageRequest.of(page, size);
        PagingResponse<GifticonListResponse> response = gifticonService.getRecentGifticon(pageable, user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_GET_SUCCESS);
    }
}
