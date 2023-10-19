package com.gdsc.moa.domain.gifticon.controller;

import com.gdsc.moa.domain.gifticon.dto.request.GifticonRequestDto;
import com.gdsc.moa.domain.gifticon.dto.response.GifticonResponseDto;
import com.gdsc.moa.domain.gifticon.service.GifticonService;
import com.gdsc.moa.global.dto.MoaApiResponse;
import com.gdsc.moa.global.jwt.oauth.UserInfo;
import com.gdsc.moa.global.message.GifticonMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gifticon")
public class GifticonController {
    private final GifticonService gifticonService;


    @PostMapping("/")
    public MoaApiResponse<GifticonResponseDto> createGifticon(@RequestBody GifticonRequestDto gifticonRequestDto, @AuthenticationPrincipal UserInfo user){

        GifticonResponseDto response = gifticonService.createGifticon(gifticonRequestDto, user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_CREATE_SUCCESS);
    }

    @GetMapping("/{gifticonId}")
    public MoaApiResponse<GifticonResponseDto> getGifticon(@PathVariable Long gifticonId, @AuthenticationPrincipal UserInfo user) {
        GifticonResponseDto response = gifticonService.getGifticonDetail(gifticonId, user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_CREATE_SUCCESS);
    }

    @PutMapping("/{gifticonId}")
    public MoaApiResponse<GifticonResponseDto> updateGifticon(@PathVariable Long gifticonId, @RequestBody GifticonRequestDto gifticonRequestDto, @AuthenticationPrincipal UserInfo user) {
        GifticonResponseDto response = gifticonService.updateGifticon(gifticonId, gifticonRequestDto, user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_UPDATE_SUCCESS);
    }

    @DeleteMapping("/{gifticonId}")

    public MoaApiResponse<GifticonResponseDto> deleteGifticon(@PathVariable Long gifticonId, @AuthenticationPrincipal UserInfo user) {
        gifticonService.deleteGifticon(gifticonId, user.getEmail());
        return MoaApiResponse.createResponse(null, GifticonMessage.GIFTICON_DELETE_SUCCESS);
    }
}
