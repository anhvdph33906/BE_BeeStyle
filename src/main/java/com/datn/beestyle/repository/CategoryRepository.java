package com.datn.beestyle.repository;

import com.datn.beestyle.common.IGenericRepository;
import com.datn.beestyle.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends IGenericRepository<Category, Integer> {
    @Query(value = """
                WITH RECURSIVE category_hierarchy AS (
                SELECT id, category_name, slug, parent_category_id, level, priority
                FROM category 
                WHERE status = 1 AND parent_category_id IS NULL
                UNION ALL
                SELECT c.id, c.category_name, c.slug, c.parent_category_id, c.level, c.priority
                FROM category AS c
                INNER JOIN category_hierarchy ch ON c.parent_category_id = ch.id
                WHERE c.status = 1 AND c.level <= 3
                )
                SELECT id, category_name, slug, parent_category_id
                ,level, priority 
                FROM category_hierarchy
                ORDER BY level, priority;
            """, nativeQuery = true)
    List<Object[]> findAllForUser();

    @Query("""
            select c from Category c 
            where 
                (:keyword is null or 
                    c.categoryName like concat('%', :keyword, '%') or 
                    c.slug like concat('%', :keyword, '%')) and
                (:status is null or c.status = :status)
            """)
    Page<Category> findAllByKeywordAndStatus(Pageable pageable,
                                             @Param("keyword") String keyword,
                                             @Param("status") Integer status);

    @Query("""
            select c.id, c.categoryName from Category c where c.id in (:ids)
            """)
    List<Object[]> findCategoryNameById(@Param("ids") Iterable<Integer> ids);

    @Query("""
            select c from Category c where c.parentCategory.id = :parentCategoryId
            """)
    List<Category> findByParentCategoryId(@Param("parentCategoryId") Integer parentCategoryId);

    @Query("""
            select count(c.id) from Category c
            where  c.level = :level and (:parentCategoryId is null or c.parentCategory.id = :parentCategoryId)
        """)
    long countByLevelAndParentCategoryId(@Param("level") Integer level,
                                         @Param("parentCategoryId") Integer parentCategoryId);

    @Query(value = """
                WITH RECURSIVE CategoryHierarchy AS (
                    SELECT id, parent_category_id
                    FROM category
                    WHERE id = :updateCategoryId
                    UNION ALL
                    SELECT c.id, c.parent_category_id
                    FROM category c
                    INNER JOIN CategoryHierarchy ch ON ch.parent_category_id = c.id
                )
                SELECT count(*) > 0 FROM CategoryHierarchy WHERE id = :parentCategoryId
            """, nativeQuery = true)
    Long isParentChildLoop(@Param("updateCategoryId") Integer updateCategoryId,
                              @Param("parentCategoryId") Integer parentCategoryId);

    @Query("select count(c.id) > 0 from Category c where c.parentCategory.id = :categoryId")
    boolean existsChildCategoryByParentId(@Param("categoryId") int categoryId);

    boolean existsByCategoryName(String name);

    boolean existsBySlug(String slug);

    Optional<Category> findByCategoryNameAndIdNot(String categoryName, Integer id);

    Optional<Category> findBySlugAndIdNot(String slug, Integer id);
}

