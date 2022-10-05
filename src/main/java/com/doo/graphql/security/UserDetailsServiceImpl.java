package com.doo.graphql.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.doo.graphql.dao.UserRepository;
import com.doo.graphql.vo.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		log.info("com.doo.graphql.security.UserDetailsServiceImpl.loadUserByUsername.email : {}", email);
		
		Optional<User> oUser = userRepository.findByEmail(email);
		if (oUser.isEmpty()) {
			return null;
		} else {
			return oUser.get();
		}
	}

}
