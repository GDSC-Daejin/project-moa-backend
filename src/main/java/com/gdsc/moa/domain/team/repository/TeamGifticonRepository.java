package com.gdsc.moa.domain.team.repository;

import com.gdsc.moa.domain.gifticon.entity.GifticonEntity;
import com.gdsc.moa.domain.team.entity.TeamGifticonEntity;
import com.gdsc.moa.domain.team.entity.TeamUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamGifticonRepository extends JpaRepository<TeamGifticonEntity, Long> {
    Optional<TeamGifticonEntity> findByTeamUserEntityAndGifticonEntity(TeamUserEntity teamUserEntity, GifticonEntity gifticonEntity);

    @Query("select t from TeamGifticonEntity t where t.teamUserEntity.teamEntity.id = :teamId")
    Page<TeamGifticonEntity> findAllByTeamId(Long teamId, Pageable pageable);

    List<TeamGifticonEntity> findByGifticonEntity(Optional<GifticonEntity> gifticonEntity);
}
