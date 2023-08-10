package com.jafa.controller;

import java.nio.file.AccessDeniedException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jafa.domain.BoardAttachVO;
import com.jafa.domain.BoardVO;
import com.jafa.domain.Criteria;
import com.jafa.domain.LikeDTO;
import com.jafa.domain.Pagination;
import com.jafa.service.BoardService;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/board")
@Log4j
public class BoardController {
	
	@Autowired
	private BoardService boardService; 
	
	@GetMapping("/list")
	public void list(Model model, Criteria criteria) {
		model.addAttribute("list", boardService.getList(criteria));
		model.addAttribute("p", new Pagination(criteria, boardService.totalCount(criteria)));
	}
	
	@GetMapping("/get")
	public void get(Long bno, Model model, Criteria criteria) {
		model.addAttribute("board", boardService.get(bno));
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify")
	public String modify(Long bno, Model model, Criteria criteria, Authentication auth) throws AccessDeniedException {
		BoardVO vo = boardService.get(bno);
		String username = auth.getName();
		if(!vo.getWriter().equals(username) && // 글작성자가 아닌경우
			!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) { // 관리자가 아닌 경우
			throw new AccessDeniedException("Access denided");
		}
		model.addAttribute("board", vo);
		return "board/modify";
	}

	@GetMapping("/getAttachList")
	@ResponseBody
	public ResponseEntity<List<BoardAttachVO>> getAttachList(Long bno){
		return new ResponseEntity<>(boardService.getAttachList(bno),HttpStatus.OK); 
	}
	
	@GetMapping("/getAttachFileInfo")
	@ResponseBody
	public ResponseEntity<BoardAttachVO> getAttach(String uuid){
		return new ResponseEntity<>(boardService.getAttach(uuid),HttpStatus.OK); 
	}
	

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/register")
	public void register() {}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/register")
	public String register(BoardVO vo, RedirectAttributes rttr) {
		boardService.register(vo);
		rttr.addFlashAttribute("result", vo.getBno()); // ${result}
		rttr.addFlashAttribute("operation", "register");
		return "redirect:/board/list";
	}

	@PreAuthorize("isAuthenticated() and principal.username==#vo.writer or hasRole('ROLE_ADMIN')")
	@PostMapping("/modify")
	public String modify(BoardVO vo, RedirectAttributes rttr, Criteria criteria) {
		if(boardService.modify(vo)) {
			rttr.addFlashAttribute("result", vo.getBno());
			rttr.addFlashAttribute("operation", "modify");
		}
		return "redirect:/board/list"+criteria.getListLink();
	}
	
	
	@PreAuthorize("isAuthenticated() and principal.username==#writer or hasRole('ROLE_ADMIN')")
	@PostMapping("/remove")
	public String remove(Long bno, RedirectAttributes rttr, Criteria criteria, String writer) {
		if(boardService.remove(bno)) {
			rttr.addFlashAttribute("result", bno);
			rttr.addFlashAttribute("operation", "remove");
		}
		return "redirect:/board/list"+criteria.getListLink();
	}
	
	// 게시물 추천 
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/like", produces = "plain/text; charset=utf-8")
	@ResponseBody
	public ResponseEntity<String> hitLike(LikeDTO likeDTO){
		String message = likeDTO.getBno() + "번 ";
		if(boardService.hitLike(likeDTO)) {
			// 추천
			message += "게시글을 추천하였습니다.";
		}else {
			// 추천 취소
			message += "게시글을 추천 취소 하였습니다.";
		}
		
		return new ResponseEntity<String>(message,HttpStatus.OK);
	}
	
	@PostMapping(value = "/islike")
	@ResponseBody
	public ResponseEntity<Boolean> isLike(LikeDTO likeDTO){
		return new ResponseEntity<Boolean>(boardService.isLike(likeDTO), HttpStatus.OK);
	}
}
