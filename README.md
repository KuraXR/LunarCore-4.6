# LunarCore 4.6

A game server reimplementation for version 4.6.0 of a certain turn-based anime game for educational purposes.

This is a port of [Melledy/LunarCore](https://github.com/Melledy/LunarCore) (4.2.0) to the 4.6.0 client. All credit for the server itself goes to Melledy and the LunarCore contributors; this repository only moves it onto the 4.6 protocol and game data.

[EN](README.md) | [ID](docs/README_id-ID.md) | [简中](docs/README_zh-CN.md) | [繁中](docs/README_zh-TW.md) | [JP](docs/README_ja-JP.md) | [RU](docs/README_ru-RU.md) | [FR](docs/README_fr-FR.md) | [KR](docs/README_ko-KR.md) | [VI](docs/README_vi-VI.md)

**Attention:** the translated READMEs are upstream's 4.2 instructions. They don't include the 4.6 steps below (game data version and `clientDataDir`).

### Notable features
- Basic game features: Logging in, team setup and switching, inventory, basic scene/entity management
- Monster battles working
- Natural world monster/prop/NPC spawns (only the groups the server owns, as on the official server)
- Character techniques
- Crafting/Consumables working
- NPC shops handled
- Gacha system
- Mail system
- Friend system (Assists are not working yet)
- In-game chat commands through the "Server" friend
- Memory of Chaos, Pure Fiction and Apocalyptic Shadow (both halves, buffs, statistics)
- 4.6 one-step level up and ascension for characters and light cones
- Multi-path characters (Trailblazer, March 7th) including light cones per path
- Simulated universe (Runs can be finished, but many features are missing)

### Known issues
- Battle pass rewards can't be claimed (upstream LunarCore never implemented claiming).
- Some puzzles (rotators, power chargers) and talk missions don't react yet; their handlers were removed upstream in 4.2.
- Several 4.x event menus open with empty data.

# Running the server and client

### Prerequisites
* [Java 21 JDK](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
* The 4.6.0 game client, logged in to an official server at least once so it has downloaded its game data

### Recommended
* [Java 25 JDK](https://www.oracle.com/java/technologies/javase/jdk25-archive-downloads.html)
* [MongoDB 4.0+](https://www.mongodb.com/try/download/community)

### Compiling the server
1. Open your system terminal, and compile the server with `./gradlew jar`
2. Create a folder named `resources` in your server directory
3. Download the `Config`, `TextMap`, and `ExcelOutput` folders for **4.6** (you need to find these on your own, look around and you'll find them pretty easily) and place them into your resources folder.
4. Run the server once with `java -jar LunarCore.jar` to create `config.json`, then stop it.
5. **4.6 only:** set `clientDataDir` in `config.json` to your client's `StarRail_Data` folder, for example:
   ```json
   "clientDataDir": "C:/Program Files/HoYoPlay/games/Star Rail Games/StarRail_Data"
   ```
   The 4.6 client won't load without design data from the server. LunarCore hands the client its own cached copy (read only, nothing is downloaded or changed). If this is empty the client stops at 0% with an empty dialog.
6. Run the server with `java -jar LunarCore.jar` from your system terminal. Lunar Core comes with a built-in internal MongoDB server for its database, so no Mongodb installation is required. However, it is highly recommended to install Mongodb anyway.

### Connecting with the client (Fiddler method)
1. **Log in with the client to an official server and Hoyoverse account at least once to download game data.**
2. Install and have [Fiddler Classic](https://www.telerik.com/fiddler) running.
3. Copy and paste the following code into the Fiddlerscript tab of Fiddler Classic. Remember to save the fiddler script after you copy and paste it:

```
import System;
import System.Windows.Forms;
import Fiddler;
import System.Text.RegularExpressions;

class Handlers
{
    static function OnBeforeRequest(oS: Session) {
        if (oS.host.EndsWith(".starrails.com") || oS.host.EndsWith(".hoyoverse.com") || oS.host.EndsWith(".mihoyo.com") || oS.host.EndsWith(".bhsr.com")) {
            oS.oRequest.headers.UriScheme = "http";
            oS.host = "localhost"; // This can also be replaced with another IP address.
        }
    }
};
```

4. If `autoCreateAccount` is set to true in the config, then you can skip this step. Otherwise, type `/account create [account name]` in the server console to create an account.
5. Login with your account name, the password field is ignored by the server and can be set to anything.

If you change `httpServer.bindPort` in the config (for example to use a launcher proxy), point the proxy at that port instead.

### Server commands
Server commands can be run in the server console or in-game. There is a dummy user named "Server" in every player's friends list that you can message to use in-game commands.

```
/account {create | delete} [username] (reserved player uid). Creates or deletes an account.
/avatar {cur | all | lineup} lv(level) p(ascension) e(eidolon) s(skill levels). Sets the current avatar's properties
/buildavatar [{avatar id} | cur | all | lineup]. Creates a set of relics for the selected avatars
/clear {relics | lightcones | materials | items} lv(filter level). Removes filter items from the targeted player's inventory.
/energy. Refills all characters energy in current lineup.
/gender {male | female}. Sets the player gender.
/give [item id] x(amount) lv(level) r(rank) p(promotion). Gives the targeted player an item.
/giveall {materials | avatars | lightcones | relics | usables} lv(level). Gives the targeted player items.
/heal. Heals your avatars.
/help. Displays a list of available commands.
/kick @[player id]. Kicks a player from the server.
/lineup [avatar ids]. USE AT YOUR OWN RISK. Sets your current lineup with the specified avatar ids.
/mail [content]. Sends the targeted player a system mail.
/permission {add | remove | clear} [permission]. Gives/removes a permission from the targeted player.
/porttest [summonswap | groupprop] [on | off]. 4.6 mapping test switches.
/refill - refill your skill points in open world.
/reload. Reloads the server config.
/scene [scene id] [floor id]. Teleports the player to the specified scene.
/setlevel [level] - Sets the targeted player's trailblazer level.
/spawn [npc monster id/prop id] s[stage id] x[amount] lv[level] r[radius] <battle monster ids...>. Spawns a monster or prop near the targeted player.
/status. Displays the status of the server.
/stop - Stops the server
/tp [x] [y] [z]. Teleports the player to the specified coordinates.
/unstuck @[player id]. Unstucks an offline player if theyre in a scene that doesnt load.
/worldlevel [world level]. Sets the targeted player's equilibrium level.
```

# Credits
- [Melledy](https://github.com/Melledy) and all [LunarCore contributors](https://github.com/Melledy/LunarCore/graphs/contributors) for the server this is built on
- [Dimbreath](https://gitlab.com/Dimbreath/turnbasedgamedata) for the game data

LunarCore is licensed under the [AGPL-3.0](LICENSE); this port keeps the same license.
