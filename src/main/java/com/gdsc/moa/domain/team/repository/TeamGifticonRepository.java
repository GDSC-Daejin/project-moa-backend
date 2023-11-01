package com.gdsc.moa.domain.team.repository;

import com.gdsc.moa.domain.gifticon.entity.GifticonEntity;
import com.gdsc.moa.domain.team.entity.TeamGifticonEntity;
import com.gdsc.moa.domain.team.entity.TeamUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamGifticonRepository extends JpaRepository<TeamGifticonEntity, Long> {
    Optional<Object> findByIdTeamUserEntityAndIdGifticonEntity(TeamUserEntity teamUserEntity, GifticonEntity gifticonEntity);
}
