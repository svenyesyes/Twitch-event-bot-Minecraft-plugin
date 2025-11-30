# Twitch Event Bot — Minecraft Plugin (Spigot/Paper) 

A powerful Twitch integration plugin that connects your Minecraft server with Twitch chat and Channel Point redemptions.  
Perfect for streamers who want to add interactive features, rewards, or chat-integration to their server.

`This is an template/example. In it's current state, this plugin deliveres very limited features.`

This is the **Spigot/Paper version**.  
For the **Fabric mod version**, click here:   
https://github.com/svenyesyes/Twitch-event-bot-Minecraft-fabric-mod

---

## Features

### Twitch Chat Integration
- The bot joins your Twitch chat
- Supports custom Twitch chat commands (`!examplecommand`)
- Server → Twitch chat messages supported
- Dynamic placeholders:
  - `{players}`
  - `{players.online}`

### Channel Points Integration
- Trigger server actions via Channel Point rewards
- Includes a full **whitelist redemption handler**

### Hot-reload Safe
- Handles server shutdown gracefully  
- Reconnects Twitch bot when server restarts

---

## Installation

1. Download the latest release:  
   https://github.com/svenyesyes/twitch-event-bot-Minecraft-plugin/releases

2. Place the JAR in:
```arduino
/plugins/
```

3. Start the server to generate `config.yml`

4. Stop the server and edit:

## Obtaining a Twitch OAuth Token

Use the official Twitch Chat OAuth generator:

https://id.twitch.tv/oauth2/authorize?client_id=YOUR_CLIENT_ID&redirect_uri=http://localhost&response_type=token&scope=chat:read+chat:edit+channel:read:redemptions

Copy the token (`access_token=...`) and paste it into your config:

```yaml
oauth-token: "your_oauth_token_here"
```
## Configuration

### Example:
```yaml
oauth-token: ""
channel: ""

ttv_chat:
  prefix: "!"
  commands:
    online: "Online players: {players.online}"
    players: "Players online: {players}"

redemption_names:
  whitelist: "Add to whitelist"
```
Redemption names should be set to the redemption title on Twitch.
For example, if my channel point redemption for Whitelist has the name "get whitelisted":
```yaml
redemption_names:
  whitelist: "get whitelisted"
```

## Custom Channel Rewards (Redemption Handlers)
Handlers live in:

```arduino
lol.sven.twitchEventBotMinecraftPlugin.twitch.redemptionHandlers
```
Create your own by extending:
```java
public abstract class RedemptionHandler {
    public abstract boolean handleRedemption(ChannelPointsRedemptionEvent event);
}
```

You can make your own interactions:
- Spawn mobs
- Give items
- Run commands
- TP players
- Trigger events
- Anything server-side

---

## Related Projects
**Fabric** mod version:
https://github.com/svenyesyes/Twitch-event-bot-Minecraft-fabric-mod
