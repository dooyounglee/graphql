package com.doo.graphql.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.Arguments;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import com.doo.graphql.dao.NoteRepository;
import com.doo.graphql.vo.Note;
import com.doo.graphql.vo.NoteDto;
import com.doo.graphql.vo.NoteFeed;
import com.doo.graphql.vo.User;

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
	public NoteFeed noteFeed(@Arguments String cursor) {
		logger.debug("com.doo.graphql.web.NoteController.noteFeed.cursor : {}", cursor);
		
		NoteFeed noteFeed = new NoteFeed();
//		int limit = 10;
		
//		PageRequest pageRequest = null;
//		if(cursor > -1) {
//			pageRequest = PageRequest.of(cursor, limit, Sort.by(Order.desc("id")));
//		}
		List<Note> notes = noteRepository.findAll();

//		if (notes.size() > limit) {
//			noteFeed.setHasNextPage(true);
//			notes = notes.subList(0, -1);
//		}
		
//		Long newCursor = notes.get(notes.size() - 1).getId();
		
		noteFeed.setNotes(notes);
		noteFeed.setCursor(0L);
		return noteFeed;
	}
	
	@SchemaMapping(typeName = "Mutation", value = "newNote")
	public Note newNote(@Arguments NoteDto noteDto) throws Exception {
		logger.debug("com.doo.graphql.web.NoteController.newNote.noteDto : {}", noteDto);
		
		// user
		User user = null;
		if (!SecurityContextHolder.getContext()
			.getAuthentication()
			.getPrincipal().equals("anonymousUser")) {
		    user = (User) (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
		if (user == null) throw new Exception();
		
		Note note = new Note();
		note.setContent(noteDto.getContent());
		note.setAuthor(user);
		note.setCreatedAt(LocalDateTime.now());
		note.setUpdatedAt(LocalDateTime.now());
		
		return noteRepository.save(note);
	}
	
	@SchemaMapping(typeName = "Mutation", value = "updateNote")
	public Note updateNote(@Arguments NoteDto noteDto) throws Exception {
		logger.debug("com.doo.graphql.web.NoteController.updateNote.noteDto : {}", noteDto);
		
		// user
		User user = null;
		if (!SecurityContextHolder.getContext()
			.getAuthentication()
			.getPrincipal().equals("anonymousUser")) {
		    user = (User) (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
		if (user == null) throw new Exception();
		
		Optional<Note> oNote = noteRepository.findById(noteDto.getId());
		if (oNote.isPresent() && oNote.get().getAuthor().getId() != user.getId()) {
			throw new Exception();
		}
		
		return noteRepository.save(oNote.get());
	}
	
	@SchemaMapping(typeName = "Mutation", value = "deleteNote")
	public Boolean deleteNote(@Arguments NoteDto noteDto) throws Exception {
		logger.debug("com.doo.graphql.web.NoteController.deleteNote.noteDto : {}", noteDto);
		
		// user
		User user = null;
		if (!SecurityContextHolder.getContext()
			.getAuthentication()
			.getPrincipal().equals("anonymousUser")) {
		    user = (User) (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
		if (user == null) throw new Exception();
		
		Optional<Note> oNote = noteRepository.findById(noteDto.getId());
		if (oNote.isPresent() && oNote.get().getAuthor().getId() != user.getId()) {
			throw new Exception();
		}
		
		try {
			noteRepository.deleteById(oNote.get().getId());
		} catch (Exception e) {
			return false;
		}
		return true;
		
	}
}
