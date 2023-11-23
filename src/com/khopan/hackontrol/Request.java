package com.khopan.hackontrol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class Request {
	private final TextChannel channel;
	private final MachineId identifier;
	private final ObjectMapper mapper;

	public Request(TextChannel channel) {
		this.channel = channel;
		this.identifier = MachineId.get();
		this.mapper = new ObjectMapper();
	}

	public void statusReport(boolean online) {
		ObjectNode node = this.mapper.createObjectNode();
		node.put("online", online);
		this.request(RequestMode.STATUS_REPORT, node);
	}

	private void request(int requestMode, ObjectNode node) {
		node.put("requestMode", requestMode);
		node.put("machineId", this.identifier.getIdentifier());
		String message = node.toString();
		this.channel.sendMessage(message).queue();
	}
}
