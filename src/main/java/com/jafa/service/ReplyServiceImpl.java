package com.jafa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jafa.domain.Criteria;
import com.jafa.domain.ReplyPageDTO;
import com.jafa.domain.ReplyVO;
import com.jafa.repository.BoardRepository;
import com.jafa.repository.ReplyRepository;

@Service
public class ReplyServiceImpl implements ReplyService {
	
	private static final int REPLY_ADD = 1;
	private static final int REPLY_DEL = -1;
	
	@Autowired
	private ReplyRepository replyRepository;
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Transactional
	@Override
	public int register(ReplyVO vo) {
		boardRepository.updateReplyCnt(vo.getBno(), REPLY_ADD);
		return replyRepository.insert(vo);
	}

	@Override
	public ReplyVO get(Long rno) {
		return replyRepository.read(rno);
	}

	@Override
	public int modify(ReplyVO vo) {
		return replyRepository.update(vo);
	}

	@Transactional
	@Override
	public int remove(Long rno) {
		ReplyVO vo = replyRepository.read(rno);
		boardRepository.updateReplyCnt(vo.getBno(), REPLY_DEL);
		return replyRepository.delete(rno);
	}

	@Override
	public ReplyPageDTO getList(Criteria criteria, Long bno) {
		return new ReplyPageDTO(
				replyRepository.getReplyCount(bno), 
				replyRepository.getList(bno, criteria));
	}
}
