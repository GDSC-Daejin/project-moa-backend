package com.gdsc.moa.domain.gifticon.repository;

import com.gdsc.moa.domain.gifticon.entity.GifticonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GifticonRepository extends JpaRepository<GifticonEntity, Long> {

    @Modifying
    @Query("UPDATE GifticonEntity g SET g.status = 'UNAVAILABLE' WHERE g.dueDate = current_date")
    void updateGifticonsWithDueDateToday();
}
