<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="addnew" style="margin-bottom: 20px">
	<%@include file="add.jsp"%>
</div>

<table class="table table-striped">
	<c:forEach var="comment" items="${comments}">
		<tr>
			<td>
				<div class="comment${comment.id}">
					<%@include file="item.jsp"%>
				</div>
			</td>
		</tr>
	</c:forEach>
</table>
