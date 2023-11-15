package com.gdsc.moa.domain.gifticon.service;

import com.gdsc.moa.domain.category.entity.CategoryEntity;
import com.gdsc.moa.domain.category.repository.CategoryRepository;
import com.gdsc.moa.domain.gifticon.dto.request.FilterListDto;
import com.gdsc.moa.domain.gifticon.dto.request.GifticonRequestDto;
import com.gdsc.moa.domain.gifticon.dto.request.GifticonUpdateRequestDto;
import com.gdsc.moa.domain.gifticon.dto.request.UseMoneyRequestDto;
import com.gdsc.moa.domain.gifticon.dto.response.GifticonDetailResponseDto;
import com.gdsc.moa.domain.gifticon.dto.response.GifticonResponseDto;
import com.gdsc.moa.domain.gifticon.dto.response.GifticonListResponse;
import com.gdsc.moa.domain.gifticon.dto.response.UseMoneyResponseDto;
import com.gdsc.moa.domain.gifticon.entity.GifticonEntity;
import com.gdsc.moa.domain.gifticon.entity.GifticonHistoryEntity;
import com.gdsc.moa.domain.gifticon.entity.Status;
import com.gdsc.moa.domain.gifticon.repository.GifticonHistoryRepository;
import com.gdsc.moa.domain.gifticon.repository.GifticonRepository;
import com.gdsc.moa.domain.team.dto.response.TeamListResponseDto;
import com.gdsc.moa.domain.user.entity.UserEntity;
import com.gdsc.moa.domain.user.repository.UserRepository;
import com.gdsc.moa.global.exception.ApiException;
import com.gdsc.moa.global.message.GifticonMessage;
import com.gdsc.moa.global.message.UserMessage;
import com.gdsc.moa.global.paging.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GifticonService {
    private final UserRepository userRepository;
    private final GifticonRepository gifticonRepository;
    private final CategoryRepository categoryRepository;
    private final GifticonHistoryRepository gifticonHistoryRepository;

    //Gifticon 생성
    @Transactional
    public GifticonResponseDto createGifticon(GifticonRequestDto gifticonRequestDto, String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. email=" + email));
        CategoryEntity category;

        if (gifticonRequestDto.getCategoryId() == null) {
            // Check if a category named "미분류" already exists for the user
            Optional<CategoryEntity> uncategorizedCategory = categoryRepository.findByUserAndCategoryName(user, "미분류");

            if (uncategorizedCategory.isPresent()) {
                // If "미분류" category already exists, use it
                category = uncategorizedCategory.get();
            } else {
                // If "미분류" category doesn't exist, create it
                category = new CategoryEntity();
                category.createCategory("미분류", user);
                category = categoryRepository.save(category);
            }
        } else {
            category = findCategory(gifticonRequestDto.getCategoryId());
        }
        GifticonEntity gifticonEntity = new GifticonEntity(gifticonRequestDto,user,category);
        GifticonEntity savedGifticon = gifticonRepository.save(gifticonEntity);

        return new GifticonResponseDto(savedGifticon);
    }
    @Transactional
    public void deleteGifticon(Long gifticonId, String email) {
        GifticonEntity gifticonEntity = finduserandgifticon(gifticonId, email);
        gifticonRepository.delete(gifticonEntity);
    }
    @Transactional
    public GifticonDetailResponseDto getGifticonDetail(Long gifticonId, String email, List<TeamListResponseDto> teamList) {
        GifticonEntity gifticonEntity = finduserandgifticon(gifticonId, email);
        GifticonResponseDto GifticonResponseDto = new GifticonResponseDto(gifticonEntity);
        return new GifticonDetailResponseDto(GifticonResponseDto, teamList);
    }
    @Transactional
    public GifticonResponseDto updateGifticon(GifticonUpdateRequestDto gifticonUpdateRequestDto, String email) {
        UserEntity user = findUser(email);
        CategoryEntity category = findCategory(gifticonUpdateRequestDto.getCategoryId());
        GifticonEntity gifticonEntity = findGifticon(gifticonUpdateRequestDto.getId());
        if (!gifticonEntity.getUser().equals(user))
            throw new ApiException(GifticonMessage.GIFTICON_NOT_BELONG_TO_USER);

        GifticonEntity updatedGifticon = new GifticonEntity(gifticonUpdateRequestDto, user,category);

        updatedGifticon = gifticonRepository.save(updatedGifticon);

        return new GifticonResponseDto(updatedGifticon);
    }


    @Transactional
    public PageResponse<GifticonListResponse> getUsableGifticon(Pageable pageable, String email) {
        UserEntity user = findUser(email);
        Page<GifticonEntity> gifticonEntities = gifticonRepository.findByUserAndStatus(user, Status.AVAILABLE, pageable);

        return createPagingResponse(gifticonEntities);
    }

    @Transactional
    public PageResponse<GifticonListResponse> getDisableGifticon(Pageable pageable, String email) {
        UserEntity user = findUser(email);
        Page<GifticonEntity> gifticonEntities = gifticonRepository.findByUserAndStatus(user, Status.UNAVAILABLE, pageable);

        return createPagingResponse(gifticonEntities);
    }

    @Transactional
    public PageResponse<GifticonListResponse> getAllGifticon(Pageable pageable, String email) {
        UserEntity user = findUser(email);
        Page<GifticonEntity> gifticonEntities = gifticonRepository.findByUser(user, pageable);

        return createPagingResponse(gifticonEntities);
    }

    @Transactional
    public PageResponse<GifticonListResponse> getGifticonByCategory(Pageable pageable, Long categoryId, String email) {
        UserEntity user = findUser(email);
        CategoryEntity category = findCategory(categoryId);
        Page<GifticonEntity> gifticonEntities = gifticonRepository.findByUserAndCategory(user, category, pageable);

        return createPagingResponse(gifticonEntities);
    }

    @Transactional
    public PageResponse<GifticonListResponse> getRecentGifticon(Pageable pageable, String email) {
        UserEntity user = findUser(email);
        Page<GifticonEntity> gifticonEntities = gifticonRepository.findBYUserOrderByDueDateDesc (user,Status.AVAILABLE, pageable);

        return createPagingResponse(gifticonEntities);
    }

    @Transactional
    public UseMoneyResponseDto addMoneyHistory(UseMoneyRequestDto useMoneyRequestDto, String email) {
        UserEntity user = findUser(email);
        GifticonEntity gifticonEntity = findGifticon(useMoneyRequestDto.getGifticonId());
        List<GifticonHistoryEntity> gifticonLastHistoryList = gifticonHistoryRepository.findLastHistory(gifticonEntity);
        GifticonHistoryEntity gifticonLastHistory = (gifticonLastHistoryList.isEmpty())
                ? new GifticonHistoryEntity(user, gifticonEntity, useMoneyRequestDto.getMoney())
                : new GifticonHistoryEntity(user, gifticonEntity, gifticonLastHistoryList.get(0).getLeftPrice(), useMoneyRequestDto.getMoney());

        GifticonHistoryEntity savedHistory =gifticonHistoryRepository.save(gifticonLastHistory);


        return new UseMoneyResponseDto(savedHistory);
    }

    public List<UseMoneyResponseDto> getMoneyHistory(Long gifticonId, String email) {
        UserEntity user = findUser(email);
        GifticonEntity gifticonEntity = findGifticon(gifticonId);
        List<GifticonHistoryEntity> historyList = gifticonHistoryRepository.findAllByGifticon(gifticonEntity);

        List<UseMoneyResponseDto> responseList = historyList.stream()
                .map(history -> new UseMoneyResponseDto(
                        history.getId(),
                        history.getUsedPrice(),
                        history.getLeftPrice(),
                        history.getUsedDate(),
                        history.getGifticon().getGifticonId(),
                        history.getUser()
                ))
                .collect(Collectors.toList());

        return responseList;
    }

    @Transactional
    public GifticonDetailResponseDto useGifticon(Long gifticonId, String email, List<TeamListResponseDto> teamList) {
        UserEntity user = findUser(email);
        GifticonEntity gifticonEntity = findGifticon(gifticonId);
        if(gifticonEntity.getStatus() == Status.AVAILABLE)
            gifticonEntity.useGifticon();
        else
            gifticonEntity.cancelUseGifticon();
        GifticonEntity savedGifticon = gifticonRepository.save(gifticonEntity);
        return new GifticonDetailResponseDto(new GifticonResponseDto(savedGifticon), teamList);

    }

    public Long getGifticonCount(String email) {
        UserEntity user = findUser(email);
        return gifticonRepository.countByUser(user);
    }

    private PageResponse<GifticonListResponse> createPagingResponse(Page<GifticonEntity> gifticonEntities) {
        List<GifticonListResponse> gifticonResponses = gifticonEntities.stream()
                .map(GifticonListResponse::new)
                .collect(Collectors.toList());

        Page<GifticonListResponse> responsePage = new PageImpl<>(gifticonResponses, gifticonEntities.getPageable(), gifticonEntities.getTotalElements());

        return new PageResponse<>(responsePage);
    }

    @Transactional
    public PageResponse<GifticonListResponse> getAllRequestGifticonList(FilterListDto request, Pageable pageable, String email) {
        UserEntity user = findUser(email);
        Page<GifticonEntity> gifticonEntities;
        switch (request) {
            case ALL_NAME_ASC -> gifticonEntities = gifticonRepository.findByUserOrderByNameAsc(user, pageable);
            case ALL_RECENT_EXPIRATION ->
                    gifticonEntities = gifticonRepository.findBYUserOrderByDueDateDesc(user, Status.AVAILABLE, pageable);
            case All_USABLE_NAME_DESC ->
                    gifticonEntities = gifticonRepository.findByUserAndStatusOrderByNameDesc(user, Status.AVAILABLE, pageable);
            case All_USABLE_NAME_ASC ->
                    gifticonEntities = gifticonRepository.findByUserAndStatusOrderByNameAsc(user, Status.AVAILABLE, pageable);
            case All_USABLE_RECENT_EXPIRATION ->
                    gifticonEntities = gifticonRepository.findByUserAndStatusOrderByDueDateDesc(user, Status.AVAILABLE, pageable);
            case All_USED_NAME_DESC ->
                    gifticonEntities = gifticonRepository.findByUserAndStatusOrderByNameDesc(user, Status.UNAVAILABLE, pageable);
            case ALL_USED_NAME_ASC ->
                    gifticonEntities = gifticonRepository.findByUserAndStatusOrderByNameAsc(user, Status.UNAVAILABLE, pageable);
            case ALL_USED_RECENT_EXPIRATION ->
                    gifticonEntities = gifticonRepository.findByUserAndStatusOrderByDueDateDesc(user, Status.UNAVAILABLE, pageable);
            default -> gifticonEntities = gifticonRepository.findByUserOrderByNameDesc(user, pageable);
        }
        return createPagingResponse(gifticonEntities);
    }

    private GifticonEntity finduserandgifticon(Long gifticonId, String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(UserMessage.USER_NOT_FOUND));
        return gifticonRepository.findById(gifticonId).orElseThrow(() -> new ApiException(GifticonMessage.GIFTICON_NOT_FOUND) );
    }

    private GifticonEntity findGifticon(Long gifticonId) {
        return gifticonRepository.findById(gifticonId).orElseThrow(() -> new ApiException(GifticonMessage.GIFTICON_NOT_FOUND) );
    }

    private UserEntity findUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ApiException(UserMessage.USER_NOT_FOUND));
    }

    private CategoryEntity findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new ApiException(GifticonMessage.CATEGORY_NOT_FOUND));
    }

    public Long getUsableGifticonCount(String email) {
        UserEntity user = findUser(email);
        return gifticonRepository.countByUserAndStatus(user, Status.AVAILABLE);
    }

    public Long getUsedGifticonCount(String email) {
        UserEntity user = findUser(email);
        return gifticonRepository.countByUserAndStatus(user, Status.UNAVAILABLE);
    }
}
