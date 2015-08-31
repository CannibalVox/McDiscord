package net.technicpack.mcdiscord.discord.callback;

/**
 * We package-protect this interface.  It's only there as a facilitator between
 * DiscordCallback & DiscordResponseHandler.
 */
interface IDiscordCallback {
    boolean shouldRunOnServer();
    void run();
}
