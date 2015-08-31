package net.technicpack.mcdiscord.discord.api;

import net.technicpack.mcdiscord.discord.callback.DiscordCallback;
import net.technicpack.mcdiscord.discord.io.auth.AuthRequest;
import net.technicpack.mcdiscord.discord.io.guild.Guild;

public interface IDiscordApi {
    void getGuild(String guildId, DiscordCallback<Guild> callback);
    void authenticate(AuthRequest request, DiscordCallback<IAuthedDiscordApi> callback);
}
