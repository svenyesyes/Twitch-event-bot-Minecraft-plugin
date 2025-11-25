package lol.sven.twitchEventBotMinecraftPlugin.twitch;

import com.github.twitch4j.pubsub.domain.ChannelPointsRedemption;
import com.github.twitch4j.pubsub.events.ChannelPointsRedemptionEvent;
import lol.sven.twitchEventBotMinecraftPlugin.TwitchEventBotMinecraftPlugin;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class TwitchRedemption {

    static Plugin plugin = TwitchEventBotMinecraftPlugin.getPlugin(TwitchEventBotMinecraftPlugin.class);
    private static HashMap<String, Redemption> redemptionNames = new HashMap<String, Redemption>();

    public static void updateRedemptionNames() {

        for (int i = 0; i < Redemption.values().length; i++) {
            Redemption r = Redemption.values()[i];
            String redemptionId = r.toString();

            String redemptionName = plugin.getConfig().getString("redemption_names." + redemptionId);

            if(redemptionName == null || redemptionName.isEmpty()) {
                plugin.getLogger().warning("Could not find channel point name for " + redemptionId);
                continue;
            }

            redemptionNames.put(redemptionName, r);
            plugin.getLogger().info("Linked " + redemptionId + " to channel point " + redemptionName);

        }
    }

    public static boolean HandleRedemption(ChannelPointsRedemptionEvent event) {

        ChannelPointsRedemption redemption = event.getRedemption();
        if (redemption == null || redemption.getReward().getTitle().isEmpty()) {
            return false;
        }

        Redemption r = redemptionNames.get(redemption.getReward().getTitle());
        if (r == null) {
            return false;
        }

        return r.getHandler().handleRedemption(event);
    }

}
