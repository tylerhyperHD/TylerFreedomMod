package me.totalfreedom.totalfreedommod.rank;

import me.totalfreedom.totalfreedommod.FreedomService;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.admin.Admin;
import me.totalfreedom.totalfreedommod.config.ConfigEntry;
import me.totalfreedom.totalfreedommod.player.FPlayer;
import me.totalfreedom.totalfreedommod.util.FUtil;
import net.pravian.aero.util.ChatUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class RankManager extends FreedomService
{

    public RankManager(TotalFreedomMod plugin)
    {
        super(plugin);
    }

    @Override
    protected void onStart()
    {
    }

    @Override
    protected void onStop()
    {
    }

    public Displayable getDisplay(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            return getRank(sender); // Consoles don't have display ranks
        }

        final Player player = (Player) sender;

        // Display impostors
        if (plugin.al.isAdminImpostor(player))
        {
            return Rank.IMPOSTOR;
        }

        // Developers always show up
        if (FUtil.DEVELOPERS.contains(player.getName()))
        {
            return Title.DEVELOPER;
        }

        if (FUtil.SYS_ADMINS.contains(player.getName()))
        {
            return Title.SYS_ADMIN;
        }

        if (FUtil.SPEC_EXECS.contains(player.getName()))
        {
            return Title.SPEC_EXEC;
        }

        final Rank rank = getRank(player);

        // Non-admins don't have titles, display actual rank
        if (!rank.isAdmin())
        {
            return rank;
        }

        // If the player's an owner, display that
        if (ConfigEntry.SERVER_OWNERS.getList().contains(player.getName()))
        {
            return Title.OWNER;
        }

        return rank;
    }

    public Rank getRank(CommandSender sender)
    {
        if (sender instanceof Player)
        {
            return getRank((Player) sender);
        }

        // CONSOLE?
        if (sender.getName().equals("CONSOLE"))
        {
            return ConfigEntry.ADMINLIST_CONSOLE_IS_SENIOR.getBoolean() ? Rank.SENIOR_CONSOLE : Rank.TELNET_CONSOLE;
        }

        // Console admin, get by name
        Admin admin = plugin.al.getEntryByName(sender.getName());

        // Unknown console: RCON?
        if (admin == null)
        {
            return Rank.SENIOR_CONSOLE;
        }

        Rank rank = admin.getRank();

        // Get console
        if (rank.hasConsoleVariant())
        {
            rank = rank.getConsoleVariant();
        }
        return rank;
    }

    public Rank getRank(Player player)
    {
        if (plugin.al.isAdminImpostor(player))
        {
            return Rank.IMPOSTOR;
        }

        final Admin entry = plugin.al.getAdmin(player);
        if (entry != null)
        {
            return entry.getRank();
        }

        return player.isOp() ? Rank.OP : Rank.NON_OP;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        final Player player = event.getPlayer();
        //plugin.pl.getData(player);
        final FPlayer fPlayer = plugin.pl.getPlayer(player);

        // Unban admins
        boolean isAdmin = plugin.al.isAdmin(player);
        if (isAdmin)
        {
            // Verify strict IP match
            if (!plugin.al.isIdentityMatched(player))
            {
                FUtil.bcastMsg("Warning: " + player.getName() + " is an admin, but is using an account not registered to one of their ip-list.", ChatColor.RED);
                fPlayer.setSuperadminIdVerified(false);
            }
            else
            {
                fPlayer.setSuperadminIdVerified(true);
                plugin.al.updateLastLogin(player);
            }
        }

        // Handle impostors
        if (plugin.al.isAdminImpostor(player))
        {
            FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + Rank.IMPOSTOR.getColoredLoginMessage());
            FUtil.bcastMsg("Warning: " + player.getName() + " has been flagged as an impostor and has been frozen!", ChatColor.RED);
            player.getInventory().clear();
            player.setOp(false);
            player.setGameMode(GameMode.SURVIVAL);
            plugin.pl.getPlayer(player).getFreezeData().setFrozen(true);
            player.sendMessage(ChatColor.RED + "You are marked as an impostor, please verify yourself!");
            return;
        }

        // Set display
        if (isAdmin || FUtil.DEVELOPERS.contains(player.getName()))
        {
            final Displayable display = getDisplay(player);
            String loginMsg = display.getColoredLoginMessage();

            if (isAdmin)
            {
                Admin admin = plugin.al.getAdmin(player);
                if (admin.hasLoginMessage())
                {
                    loginMsg = ChatUtils.colorize(admin.getLoginMessage());
                }

                if (player.getName().equals("hypertechHD"))
                {
                    FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is the " + ChatColor.translateAlternateColorCodes('&', "&9&lOwner &bof &6&lTylerFreedom"));
                    player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&9Owner&8] &9" + player.getName()));
                    plugin.pl.getPlayer(player).setTag(display.getColoredTag());
                    flipCmdSpy(fPlayer);
                }
                else if (player.getName().equals("Generic_Trees"))
                {
                    FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is the " + ChatColor.translateAlternateColorCodes('&', "&9&lCo-Owner &bof &6&lTylerFreedom"));
                    player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&9Co-Owner&8] &9" + player.getName()));
                    plugin.pl.getPlayer(player).setTag(display.getColoredTag());
                    flipCmdSpy(fPlayer);
                }
                else if (FUtil.DEVELOPERS.contains(player.getName()))
                {
                    FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + loginMsg);
                    player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&5Dev&8] &5" + player.getName()));
                    plugin.pl.getPlayer(player).setTag(display.getColoredTag());
                    flipCmdSpy(fPlayer);
                }
                else if (FUtil.SPEC_EXECS.contains(player.getName()))
                {
                    FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + loginMsg);
                    player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&eSpEx&8] &e" + player.getName()));
                    plugin.pl.getPlayer(player).setTag(display.getColoredTag());
                    flipCmdSpy(fPlayer);
                }
                else if (FUtil.SYS_ADMINS.contains(player.getName()))
                {
                    FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + loginMsg);
                    player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&4SyS&8] &4" + player.getName()));
                    plugin.pl.getPlayer(player).setTag(display.getColoredTag());
                    flipCmdSpy(fPlayer);
                }
                else if (plugin.al.isTelnetAdmin(player)) {
                    FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + loginMsg);
                    player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&2STA&8] &2" + player.getName()));
                    plugin.pl.getPlayer(player).setTag(display.getColoredTag());
                    flipCmdSpy(fPlayer);
                }
                else if (plugin.al.isSeniorAdmin(player))
                {
                    FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + loginMsg);
                    player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&dSrA&8] &d" + player.getName()));
                    plugin.pl.getPlayer(player).setTag(display.getColoredTag());
                    flipCmdSpy(fPlayer);
                }
                else
                {
                    FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + loginMsg);
                    player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&6SA&8] &b" + player.getName()));
                    plugin.pl.getPlayer(player).setTag(display.getColoredTag());
                    flipCmdSpy(fPlayer);
                }

            }

//              This method breaks too much, imma remove it.
//            String displayName = display.getColor() + player.getName();
//            try
//            {
//                player.setPlayerListName(StringUtils.substring(displayName, 0, 16));
//            }
//            catch (IllegalArgumentException ex)
//            {
//            }
        }
    }

    public static void flipCmdSpy(FPlayer fPlayer)
    {
        fPlayer.setCommandSpy(true);
    }
}
