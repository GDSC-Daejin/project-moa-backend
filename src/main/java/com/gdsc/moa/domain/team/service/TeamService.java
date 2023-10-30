package com.gdsc.moa.domain.team.service;

import com.gdsc.moa.domain.team.repository.TeamRepository;
import com.gdsc.moa.domain.team.dto.TeamCreateRequestDto;
import com.gdsc.moa.domain.team.dto.TeamCreateResponseDto;
import com.gdsc.moa.domain.team.entity.TeamEntity;
import com.gdsc.moa.domain.user.entity.UserEntity;
import com.gdsc.moa.domain.user.repository.UserRepository;
import com.gdsc.moa.global.exception.ApiException;
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
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 16;

    @Transactional
    public TeamCreateResponseDto createTeam(TeamCreateRequestDto teamCreateRequestDto, String email) {
        UserEntity user = findUser(email);
        // 랜덤 초대 코드 생성
        String inviteCode = generateInviteCode();

        // TeamEntity를 생성
        TeamEntity teamEntity = new TeamEntity(teamCreateRequestDto, user, inviteCode);


        // TeamEntity를 데이터베이스에 저장
        teamEntity = teamRepository.save(teamEntity);

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

    private UserEntity findUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ApiException(UserMessage.USER_NOT_FOUND));
    }

}
