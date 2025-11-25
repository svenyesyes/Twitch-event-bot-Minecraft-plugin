package lol.sven.twitchEventBotMinecraftPlugin.twitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.pubsub.events.*;
import lol.sven.twitchEventBotMinecraftPlugin.TwitchEventBotMinecraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TwitchBot {

    private TwitchClient twitchClient;
    private final Plugin plugin = TwitchEventBotMinecraftPlugin.getPlugin(TwitchEventBotMinecraftPlugin.class);

    private String channelName;
    private String broadcasterId;

    public TwitchBot() {

        this.channelName = plugin.getConfig().getString("channel");
        String twitchToken = plugin.getConfig().getString("oauth-token");

        if (twitchToken == null || channelName == null) {
            plugin.getLogger().warning("Twitch Bot disabled: Missing token or channel in config.yml");
            return;
        }

        OAuth2Credential credential = new OAuth2Credential("twitch", twitchToken);

        twitchClient = TwitchClientBuilder.builder()
                .withEnableHelix(true)
                .withEnableChat(true)
                .withEnablePubSub(true)
                .withChatAccount(credential)
                .withDefaultAuthToken(credential)
                .build();

        CompletableFuture.runAsync(() -> {
            try {
                plugin.getLogger().info("[Twitch] Fetching broadcaster ID...");

                broadcasterId = twitchClient.getHelix()
                        .getUsers(null, null, List.of(channelName))
                        .execute()
                        .getUsers()
                        .get(0)
                        .getId();

                plugin.getLogger().info("[Twitch] Broadcaster ID = " + broadcasterId);

                initPubSub(credential);
                joinChat();

            } catch (Exception ex) {
                plugin.getLogger().severe("[Twitch] Failed to start bot: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }

    private void initPubSub(OAuth2Credential credential) {
        if (broadcasterId == null) {
            plugin.getLogger().severe("[Twitch] Cannot init PubSub: broadcasterId = null");
            return;
        }

        plugin.getLogger().info("[Twitch] Subscribing to Channel Points...");
        twitchClient.getPubSub().listenForChannelPointsRedemptionEvents(credential, broadcasterId);

        twitchClient.getEventManager().onEvent(RewardRedeemedEvent.class, TwitchRedemption::HandleRedemption);


        plugin.getLogger().info("[Twitch] PubSub initialized.");
    }

    private void joinChat() {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            try {
                twitchClient.getChat().joinChannel(channelName);
                plugin.getLogger().info("[Twitch] Joined chat: #" + channelName);
                twitchClient.getEventManager().onEvent(com.github.twitch4j.chat.events.channel.ChannelMessageEvent.class, TwitchChat::handleChatMessage);

            } catch (Exception ex) {
                plugin.getLogger().warning("[Twitch] Failed to join chat: " + ex.getMessage());
            }
        }, 40L);
    }

    /**
     * Send message to chat safely
     */
    public void sendChat(String message) {
        if (twitchClient == null) return;

        twitchClient.getChat().sendMessage(channelName, message);
    }

    /**
     * Stop bot
     */
    public void stop() {
        try {
            if (twitchClient != null) {
                twitchClient.close();
                plugin.getLogger().info("[Twitch] Bot shut down.");
            }
        } catch (Exception ex) {
            plugin.getLogger().warning("[Twitch] Error shutting down: " + ex.getMessage());
        }
    }
}
