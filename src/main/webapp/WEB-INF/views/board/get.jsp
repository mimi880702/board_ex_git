<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>
<div class="container">
	<div class="row">
		<div class="col-12">
			<h1 class="page-header">Board Read Page</h1>
		</div>
	</div>
	
	<div class="row">
		<div class="col-12">
			<div class="card">
				<div class="card-header">
					Board Read Page
				</div>
				<div class="card-body">
					<div class="form-group">
						<label>Bno</label>	
						<input class="form-control" name="bno" value="${board.bno}" readonly="readonly"/>
					</div>
					<div class="form-group">
						<label>Title</label>
						<input class="form-control" name="title" value="${board.title}" readonly="readonly"/> 
					</div>
					<div class="form-group">
						<label>Text area </label>
						<textarea class="form-control" rows="10" name="content" readonly="readonly">${board.title}</textarea>
					</div>
					<div class="form-group">
						<label>Writer </label>
						<input class="form-control" name="writer" value="${board.writer }" readonly="readonly"/>
					</div>
					<div class="getBtns">
						<sec:authorize access="isAuthenticated() and principal.username == #board.writer or hasRole('ROLE_ADMIN')">
							<button data-oper='modify' class="btn btn-light modify">Modify</button>
					 	</sec:authorize>
						<button data-oper='list' class="btn btn-info list">List</button>
					</div>
					<div class="text-center">
						<sec:authorize access="isAuthenticated()">
							<button class="btn btn-outline-primary like">추천</button>
						</sec:authorize>
					</div>						
				</div>
			</div>
		</div>
	</div> <!-- row end -->

	<div class="row my-5">
		<div class="col-12">
			<div class="card">
				<div class="card-header">
					<h4>파일 첨부</h4>
				</div>
				<div class="card-body">
					<div class="uploadResultDiv form-group">
						<ul class="list-group"></ul>
					</div>
				</div>
			</div>
		</div>
	</div>	
	
	<h3 class="mt-5">댓글</h3>
	<div class="row">
		<div class="col-12">
			<ul class="list-group chat">
				<li class="list-group-item" data-rno="댓글번호" >
					<div class="d-flex justify-content-between">
					  <div class="d-flex">
					    <div class="user_image mr-3" style="width: 75px">
					      <img class="rounded-circle" src="${ctxPath}/resources/images/userImage.png" style="width: 100%">
					    </div>
					    <div class="comment_wrap">
					      <div class="comment_info">
					        <span class="userName badge badge-pill badge-info mr-2">홍길동</span>
					        <span class="badge badge-dark">2023-06-20 09:30</span>
					      </div>
					      <div class="comment_content py-2">댓글 내용입니다. </div>
					    </div>
					  </div>
					  <div class="reply_modify">
					    <button type="button" class="btn btn-light dropdown-toggle" data-toggle="dropdown">변경</button>
					    <div class="dropdown-menu">						   
					      <a class="dropdown-item" href="modify">수정</a>
					      <a class="dropdown-item" href="delete">삭제</a>
					    </div>
					  </div>
					 </div>
				</li>
			</ul>		
		</div>
	</div>
	
	<div class="row mt-3">
		<div class="col-12 pagination_wrap"></div>
	</div>
	
	<!-- 댓글작성 -->	
	<div class="my-3 replyWriterForm">
		<sec:authorize access="isAnonymous()">
			<textarea  rows="6" placeholder="로그인한 사용자만 댓글을 쓸수 있습니다." 
			maxlength="400" class="form-control" readonly="readonly"></textarea>
		</sec:authorize>
		<sec:authorize access="isAuthenticated()">
		<textarea  rows="6" placeholder="댓글을 작성해주세요" 
			maxlength="400" class="replyContent form-control"></textarea>
		<div class="text-right">
			<div class="submit p-2">
				<span class="btn btn-outline-info col-2 replyer">${authInfo.memberId }</span>
				<button class="btn btn-outline-primary col-3">등록</button>
			</div>
		</div>
		</sec:authorize>
	</div>
</div> <!-- end container -->

<form>
	<input type="hidden" name="bno" id="bno" value="${board.bno }">	
</form>

<!-- Modal -->
<div class="modal fade" id="showImage">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title">원본 이미지보기</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <!-- Modal body -->
            <div class="modal-body"></div>
        </div>
    </div>
</div>

<%@ include file="../includes/footer.jsp" %>

<script>
$(function(){
	// 목록 or 수정 페이지로
	let form = $('form')
	$('.getBtns button').click(function(){
		let operration = $(this).data('oper');
		let type = '${criteria.type}'
		let keyword = '${criteria.keyword}'
		
		form.append($('<input/>',{type : 'hidden', name : 'pageNum', value : '${criteria.pageNum}'}))
			.append($('<input/>',{type : 'hidden', name : 'amount', value : '${criteria.amount}'}))
			.attr('method','get')
			
		if(type&&keyword){
			form.append($('<input/>',{type : 'hidden', name : 'type', value : '${criteria.type}'}))
				.append($('<input/>',{type : 'hidden', name : 'keyword', value : '${criteria.keyword}'}))
		}
			
		if(operration=='list'){
			form.find('#bno').remove();
			form.attr('action','${ctxPath}/board/list')
		} else if(operration=='modify'){
			form.attr('action','${ctxPath}/board/modify')
		}
		form.submit();
	});
	
	$('.like').click(function(){
		let bno = $('[name="bno"]').val();
		$.ajax({
			type : 'post', 
			url : '${ctxPath}/board/like', 
			data : {memberId : memberId, bno : bno}, 
			success : function(message){
				alert(message);
				isLike(); 
			}
		})
	});
	
	// 추천 여부 
	function isLike(){
		let bno = $('[name="bno"]').val();
		$.ajax({
			type : 'post', 
			url : '${ctxPath}/board/islike', 
			data : {memberId : memberId, bno : bno}, 
			success : function(result){
				if(result){
					$('.like').html('추천취소')
				} else {
					$('.like').html('추천')
				}
			}
		});
	}
	if(memberId!=''){
		isLike(); 
	}
	
});
</script>
<script src="${ctxPath}/resources/js/get.js"></script>
<script src="${ctxPath}/resources/js/replyService.js"></script>
<script src="${ctxPath}/resources/js/reply.js"></script>
