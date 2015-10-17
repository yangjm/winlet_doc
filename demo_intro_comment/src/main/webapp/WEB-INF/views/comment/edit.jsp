<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<form action="save" method="post" data-winlet-focus="email">
	<div class="form-group">
		<label>邮件</label>
		<input type="email" name="email" class="form-control" placeholder="邮件地址" value="${comment.email}">
	</div>
	<div class="form-group">
		<label>留言</label>
		<textarea name="content" class="form-control" rows="3" placeholder="留言">${comment.content.replace("<br>", "&#xd;&#xa;")}</textarea>
	</div>

	<button class="btn btn-primary" type="submit">保存</button>
	<button class="btn btn-default" type="button" onclick="win$.post('cancelEdit')">取消</button>
</form>
