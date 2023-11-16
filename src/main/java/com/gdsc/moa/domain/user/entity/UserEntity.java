package com.gdsc.moa.domain.user.entity;

import com.gdsc.moa.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@NoArgsConstructor
public class UserEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    private String nickname;
    private String profileImageUrl;
    @Enumerated(EnumType.STRING)
    private RoleType userRole;
    @Enumerated(EnumType.STRING)
    private SocialType socialType;
    @Enumerated(EnumType.STRING)
    private Status status;

    @ColumnDefault("1")
    private int reminderDay;

    @Builder
    public UserEntity(String email,
                      String nickname,
                      String profileImageUrl,
                      RoleType userRole,
                      SocialType socialType,
                      Status status) {
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.userRole = userRole;
        this.socialType = socialType;
        this.status = status;
    }

    public void updateNickName(String nickname) {
        this.nickname = nickname;
    }

    public void updateReminderDay(int reminderDay) {
        this.reminderDay = reminderDay;
    }
}
