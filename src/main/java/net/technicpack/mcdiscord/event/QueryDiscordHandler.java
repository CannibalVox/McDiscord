package net.technicpack.mcdiscord.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.technicpack.mcdiscord.McDiscord;
import net.technicpack.mcdiscord.data.ServerModel;
import net.technicpack.mcdiscord.discord.api.IDiscordApi;
import net.technicpack.mcdiscord.discord.callback.DiscordCallback;
import net.technicpack.mcdiscord.discord.io.server.Server;
import net.technicpack.mcdiscord.network.DiscordNetwork;
import net.technicpack.mcdiscord.network.packet.UpdateServerPacket;

public class QueryDiscordHandler {
    private IDiscordApi discordApi;
    private ServerModel model;

    public QueryDiscordHandler(IDiscordApi discordApi, ServerModel model) {
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
        this.discordApi.getServer(model.getServerId(), new DiscordCallback<Server>(true) {
            @Override
            public void callback(Server result) {
                if (result != null && !McDiscord.proxy.getServerModel().equals(result)) {
                    McDiscord.proxy.getServerModel().updateServer(result);
                    DiscordNetwork.sendToAllPlayers(new UpdateServerPacket(result));
                }
            }
        });
    }
}
