package com.khopan.hackontrol;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Hackontrol {
	static {
		System.load("D:\\GitHub Repository\\Hackontrol\\dll\\token.dll");
	}

	private static final String BOT_TOKEN = Hackontrol.getBotToken();

	private static native String getBotToken();

	private final JDA bot;

	private Hackontrol() throws Throwable {
		this.bot = JDABuilder.createDefault(Hackontrol.BOT_TOKEN)
				.enableIntents(GatewayIntent.GUILD_MESSAGES)
				.build();

		this.bot.awaitReady();
	}

	public static void main(String[] args) throws Throwable {
		new Hackontrol();
	}
}
