package com.jafa.service;

import java.util.List;

import com.jafa.domain.BoardAttachVO;
import com.jafa.domain.BoardVO;
import com.jafa.domain.Criteria;
import com.jafa.domain.LikeDTO;

public interface BoardService {

	List<BoardVO> getList(Criteria criteria); // 목록
	
	void register(BoardVO board); //등록

	BoardVO get(Long bno); // 조회

	boolean modify(BoardVO board); // 수정

	boolean remove(Long bno); // 삭제
	
	//게시물수
	int totalCount(Criteria criteria);
	
	List<BoardAttachVO> getAttachList(Long bno);
	
	BoardAttachVO getAttach(String uuid);
	
	boolean hitLike(LikeDTO likeDTO);
	
	boolean isLike(LikeDTO likeDTO);
}
