package com.khopan.hackontrol;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class CommandPrompt {
	private CommandPrompt() {}

	public static CompletableFuture<String> executeNonBlocking(String command) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
				builder.redirectErrorStream(true);
				Process process = builder.start();
				return new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
			} catch(Throwable Errors) {
				return "";
			}
		});
	}

	public static String execute(String command) {
		try {
			return CommandPrompt.executeNonBlocking(command).get();
		} catch(Throwable Errors) {
			throw new RuntimeException(Errors);
		}
	}
}
