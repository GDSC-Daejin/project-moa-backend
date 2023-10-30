package com.gdsc.moa.domain.team.service;

import com.gdsc.moa.domain.team.dto.request.TeamJoinRequestDto;
import com.gdsc.moa.domain.team.entity.TeamUserEntity;
import com.gdsc.moa.domain.team.repository.TeamRepository;
import com.gdsc.moa.domain.team.dto.request.TeamCreateRequestDto;
import com.gdsc.moa.domain.team.dto.response.TeamCreateResponseDto;
import com.gdsc.moa.domain.team.entity.TeamEntity;
import com.gdsc.moa.domain.team.repository.TeamUserRepository;
import com.gdsc.moa.domain.user.entity.UserEntity;
import com.gdsc.moa.domain.user.repository.UserRepository;
import com.gdsc.moa.global.exception.ApiException;
import com.gdsc.moa.global.message.TeamMessage;
import com.gdsc.moa.global.message.UserMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamUserRepository teamUserRepository;
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
}
