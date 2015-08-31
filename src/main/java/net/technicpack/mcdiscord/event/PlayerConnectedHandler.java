package net.technicpack.mcdiscord.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.technicpack.mcdiscord.McDiscord;
import net.technicpack.mcdiscord.network.DiscordNetwork;
import net.technicpack.mcdiscord.network.packet.UpdateGuildPacket;

public class PlayerConnectedHandler {
    @SubscribeEvent
    public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        DiscordNetwork.sendToPlayer(new UpdateGuildPacket(McDiscord.proxy.getGuildModel()), event.player);
    }
}
