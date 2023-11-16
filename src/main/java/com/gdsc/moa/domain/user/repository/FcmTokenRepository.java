package com.gdsc.moa.domain.user.repository;

import com.gdsc.moa.domain.user.entity.FcmTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenRepository extends JpaRepository<FcmTokenEntity, Long> {

}
