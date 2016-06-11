package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.rank.RankManager;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@CommandPermissions(level = Rank.OP, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Makes you invisible.", usage = "/<command> [on/off]", aliases = "vanish")
public class Command_v extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (plugin.al.isSeniorAdmin(sender))
        {
            if (args.length != 2)
            {
                return false;
            }

            Player player = (Player) sender;
            boolean fakeleavejoin = false;

            if (args[1].equals("fake"))
            {
                fakeleavejoin = true;
            }
            if (args[0].equals("on"))
            {
                PotionEffect invis = PotionEffectType.INVISIBILITY.createEffect(1000000000, 255);
                ((Player) sender).addPotionEffect(invis, true);

                for (Player pl : server.getOnlinePlayers())
                {
                    pl.hidePlayer(player);
                }
                player.setPlayerListName("");
                if (fakeleavejoin == true)
                {
                    FUtil.bcastMsg(player.getName() + "has left the game.", ChatColor.YELLOW);
                }
                sender_p.sendMessage(ChatColor.GOLD + "You are now invisible to other players.");
            }
            if (args[0].equals("off"))
            {
                PotionEffect noinvis = PotionEffectType.INVISIBILITY.createEffect(1, 0);
                ((Player) sender).addPotionEffect(noinvis, true);
                for (Player pl : server.getOnlinePlayers())
                {
                    pl.showPlayer(player);
                }

                sender_p.sendMessage(ChatColor.GOLD + "You are now visible to other players.");
                for (PotionEffect potion_effect : sender_p.getActivePotionEffects())
                {
                    sender_p.removePotionEffect(potion_effect.getType());
                }
                if (fakeleavejoin == true)
                {
                    RankManager.getPlayerName(player, plugin, true);
                    FUtil.bcastMsg(player.getName() + "has joined the game.", ChatColor.YELLOW);
                }
                else
                {
                    RankManager.getPlayerName(player, plugin, false);
                }
            }
        }
        else if (args.length != 1)
        {
            sender_p.sendMessage(ChatColor.RED + sender.getName() + String.format(", no invis please."));
            sender_p.setGameMode(GameMode.SURVIVAL);
            sender_p.getWorld().createExplosion(sender_p.getLocation().getBlockX(), sender_p.getLocation().getBlockY(), sender_p.getLocation().getBlockZ(), 0, false, false);
            sender_p.setHealth(0);
        }
        return true;
    }
}
