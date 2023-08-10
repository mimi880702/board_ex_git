package com.jafa.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.jafa.AppTest;

import lombok.extern.log4j.Log4j;

@Log4j
public class BoardControllerTest extends AppTest {

	@Autowired
	WebApplicationContext ctx;

	private MockMvc mockMvc; 
	
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}
	
	@Test
	@Ignore
	public void testRegister() throws Exception {
		ModelAndView modelAndView = mockMvc.perform(post("/board/register")
			.param("title", "테스트 글 제목")
			.param("content", "테스트 글 내용")
			.param("writer", "작성자"))
		.andReturn()
		.getModelAndView();
		log.info(modelAndView.getViewName());
		log.info(modelAndView.getStatus());
		Map<String,Object> model = modelAndView.getModel();
		log.info(model);
	}


	@Test
	@Ignore
	public void testList() throws Exception {
		ModelAndView modelAndView = mockMvc.perform(get("/board/list"))
			.andReturn()
			.getModelAndView();
		Map<String,Object> model = modelAndView.getModel();
		log.info(model);
		log.info(modelAndView.getViewName());
	}
	
	@Test
//	@Ignore
	public void testGet() throws Exception {
		ModelAndView modelAndView = mockMvc.perform(get("/board/get").param("bno", "3"))
			.andReturn()
			.getModelAndView();
		Map<String,Object> model = modelAndView.getModel();
		log.info(model); // {board =[BoardVO(...)]}
		log.info(modelAndView.getViewName()); // board/get
		assertEquals("board/get", modelAndView.getViewName());
	}
	
	
	@Test
	@Ignore
	public void testModify() throws Exception {
		ModelAndView modelAndView = mockMvc.perform(post("/board/modify")
			.param("bno", "1")
			.param("title", "테스트 글 제목 --수정")
			.param("content", "테스트 글 내용 --수정")
			.param("writer", "작성자"))
		.andReturn()
		.getModelAndView();
		log.info(modelAndView.getViewName());
	}
	
	@Test
	@Ignore
	public void testRemove() throws Exception {
		ModelAndView modelAndView = mockMvc.perform(post("/board/remove")
			.param("bno", "1"))
		.andReturn()
		.getModelAndView();
		log.info(modelAndView.getViewName());
	}
}
