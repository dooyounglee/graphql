package com.doo.graphql.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doo.graphql.vo.Note;

public interface NoteRepository extends JpaRepository<Note, Long>{

	List<Note> findByAuthorId(Long id);

}
