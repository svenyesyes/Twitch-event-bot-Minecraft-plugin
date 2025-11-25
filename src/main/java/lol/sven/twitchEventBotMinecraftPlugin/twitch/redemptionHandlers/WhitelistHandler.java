package lol.sven.twitchEventBotMinecraftPlugin.twitch.redemptionHandlers;

import com.github.twitch4j.pubsub.events.ChannelPointsRedemptionEvent;
import lol.sven.twitchEventBotMinecraftPlugin.TwitchEventBotMinecraftPlugin;
import lol.sven.twitchEventBotMinecraftPlugin.twitch.TwitchChat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.util.Set;
import java.util.UUID;

public class WhitelistHandler extends RedemptionHandler {

    static Plugin plugin = TwitchEventBotMinecraftPlugin.getPlugin(TwitchEventBotMinecraftPlugin.class);

    public boolean handleRedemption(ChannelPointsRedemptionEvent event) {

        String username = event.getRedemption().getUserInput();

        if (username == null || username.length() < 3 || (username.length() > 16 && !(username.length() == 32 || username.length() == 36 ))) {
            TwitchEventBotMinecraftPlugin.getTwitchBot().sendChat("Failed to whitelist this player: Invalid username or UUID.");
            return false;
        }

        OfflinePlayer p;

        try {


            if (username.replaceAll("-", "").length() == 32) {
                p = Bukkit.getOfflinePlayer(UUID.fromString(username));
            } else {
                p = Bukkit.getOfflinePlayer(username);
            }

            if (p.isBanned()) {
                TwitchEventBotMinecraftPlugin.getTwitchBot().sendChat("Failed to whitelist this player: This player is currently banned.");
                return false;
            }

            if(p.isWhitelisted()) {
                TwitchEventBotMinecraftPlugin.getTwitchBot().sendChat("Failed to whitelist this player: This player is already whitelisted.");
                return false;
            }

        } catch (Exception e) {
            TwitchEventBotMinecraftPlugin.getTwitchBot().sendChat("Failed to whitelist this player: Player may not exist.");
            return false;
        }

        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            public void run() {
                p.setWhitelisted(true);
                Bukkit.reloadWhitelist();
                TwitchEventBotMinecraftPlugin.getTwitchBot().sendChat("Added player " + p.getName() + " to whitelist.");
                plugin.getLogger().info("Added " + p.getName() + " to whitelist.");
            }
        });

        return true;
    }
}
