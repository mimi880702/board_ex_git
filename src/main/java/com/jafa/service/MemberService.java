package com.jafa.service;

import java.util.Map;

import com.jafa.domain.MemberVO;

public interface MemberService {
	
	void join(MemberVO vo);
	
	void modify(MemberVO vo);
	
	MemberVO read(String memberId);
	
	void changePassword(Map<String,String> memberMap);
}
