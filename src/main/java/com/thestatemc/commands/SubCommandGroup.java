package com.thestatemc.commands;

import com.thestatemc.State;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class SubCommandGroup implements CommandExecutor, TabCompleter {
    private final HashMap<String, SubCommand> commands = new HashMap<>();
    private final HelpCommand helpCommand;

    public SubCommandGroup(String commandName) {
        this(commandName, ChatColor.GOLD);
    }

    public SubCommandGroup(String commandName, org.bukkit.ChatColor commandColor) {
        this(commandName, commandColor.asBungee());
    }

    public SubCommandGroup(String commandName, ChatColor commandColor) {
        PluginCommand command = Bukkit.getPluginCommand(commandName);
        if (command == null) {
            throw new NoSuchElementException(String.format("Command %s not found", commandName));
        }

        command.setExecutor(this);
        command.setTabCompleter(this);

        helpCommand = new HelpCommand(commandName, commandColor);
        addCommand(helpCommand);
    }

    public void addCommand(SubCommand subCommand) {
        commands.put(subCommand.getName(), subCommand);
        helpCommand.addCommand(subCommand);
        for (String alias : subCommand.getAliases()) {
            commands.put(alias, subCommand);
        }
    }

    public SubCommand getCommand(String commandName) {
        return commands.get(commandName.toLowerCase());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            SubCommand subCommand = getCommand(args[0]);

            if (subCommand != null) {

                if (!subCommand.isVisible(sender) ||
                        (subCommand.getPermission() != null && !sender.hasPermission(subCommand.getPermission())))
                    return helpCommand.onCommand(sender, "", args);

                String[] newArgs = new String[args.length - 1];
                System.arraycopy(args, 1, newArgs, 0, newArgs.length);
                Bukkit.getScheduler().runTask(State.getPlugin(), () -> {
                    try {
                        subCommand.onCommand(sender, args[0], newArgs);
                    } catch (Exception e) {
                        sender.sendMessage(ChatColor.RED + "Unhandled exception: " + e.getClass().getSimpleName() + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                });
                return true;
            }
        }

        return helpCommand.onCommand(sender, "", args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> tabCompletion = new ArrayList<>();

        if (args.length >= 2) {
            SubCommand subCommand = commands.get(args[0]);

            if (subCommand != null) {
                String[] newArgs = new String[args.length - 1];
                System.arraycopy(args, 1, newArgs, 0, newArgs.length);
                subCommand.onTabComplete(sender, newArgs, tabCompletion);
            }

        } else if (args.length == 1) {
            commands.forEach((key, subCommand) -> {
                if ((subCommand.getPermission() == null || sender.hasPermission(subCommand.getPermission()))
                        && key.startsWith(args[0].toLowerCase()) && subCommand.isVisible(sender)) {
                    tabCompletion.add(key);
                }
            });
        }

        return tabCompletion;
    }
}
