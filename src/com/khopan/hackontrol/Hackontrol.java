package com.khopan.hackontrol;

import java.util.List;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Hackontrol {
	static {
		System.load("D:\\GitHub Repository\\Hackontrol\\dll\\token.dll");
	}

	private static final String BOT_TOKEN = Hackontrol.getBotToken();

	private static native String getBotToken();

	private final JDA bot;
	private final Guild guild;
	private final TextChannel channel;
	private final Request request;

	private Hackontrol() {
		this.bot = JDABuilder.createDefault(Hackontrol.BOT_TOKEN)
				.enableIntents(GatewayIntent.GUILD_MESSAGES)
				.build();

		try {
			this.bot.awaitReady();
		} catch(Throwable Errors) {
			System.exit(1);
		}

		this.guild = this.bot.getGuildById(1173967259304198154L);

		if(this.guild == null) {
			System.exit(1);
		}

		List<TextChannel> channels = this.guild.getTextChannels();

		if(channels.isEmpty()) {
			System.exit(1);
		}

		this.channel = channels.get(0);

		if(this.channel == null) {
			System.exit(1);
		}

		this.request = new Request(this.channel);
		this.request.statusReport(true);
	}

	public static void main(String[] args) throws Throwable {
		new Hackontrol();
	}
}
