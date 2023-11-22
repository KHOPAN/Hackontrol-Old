package com.khopan.hackontrol;

import java.util.concurrent.CompletableFuture;

public class MachineId {
	private final String identifier;

	public MachineId(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public static CompletableFuture<MachineId> getNonBlocking() {
		return CompletableFuture.supplyAsync(() -> new MachineId(CommandPrompt.execute("wmic csproduct get uuid").replaceAll("\\s+", "").substring(4)));
	}

	public static MachineId get() {
		try {
			return MachineId.getNonBlocking().get();
		} catch(Throwable Errors) {
			return new MachineId("");
		}
	}
}
