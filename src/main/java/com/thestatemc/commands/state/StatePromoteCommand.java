package com.thestatemc.commands.state;

import com.thestatemc.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class StatePromoteCommand extends SubCommand {
    public StatePromoteCommand() {
        super("/state promote <player>", "promote", "Promote a player to admin");
    }

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        return true;
    }

    @Override
    public void onTabComplete(CommandSender sender, String[] args, List<String> tabComplete) {
    }

    @Override
    public boolean isVisible(CommandSender sender) {
        return true;
    }
}
