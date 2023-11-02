package com.gdsc.moa.domain.team.repository;

import com.gdsc.moa.domain.team.entity.TeamEntity;
import com.gdsc.moa.domain.team.entity.TeamUserEntity;
import com.gdsc.moa.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TeamUserRepository extends JpaRepository<TeamUserEntity, Long> {
    Optional<TeamUserEntity> findByTeamEntityAndUserEntity(TeamEntity teamEntity, UserEntity user);

    Optional<TeamUserEntity> findByUserEntity(UserEntity user);

    List<TeamUserEntity> findAllByUserEntity(UserEntity user);

    List<TeamUserEntity> findAllByTeamEntity(TeamEntity teamEntity);
}
