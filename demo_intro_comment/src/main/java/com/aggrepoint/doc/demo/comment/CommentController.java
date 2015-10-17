package com.aggrepoint.doc.demo.comment;

import java.util.ArrayList;

import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;

import com.aggrepoint.winlet.spring.annotation.Action;
import com.aggrepoint.winlet.spring.annotation.Return;
import com.aggrepoint.winlet.spring.annotation.Window;
import com.aggrepoint.winlet.spring.annotation.Winlet;

/**
 * 实现留言的查看、增加、编辑和删除功能
 */
@Winlet("comment")
public class CommentController {
	/** 留言数据保存在这个列表中 */
	private ArrayList<Comment> comments = new ArrayList<Comment>();

	/**
	 * 根据ID在comments中查找留言
	 */
	private Comment findCommentById(Integer id) {
		if (id == null)
			return null;
		for (Comment comment : comments)
			if (comment.getId() == id)
				return comment;
		return null;
	}

	/**
	 * 显示留言列表
	 */
	@Window("list")
	public String listWin(Model model) {
		model.addAttribute("comments", comments);
		return "list";
	}

	/**
	 * 新建或编辑留言。id值为null表示新建，否则为要编辑的留言的id
	 */
	@Action
	public String edit(
			@RequestParam(value = "id", required = false) Integer id,
			Model model) {
		Comment comment = null;

		if (id == null) // 新建
			comment = new Comment();
		else
			comment = findCommentById(id);

		if (comment != null) {
			model.addAttribute("comment", comment);
			return "edit";
		}

		// 找不到要编辑的留言
		return "";
	}

	/**
	 * 取消编辑
	 */
	@Action
	public String cancelEdit(
			@RequestParam(value = "id", required = false) Integer id,
			Model model) {
		if (id == null) // 在添加新留言的区域中取消编辑，显示添加留言的按钮
			return "add";

		// 在编辑留言区域中取消编辑，重新显示被编辑的留言
		Comment comment = findCommentById(id);
		if (comment != null) {
			model.addAttribute("comment", comment);
			return "item";
		}

		// 找不到id对应的留言，不显示
		return "";
	}

	/**
	 * 保存留言
	 */
	@Action
	@Return(value = "error", log = "validation error", view = "")
	@Return(value = "added", log = "new comment saved", view = "", update = "!list")
	@Return(value = "update", log = "comment updated", view = "item")
	public String save(@Valid Comment comment, BindingResult bresult,
			Model model) {
		if (bresult.getErrorCount() > 0) // 输入有错误
			return "error";

		if (comment.getId() == 0) {
			// 添加新留言
			comments.add(comment.assignId());

			// 最多只保留20条留言
			while (comments.size() > 20)
				comments.remove(0);

			// 添加了新留言，刷新整个Winlet窗口
			return "added";
		}

		// 修改现有留言
		Comment current = findCommentById(comment.getId());
		if (current == null) // 找不到被修改的留言
			return "";

		current.copy(comment);

		// 重新显示被修改了的留言
		model.addAttribute("comment", current);
		return "update";
	}

	/**
	 * 删除留言
	 */
	@Action
	@Return(value = "", update = "window")
	public String delete(@RequestParam("id") int id) {
		Comment current = findCommentById(id);
		if (current != null)
			comments.remove(current);

		return "";
	}
}
