package net.technicpack.mcdiscord.client;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;
import net.technicpack.mcdiscord.CommonProxy;
import net.technicpack.mcdiscord.event.HudHandler;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerHudHandler() {
        HudHandler handler = new HudHandler(getGuildModel());
        FMLCommonHandler.instance().bus().register(handler);
        MinecraftForge.EVENT_BUS.register(handler);
    }
}
