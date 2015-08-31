package net.technicpack.mcdiscord.discord.callback;

public abstract class DiscordCallback<T> implements IDiscordCallback {

    private boolean runOnServer;

    public DiscordCallback(boolean runOnServer) {
        this.runOnServer = runOnServer;
    }

    public boolean shouldRunOnServer() { return this.runOnServer; }
    public abstract void callback(T result);

    private T storedValue;
    public void setValue(T value) {
        this.storedValue = value;
    }

    @Override
    public void run() {
        this.callback(this.storedValue);
    }
}
