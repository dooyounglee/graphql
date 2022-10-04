package com.doo.graphql.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestDao extends JpaRepository<Product, Long>{

}
