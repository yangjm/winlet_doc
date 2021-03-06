package com.aggrepoint.doc;

import java.io.IOException;
import java.util.function.Function;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtil {
	// http://stackoverflow.com/questions/3907929/should-i-make-jacksons-objectmapper-as-static-final
	static final ObjectMapper mapper = new ObjectMapper();

	public static String toJSON(Object obj) throws JsonGenerationException,
			JsonMappingException, IOException {
		return mapper.writeValueAsString(obj);
	}

	public static JsonNode fromJSON(String json)
			throws JsonProcessingException, IOException {
		return mapper.readTree(json);
	}

	public static <T> T getFieldValue(JsonNode node, String field,
			Function<JsonNode, T> func, T def) {
		JsonNode fieldNode = node.get(field);
		if (fieldNode == null)
			return def;
		T ret = func.apply(fieldNode);
		return ret == null ? def : ret;
	}

	public static <T> T getFieldValue(JsonNode node, String field,
			Function<JsonNode, T> func) {
		return getFieldValue(node, field, func, null);
	}

	public static String getTextFieldValue(JsonNode node, String field,
			String def) {
		return getFieldValue(node, field, JsonNode::asText, def);
	}

	public static Integer getIntFieldValue(JsonNode node, String field,
			Integer def) {
		// 用asInt无法获得NULl结果
		String val = getTextFieldValue(node, field, null);
		if (val == null || val.trim().equalsIgnoreCase(""))
			return def;
		try {
			return Integer.parseInt(val);
		} catch (Exception e) {
		}
		return def;
	}
}
