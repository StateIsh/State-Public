package com.thestatemc.commands.state;

import com.thestatemc.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class StateInfoCommand extends SubCommand {
    public StateInfoCommand() {
        super("/state info [state]", "info", "Get info about states");
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
