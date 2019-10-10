package com.ag.poc.integration.scrapping.service.dealhub;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ag.poc.constant.DealSignConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

@Component
public class JsonUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

	public static String getStringNodeValue(JsonNode node, String path) {
		if (node != null) {
			String returnValue = getNodeValue(node, path);
			return StringUtils.isNotBlank(returnValue) ? returnValue : DealSignConstants.EMPTY_STRING;
		}
		return DealSignConstants.EMPTY_STRING;
	}

	public static String getNodeValue(JsonNode json, String path) {
		JsonNode nodevalue = retrieveNodeValueByPath(json, path);
		if (nodevalue != null && nodevalue.isTextual()) {
			return nodevalue.textValue();
		}
		return null;
	}

	public static JsonNode retrieveNodeValueByPath(JsonNode node, String nodePath) {
		JsonNode current = node;
		if (node != null && nodePath != null) {
			String[] paths = nodePath.split("\\.");
			for (String path : paths) {
				current = getSingleNodeValueFromPath(current, paths, path);
				if (current == null) {
					return current;
				}
			}
		}
		return current;
	}

	public static boolean isValidArrayNode(JsonNode node) {
		return node != null && node.isArray();
	}

	@SuppressWarnings("deprecation")
	private static JsonNode getSingleNodeValueFromPath(JsonNode node, String[] paths, String path) {
		JsonNode current = node;
		if (isValidArrayNode(current) && NumberUtils.isNumber(path)) {
			current = current.get(Integer.parseInt(path));
		} else if (current != null) {
			current = current.get(path);
		} else {
			LOGGER.error("'" + path + "' node not found while walking for '" + StringUtils.join(paths, ".") + "'");
			return current;
		}
		return current;
	}

	public static JsonNode toJson(String jsonString) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readTree(jsonString);
		} catch (IOException e) {
			return JsonNodeFactory.instance.nullNode();
		}
	}
}
