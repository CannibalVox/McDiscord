package net.technicpack.mcdiscord.discord.api;

import net.technicpack.mcdiscord.discord.callback.DiscordCallback;
import net.technicpack.mcdiscord.discord.io.auth.AuthRequest;
import net.technicpack.mcdiscord.discord.io.server.Server;

public interface IDiscordApi {
    void getServer(String serverId, DiscordCallback<Server> callback);
    void authenticate(AuthRequest request, DiscordCallback<IAuthedDiscordApi> callback);
}
