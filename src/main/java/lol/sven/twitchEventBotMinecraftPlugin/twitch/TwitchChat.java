package lol.sven.twitchEventBotMinecraftPlugin.twitch;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import lol.sven.twitchEventBotMinecraftPlugin.TwitchEventBotMinecraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class TwitchChat {

    static Plugin plugin = TwitchEventBotMinecraftPlugin.getPlugin(TwitchEventBotMinecraftPlugin.class);

    private static Map<String, Object> commands;
    private static String prefix;

    public static void registerCommands() {
        prefix = plugin.getConfig().getString("ttv_chat.prefix", "!");

        var section = plugin.getConfig().getConfigurationSection("ttv_chat.commands");

        if (section == null) {
            commands = Map.of(); // leeg
            plugin.getLogger().warning("[TTV Chat] No commands found in config!");
            return;
        }

        commands = section.getValues(false);
        plugin.getLogger().info("[TTV Chat] Loaded " + commands.size() + " Twitch commands.");
    }


    public static void handleChatMessage(ChannelMessageEvent event) {

        String msg = event.getMessage().toLowerCase();

        if (!msg.startsWith(prefix.toLowerCase())) return;

        String raw = msg.substring(prefix.length());
        String cmd = raw.split(" ")[0];

        if (!commands.containsKey(cmd)) return;

        String response = dynamicText(String.valueOf(commands.get(cmd)));

        TwitchEventBotMinecraftPlugin
                .getTwitchBot()
                .sendChat(response);
    }

    private static String dynamicText(String raw) {
        int onlineCount = Bukkit.getOnlinePlayers().size();

        String players = String.join(", ",
                Bukkit.getOnlinePlayers().stream().map(p -> p.getName()).toList()
        );

        return raw
                .replace("{players}", players)
                .replace("{players.online}", String.valueOf(onlineCount));
    }

}
