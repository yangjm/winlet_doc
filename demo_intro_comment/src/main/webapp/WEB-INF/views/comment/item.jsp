<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="pull-right">
	<button type="button" class="btn btn-default"
		onclick="win$.embed('div.comment${comment.id}', 'edit', {id: ${comment.id}})">编辑</button>
	<button type="button" class="btn btn-default"
		onclick="win$.post('delete', {id: ${comment.id}})">删除</button>
</div>
<div>${comment.createTime}</div>
<div>${comment.email}</div>
<div>${comment.content}</div>
