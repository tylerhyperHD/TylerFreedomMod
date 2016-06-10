package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.config.ConfigEntry;
import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SENIOR_ADMIN, source = SourceType.BOTH, blockHostConsole = true)
@CommandParameters(description = "Close server to non-superadmins.", usage = "/<command> [on | off]", aliases = "tm")
public class Command_trainingmode extends FreedomCommand
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length != 1)
        {
            return false;
        }

        if (args[0].equalsIgnoreCase("off -s"))
        {
            ConfigEntry.TRAINING_SESSION.setBoolean(false);
            ConfigEntry.ADMIN_ONLY_MODE.setBoolean(false);
            ConfigEntry.TRAINING_SESSION.setBoolean(false);
            return true;
        }

        if (args[0].equalsIgnoreCase("off"))
        {
            ConfigEntry.TRAINING_SESSION.setBoolean(false);
            ConfigEntry.ADMIN_ONLY_MODE.setBoolean(false);
            ConfigEntry.TRAINING_SESSION.setBoolean(false);
            FUtil.adminAction(sender.getName(), "Stopping the Training Mode Session...", true);
            return true;
        }
        else if (args[0].equalsIgnoreCase("on"))
        {
            ConfigEntry.TRAINING_SESSION.setBoolean(true);
            ConfigEntry.ADMIN_ONLY_MODE.setBoolean(true);
            ConfigEntry.TRAINING_SESSION.setBoolean(true);
            FUtil.adminAction(sender.getName(), "Starting the Training Mode Session...", true);
            for (Player player : server.getOnlinePlayers())
            {
                checkRank(Rank.SENIOR_ADMIN);
                player.kickPlayer(ConfigEntry.SERVER_NAME.getString() + " is now in a Training Session.");
            }
            return true;
        }

        return false;
    }
}
