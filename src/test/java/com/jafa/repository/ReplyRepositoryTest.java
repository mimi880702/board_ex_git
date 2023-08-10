package com.jafa.repository;

import java.util.stream.IntStream;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jafa.AppTest;
import com.jafa.domain.Criteria;
import com.jafa.domain.ReplyVO;

import lombok.extern.log4j.Log4j;

@Log4j
public class ReplyRepositoryTest extends AppTest{

	@Autowired
	ReplyRepository replyRepository;
	
	@Test
	@Ignore
	public void insertTest() {
		IntStream.rangeClosed(1, 10).forEach(i->{			
			ReplyVO vo = ReplyVO.builder()
					.bno(1L)
					.reply("댓글 테스트"+i)
					.replyer("사용자"+i)
					.build();
			replyRepository.insert(vo);
		});
	}

	@Test
	@Ignore
	public void readTest() {
		ReplyVO vo = replyRepository.read(1L);
		log.info(vo);
	}
	
	@Test
	@Ignore
	public void updateTest() {
		ReplyVO vo = new ReplyVO();
		vo.setReply("댓글 테스트 -- 수정");
		vo.setRno(2L);
		replyRepository.update(vo);
	}
	
	@Test
	@Ignore
	public void deleteTest() {
		replyRepository.delete(3L);
	}
	
	@Test
	public void getListTest() {
		replyRepository.getList(1L, new Criteria())
			.forEach(r -> log.info(r));
	}
}
