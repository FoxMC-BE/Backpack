package me.nico.backpack.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import me.nico.backpack.Backpack;

public class BackpackCommand extends PluginCommand<Backpack> {

    public BackpackCommand(Backpack plugin) {
        super("backpack", plugin);
        this.setAliases(new String[]{ "bp" });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player && sender.hasPermission("backpack.use")) {
            this.getPlugin().getManager().showBackpack(((Player) sender));
        }
        return false;
    }
}
