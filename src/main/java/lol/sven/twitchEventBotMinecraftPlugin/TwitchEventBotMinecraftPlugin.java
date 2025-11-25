package lol.sven.twitchEventBotMinecraftPlugin;

import lol.sven.twitchEventBotMinecraftPlugin.twitch.TwitchBot;
import lol.sven.twitchEventBotMinecraftPlugin.twitch.TwitchChat;
import lol.sven.twitchEventBotMinecraftPlugin.twitch.TwitchRedemption;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class TwitchEventBotMinecraftPlugin extends JavaPlugin {

    private static TwitchBot twitchBot;
    Plugin plugin = this;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        TwitchRedemption.updateRedemptionNames();
        TwitchChat.registerCommands();

        twitchBot = new TwitchBot();

    }

    @Override
    public void onDisable() {
        if (twitchBot != null) {
            twitchBot.stop();
        }
    }

    public static TwitchBot getTwitchBot() {
        return twitchBot;
    }

}
