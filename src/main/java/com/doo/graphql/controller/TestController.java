package com.doo.graphql.controller;

import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class TestController {

	@SchemaMapping(typeName = "Query", value = "test")
	public String test() {
		return "test";
	}
}
