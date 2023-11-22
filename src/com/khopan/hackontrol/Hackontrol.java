package com.khopan.hackontrol;

import java.util.Objects;

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

	private Hackontrol() throws Throwable {
		this.bot = JDABuilder.createDefault(Hackontrol.BOT_TOKEN)
				.enableIntents(GatewayIntent.GUILD_MESSAGES)
				.build();

		this.bot.awaitReady();
		this.guild = Objects.requireNonNull(this.bot.getGuildById(1173967259304198154L));
		this.channel = Objects.requireNonNull(this.guild.getTextChannelById(1173967259862048891L));
		this.channel.sendMessage("Hello, world!").queue();
	}

	public static void main(String[] args) throws Throwable {
		new Hackontrol();
	}
}
