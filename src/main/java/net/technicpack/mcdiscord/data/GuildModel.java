package net.technicpack.mcdiscord.data;

import net.technicpack.mcdiscord.discord.io.guild.Guild;
import net.technicpack.mcdiscord.network.packet.UpdateGuildPacket;

public class GuildModel {
    private String guildId;
    public String getGuildId() { return this.guildId; }

    private int discordPlayerCount;
    public int getDiscordPlayerCount() { return this.discordPlayerCount; }

    private String inviteLink;
    public String getInviteLink() { return this.inviteLink; }

    public GuildModel(String guildId) {
        this.guildId = guildId;
    }

    public boolean equals(Guild guild) {
        if (guild == null)
            return false;

        if ((guildId == null) != (guild.getId() == null))
            return false;

        if (guildId != null && !guildId.equals(guild.getId()))
            return false;

        if (discordPlayerCount != guild.getMemberCount())
            return false;

        if ((inviteLink == null) != (guild.getInviteLink() == null))
            return false;

        if (inviteLink != null && !inviteLink.equals(guild.getInviteLink()))
            return false;

        return true;
    }
    public void updateGuild(Guild guild) {
        this.guildId = guild.getId();
        this.discordPlayerCount = guild.getMemberCount();
        this.inviteLink = guild.getInviteLink();
    }

    public void updateGuild(UpdateGuildPacket packet) {
        this.guildId = packet.getGuildId();
        this.discordPlayerCount = packet.getMemberCount();
        this.inviteLink = packet.getChannelInvite();
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
        this.discordPlayerCount = 0;
        this.inviteLink = null;
    }
}
