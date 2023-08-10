package com.jafa.repository;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jafa.AppTest;
import com.jafa.domain.MemberVO;

import lombok.extern.log4j.Log4j;

@Log4j
public class MemberRepositoryTest extends AppTest{

	@Autowired
	MemberRepository memberRepository; 
	
	@Test
	public void test() {
		MemberVO read = memberRepository.read("admin");
		log.info(read);
	}
}
