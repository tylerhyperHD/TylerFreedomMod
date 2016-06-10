package me.totalfreedom.totalfreedommod.command;

import com.earth2me.essentials.commands.PlayerNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.totalfreedom.totalfreedommod.player.FPlayer;
import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

    @CommandPermissions(level = Rank.OP, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Changes double jump mode", usage = "/<command> [player]", aliases = "/doublejump")
public class Command_djump extends FreedomCommand
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        FPlayer playerdata = plugin.pl.getPlayer(sender_p);
                
        if (args.length == 0)
        {
            
            FUtil.setDoubleJumper(playerdata, !FUtil.isDoubleJumper(playerdata));
            FUtil.playerMsg(sender_p, "Double Jump mode set to " + FUtil.isDoubleJumper(playerdata));
        }
        if (args.length == 1)
        {
            checkRank(Rank.SUPER_ADMIN);
            Player player = getPlayer(args[0]);
            FPlayer playme = plugin.pl.getPlayer(player);
            if (playme == null)
            {
                FUtil.playerMsg(sender, "Player not found!");
            }
            else
            {
                FUtil.setDoubleJumper(playme, !FUtil.isDoubleJumper(playme));
                FUtil.playerMsg(player, "Double Jump mode of " + player.getName() + " set to " + FUtil.isDoubleJumper(playme));
            }
        }
        return true;
    }
}