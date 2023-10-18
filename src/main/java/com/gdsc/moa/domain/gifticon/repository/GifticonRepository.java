package com.gdsc.moa.domain.gifticon.repository;

import com.gdsc.moa.domain.gifticon.entity.GifticonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GifticonRepository extends JpaRepository<GifticonEntity, Long> {

}
