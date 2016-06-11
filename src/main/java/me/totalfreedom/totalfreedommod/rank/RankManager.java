package me.totalfreedom.totalfreedommod.rank;

import me.totalfreedom.totalfreedommod.FreedomService;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import me.totalfreedom.totalfreedommod.admin.Admin;
import me.totalfreedom.totalfreedommod.config.ConfigEntry;
import me.totalfreedom.totalfreedommod.player.FPlayer;
import me.totalfreedom.totalfreedommod.util.FUtil;
import net.pravian.aero.util.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import static org.bukkit.potion.PotionEffectType.INVISIBILITY;

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

    public static void getPlayerName(Player player, TotalFreedomMod plugin, boolean announce)
    {
        final Displayable display = plugin.rm.getDisplay(player);
        //plugin.pl.getData(player);
        final FPlayer fPlayer = plugin.pl.getPlayer(player);

        // Unban admins
        boolean isAdmin = plugin.al.isAdmin(player);
        if (isAdmin)
        {
            // Verify strict IP match
            if (!plugin.al.isIdentityMatched(player))
            {
                if (announce)
                {
                    FUtil.bcastMsg("Warning: " + player.getName() + " is an admin, but is using an account not registered to one of their ip-list.", ChatColor.RED);
                }
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
            if (announce)
            {
                FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + Rank.IMPOSTOR.getColoredLoginMessage());
            }
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
                    if (announce)
                    {
                        FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is the " + ChatColor.translateAlternateColorCodes('&', "&9&lOwner &bof &6&lTylerFreedom"));
                    }
                    player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&9Owner&8] &9" + player.getName()));
                    plugin.pl.getPlayer(player).setTag(display.getColoredTag());
                    flipCmdSpy(fPlayer);
                    return;
                }
                else if (player.getName().equals("Generic_Trees"))
                {
                    if (announce)
                    {
                        FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is the " + ChatColor.translateAlternateColorCodes('&', "&9&lCo-Owner &bof &6&lTylerFreedom"));
                    }
                    player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&9Co-Owner&8] &9" + player.getName()));
                    plugin.pl.getPlayer(player).setTag(display.getColoredTag());
                    flipCmdSpy(fPlayer);
                    return;
                }
                else if (FUtil.DEVELOPERS.contains(player.getName()))
                {
                    if (announce)
                    {
                        FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + loginMsg);
                    }
                    player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&5Dev&8] &5" + player.getName()));
                    plugin.pl.getPlayer(player).setTag(display.getColoredTag());
                    flipCmdSpy(fPlayer);
                    return;
                }
                else if (FUtil.SPEC_EXECS.contains(player.getName()))
                {
                    if (announce)
                    {
                        FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + loginMsg);
                    }
                    player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&eSpEx&8] &e" + player.getName()));
                    plugin.pl.getPlayer(player).setTag(display.getColoredTag());
                    flipCmdSpy(fPlayer);
                    return;
                }
                else if (FUtil.SYS_ADMINS.contains(player.getName()))
                {
                    if (announce)
                    {
                        FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + loginMsg);
                    }
                    player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&4SyS&8] &4" + player.getName()));
                    plugin.pl.getPlayer(player).setTag(display.getColoredTag());
                    flipCmdSpy(fPlayer);
                    return;
                }
                else if (plugin.al.isSeniorAdmin(player))
                {
                    if (announce)
                    {
                        FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + loginMsg);
                    }
                    player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&dSrA&8] &d" + player.getName()));
                    plugin.pl.getPlayer(player).setTag(display.getColoredTag());
                    flipCmdSpy(fPlayer);
                    return;
                }
                else if (plugin.al.isTelnetAdmin(player))
                {
                    if (announce)
                    {
                        FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + loginMsg);
                    }
                    player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&2STA&8] &2" + player.getName()));
                    plugin.pl.getPlayer(player).setTag(display.getColoredTag());
                    flipCmdSpy(fPlayer);
                    return;
                }
                else
                {
                    if (announce)
                    {
                        FUtil.bcastMsg(ChatColor.AQUA + player.getName() + " is " + loginMsg);
                    }
                    player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&8[&6SA&8] &b" + player.getName()));
                    plugin.pl.getPlayer(player).setTag(display.getColoredTag());
                    flipCmdSpy(fPlayer);
                    return;
                }

            }
        }
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

        if (player.getName().equals("Generic_Trees"))
        {
            return Title.COOWNER;
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJump(PlayerMoveEvent event)
    {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (to.getBlockY() > from.getBlockY())
        {
            Player player = event.getPlayer();
            FPlayer playme = plugin.pl.getPlayer(event.getPlayer());
            if (FUtil.isDoubleJumper(playme))
            {
                player.setAllowFlight(true);
            }
        }
    }

    @EventHandler
    public void shootArrowEvent(EntityShootBowEvent event)
    {
        if (event.getProjectile() instanceof Arrow)
        {
            if (event.getEntity() instanceof Player)
            {
                Arrow arrow = (Arrow) event.getProjectile();
                ItemStack bow = event.getBow();
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerHurt(EntityDamageEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            Player player = (Player) event.getEntity();
            FPlayer playme = plugin.pl.getPlayer(player);
            if (FUtil.inGod(playme))
            {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            if (event.getDamager() instanceof Player)
            {
                Player player = (Player) event.getDamager();
                if (player.getGameMode() == GameMode.CREATIVE)
                {
                    FUtil.playerMsg(player, "NO GM / GOD PVP!", ChatColor.DARK_RED);
                    event.setCancelled(true);
                }
            }
            if (event.getDamager() instanceof Arrow)
            {
                Arrow arrow = (Arrow) event.getDamager();
                if (arrow.getShooter() instanceof Player)
                {
                    Player player = (Player) arrow.getShooter();
                    FPlayer playme = plugin.pl.getPlayer(player);
                    if (player.getGameMode() == GameMode.CREATIVE || FUtil.inGod(playme))
                    {
                        FUtil.playerMsg(player, "NO GM / GOD PVP!", ChatColor.DARK_RED);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDrinkPotion(PlayerItemConsumeEvent event)
    {
        Player player = event.getPlayer();
        if (event.getItem().getType() == Material.POTION)
        {
            if (FUtil.SYS_ADMINS.contains(player.getName()) && FUtil.SPEC_EXECS.contains(player.getName()) && ConfigEntry.SERVER_OWNERS.getList().contains(player.getName()) && player.getName().equals("Generic_Trees"))
            {
                event.getPlayer().sendMessage(ChatColor.GREEN + "Please use /potion to add potion effects, thank you!");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSplashPotion(PotionSplashEvent event)
    {
        for (PotionEffect effect : event.getPotion().getEffects())
        {
            if (effect.getType() == INVISIBILITY)
            {
                Entity e = event.getEntity();
                if (e instanceof Player)
                {
                    Player player = (Player) e;
                    player.sendMessage(ChatColor.RED + "You are not permitted to use invisibility potions!");
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        getPlayerName(player, plugin, true);
    }

    public static void flipCmdSpy(FPlayer fPlayer)
    {
        fPlayer.setCommandSpy(true);
    }
}
