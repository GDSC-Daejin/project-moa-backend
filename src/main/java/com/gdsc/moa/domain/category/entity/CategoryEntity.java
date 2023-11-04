package com.gdsc.moa.domain.category.entity;

import com.gdsc.moa.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "tb_category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;


    public void createCategory(String name, UserEntity user) {
        this.categoryName = name;
        this.user = user;
    }

    public void updateCategory(String categoryName, UserEntity user) {
        this.categoryName = categoryName;
        this.user = user;
    }
}
