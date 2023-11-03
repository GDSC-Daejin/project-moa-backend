package com.gdsc.moa.domain.user.service;

import com.gdsc.moa.domain.user.entity.UserEntity;
import com.gdsc.moa.domain.user.repository.UserRepository;
import com.gdsc.moa.global.exception.ApiException;
import com.gdsc.moa.global.message.UserMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void updateNickname(String email, String nickname) {
        UserEntity user = findUserByEmail(email);
        user.updateNickName(nickname);
        userRepository.save(user);
    }

    private UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ApiException(UserMessage.USER_NOT_FOUND)
                );
    }
}
