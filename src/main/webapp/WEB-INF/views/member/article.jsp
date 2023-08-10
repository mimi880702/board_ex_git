<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>

<div class="container">
	<ul class="list-group list-group-horizontal justify-content-center mt-3">
		<li class="list-group-item">
			<a href="${ctxPath}/mypage">회원정보변경</a>
		</li>
		<li class="list-group-item">
			<a href="${ctxPath}/mypage/article">내가 쓴 글</a>
		</li>
		<li class="list-group-item">
			<a href="${ctxPath}/mypage/comment">내가 쓴 댓글</a>
		</li>
		<li class="list-group-item">
			<a href="${ctxPath}/mypage/articleLike">좋아요 한 글</a>
		</li>
		<li class="list-group-item">
			<a href="${ctxPath}/mypage/commentLike">좋아요 한 댓글</a>
		</li>
	</ul>
	
	<h1>내가 쓴 글</h1>
	
</div>

<%@ include file="../includes/footer.jsp"%>
