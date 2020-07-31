package ru.servbuy.regions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;



public class Runnable implements java.lang.Runnable
{
    private static Plugin plugin;

    public Runnable(Plugin plugin){
        this.plugin = plugin;
    }

    @Override
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
            for (final String region : plugin.getConfig().getConfigurationSection("regions").getKeys(false)) {
                try {
                    if (!API.getRegionName(player).equalsIgnoreCase(region)) continue;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                regionName = plugin.getConfig().getString("regions." + region);
                break;
            }
            if (!plugin.getConfig().getBoolean("showBar") || !ActionBar.works) return;
            for (String ignoredWorld : plugin.getConfig().getStringList("disabled_worlds"))
                if (player.getWorld().getName().equalsIgnoreCase(ignoredWorld)) return;
            checkRegion(player, regionName);
        }
    }

    public void checkRegion(final Player player, final String region_name) {
        if (region_name.equalsIgnoreCase("__global__") && plugin.getConfig().getBoolean("showBarIfNoRegion"))
            ActionBar.sendActionBar(player, Main.translateColor(plugin.getConfig().getString("global")));
        else if(!region_name.equalsIgnoreCase("__global__"))
            ActionBar.sendActionBar(player, Main.translateColor(plugin.getConfig().getString("format").replace("%region%", region_name)));
    }
}