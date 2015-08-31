package net.technicpack.mcdiscord;

import net.technicpack.mcdiscord.data.GuildModel;

public class CommonProxy {

    private GuildModel guildModel = null;
    public void setGuildModel(GuildModel guildModel) { this.guildModel = guildModel; }
    public GuildModel getGuildModel() { return this.guildModel; }

    public void registerHudHandler() { }
}
