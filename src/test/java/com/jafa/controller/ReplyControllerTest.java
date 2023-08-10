package com.jafa.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jafa.AppTest;
import com.jafa.domain.ReplyVO;

public class ReplyControllerTest extends AppTest{
	
	@Autowired
	WebApplicationContext ctx;
	
	ObjectMapper objectMapper; 
	
	private MockMvc mockMvc; 
	
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
		objectMapper = Jackson2ObjectMapperBuilder.json().build(); 
	}
	
	@Test
	@Ignore
	public void testRegister() throws Exception {
		ReplyVO vo = ReplyVO.builder()
				.bno(1L)
				.reply("웹 계층 : 댓글추가2")
				.replyer("작성자")
				.build();
		String content = objectMapper.writeValueAsString(vo);
		mockMvc.perform(post("/replies/new")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
		).andReturn();
	}
	
	@Test
	@Ignore
	public void testModify() throws Exception {
		ReplyVO vo = ReplyVO.builder()
				.reply("웹 계층 : 댓글 -- 수정")
				.build();
		String content = objectMapper.writeValueAsString(vo);
		mockMvc.perform(put("/replies/12")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
		).andReturn();
	}
	
	@Test
	@Ignore
	public void testRemove() throws Exception {
		mockMvc.perform(delete("/replies/12")); 
	}
}
