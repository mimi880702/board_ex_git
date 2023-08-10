package com.jafa.service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jafa.domain.BoardAttachVO;
import com.jafa.domain.BoardVO;
import com.jafa.domain.Criteria;
import com.jafa.domain.LikeDTO;
import com.jafa.repository.ArticleLikeRepository;
import com.jafa.repository.BoardAttachRepository;
import com.jafa.repository.BoardRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Service
@RequiredArgsConstructor
@Log4j
public class BoardServiceImpl implements BoardService {

	private final BoardRepository boardRepository;
	
	@Autowired
	private BoardAttachRepository boardAttachRepository; 
	
	@Autowired
	private ArticleLikeRepository articleLikeRepository;

	@Override
	public List<BoardVO> getList(Criteria criteria) {
		return boardRepository.getList(criteria);
	}

	@Transactional
	@Override
	public void register(BoardVO board) {
		boardRepository.insertSelectKey(board);
		// 첨부파일이 있을 때
		if(board.getAttachList()!=null && !board.getAttachList().isEmpty()) {
			board.getAttachList().forEach(attachFile->{
				attachFile.setBno(board.getBno());
				boardAttachRepository.insert(attachFile); 
			});
		}
	}

	@Override
	public BoardVO get(Long bno) {
		return boardRepository.read(bno);
	}

	@Override
	public boolean modify(BoardVO board) {
		
		List<BoardAttachVO> attachList = board.getAttachList();
		
		if(attachList!=null) {
			// 기존 파일 삭제
			List<BoardAttachVO> delList = attachList.stream().filter(attach -> attach.getBno()!=null).collect(Collectors.toList());
			deleteFiles(delList); // 파일 삭제 
			delList.forEach(vo->{
				boardAttachRepository.delete(vo.getUuid()); // 데이터베이스 기록 삭제 
			});
			
			// 새로운 파일 추가 
			attachList.stream().filter(attach -> attach.getBno()==null).forEach(vo->{
				vo.setBno(board.getBno());
				boardAttachRepository.insert(vo); // 데이터베이스 기록 
			});
		}
		
		return boardRepository.update(board)==1;
	}

	private void deleteFiles(List<BoardAttachVO> delList) {
		delList.forEach(vo -> {
			File file = new File("c:/storage/"+vo.getUploadPath(),vo.getUuid()+"_"+vo.getFileName());
			file.delete();
			// 섬네일 삭제 
			if(vo.isFileType()) {
				file = new File("c:/storage/"+vo.getUploadPath(),"s_"+vo.getUuid()+"_"+vo.getFileName());
				file.delete();
			}
		});
	}

	@Transactional
	@Override
	public boolean remove(Long bno) {
		List<BoardAttachVO> attachList = getAttachList(bno);
		if(attachList!=null) {
			deleteFiles(attachList);
			boardAttachRepository.deleteAll(bno);
		}
		return boardRepository.delete(bno)==1;
	}

	@Override
	public int totalCount(Criteria criteria) {
		return boardRepository.getTotalCount(criteria);
	}

	@Override
	public List<BoardAttachVO> getAttachList(Long bno) {
		return boardAttachRepository.selectByBno(bno);
	}

	@Override
	public BoardAttachVO getAttach(String uuid) {
		return boardAttachRepository.selectByUuid(uuid);
	}

	@Transactional
	@Override
	public boolean hitLike(LikeDTO likeDTO) {
		LikeDTO result = articleLikeRepository.get(likeDTO);
		if(result==null) { // 추천
			articleLikeRepository.insert(likeDTO);
			boardRepository.updateLikeCnt(likeDTO.getBno(), 1);
			return true;
		} else { // 추천 취소 
			articleLikeRepository.delete(likeDTO);
			boardRepository.updateLikeCnt(likeDTO.getBno(), -1);
			return false;
		}
	}

	@Override
	public boolean isLike(LikeDTO likeDTO) {
		return articleLikeRepository.get(likeDTO) != null ;
	}

}
