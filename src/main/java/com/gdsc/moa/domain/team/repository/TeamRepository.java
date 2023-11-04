package com.gdsc.moa.domain.team.repository;

import com.gdsc.moa.domain.gifticon.dto.response.GifticonResponseDto;
import com.gdsc.moa.domain.gifticon.entity.GifticonEntity;
import com.gdsc.moa.domain.team.entity.TeamEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Long> {

    Optional<TeamEntity> findByTeamCode(String teamCode);

    @Query("select t from TeamEntity t where t.id = :teamId")
    TeamEntity findByTeamId(Long teamId);
}
