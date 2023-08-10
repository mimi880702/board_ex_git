<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>

<div class="login_area container d-flex justify-content-center align-items-center">
	<div class="w-50">
		<h1>로그인</h1>
		<form action="${ctxPath}/member/login" method="post">
			<div class="form-group">
				<input type="text" class="form-control" name="memberId" value="${memberId}" placeholder="아이디">
			</div>
			<div class="form-group">
				<input type="text" class="form-control" name="memberPwd" placeholder="비밀번호">
			</div>
			<c:if test="${not empty loginFail}">
				<p style="color:red;font-size: 10px;">${loginFail}</p>
			</c:if>
			<label>
				<input type="checkbox" name="remember-me"  class="mr-2">Remember-Me
			</label>
			<button class="form-control btn btn-outline-primary mb-3">로그인</button>
			<a href="${ctxPath}/findMemberInfo">아이디찾기/비밀번호재발급</a>		
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		</form>
	</div>
</div>
<%@ include file="../includes/footer.jsp"%>
<style>
.login_area {
	height: 50vh;
}
</style>