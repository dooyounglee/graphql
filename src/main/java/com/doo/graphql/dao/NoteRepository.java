package com.doo.graphql.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doo.graphql.vo.Note;

public interface NoteRepository extends JpaRepository<Note, Long>{

}
