package com.aggrepoint.doc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.aggrepoint.winlet.ReqInfo;
import com.aggrepoint.winlet.site.SiteContext;
import com.aggrepoint.winlet.site.domain.Area;
import com.aggrepoint.winlet.site.domain.Page;
import com.aggrepoint.winlet.spring.annotation.Return;
import com.aggrepoint.winlet.spring.annotation.Window;
import com.aggrepoint.winlet.spring.annotation.Winlet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * 从Github获取源代码显示
 * 
 * @author jiangmingyang
 *
 */
@Winlet("source")
public class GithubViewSourceController {
	/**
	 * 单个源文件
	 */
	static public class SourceFile {
		private int id;
		private String name;
		private String path;
		private String url;
		private String content;

		public SourceFile(int id, String url, String path) {
			this.id = id;
			this.path = path;
			int idx = path.lastIndexOf("/");
			name = idx >= 0 ? path.substring(idx + 1) : path;
			this.url = url + path;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		static final Pattern P_CONTENT = Pattern
				.compile(
						"<div class=\"blob-wrapper[^>]+>[\\s\\r\\n]+<table\\s+[^>]*>.*?</table>[\\s\\r\\n]+</div>",
						Pattern.DOTALL);
		static RestTemplate template = new RestTemplate();

		public String getContent() {
			if (content == null) { // 需要获取内容
				try {
					String html = template.getForObject(url, String.class);
					Matcher m = P_CONTENT.matcher(html);
					if (m.find())
						content = m.group(0);

					if (content == null) { // Jsoup会把代码中的tab替换为空格。用Pattern匹配不成功时才使用
						Document doc = Jsoup.connect(url).get();
						Elements code = doc.select("div.blob-wrapper");
						content = code.outerHtml();
					}

					// 防止win$.被winlet_local代码处理，替换$
					content = content.replace("win$.", "win&#36;.");
				} catch (IOException e) {
					content = "";
				}
			}

			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}

	/**
	 * 一组源文件
	 */
	static public class FileGroup {
		private String name;
		private List<SourceFile> files = new ArrayList<SourceFile>();

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<SourceFile> getFiles() {
			return files;
		}

		public void setFiles(List<SourceFile> files) {
			this.files = files;
		}
	}

	static public class FileGroups {
		private List<FileGroup> groups;
		private HashMap<Integer, SourceFile> fileMap;

		public FileGroups(List<FileGroup> groups) {
			this.groups = groups;
			fileMap = new HashMap<Integer, SourceFile>();
			for (FileGroup group : groups)
				for (SourceFile file : group.getFiles())
					fileMap.put(file.getId(), file);
		}

		public List<FileGroup> getGroups() {
			return groups;
		}

		public void setGroups(List<FileGroup> groups) {
			this.groups = groups;
		}

		public HashMap<Integer, SourceFile> getFileMap() {
			return fileMap;
		}

		public void setFileMap(HashMap<Integer, SourceFile> fileMap) {
			this.fileMap = fileMap;
		}

		public SourceFile getFile(int id) {
			return fileMap.get(id);
		}
	}

	HashMap<String, FileGroups> fileGroups = new HashMap<String, FileGroups>();

	@Window("main")
	@Return(value = "nocontext", log = "SiteContext not found", view = "")
	@Return(value = "noarea", log = "area not found", view = "")
	@Return(value = "nourl", log = "url not defined in JSON", view = "")
	@Return(value = "nofile", log = "no source file defined", view = "")
	public String mainWin(
			ReqInfo rInfo,
			@RequestParam("def") String def,
			@RequestParam(value = "id", required = false) Integer id,
			@RequestHeader(value = "X-Url-Prefix", required = false) String urlPrefix,
			Model model) throws JsonProcessingException, IOException {
		String pageId = rInfo.getPageId();

		if (urlPrefix != null)
			if (pageId.startsWith(urlPrefix))
				pageId = pageId.substring(urlPrefix.length());
System.out.println("########################################################################################");
System.out.println(pageId);
System.out.println(urlPrefix);
System.out.println("########################################################################################");

		FileGroups fg = fileGroups.get(pageId + def);

		if (fg == null) { // 还未把JSON加载到fileGroups中
			SiteContext sc = (SiteContext) rInfo.getRequest().getAttribute(
					SiteContext.SITE_CONTEXT_KEY);
			if (sc == null)
				return "nocontext";

			Page page = sc.getPage();
			List<Area> areas = page.getAreas(def);
			if (areas == null || areas.size() < 1)
				return "noarea";

			List<FileGroup> groups = new ArrayList<FileGroup>();
			int idx = 0;

			JsonNode node = JSONUtil.fromJSON(areas.get(0).getContent());
			String url = JSONUtil.getTextFieldValue(node, "url", null);
			if (url == null)
				return "nourl";
			ArrayNode arr = (ArrayNode) node.get("groups");

			for (int i = 0; i < arr.size(); i++) {
				node = arr.get(i);
				FileGroup group = new FileGroup();
				group.setName(JSONUtil.getTextFieldValue(node, "group", null));
				groups.add(group);

				ArrayNode files = (ArrayNode) node.get("files");

				for (int j = 0; j < files.size(); j++)
					group.getFiles().add(
							new SourceFile(idx++, url, files.get(j).asText()));
			}

			fg = new FileGroups(groups);
			fileGroups.put(rInfo.getPageId() + def, fg);
		}

		if (id == null)
			id = 0;
		SourceFile source = fg.getFile(id);
		if (source == null)
			source = fg.getFile(0);
		if (source == null)
			return "nofile";

		model.addAttribute("groups", fg.getGroups());
		model.addAttribute("source", source);
		return "view";
	}
}
