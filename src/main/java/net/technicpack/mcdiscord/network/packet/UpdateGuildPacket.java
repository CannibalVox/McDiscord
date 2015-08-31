package net.technicpack.mcdiscord.network.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.technicpack.mcdiscord.data.GuildModel;
import net.technicpack.mcdiscord.discord.io.guild.Guild;

public class UpdateGuildPacket implements IMessage {

    private String guildId;
    public String getGuildId() { return this.guildId; }

    private int memberCount;
    public int getMemberCount() { return this.memberCount; }

    private String channelInvite;
    public String getChannelInvite() { return this.channelInvite; }

    public UpdateGuildPacket() {}
    public UpdateGuildPacket(Guild guild) {
        this.guildId = guild.getId();
        this.memberCount = guild.getMemberCount();
        this.channelInvite = guild.getInviteLink();
    }

    public UpdateGuildPacket(GuildModel guild) {
        this.guildId = guild.getGuildId();
        this.memberCount = guild.getDiscordPlayerCount();
        this.channelInvite = guild.getInviteLink();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.guildId = readString(buf);
        this.memberCount = buf.readInt();
        this.channelInvite = readString(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {

        writeString(buf, this.guildId);
        buf.writeInt(this.memberCount);
        writeString(buf, this.channelInvite);
    }

    private String readString(ByteBuf buf) {
        boolean hasString = buf.readBoolean();
        if (!hasString)
            return null;

        return ByteBufUtils.readUTF8String(buf);
    }

    private void writeString(ByteBuf buf, String string) {
        if (string == null) {
            buf.writeBoolean(false);
            return;
        }

        buf.writeBoolean(true);
        ByteBufUtils.writeUTF8String(buf, string);
    }
}
