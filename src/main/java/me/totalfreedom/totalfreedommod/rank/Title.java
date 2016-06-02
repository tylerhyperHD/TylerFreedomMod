package me.totalfreedom.totalfreedommod.rank;

import lombok.Getter;
import org.bukkit.ChatColor;

public enum Title implements Displayable
{
    DEVELOPER("a", "Developer", ChatColor.DARK_PURPLE, "Dev"),
    SYS_ADMIN("a", "System Admin", ChatColor.DARK_RED, "SyS"),
    SPEC_EXEC("a", "Special Executive", ChatColor.YELLOW, "SpEx"),
    COOWNER("the", "&9Co-Owner &fof &6TylerFreedom", ChatColor.BLUE, "Co-Owner"),
    OWNER("the", "&9Owner &fof &6TylerFreedom", ChatColor.BLUE, "Owner");

    private final String determiner;
    @Getter
    private final String name;
    @Getter
    private final String tag;
    @Getter
    private final String coloredTag;
    @Getter
    private final ChatColor color;

    private Title(String determiner, String name, ChatColor color, String tag)
    {
        this.determiner = determiner;
        this.name = name;
        this.tag = "[" + tag + "]";
        this.coloredTag = ChatColor.DARK_GRAY + "[" + color + tag + ChatColor.DARK_GRAY + "]" + color;
        this.color = color;
    }

    @Override
    public String getColoredName()
    {
        return color + name;
    }

    @Override
    public String getColoredLoginMessage()
    {
        return determiner + " " + color + ChatColor.ITALIC + name;
    }

}
