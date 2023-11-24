package com.khopan.hackontrol.network;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Response {
	private final Request request;
	private final ObjectMapper mapper;

	public Response(Request request) {
		this.request = request;
		this.mapper = new ObjectMapper();
	}

	public void parse(String response) {
		if(response == null) {
			return;
		}

		JsonNode node;

		try {
			node = this.mapper.readTree(response);
		} catch(Throwable ignored) {
			return;
		}

		if(!(node instanceof ObjectNode)) {
			return;
		}

		ObjectNode objectNode = (ObjectNode) node;

		if(!objectNode.has("requestMode")) {
			return;
		}

		int mode = objectNode.get("requestMode").asInt(-1);

		if(mode < 0) {
			return;
		}

		switch(mode) {
		case ResponseMode.STATUS_QUERY:
			this.request.statusReport(true);
			break;
		}
	}
}
