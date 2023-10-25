package com.gdsc.moa.domain.gifticon.repository;

import com.gdsc.moa.domain.category.entity.CategoryEntity;
import com.gdsc.moa.domain.gifticon.entity.GifticonEntity;
import com.gdsc.moa.domain.gifticon.entity.Status;
import com.gdsc.moa.domain.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GifticonRepository extends JpaRepository<GifticonEntity, Long> {

    @Modifying
    @Query("UPDATE GifticonEntity g SET g.status = 'UNAVAILABLE' WHERE g.dueDate = current_date")
    void updateGifticonsWithDueDateToday();

    @Query("SELECT g FROM GifticonEntity g WHERE g.user = :user AND g.status = :status")
    Page<GifticonEntity> findByUserAndStatus(@Param("user") UserEntity user, @Param("status") Status status, Pageable pageable);

    @Query("SELECT g FROM GifticonEntity g WHERE g.user = :user ")
    Page<GifticonEntity> findByUser(UserEntity user, Pageable pageable);

    @Query("SELECT g FROM GifticonEntity g WHERE g.user = :user AND g.category = :category")
    Page<GifticonEntity> findByUserAndCategory(UserEntity user, CategoryEntity category, Pageable pageable);

    @Query("SELECT g FROM GifticonEntity g WHERE g.user = :user AND g.status = :status order by g.dueDate desc")
    Page<GifticonEntity> findBYUserOrderByDueDateDesc(UserEntity user,Status status, Pageable pageable);
}
