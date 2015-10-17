package com.aggrepoint.doc;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.aggrepoint.winlet.site.SiteContext;
import com.aggrepoint.winlet.site.domain.Area;
import com.aggrepoint.winlet.site.domain.Page;
import com.aggrepoint.winlet.spring.annotation.Window;
import com.aggrepoint.winlet.spring.annotation.Winlet;

@Winlet("toc")
public class TocController {
	HashMap<String, String> tocMap = new HashMap<String, String>();

	static Pattern LINKS = Pattern.compile(
			"<h([12])\\s+id\\s*=([\"'])((?:(?!\\2).)*)\\2[^>]*>(.*?)</h\\1>",
			Pattern.DOTALL);

	/**
	 * <pre>
	 * 根据area中的内容里包含的 &lt; h1 &gt; 和 &lt; h2 &gt; 元素自动生成目录
	 * </pre>
	 * 
	 * @param request
	 * @param area
	 * @param model
	 * @return
	 */
	@Window("toc")
	public String tocWindow(HttpServletRequest request,
			@RequestParam("area") String area, Model model) {
		SiteContext sc = (SiteContext) request
				.getAttribute(SiteContext.SITE_CONTEXT_KEY);
		if (sc == null)
			return "";

		Page page = sc.getPage();
		String index = tocMap.get(page.getPath());
		if (index == null) {
			List<Area> areas = page.getAreas(area);
			if (areas == null || areas.size() < 1)
				return "";

			StringBuffer sb = new StringBuffer();
			sb.append("<nav id=\"docindex\" class=\"bs-docs-sidebar\"><ul class=\"nav bs-docs-sidenav\">");
			int level = 0;

			Matcher m = LINKS.matcher(areas.get(0).getContent());
			while (m.find()) {
				switch (m.group(1)) {
				case "1":
					switch (level) {
					case 0:
						sb.append("<li>");
						break;
					case 1:
						sb.append("</li><li>");
						break;
					case 2:
						sb.append("</li></ul></li><li>");
						break;
					}

					level = 1;
					break;
				case "2":
					switch (level) {
					case 0:
						sb.append("<li><ul class=\"nav\"><li>");
						break;
					case 1:
						sb.append("<ul class=\"nav\"><li>");
						break;
					case 2:
						sb.append("</li><li>");
						break;
					}

					level = 2;
					break;
				}

				sb.append("<a class=\"scroll\" href=\"#").append(m.group(3)).append("\">")
						.append(m.group(4)).append("</a>");
			}

			switch (level) {
			case 1:
				sb.append("</li></ul></nav>");
				break;
			case 2:
				sb.append("</li></ul></li></ul></nav>");
				break;
			}

			index = sb.toString();
			tocMap.put(page.getPath(), index);
		}

		model.addAttribute("content", index);
		return "toc";
	}
}
