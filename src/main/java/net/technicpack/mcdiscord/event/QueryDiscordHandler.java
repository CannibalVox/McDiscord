package net.technicpack.mcdiscord.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.technicpack.mcdiscord.McDiscord;
import net.technicpack.mcdiscord.data.GuildModel;
import net.technicpack.mcdiscord.discord.api.IDiscordApi;
import net.technicpack.mcdiscord.discord.callback.DiscordCallback;
import net.technicpack.mcdiscord.discord.io.guild.Guild;
import net.technicpack.mcdiscord.network.DiscordNetwork;
import net.technicpack.mcdiscord.network.packet.UpdateGuildPacket;

public class QueryDiscordHandler {
    private IDiscordApi discordApi;
    private GuildModel model;

    public QueryDiscordHandler(IDiscordApi discordApi, GuildModel model) {
        this.discordApi = discordApi;
        this.model = model;
    }

    private int ticksUntilQuery = 1;
    @SubscribeEvent
    public void periodicallyQuery(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;

        ticksUntilQuery--;

        if (ticksUntilQuery <= 0) {
            ticksUntilQuery = 20 * 15;

            forceQuery();
        }
    }

    public void forceQuery() {
        this.discordApi.getGuild(model.getGuildId(), new DiscordCallback<Guild>(true) {
            @Override
            public void callback(Guild result) {
                if (result != null && !McDiscord.proxy.getGuildModel().equals(result)) {
                    McDiscord.proxy.getGuildModel().updateGuild(result);
                    DiscordNetwork.sendToAllPlayers(new UpdateGuildPacket(result));
                }
            }
        });
    }
}
