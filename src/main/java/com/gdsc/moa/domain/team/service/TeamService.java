package com.gdsc.moa.domain.team.service;

import com.gdsc.moa.domain.gifticon.dto.request.FilterListDto;
import com.gdsc.moa.domain.gifticon.dto.response.GifticonListResponse;
import com.gdsc.moa.domain.gifticon.dto.response.GifticonResponseDto;
import com.gdsc.moa.domain.gifticon.entity.GifticonEntity;
import com.gdsc.moa.domain.gifticon.entity.Status;
import com.gdsc.moa.domain.gifticon.repository.GifticonRepository;
import com.gdsc.moa.domain.gifticon.service.GifticonService;
import com.gdsc.moa.domain.team.dto.request.ShareTeamGifticonRequestDto;
import com.gdsc.moa.domain.team.dto.request.TeamJoinRequestDto;
import com.gdsc.moa.domain.team.dto.response.ShareTeamGifticonResponseDto;
import com.gdsc.moa.domain.team.dto.response.TeamListResponseDto;
import com.gdsc.moa.domain.team.entity.TeamGifticonEntity;
import com.gdsc.moa.domain.team.entity.TeamUserEntity;
import com.gdsc.moa.domain.team.repository.TeamGifticonRepository;
import com.gdsc.moa.domain.team.repository.TeamRepository;
import com.gdsc.moa.domain.team.dto.request.TeamCreateRequestDto;
import com.gdsc.moa.domain.team.dto.response.TeamCreateResponseDto;
import com.gdsc.moa.domain.team.entity.TeamEntity;
import com.gdsc.moa.domain.team.repository.TeamUserRepository;
import com.gdsc.moa.domain.user.entity.UserEntity;
import com.gdsc.moa.domain.user.repository.UserRepository;
import com.gdsc.moa.global.exception.ApiException;
import com.gdsc.moa.global.message.GifticonMessage;
import com.gdsc.moa.global.message.TeamMessage;
import com.gdsc.moa.global.message.UserMessage;
import com.gdsc.moa.global.paging.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamUserRepository teamUserRepository;
    private final GifticonRepository gifticonRepository;
    private final TeamGifticonRepository teamGifticonRepository;
    private final GifticonService gifticonService;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 16;

    @Transactional
    public TeamCreateResponseDto createTeam(TeamCreateRequestDto teamCreateRequestDto, String email) {
        UserEntity user = findUser(email);
        // 랜덤 초대 코드 생성
        String inviteCode = generateInviteCode();

        // TeamEntity를 생성
        TeamEntity teamEntity = new TeamEntity(teamCreateRequestDto, user, inviteCode);
        TeamUserEntity teamUserEntity = new TeamUserEntity(teamEntity, user);

        // TeamEntity를 데이터베이스에 저장
        teamEntity = teamRepository.save(teamEntity);
        teamUserRepository.save(teamUserEntity);

        return new TeamCreateResponseDto(teamEntity);

    }

    @Transactional
    public TeamCreateResponseDto joinTeam(TeamJoinRequestDto teamJoinRequestDto, String email) {
        UserEntity user = findUser(email);
        TeamEntity teamEntity = findTeamByTeamCode(teamJoinRequestDto.getTeamCode());
        if (isUserAlreadyJoinedTeam(teamEntity, user)) {
            throw new ApiException(TeamMessage.TEAM_ALREADY_JOINED);
        }
        //팀 가입
        TeamUserEntity teamUserEntity = new TeamUserEntity(teamEntity, user);

        // TeamUser 데이터베이스에 사용자 저장
        teamUserEntity = teamUserRepository.save(teamUserEntity);

        return new TeamCreateResponseDto(teamEntity);
    }

    @Transactional
    public List<TeamListResponseDto> getMyTeams(String email) {
        UserEntity user = findUser(email);
        List<TeamListResponseDto> responses = new ArrayList<>();

        List<TeamUserEntity> teamUserEntities = teamUserRepository.findAllByUserEntity(user);
        if (teamUserEntities.isEmpty()) {
            throw new ApiException(TeamMessage.TEAM_NOT_FOUND);
        }

        for (TeamUserEntity teamUserEntity : teamUserEntities) {
            TeamEntity teamEntity = teamUserEntity.getTeamEntity();
            List<TeamUserEntity> teamMembers = teamUserRepository.findAllByTeamEntity(teamEntity);
            TeamListResponseDto response = new TeamListResponseDto(teamEntity, teamMembers);
            responses.add(response);
        }

        return responses;
    }

    @Transactional
    public void leaveTeam(Long teamId, String email) {
        UserEntity user = findUser(email);
        TeamEntity teamEntity = findTeamByTeamCode(teamId.toString());
        TeamUserEntity teamUserEntity = findTeamUserEntity(teamEntity, user);
        //방장일 경우
        if(Objects.equals(teamEntity.getUser().getEmail(), email))
            teamRepository.delete(teamEntity);
        // TODO: 10/31/23 cascade 
        teamUserRepository.delete(teamUserEntity);
    }

    @Transactional
    public ShareTeamGifticonResponseDto shareTeamGifticon(ShareTeamGifticonRequestDto shareTeamGifticonRequestDto, String email) {
        UserEntity user = findUser(email);
        TeamEntity teamEntity = teamRepository.findByTeamId(shareTeamGifticonRequestDto.getTeamId());
        TeamUserEntity teamUserEntity = findTeamUserEntity(teamEntity, user);
        GifticonEntity gifticonEntity = gifticonRepository.findById(shareTeamGifticonRequestDto.getGifticonId()).orElse(null);

        if (gifticonEntity != null) {
            // Check if the gifticon is already shared
            if (isGifticonAlreadyShared(teamUserEntity, gifticonEntity)) {
                throw new ApiException(TeamMessage.TEAM_GIFTICON_ALREADY_EXIST);
            }

            // Create and save the TeamGifticonEntity
            TeamGifticonEntity teamGifticonEntity = new TeamGifticonEntity(teamUserEntity, gifticonEntity);
            teamGifticonEntity = teamGifticonRepository.save(teamGifticonEntity);
            return new ShareTeamGifticonResponseDto(teamGifticonEntity);
        } else {
            throw new ApiException(GifticonMessage.GIFTICON_NOT_FOUND);
        }
    }

    public List<TeamListResponseDto> getTeamListByGifticon(Long gifticonId, String email) {
        UserEntity user = findUser(email);
        Optional<GifticonEntity> gifticonEntity = gifticonRepository.findById(gifticonId);

        List<TeamGifticonEntity> teamGifticonEntities = teamGifticonRepository.findByGifticonEntity(gifticonEntity);

        return teamGifticonEntities.stream()
                .map(TeamGifticonEntity::getTeamUserEntity)
                .map(TeamUserEntity::getTeamEntity)
                .distinct()
                .map(teamEntity -> {
                    List<TeamUserEntity> teamMembers = teamUserRepository.findAllByTeamEntity(teamEntity);
                    return new TeamListResponseDto(teamEntity, teamMembers);
                })
                .collect(Collectors.toList());
    }

    // 랜덤 초대 코드 생성 메서드

    private String generateInviteCode() {
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }

    private TeamEntity findTeamByTeamCode(String teamCode) {
        return teamRepository.findByTeamCode(teamCode).orElseThrow(() -> new ApiException(TeamMessage.TEAM_NOT_FOUND));
    }

    private boolean isUserAlreadyJoinedTeam(TeamEntity teamEntity, UserEntity user) {
        return teamUserRepository.findByTeamEntityAndUserEntity(teamEntity, user).isPresent();
    }

    private UserEntity findUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ApiException(UserMessage.USER_NOT_FOUND));
    }

    private TeamUserEntity findTeamUserEntity(TeamEntity teamEntity, UserEntity user) {
        return teamUserRepository.findByTeamEntityAndUserEntity(teamEntity, user).orElseThrow(() -> new ApiException(TeamMessage.TEAM_NOT_FOUND));
    }

    private void shareGifticonWithTeam(TeamUserEntity teamUserEntity, GifticonEntity gifticonEntity) {
        TeamGifticonEntity teamGifticonEntity = new TeamGifticonEntity(teamUserEntity, gifticonEntity);
        teamGifticonRepository.save(teamGifticonEntity);
    }

    private boolean isGifticonAlreadyShared(TeamUserEntity teamUserEntity, GifticonEntity gifticonEntity) {
        return teamGifticonRepository.findByTeamUserEntityAndGifticonEntity(teamUserEntity, gifticonEntity).isPresent();
    }

    @Transactional
    public PageResponse<GifticonResponseDto> getTeamGifticon(Long teamId, Pageable pageable, String email) {
        UserEntity user = findUser(email);
        TeamEntity teamEntity = teamRepository.findByTeamId(teamId);
        Page<TeamGifticonEntity> teamGifticonEntity = teamGifticonRepository.findAllByTeamId(teamId, pageable);
        return createTeamGifticonPagingResponse(teamGifticonEntity, pageable);


    }

    private PageResponse<GifticonResponseDto> createTeamGifticonPagingResponse(Page<TeamGifticonEntity> teamGifticonEntity, Pageable pageable) {
        List<GifticonResponseDto> gifticonResponseDtos = teamGifticonEntity.stream()
                .map(TeamGifticonEntity::getGifticonEntity)
                .map(GifticonResponseDto::new)
                .collect(Collectors.toList());
        return new PageResponse<>(new PageImpl<>(gifticonResponseDtos, pageable, teamGifticonEntity.getTotalElements()));
    }


    public PageResponse<GifticonListResponse> getAllTeamRequestGifticonList(Long teamId, FilterListDto request, Pageable pageable, String email) {
        UserEntity user = findUser(email);
        //팀
        TeamEntity teamEntity = teamRepository.findByTeamId(teamId);
        List<GifticonEntity> allGifticonEntities = new ArrayList<>();
        //팀유저리스트[1,2,3]
        List<TeamUserEntity> teamUserEntities = teamUserRepository.findAllByTeamEntity(teamEntity);
        //- 반복문 : 팀유저엔티티 1개로 팀기프티콘 리스트들 찾기
        for (TeamUserEntity teamUserEntity : teamUserEntities) {
            List<TeamGifticonEntity>teamGifticonEntities = teamGifticonRepository.findAllByTeamUserEntity(teamUserEntity);
            //- 반복문 : 팀기프티콘 리스트[1] -> 리스폰스 add
            for (TeamGifticonEntity teamGifticonEntity : teamGifticonEntities) {
                allGifticonEntities.add(teamGifticonEntity.getGifticonEntity());
            }
        }
        Page<GifticonEntity> gifticonPage = findFilterGifticonEntities(pageable, user, request, allGifticonEntities);
        return createPagingResponse(gifticonPage);
    }

    @Transactional
    public TeamCreateResponseDto updateTeam(Long teamId, TeamCreateRequestDto teamCreateRequestDto, String email) {
        UserEntity user = findUser(email);
        TeamEntity teamEntity = teamRepository.findByTeamId(teamId);
        checkTeamLeader(teamEntity, user);
        teamEntity.updateTeam(teamCreateRequestDto);
        return new TeamCreateResponseDto(teamEntity);
    }

    @Transactional
    public TeamCreateResponseDto updateTeamInviteCode(Long teamId, String email) {
        UserEntity user = findUser(email);
        TeamEntity teamEntity = teamRepository.findByTeamId(teamId);
        if(!Objects.equals(teamEntity.getUser().getEmail(), email))
            throw new ApiException(TeamMessage.TEAM_NOT_LEADER);
        teamEntity.updateTeamInviteCode(generateInviteCode());
        return new TeamCreateResponseDto(teamEntity);
    }

    private Page<GifticonEntity> findFilterGifticonEntities(Pageable pageable, UserEntity user, FilterListDto request, List<GifticonEntity> allGifticonEntities) {
        return switch (request) {
            case ALL_NAME_DESC -> new PageImpl<>(allGifticonEntities.stream()
                    .sorted(Comparator.comparing(GifticonEntity::getName).reversed())
                    .collect(Collectors.toList()), pageable, allGifticonEntities.size());
            case ALL_NAME_ASC -> new PageImpl<>(allGifticonEntities.stream()
                    .sorted(Comparator.comparing(GifticonEntity::getName))
                    .collect(Collectors.toList()), pageable, allGifticonEntities.size());
            case ALL_RECENT_EXPIRATION -> new PageImpl<>(allGifticonEntities.stream()
                    .sorted(Comparator.comparing(GifticonEntity::getDueDate).reversed())
                    .collect(Collectors.toList()), pageable, allGifticonEntities.size());
            case All_USABLE_NAME_DESC -> new PageImpl<>(allGifticonEntities.stream()
                    .filter(gifticonEntity -> gifticonEntity.getStatus() == Status.AVAILABLE)
                    .sorted(Comparator.comparing(GifticonEntity::getName).reversed())
                    .collect(Collectors.toList()), pageable, allGifticonEntities.size());
            case All_USABLE_NAME_ASC -> new PageImpl<>(allGifticonEntities.stream()
                    .filter(gifticonEntity -> gifticonEntity.getStatus() == Status.AVAILABLE)
                    .sorted(Comparator.comparing(GifticonEntity::getName))
                    .collect(Collectors.toList()), pageable, allGifticonEntities.size());
            case All_USABLE_RECENT_EXPIRATION -> new PageImpl<>(allGifticonEntities.stream()
                    .filter(gifticonEntity -> gifticonEntity.getStatus() == Status.AVAILABLE)
                    .sorted(Comparator.comparing(GifticonEntity::getDueDate).reversed())
                    .collect(Collectors.toList()), pageable, allGifticonEntities.size());
            case All_USED_NAME_DESC -> new PageImpl<>(allGifticonEntities.stream()
                    .filter(gifticonEntity -> gifticonEntity.getStatus() == Status.UNAVAILABLE)
                    .sorted(Comparator.comparing(GifticonEntity::getName).reversed())
                    .collect(Collectors.toList()), pageable, allGifticonEntities.size());
            case ALL_USED_NAME_ASC -> new PageImpl<>(allGifticonEntities.stream()
                    .filter(gifticonEntity -> gifticonEntity.getStatus() == Status.UNAVAILABLE)
                    .sorted(Comparator.comparing(GifticonEntity::getName))
                    .collect(Collectors.toList()), pageable, allGifticonEntities.size());
            case ALL_USED_RECENT_EXPIRATION -> new PageImpl<>(allGifticonEntities.stream()
                    .filter(gifticonEntity -> gifticonEntity.getStatus() == Status.UNAVAILABLE)
                    .sorted(Comparator.comparing(GifticonEntity::getDueDate).reversed())
                    .collect(Collectors.toList()), pageable, allGifticonEntities.size());
            default -> new PageImpl<>(Collections.emptyList(), pageable, 0);
        };
    }


    private PageResponse<GifticonListResponse> createPagingResponse(Page<GifticonEntity> gifticonEntities) {
        List<GifticonListResponse> gifticonResponses = gifticonEntities.stream()
                .map(GifticonListResponse::new)
                .collect(Collectors.toList());

        Page<GifticonListResponse> responsePage = new PageImpl<>(gifticonResponses, gifticonEntities.getPageable(), gifticonEntities.getTotalElements());

        return new PageResponse<>(responsePage);
    }

    @Transactional
    public PageResponse<GifticonListResponse> getRecentTeamGifticonList(Long teamId, Pageable pageable, String email) {
UserEntity user = findUser(email);
        TeamEntity teamEntity = teamRepository.findByTeamId(teamId);
        List<GifticonEntity> allGifticonEntities = new ArrayList<>();
        //팀아이디로 팀에속한 유저 가져오기
        List<TeamUserEntity> teamUserEntities = teamUserRepository.findAllByTeamEntity(teamEntity);
        //- 반복문 : 팀유저들의 기프티콘 가져오기
        for (TeamUserEntity teamUserEntity : teamUserEntities) {
            List<TeamGifticonEntity>teamGifticonEntities = teamGifticonRepository.findAllByTeamUserEntity(teamUserEntity);
            //- 반복문 : 팀원들의 기프티콘 리스트들 add
            for (TeamGifticonEntity teamGifticonEntity : teamGifticonEntities) {
                allGifticonEntities.add(teamGifticonEntity.getGifticonEntity());
            }
        }
       //최근사용한 순으로 정렬하기
        Page<GifticonEntity> gifticonPage = gifticonRepository.findAllByStatusOrderByUsedDate(pageable, Status.UNAVAILABLE);
        return createPagingResponse(gifticonPage);
    }

    @Transactional
    public PageResponse<GifticonListResponse> getNotShareTeamGifticonList(Long teamId, Pageable pageable, String email) {
        UserEntity user = findUser(email);
        TeamEntity teamEntity = teamRepository.findByTeamId(teamId);
        TeamUserEntity teamUserEntity = findTeamUserEntity(teamEntity, user);

        List<TeamGifticonEntity> teamGifticonEntity = teamGifticonRepository.findAllByTeamUserEntity(teamUserEntity);

        // 사용자의 기프티콘 목록 가져오기
        Page<GifticonEntity> userGifticonPage = gifticonRepository.findAllByUser(user, pageable);

        // TODO: 11/17/23 고민사항 : 쿼리로 짜면 속도 빨라질것 같은데 어렵다... 
        // 팀의 기프티콘 목록 가져오기
        List<GifticonEntity> teamGifticonList = teamGifticonEntity.stream()
                .map(TeamGifticonEntity::getGifticonEntity).toList();

        // 사용자 기프티콘 목록에서 팀 기프티콘을 제외하여 최종 목록 생성
        List<GifticonEntity> finalGifticonList = userGifticonPage.getContent()
                .stream()
                .filter(gifticonEntity -> !teamGifticonList.contains(gifticonEntity)).toList();

        Page<GifticonEntity> gifticonPage = new PageImpl<>(finalGifticonList, pageable, finalGifticonList.size());

        return createPagingResponse(gifticonPage);
    }

    private void checkTeamLeader(TeamEntity teamEntity, UserEntity user) {
        if(!Objects.equals(teamEntity.getUser(), user))
            throw new ApiException(TeamMessage.TEAM_NOT_LEADER);
    }
}
