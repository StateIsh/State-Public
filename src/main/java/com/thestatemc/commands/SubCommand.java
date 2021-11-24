package com.thestatemc.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand {
    private final String usage;
    private final String name;
    private final String description;
    private final String[] aliases;

    public SubCommand(String usage, String name, String description, String... aliases) {
        this.usage = usage;
        this.name = name;
        this.description = description;
        this.aliases = aliases;
    }

    public abstract boolean onCommand(CommandSender sender, String label, String[] args);

    public abstract void onTabComplete(CommandSender sender, String[] args, List<String> tabComplete);

    public abstract boolean isVisible(CommandSender sender);

    public String getDescription() {
        return description;
    }

    public java.lang.String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getUsage() {
        return usage;
    }

    public String getPermission() {
        return null;
    }
}
