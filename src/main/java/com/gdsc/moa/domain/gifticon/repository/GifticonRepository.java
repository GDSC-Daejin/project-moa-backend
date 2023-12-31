package com.gdsc.moa.domain.gifticon.repository;

import com.gdsc.moa.domain.category.entity.CategoryEntity;
import com.gdsc.moa.domain.gifticon.entity.GifticonEntity;
import com.gdsc.moa.domain.gifticon.entity.Status;
import com.gdsc.moa.domain.user.entity.UserEntity;
import java.util.Date;
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

    Long countByUser(UserEntity user);

    Long countByUserAndStatus(UserEntity user, Status available);

    @Query("SELECT g FROM GifticonEntity g WHERE g.user = :user ORDER BY g.name DESC")
    Page<GifticonEntity> findByUserOrderByNameDesc(UserEntity user, Pageable pageable);

    @Query("SELECT g FROM GifticonEntity g WHERE g.user = :user ORDER BY g.name ASC")
    Page<GifticonEntity> findByUserOrderByNameAsc(UserEntity user, Pageable pageable);

    @Query("SELECT g FROM GifticonEntity g WHERE g.user = :user AND g.status = :status ORDER BY g.name DESC")
    Page<GifticonEntity> findByUserAndStatusOrderByNameDesc(@Param("user") UserEntity user, @Param("status") Status status, Pageable pageable);

    @Query("SELECT g FROM GifticonEntity g WHERE g.user = :user AND g.status = :status ORDER BY g.name ASC")
    Page<GifticonEntity> findByUserAndStatusOrderByNameAsc(@Param("user") UserEntity user, @Param("status") Status status, Pageable pageable);

    @Query("SELECT g FROM GifticonEntity g WHERE g.user = :user AND g.status = :status ORDER BY g.dueDate DESC")
    Page<GifticonEntity> findByUserAndStatusOrderByDueDateDesc(@Param("user") UserEntity user, @Param("status") Status status, Pageable pageable);

    @Query("SELECT g FROM GifticonEntity g WHERE g.user = :user AND g.dueDate = :reminderDay")
    List<GifticonEntity> findByUserAndDueDate(UserEntity user, Date reminderDay);

    Page<GifticonEntity> findAllByStatusOrderByUsedDate(Pageable pageable, Status unavailable);

    Page<GifticonEntity> findAllByUser(UserEntity user, Pageable pageable);

    //@Query("SELECT g FROM GifticonEntity g WHERE g.user = :user And g.status = :status AND g NOT IN :teamGifticonEntity")
    //@Query("SELECT g FROM GifticonEntity g WHERE g.status = :status AND g.user != :user AND g NOT IN (SELECT tg.sharedGifticon FROM TeamGifticonEntity tg WHERE tg.teamUser = :teamUser)")
    //Page<GifticonEntity> findAllByStatusAndNotInTeamGifticonEntityList(Pageable pageable, UserEntity user, Status available, List<TeamGifticonEntity> teamGifticonEntity);

}
