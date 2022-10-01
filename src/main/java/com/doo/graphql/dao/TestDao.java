package com.doo.graphql.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doo.graphql.vo.Product;

@Repository
public interface TestDao extends JpaRepository<Product, Long>{

}
