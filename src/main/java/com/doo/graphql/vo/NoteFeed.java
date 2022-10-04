package com.doo.graphql.vo;

import java.util.List;

import lombok.Data;

@Data
public class NoteFeed {

	private List<Note> notes;
	private Long cursor;
	private boolean hasNextPage = false;
}
