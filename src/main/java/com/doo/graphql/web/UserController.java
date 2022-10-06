package com.doo.graphql.web;

import java.util.List;
import java.util.Optional;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.Arguments;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import com.doo.graphql.dao.FavoriteRepository;
import com.doo.graphql.dao.NoteRepository;
import com.doo.graphql.dao.UserRepository;
import com.doo.graphql.security.JwtTokenProvider;
import com.doo.graphql.util.MD5Util;
import com.doo.graphql.vo.Note;
import com.doo.graphql.vo.User;
import com.doo.graphql.vo.UserDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController{

	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	private final UserRepository userRepository;
	private final NoteRepository noteRepository;
	private final FavoriteRepository favoriteRepository;
	private final JwtTokenProvider jwtTokenProvider;
	
	@SchemaMapping(typeName = "Query", value = "user")
	public User user(@Argument String email) {
		return userRepository.findByEmail(email).get();
	}
	
	@SchemaMapping(typeName = "Query", value = "users")
	public List<User> users(@Argument Long id) {
		return userRepository.findAll();
	}
	
	@SchemaMapping(typeName = "Query", value = "me")
	public User me() {
		log.debug("com.doo.graphql.web.UserController.me");
		
		User user = null;
		if (!SecurityContextHolder.getContext()
			.getAuthentication()
			.getPrincipal().equals("anonymousUser")) {
		    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		    
		    user = (User)userDetails;
		    List<Note> notes = noteRepository.findByAuthorId(user.getId());
		    user.setNotes(notes);
		    
		    List<Note> favorites = favoriteRepository.findByUserId(user.getId());
		    user.setFavorites(favorites);
        }
		return user;
	}
	
	@SchemaMapping(typeName = "Mutation", value = "signUp")
	public User signUp(@Arguments UserDto userDto) {
		log.debug("com.doo.graphql.web.UserController.signUp.userDto : {}", userDto);
		
		String email = userDto.getEmail().trim().toLowerCase();
		String avatar = gravatar(email);
		
		User user = new User();
		user.setUsername(userDto.getUsername());
		user.setPassword(encoder.encode(userDto.getPassword()));
		user.setEmail(email);
		user.setAvatar(avatar);
		
		log.debug("com.doo.graphql.web.UserController.user : {}", user);
		
		return userRepository.save(user);
	}
	
	@SchemaMapping(typeName = "Mutation", value = "signIn")
	public String signIn(@Arguments UserDto userDto) throws Exception {
		log.debug("com.doo.graphql.web.UserController.signUp.userDto : {}", userDto);
		
		String email = userDto.getEmail().trim().toLowerCase();
		Optional<User> user = userRepository.findByEmail(email);
		if (user == null) {
			throw new Exception();
		}
		
		boolean valid = encoder.matches(userDto.getPassword(), user.get().getPassword());
		if (!valid) {
			throw new Exception();
		}
		
		return jwtTokenProvider.createToken(email, user.get().getRoles());
	}
	
	private String gravatar(String email) {
		String hash = MD5Util.md5Hex(email);
		return "https://www.gravatar.com/avatar/" + hash + ".jpg?d=identicon";
	}
}
