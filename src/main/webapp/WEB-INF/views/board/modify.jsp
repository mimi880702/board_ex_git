<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp"%>
<div class="container">
	<div class="row">
		<div class="col-12">
			<h1 class="page-header">Board Modify Page</h1>
		</div>
	</div>
	
	<div class="row">
		<div class="col-12">
			<div class="card">
				<div class="card-header">
					Board Modify Page
				</div>
				<div class="card-body">
					<form action="${ctxPath}/board/modify" method="post" class="modifyForm">
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
						<div class="form-group">
							<label>Bno</label>	
							<input class="form-control" name="bno" value="${board.bno}" readonly="readonly"/>
						</div>
						<div class="form-group">
							<label>Title</label>
							<input class="form-control" name="title" value="${board.title}" />
						</div>
						<div class="form-group">
							<label>Text area </label>
							<textarea class="form-control" rows="10" name="content">${board.content}</textarea>
						</div>
						<div class="form-group">
							<label>Writer </label>
							<input class="form-control" name="writer" value="${board.writer }" readonly="readonly"/>
						</div>
						<div class="form-group">
							<label>Register Date</label>
							<input class="form-control" readonly="readonly" name="regDate" 
								value="<tf:formatDateTime value="${board.regDate}" pattern="yyyy년MM월dd일 HH시mm분"/>">
						</div>
						<div class="form-group">
							<label>Update Date</label>
							<input class="form-control" readonly="readonly" name="updateDate" 
								value="<tf:formatDateTime value="${board.updateDate}" pattern="yyyy년MM월dd일 HH시mm분"/>">
						</div>
						<button type="button" data-oper='modify' class="btn btn-light">Modify</button>
						<button type="button" data-oper='remove' class="btn btn-danger">Remove</button>
						<button type="button" data-oper='list' class="btn btn-info">List</button>
					</form>						
				</div>
			</div>
		</div>
	</div>
	
	<div class="row my-5">
		<div class="col-12">
			<div class="card">
				<div class="card-header">
					<h4>파일 첨부</h4>
				</div>
				<div class="card-body">
					<div class="uploadDiv form-group">
						<input type="file"  class="form-control" name="uploadFile" multiple="multiple">
					</div>
					<div class="uploadResultDiv form-group">
						<ul class="list-group"></ul>
					</div>
				</div>
			</div>
		</div>
	</div>	
	
</div>

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
let formObj = $('.modifyForm')
let addCriteria = function(){
	formObj.append($('<input/>',{type : 'hidden', name : 'pageNum', value : '${criteria.pageNum}'}))
		   .append($('<input/>',{type : 'hidden', name : 'amount', value : '${criteria.amount}'}))
	if(type&&keyword){
		formObj.append($('<input/>',{type : 'hidden', name : 'type', value : '${criteria.type}'}))
			.append($('<input/>',{type : 'hidden', name : 'keyword', value : '${criteria.keyword}'}))
	}
}
let type = '${criteria.type}'
let keyword = '${criteria.keyword}'
</script>
<script src="${ctxPath}/resources/js/modify.js"></script>
