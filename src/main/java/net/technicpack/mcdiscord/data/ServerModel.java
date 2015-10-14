package net.technicpack.mcdiscord.data;

import net.technicpack.mcdiscord.discord.io.server.Server;
import net.technicpack.mcdiscord.network.packet.UpdateServerPacket;

public class ServerModel {
    private String serverId;
    public String getServerId() { return this.serverId; }

    private int discordPlayerCount;
    public int getDiscordPlayerCount() { return this.discordPlayerCount; }

    private String inviteLink;
    public String getInviteLink() { return this.inviteLink; }

    public ServerModel(String serverId) {
        this.serverId = serverId;
    }

    public boolean equals(Server server) {
        if (server == null)
            return false;

        if ((serverId == null) != (server.getId() == null))
            return false;

        if (serverId != null && !serverId.equals(server.getId()))
            return false;

        if (discordPlayerCount != server.getMemberCount())
            return false;

        if ((inviteLink == null) != (server.getInviteLink() == null))
            return false;

        if (inviteLink != null && !inviteLink.equals(server.getInviteLink()))
            return false;

        return true;
    }
    public void updateServer(Server server) {
        this.serverId = server.getId();
        this.discordPlayerCount = server.getMemberCount();
        this.inviteLink = server.getInviteLink();
    }

    public void updateServer(UpdateServerPacket packet) {
        this.serverId = packet.getServerId();
        this.discordPlayerCount = packet.getMemberCount();
        this.inviteLink = packet.getChannelInvite();
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
        this.discordPlayerCount = 0;
        this.inviteLink = null;
    }
}
