package com.example.moa.user.entity;

import com.example.moa.user.entity.RoleType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String nickname;
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private RoleType userRole;

    public void setId(Long kakaoId) {
        this.id = kakaoId;
    }

    @Builder
    public UserEntity(Long id, String kakaoProfileImg, String kakaoNickname,
                String kakaoEmail, RoleType userRole) {

        this.id = id;
        this.email = kakaoEmail;
        this.nickname = kakaoNickname;
        this.profileImageUrl = kakaoProfileImg;
        this.userRole = userRole;
    }

}
