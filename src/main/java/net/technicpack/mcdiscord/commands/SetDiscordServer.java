package net.technicpack.mcdiscord.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.technicpack.mcdiscord.McDiscord;
import net.technicpack.mcdiscord.event.QueryDiscordHandler;
import net.technicpack.mcdiscord.network.DiscordNetwork;
import net.technicpack.mcdiscord.network.packet.UpdateServerPacket;

public class SetDiscordServer extends CommandBase {

    private QueryDiscordHandler queryDiscordHandler;

    public SetDiscordServer(QueryDiscordHandler handler) {
        this.queryDiscordHandler = handler;
    }

    @Override
    public String getCommandName() {
        return "discord-server";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "/discord-server <server ID> or /discord-server to remove";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            McDiscord.proxy.getServerModel().setServerId("");
            DiscordNetwork.sendToAllPlayers(new UpdateServerPacket(McDiscord.proxy.getServerModel()));
            return;
        }

        String serverId = args[0];
        McDiscord.proxy.getServerModel().setServerId(serverId);
        queryDiscordHandler.forceQuery();
    }
}
