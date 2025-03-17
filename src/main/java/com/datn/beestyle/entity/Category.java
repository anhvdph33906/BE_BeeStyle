package com.datn.beestyle.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Table(name = "category")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category extends BaseEntity<Integer> {
    @Column(name = "category_name")
    String categoryName;

    @Column(name = "slug")
    String slug;

    @Column(name = "level")
    int level;

    @Column(name = "priority")
    int priority;

    @Column(name = "status")
    int status;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = {MERGE, PERSIST})
    @JoinColumn(name = "parent_category_id", referencedColumnName = "id")
    Category parentCategory ;

    @JsonManagedReference
    @OneToMany(mappedBy = "parentCategory", cascade = ALL, fetch = FetchType.LAZY)
    List<Category> categoryChildren = new ArrayList<>();
}
