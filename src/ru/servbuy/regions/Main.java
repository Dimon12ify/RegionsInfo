package ru.servbuy.regions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

public class Main extends JavaPlugin
{
    public static FileConfiguration locale;
    public static File localeFile;

    private void start() {
        new BukkitRunnable() {
            public void run() {
                for (final Player player : Bukkit.getOnlinePlayers()) {
                    String regionName = null;
                    try {
                        regionName = API.getRegionName(player);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    for (final String region : Main.this.getConfig().getConfigurationSection("regions").getKeys(false)) {
                        try {
                            if (!API.getRegionName(player).equalsIgnoreCase(region)) continue;
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        regionName = getConfig().getString("regions." + region);
                        break;
                    }
                    if (!getConfig().getBoolean("showBar") || !ActionBar.works) return;
                    for (String ignoredWorld : getConfig().getStringList("disabled_worlds"))
                        if (player.getWorld().getName().equalsIgnoreCase(ignoredWorld)) return;
                    checkRegion(player, regionName);
                }
            }
        }.runTaskTimerAsynchronously(this, 0L, 30);
    }


    public static String translateColor(final String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    private void checkRegion(final Player player, final String region_name) {
        if (region_name.equalsIgnoreCase("__global__") && getConfig().getBoolean("showBarIfNoRegion"))
            ActionBar.sendActionBar(player, translateColor(getConfig().getString("global")));
        else if(getConfig().getBoolean("showBarIfNoRegion"))
            ActionBar.sendActionBar(player, translateColor(getConfig().getString("format").replace("%region%", region_name)));
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
        start();
    }
}
