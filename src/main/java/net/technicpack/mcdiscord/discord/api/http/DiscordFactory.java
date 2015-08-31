package net.technicpack.mcdiscord.discord.api.http;

import net.technicpack.mcdiscord.discord.api.IDiscordApi;
import net.technicpack.mcdiscord.discord.callback.DiscordResponseHandler;

public class DiscordFactory {
    public static IDiscordApi createDiscordApi(DiscordResponseHandler handler) {
        return new HttpDiscordApi("https://discordapp.com/api/", handler);
    }
}
