package com.gdsc.moa.domain.team.service;

import com.gdsc.moa.domain.gifticon.dto.response.GifticonResponseDto;
import com.gdsc.moa.domain.gifticon.entity.GifticonEntity;
import com.gdsc.moa.domain.gifticon.repository.GifticonRepository;
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


}
