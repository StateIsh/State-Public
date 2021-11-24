package com.thestatemc.commands.state;

import com.thestatemc.commands.SubCommandGroup;
import org.bukkit.ChatColor;

public class StateCommand extends SubCommandGroup {
    public StateCommand() {
        super("state", ChatColor.GOLD);

        addCommand(new StateColorCommand());
        addCommand(new StateCreateCommand());
        addCommand(new StateDemoteCommand());
        addCommand(new StateDisbandCommand());
        addCommand(new StateDiscordCommand());
        addCommand(new StateInfoCommand());
        addCommand(new StateInviteCommand());
        addCommand(new StateJoinCommand());
        addCommand(new StateInfoCommand());
        addCommand(new StateInviteCommand());
        addCommand(new StateJoinCommand());
        addCommand(new StateKickCommand());
        addCommand(new StateLeaveCommand());
        addCommand(new StatePromoteCommand());
        addCommand(new StateTransferCommand());
    }
}
