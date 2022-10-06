package com.doo.graphql.vo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "favorite")
public class Favorite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "noteId") // 이 이름으로 컬럼 생김
	private Note note;
	
	@OneToOne
	@JoinColumn(name = "userId") // 이 이름으로 컬럼 생김
	private User user;
}
