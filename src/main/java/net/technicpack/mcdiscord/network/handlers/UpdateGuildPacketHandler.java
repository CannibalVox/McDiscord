package net.technicpack.mcdiscord.network.handlers;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.technicpack.mcdiscord.McDiscord;
import net.technicpack.mcdiscord.network.packet.UpdateGuildPacket;

public class UpdateGuildPacketHandler implements IMessageHandler<UpdateGuildPacket, IMessage> {
    @Override
    public IMessage onMessage(UpdateGuildPacket message, MessageContext ctx) {
        McDiscord.proxy.getGuildModel().updateGuild(message);

        return null;
    }
}
