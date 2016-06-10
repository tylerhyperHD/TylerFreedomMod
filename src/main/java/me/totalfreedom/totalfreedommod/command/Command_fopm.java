package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FLog;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.OP, source = SourceType.BOTH)
@CommandParameters(description = "Shows information about FreedomOpMod or reloads it", usage = "/<command> [reload]")
public class Command_fopm extends FreedomCommand
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length == 1)
        {
            if (!args[0].equals("reload"))
            {
                return false;
            }
            
            if (!plugin.al.isAdmin(sender))
            {
                noPerms();
                return true;
            }

            plugin.config.load();
            plugin.services.stop();
            plugin.services.start();

            final String message = String.format("%s v%s reloaded.",
                    TotalFreedomMod.pluginName,
                    TotalFreedomMod.pluginVersion);

            msg(message);
            FLog.info(message);
            return true;
        }

        msg("Welcome to the FreedomOpMod; our server's main plugin!", ChatColor.GOLD);
        msg("Created by Camzie99", ChatColor.GOLD);
        StringBuilder developers = new StringBuilder();
        developers.append("Later worked on by: CrafterSmith12");
        for (String dev : FUtil.FOP_DEVELOPERS)
        {
            developers.append(", " + dev);
        }
        developers.append(".");
        msg(developers.toString(), ChatColor.AQUA);
        msg("Visit " + ChatColor.AQUA + "http://freedomop.boards.net/" + ChatColor.GREEN + " to visit our forums and get support!", ChatColor.GREEN);

        return true;
    }
}