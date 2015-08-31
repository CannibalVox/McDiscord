package net.technicpack.mcdiscord;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import net.minecraftforge.common.config.Configuration;
import net.technicpack.mcdiscord.coremod.DiscordResourcePack;
import net.technicpack.mcdiscord.data.GuildModel;
import net.technicpack.mcdiscord.discord.api.IAuthedDiscordApi;
import net.technicpack.mcdiscord.discord.api.IDiscordApi;
import net.technicpack.mcdiscord.discord.api.http.DiscordFactory;
import net.technicpack.mcdiscord.discord.callback.DiscordCallback;
import net.technicpack.mcdiscord.discord.callback.DiscordResponseHandler;
import net.technicpack.mcdiscord.discord.io.auth.AuthRequest;
import net.technicpack.mcdiscord.event.PlayerConnectedHandler;
import net.technicpack.mcdiscord.event.QueryDiscordHandler;
import net.technicpack.mcdiscord.network.DiscordNetwork;
import org.apache.logging.log4j.Level;

public class McDiscord extends DummyModContainer {
    public static final String MODID = "mcdiscord";
    public static final String NAME = "MC Discord";
    public static final String VERSION = "0.1.0";

    public static CommonProxy proxy;

    @Mod.Instance
    public static McDiscord instance;

    public McDiscord() {
        super(new ModMetadata());

        ModMetadata metadata = getMetadata();
        metadata.modId = McDiscord.MODID;
        metadata.version = McDiscord.VERSION;
        metadata.name = "MC Discord";
        metadata.authorList = ImmutableList.of("Cannibalvox");
        metadata.url = "http://www.technicpack.net/";
        metadata.credits = "Developed by Technic";
        metadata.description = "Discord (http://discordapp.com/) integration in Minecraft.";
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }


    @Override
    public Class<?> getCustomResourcePackClass() { return DiscordResourcePack.class; }

    private void initProxy(String commonType, String clientType) {
        ClassLoader mcl = Loader.instance().getModClassLoader();
        String target = FMLCommonHandler.instance().getSide().isClient()?clientType:commonType;

        try {
            McDiscord.proxy = (CommonProxy)(Class.forName(target, true, mcl).newInstance());
        } catch (Exception ex) {
            FMLLog.log(Level.ERROR, ex, "An error occured trying to load a proxy into MCDiscord");
            throw new LoaderException(ex);
        }
    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent event) {
        initProxy("net.technicpack.mcdiscord.CommonProxy", "net.technicpack.mcdiscord.client.ClientProxy");

        DiscordNetwork.init();
        Configuration configFile = new Configuration(event.getSuggestedConfigurationFile());
        configFile.load();
        String guildId = configFile.get(Configuration.CATEGORY_GENERAL, "Discord Server ID", "", "The discord server ID can be found in the Widgets tab of the Server Settings page.  This only has to be set on the server-side.  It may take several hours after creating a server before it's available on the API.").getString();
        configFile.save();

        GuildModel model = new GuildModel(guildId);
        proxy.setGuildModel(model);

        FMLCommonHandler.instance().bus().register(new PlayerConnectedHandler());

        DiscordResponseHandler responseHandler = new DiscordResponseHandler();
        FMLCommonHandler.instance().bus().register(responseHandler);

        IDiscordApi api = DiscordFactory.createDiscordApi(responseHandler);

        FMLCommonHandler.instance().bus().register(new QueryDiscordHandler(api, model));

        proxy.registerHudHandler();
    }

}
