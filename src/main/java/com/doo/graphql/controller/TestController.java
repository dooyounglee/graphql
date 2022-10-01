package com.doo.graphql.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.doo.graphql.dao.TestDao;
import com.doo.graphql.vo.Product;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class TestController {

	private MediaType json = MediaType.APPLICATION_JSON;
	public final TestDao testDao;
	
	@SchemaMapping(typeName = "Query", value = "test")
	public String test() {
		return "test";
	}
	
	@QueryMapping(value = "test1")
	public Map test1() {
		System.out.println("dddddddddddddddd");
//		{
//			test1 {
//				question
//				answer
//			}
//		}
//		{
//			  "data": {
//			    "test1": {
//			      "question": "q",
//			      "answer": "a"
//			    }
//			  }
//			}
		Map returnMap = new HashMap<>();
		returnMap.put("question", "q");
		returnMap.put("answer", "a");
		return returnMap;
	}
	
	
	@QueryMapping(value = "test2")
	public List<Product> test2() {
		System.out.println(testDao.findAll());
		return testDao.findAll();
	}
	
	@QueryMapping(value = "test3")
	public ResponseEntity<List<Product>> test3() {
		System.out.println(new ResponseEntity<>(testDao.findAll(), HttpStatus.OK));
		return new ResponseEntity<>(testDao.findAll(), HttpStatus.OK);
	}
	
	@QueryMapping(value = "test4")
	public List<Product> test4() {
		return testDao.findAll();
	}
	
	@QueryMapping(value = "test5")
	public Mono<ServerResponse> test5() {
		Mono<Product> product = Mono.just(new Product(1L, "title", "content1"));
		return ServerResponse.ok().contentType(json).body(product, Product.class);
	}
	
	@QueryMapping(value = "test6")
	public Product test6() {
		System.out.println("ddddddddd");
		Product product = new Product(1L, "title", "content1");
		Mono<Product> mProduct = Mono.just(product);
		return product;
	}
	
	/*
	 *  curl -d "{""query"": ""{ test7 { num } }"" }"
	 *       -H "Content-Type: application/json"
	 *       -X POST http://localhost:8080/graphql
	 */
	@QueryMapping(value = "test7")
	public Flux<Product> test7() {
		Product product = new Product(1L, "title", "content1");
		Mono<Product> mProduct = Mono.just(product);
		return Flux.just(product, product);
	}
}
