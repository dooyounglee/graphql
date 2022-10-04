package com.doo.graphql.web;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.Arguments;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

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
	private JwtTokenProvider jwtTokenProvider;
	
	@SchemaMapping(typeName = "Query", value = "user")
	public User user(@Argument String username) {
		return userRepository.findByUsername(username);
	}
	
	@SchemaMapping(typeName = "Query", value = "users")
	public List<User> users(@Argument Long id) {
		return userRepository.findAll();
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
		User user = userRepository.findByEmail(email);
		if (user != null) {
			throw new Exception();
		}
		
		boolean valid = encoder.matches(userDto.getPassword(), user.getPassword());
		if (!valid) {
			throw new Exception();
		}
		
		return jwtTokenProvider.createToken(email, user.getRoles());
	}
	
	private String gravatar(String email) {
		String hash = MD5Util.md5Hex(email);
		return "https://www.gravatar.com/avatar/" + hash + ".jpg?d=identicon";
	}
}
