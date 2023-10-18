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

@RestController
@RequiredArgsConstructor
public class GifticonController {
    private final GifticonService gifticonService;


    @GetMapping("/gifticon/{gifticonId}")
    public MoaApiResponse<GifticonResponseDto> getGifticon(@PathVariable Long gifticonId, @AuthenticationPrincipal UserInfo user){
        GifticonResponseDto response = gifticonService.getGifticonDetail(gifticonId, user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_CREATE_SUCCESS);
    }


    @PostMapping("/gifticon")
    public MoaApiResponse<GifticonResponseDto> createGifticon(@RequestBody GifticonRequestDto gifticonRequestDto, @AuthenticationPrincipal UserInfo user){
        GifticonResponseDto response = gifticonService.createGifticon(gifticonRequestDto, user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_CREATE_SUCCESS);
    }

    @DeleteMapping("/gifticon/{gifticonId}")
    public MoaApiResponse<GifticonResponseDto> deleteGifticon(@PathVariable Long gifticonId, @AuthenticationPrincipal UserInfo user){
        gifticonService.deleteGifticon(gifticonId , user.getEmail());
        return MoaApiResponse.createResponse(null, GifticonMessage.GIFTICON_DELETE_SUCCESS);
    }
}
