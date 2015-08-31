package net.technicpack.mcdiscord.discord.callback;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.server.MinecraftServer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class exists to collect async responses from discord and pipe them to
 * callbacks during a tick handler.  The goal here is to put callbacks in a context
 * where worlds are accessible & editable.  We also do some work to make sure that
 * we can specify whether the callback is to be run in client or server contexts.
 */
public class DiscordResponseHandler {
    private Queue<IDiscordCallback> discordResponses = new ConcurrentLinkedQueue<IDiscordCallback>();
    private Queue<IDiscordCallback> clientDiscordResponses = new LinkedList<IDiscordCallback>();

    public DiscordResponseHandler() {

    }

    public void add(IDiscordCallback discordResponse) {
        this.discordResponses.add(discordResponse);
    }

    @SubscribeEvent
    public void onTickEnd(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;
        if (event.world.provider.dimensionId != 0)
            return;

        Queue<IDiscordCallback> processQueue = discordResponses;
        Queue<IDiscordCallback> transferQueue = clientDiscordResponses;
        if (event.world.isRemote) {
            processQueue = clientDiscordResponses;
            transferQueue = discordResponses;
        }

        //We only have one queue accessible externally to simplify the API.
        //Instead of having the network layer handle the difference, the caller
        //just makes sure that the difference is available in the callback object.
        //If we are running on a dedicated server, then client callbacks are
        //discarded.  Ideally, we shouldn't be generating them.
        while (!processQueue.isEmpty()) {
            IDiscordCallback response = processQueue.remove();

            if (response.shouldRunOnServer() == !event.world.isRemote)
                response.run();
            else if (!MinecraftServer.getServer().isDedicatedServer())
                transferQueue.add(response);
        }
    }
}
