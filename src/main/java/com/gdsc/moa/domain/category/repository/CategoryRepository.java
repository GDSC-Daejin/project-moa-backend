package com.gdsc.moa.domain.category.repository;

import com.gdsc.moa.domain.category.entity.CategoryEntity;
import com.gdsc.moa.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByCategoryName(String name);

    List<CategoryEntity> findAllByUser(UserEntity user);

    Optional<CategoryEntity> findByUserAndCategoryName(UserEntity user, String 미분류);
}
