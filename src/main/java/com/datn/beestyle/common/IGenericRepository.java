package com.datn.beestyle.common;

import com.datn.beestyle.entity.product.attributes.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@NoRepositoryBean
public interface IGenericRepository<T, ID> extends JpaRepository<T, ID> { }
