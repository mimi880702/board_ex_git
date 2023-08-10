package com.jafa.repository;

import org.apache.ibatis.annotations.Param;

import com.jafa.domain.MemberVO;

public interface MemberRepository {
	
	MemberVO read(String memberId);
	
	void insert(MemberVO vo);
	
	void update(MemberVO vo);

	MemberVO selectById(String memberId);
	
	String selectByEamil(String email);
	
	void updatePassword(
			@Param("memberId") String memberId,  
			@Param("memberPwd") String memberPwd);
}
