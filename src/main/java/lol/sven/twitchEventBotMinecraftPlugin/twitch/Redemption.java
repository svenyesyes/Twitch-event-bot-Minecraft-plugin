package lol.sven.twitchEventBotMinecraftPlugin.twitch;

import lol.sven.twitchEventBotMinecraftPlugin.twitch.redemptionHandlers.RedemptionHandler;
import lol.sven.twitchEventBotMinecraftPlugin.twitch.redemptionHandlers.WhitelistHandler;

public enum Redemption {

    ADD_WHITELIST("whitelist", new WhitelistHandler());

    private final String id;
    private final RedemptionHandler handler;

    Redemption(String id, RedemptionHandler handler) {
        this.id = id;
        this.handler = handler;
    }

    public String getId() {
        return id;
    }

    public RedemptionHandler getHandler() {
        return handler;
    }

    @Override
    public String toString() {
        return id;
    }
}
