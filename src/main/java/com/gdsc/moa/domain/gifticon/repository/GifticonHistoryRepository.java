package com.gdsc.moa.domain.gifticon.repository;

import com.gdsc.moa.domain.gifticon.entity.GifticonEntity;
import com.gdsc.moa.domain.gifticon.entity.GifticonHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GifticonHistoryRepository extends JpaRepository<GifticonHistoryEntity, Long> {

    @Query("SELECT g FROM GifticonHistoryEntity g WHERE g.gifticon = :gifticonEntity order by g.usedDate desc ")
    GifticonHistoryEntity findLastHistory(GifticonEntity gifticonEntity);
}
