package net.technicpack.mcdiscord.network.handlers;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.technicpack.mcdiscord.McDiscord;
import net.technicpack.mcdiscord.network.packet.UpdateServerPacket;

public class UpdateServerPacketHandler implements IMessageHandler<UpdateServerPacket, IMessage> {
    @Override
    public IMessage onMessage(UpdateServerPacket message, MessageContext ctx) {
        McDiscord.proxy.getServerModel().updateServer(message);

        return null;
    }
}
