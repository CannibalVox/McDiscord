package net.technicpack.mcdiscord.network.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.technicpack.mcdiscord.data.ServerModel;
import net.technicpack.mcdiscord.discord.io.server.Server;

public class UpdateServerPacket implements IMessage {

    private String serverId;
    public String getServerId() { return this.serverId; }

    private int memberCount;
    public int getMemberCount() { return this.memberCount; }

    private String channelInvite;
    public String getChannelInvite() { return this.channelInvite; }

    public UpdateServerPacket() {}
    public UpdateServerPacket(Server server) {
        this.serverId = server.getId();
        this.memberCount = server.getMemberCount();
        this.channelInvite = server.getInviteLink();
    }

    public UpdateServerPacket(ServerModel server) {
        this.serverId = server.getServerId();
        this.memberCount = server.getDiscordPlayerCount();
        this.channelInvite = server.getInviteLink();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.serverId = readString(buf);
        this.memberCount = buf.readInt();
        this.channelInvite = readString(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {

        writeString(buf, this.serverId);
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
