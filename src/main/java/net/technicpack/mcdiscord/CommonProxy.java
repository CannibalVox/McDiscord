package net.technicpack.mcdiscord;

import net.technicpack.mcdiscord.data.ServerModel;

public class CommonProxy {

    private ServerModel serverModel = null;
    public void setServerModel(ServerModel serverModel) { this.serverModel = serverModel; }
    public ServerModel getServerModel() { return this.serverModel; }

    public void registerHudHandler() { }
}
