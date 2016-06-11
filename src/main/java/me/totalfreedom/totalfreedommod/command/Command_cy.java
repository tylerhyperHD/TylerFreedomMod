package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.OP, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Clear your chat", usage = "/<command>", aliases = "cm")
public class Command_cy extends FreedomCommand
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (senderIsConsole)
        {
            msg("You must be in-game to use this command.");
        }

        for(int i = 0; i <= 20; i++)
        {
            msg("");
        }
        msg("Chat cleared!", ChatColor.RED);
        return true;
    }
}