package me.totalfreedom.totalfreedommod;

import me.totalfreedom.totalfreedommod.config.ConfigEntry;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FOPMUtil
{
    public static boolean isHighRank(Player player)
    {
        if (FUtil.SYS_ADMINS.contains(player.getName()) && FUtil.SPEC_EXECS.contains(player.getName()) && ConfigEntry.SERVER_OWNERS.getList().contains(player.getName()) && player.getName().equals("Generic_Trees")) {
            return true;
        }
        else {
            return false;
        }
    }
    
    public static boolean isHighRank(CommandSender player)
    {
        if (FUtil.SYS_ADMINS.contains(player.getName()) && FUtil.SPEC_EXECS.contains(player.getName()) && ConfigEntry.SERVER_OWNERS.getList().contains(player.getName()) && player.getName().equals("Generic_Trees")) {
            return true;
        }
        else {
            return false;
        }
    }
}
