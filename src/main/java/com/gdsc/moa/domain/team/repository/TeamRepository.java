package com.gdsc.moa.domain.team.repository;

import com.gdsc.moa.domain.team.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Long> {

    Optional<TeamEntity> findByTeamCode(String teamCode);
}
