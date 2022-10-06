package com.doo.graphql.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doo.graphql.vo.Favorite;
import com.doo.graphql.vo.Note;

public interface FavoriteRepository extends JpaRepository<Favorite, Long>{

	List<Note> findByUserId(Long id);

}
