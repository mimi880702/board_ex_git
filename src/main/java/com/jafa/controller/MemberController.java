package com.jafa.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jafa.domain.MemberVO;
import com.jafa.exception.NotFoundMemberException;
import com.jafa.exception.PasswordMisMatchException;
import com.jafa.service.MailSendService;
import com.jafa.service.MemberService;

import lombok.extern.log4j.Log4j;

@Log4j
@Controller
public class MemberController {
	
	@Autowired
	MemberService memberService; 
	
	@Autowired
	MailSendService mailSendService;
	
	// 마이페이지 
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MEMBER')")
	@GetMapping({"/mypage","/mypage/{path}"})
	public String myPage(Principal principal, Model model, @PathVariable(required = false) String path) {
		String memberId = principal.getName();
		if(path==null) {
			MemberVO memberVO = memberService.read(memberId);
			model.addAttribute("vo", memberVO);			
			return "member/mypage";
		}
		return "member/" + path;
	}
	
	// 회원정보수정
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MEMBER')")
	@PostMapping("/member/modify")
	public String modify(MemberVO memberVO, RedirectAttributes rttr) {
		memberService.modify(memberVO);
		rttr.addFlashAttribute("result", "modify");
		return "redirect:/mypage";
	}
	
	// 비밀번호 변경 처리
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MEMBER')")
	@PostMapping(value = "/mypage/changePwd", produces = "plain/text; charset=utf-8")
	@ResponseBody
	public ResponseEntity<String> chnagePwd(@RequestParam Map<String, String> memberMap){
		
		try {
			memberService.changePassword(memberMap);
		} catch (PasswordMisMatchException e) {
			return new ResponseEntity<String>("비밀번호가 일치하지 않습니다.",HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<String>("성공",HttpStatus.OK);
	}
	
	
	// 약관 동의
	@GetMapping("/join/step1")
	public String step1() {
		return "member/step1";
	}
	
	// 이메일 인증
	@PostMapping("/join/step2")
	public String step2(@RequestParam(defaultValue = "false") List<Boolean> agreement) {
		log.info(agreement);
		if(agreement.size()>=2 && agreement.stream().allMatch(v-> v)) {
			return "member/step2";			
		}
		return "member/step1";
	}
	
	// 인증번호 이메일 요청
	@GetMapping("/mailCheck")
	@ResponseBody
	public String mailCheck(String email) {
		return mailSendService.joinEmail(email);
	}
	
	
	@PostMapping("/join/step3")
	public String joinForm(MemberVO memberVO) {
		return "member/join";
	}
	
	@PostMapping("/member/join")
	public String join(MemberVO memberVO, RedirectAttributes rttr) {
		memberService.join(memberVO);
		return "redirect:/";
	}
	
	// 아이디 중복 체크 
	@PostMapping("/member/idCheck")
	@ResponseBody
	public ResponseEntity<Boolean> isDuplicateCheck(String memberId){
		MemberVO vo = memberService.read(memberId);
		return vo == null ? new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK) :
			new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.OK);
	}
	
	// 아이디 찾기 또는 임시비밀번호 발급 페이지
	@GetMapping("/findMemberInfo")
	public String findMemberInfo() {
		return "member/findMemberInfo";
	}
	
	// 아이디 찾기 이메일 전송 
	@PostMapping(value = "/findMemberId", produces = "plain/text; charset=utf-8")
	@ResponseBody
	public ResponseEntity<String> findMemberId(String email){
		try {
			mailSendService.findIdByEmail(email);			
		} catch (NotFoundMemberException e) {
			return new ResponseEntity<String>("회원정보를 찾을 수 없습니다.",HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("가입하신 이메일로 전송되었습니다.",HttpStatus.OK);
	}
	
	// 비밀번호 재발급 이메일 전송 
	@PostMapping(value = "/findMemberPwd", produces = "plain/text; charset=utf-8")
	@ResponseBody
	public ResponseEntity<String> findMemberPwd(String email){
		try {
			mailSendService.findPwdByEamil(email);			
		} catch (NotFoundMemberException e) {
			return new ResponseEntity<String>("회원정보를 찾을 수 없습니다.",HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("가입하신 이메일로 전송되었습니다.",HttpStatus.OK);
	}
	
	
	// 403 에러 처리
	@GetMapping("/accessDenied")
	public String accessDenided() {
		return "accessError";
	}
	
	// 로그인 페이지
	@RequestMapping("/login")
	public String loginPage(HttpServletRequest request, Authentication auth, RedirectAttributes rttr) {
		String uri = request.getHeader("Referer"); // 로그인 전 사용자가 보던 페이지 
		if(uri!=null && !uri.contains("/login")) {
			request.getSession().setAttribute("prevPage", uri);
		}
		log.info(uri);
		if(auth!=null) { // 이미 로그인 중 
			rttr.addFlashAttribute("duplicationLogin", "이미 로그인 중입니다.");
			if(uri==null) uri="/";
			return "redirect:"+uri;
		}
		return "member/login";
	}
}
