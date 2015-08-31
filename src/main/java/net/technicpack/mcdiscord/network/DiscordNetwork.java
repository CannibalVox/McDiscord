package net.technicpack.mcdiscord.network;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.ChannelHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.technicpack.mcdiscord.McDiscord;
import net.technicpack.mcdiscord.network.handlers.UpdateGuildPacketHandler;
import net.technicpack.mcdiscord.network.packet.UpdateGuildPacket;

@ChannelHandler.Sharable
public class DiscordNetwork {
    private static final DiscordNetwork INSTANCE = new DiscordNetwork();
    private SimpleNetworkWrapper networkWrapper;

    public static void init() {
        INSTANCE.networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(McDiscord.MODID);
        INSTANCE.networkWrapper.registerMessage(UpdateGuildPacketHandler.class, UpdateGuildPacket.class, 0, Side.CLIENT);
    }

    public static void sendToAllPlayers(IMessage packet) {
        INSTANCE.networkWrapper.sendToAll(packet);
    }

    public static void sendToNearbyPlayers(IMessage message, int dimension, float x, float y, float z, float radius) {
        INSTANCE.networkWrapper.sendToAllAround(message, new NetworkRegistry.TargetPoint(dimension, x, y, z, radius));
    }

    public static void sendToPlayer(IMessage message, EntityPlayer player) {
        if (player instanceof EntityPlayerMP)
            INSTANCE.networkWrapper.sendTo(message, (EntityPlayerMP)player);
    }

}
