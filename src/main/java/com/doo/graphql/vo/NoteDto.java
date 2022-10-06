package com.doo.graphql.vo;

import lombok.Data;

@Data
public class NoteDto {

	private Long id;
	private String content;
	private String authorId;
}
