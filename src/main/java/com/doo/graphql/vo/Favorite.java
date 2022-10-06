package com.doo.graphql.vo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "favorite")
public class Favorite {

	@Id
	private Long id;

	private Long noteId;
	private Long userId;
}
