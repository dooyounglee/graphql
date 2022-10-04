package com.doo.graphql.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doo.graphql.vo.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String email);

	User findByUsername(String username);

}
