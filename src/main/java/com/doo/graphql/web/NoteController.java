package com.doo.graphql.web;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.Arguments;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.doo.graphql.dao.NoteRepository;
import com.doo.graphql.vo.Note;
import com.doo.graphql.vo.NoteFeed;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NoteController {
	
	final static Logger logger = LoggerFactory.getLogger(NoteController.class);
	private final NoteRepository noteRepository;

	@SchemaMapping(typeName = "Query", value = "notes")
	public List<Note> notes() {
		return noteRepository.findAll();
	}
	
	@SchemaMapping(typeName = "Query", value = "note")
	public Note note(@Argument Long id) {
		return noteRepository.findById(id).get();
	}
	
	@SchemaMapping(typeName = "Query", value = "noteFeed")
	public NoteFeed noteFeed(@Arguments Map param) {
		logger.debug("com.doo.graphql.web.NoteController.noteFeed.param : {}", param);
		
		NoteFeed noteFeed = new NoteFeed();
		int limit = 10;
		
//		PageRequest pageRequest = null;
//		if(cursor > -1) {
//			pageRequest = PageRequest.of(cursor, limit, Sort.by(Order.desc("id")));
//		}
		List<Note> notes = noteRepository.findAll();

		if (notes.size() > limit) {
			noteFeed.setHasNextPage(true);
			notes = notes.subList(0, -1);
		}
		
		Long newCursor = notes.get(notes.size() - 1).getId();
		
		noteFeed.setNotes(notes);
		noteFeed.setCursor(newCursor);
		return noteFeed;
	}
}
