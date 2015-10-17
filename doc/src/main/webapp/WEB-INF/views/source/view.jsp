<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ taglib uri="http://www.aggrepoint.com/winlet" prefix="win"%>

<div class="clearfix">
<c:forEach var="group" items="${groups}">
	<div class="file-group">
		<div>${group.name}</div>
		<ul>
			<c:forEach var="source" items="${group.files}">
				<li><a href="javascript:void(0)" onclick="win$.get(null, {id: ${source.id}})">${source.name}</a></li>
			</c:forEach>
		</ul>
	</div>
</c:forEach>		
</div>

<div class="file">
	<div class="file-header">${source.path}</div>
	${source.content}
</div>
