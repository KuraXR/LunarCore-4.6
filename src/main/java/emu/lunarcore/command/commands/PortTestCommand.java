package emu.lunarcore.command.commands;

import emu.lunarcore.command.Command;
import emu.lunarcore.command.CommandArgs;
import emu.lunarcore.command.CommandHandler;

/**
 * 4.6 test switches for field mappings that are not settled yet.
 * Each switch picks the other candidate field so both can be tried in game without a rebuild.
 */
@Command(label = "porttest", permission = "admin.porttest", desc = "/porttest [summonswap | groupprop] [on | off]. 4.6 mapping test switches.")
public class PortTestCommand implements CommandHandler {
    // SceneSummonUnitInfo: swap the caster entity id and the summon unit id (fields 6 / 13)
    public static volatile boolean summonSwap = false;
    // UpdateGroupPropertyScRsp: send the new value in field 15 instead of 14
    public static volatile boolean groupPropAlt = false;

    @Override
    public void execute(CommandArgs args) {
        String name = args.get(0).toLowerCase();
        String value = args.get(1).toLowerCase();

        if (name.isEmpty()) {
            args.sendMessage("summonswap=" + summonSwap + " groupprop=" + groupPropAlt);
            return;
        }

        boolean on = !value.equals("off");

        switch (name) {
            case "summonswap" -> summonSwap = on;
            case "groupprop" -> groupPropAlt = on;
            default -> {
                args.sendMessage("Unknown switch. Use summonswap or groupprop");
                return;
            }
        }

        args.sendMessage(name + " is now " + (on ? "on" : "off"));
    }

}
