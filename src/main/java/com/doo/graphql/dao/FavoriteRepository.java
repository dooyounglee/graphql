package com.doo.graphql.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doo.graphql.vo.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long>{

	List<Favorite> findByUserId(Long id);

	List<Favorite> findByNoteId(Long id);

	long deleteByNoteIdAndUserId(Long noteId, Long userId);

}
