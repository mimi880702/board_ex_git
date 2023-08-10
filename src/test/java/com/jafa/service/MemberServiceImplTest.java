package com.jafa.service;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jafa.AppTest;
import com.jafa.domain.MemberVO;

public class MemberServiceImplTest extends AppTest{
	
	@Autowired
	MemberService memberService;

	@Test
	@Ignore
	public void test() {
		MemberVO vo =new MemberVO(); 
		vo.setMemberId("admin");
		vo.setMemberPwd("1234");
		vo.setMemberName("관리자");
		vo.setEmail("admin@test.com");
		memberService.join(vo);
	}
	
	@Test
	@Ignore
	public void test2() {
		MemberVO vo =new MemberVO(); 
		vo.setMemberId("scott");
		vo.setMemberPwd("1234");
		vo.setMemberName("스카");
		vo.setEmail("scott@test.com");
		memberService.join(vo);
	}
	
	@Test
	public void test3() {
		MemberVO vo =new MemberVO(); 
		vo.setMemberId("lee");
		vo.setMemberPwd("1234");
		vo.setMemberName("이광협");
		vo.setEmail("lee@test.com");
		memberService.join(vo);
	}

}
