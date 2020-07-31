package ru.servbuy.regions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin
{
    public static FileConfiguration locale;
    public static File localeFile;


    public static String translateColor(final String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public void onEnable() {
        ActionBar.plugin = this;
        ActionBar.version = Bukkit.getServer().getClass().getPackage().getName();
        ActionBar.version = ActionBar.version.substring(ActionBar.version.lastIndexOf(".") + 1);
        getCommand("rginfo").setExecutor(new CommandExecutor(this));
        saveDefaultConfig();
        localeFile = new File(this.getDataFolder(),"locale.yml");
        if (!localeFile.exists())
            saveResource("locale.yml", false);
        locale = YamlConfiguration.loadConfiguration(localeFile);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(this), 0L, 30L);
    }
}
