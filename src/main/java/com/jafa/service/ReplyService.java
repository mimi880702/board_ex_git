package com.jafa.service;

import com.jafa.domain.Criteria;
import com.jafa.domain.ReplyPageDTO;
import com.jafa.domain.ReplyVO;

public interface ReplyService {
	
	int register(ReplyVO vo);
	
	ReplyVO get(Long rno);
	
	int modify(ReplyVO vo);
	
	int remove(Long rno);
	
	ReplyPageDTO getList(Criteria criteria, Long bno);
}
