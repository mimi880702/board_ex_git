package com.jafa.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;

@Component
@Log4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler{

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		log.info("로그인 실패");
		if(exception instanceof BadCredentialsException) {
			String memberId = request.getParameter("memberId");
			request.setAttribute("memberId", memberId);
			request.setAttribute("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
		}
		request.getRequestDispatcher("/login").forward(request, response);
	}

}
