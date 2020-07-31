package ru.servbuy.regions;


import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class API
{
    private static WorldGuardPlugin worldGuardPlugin;

    public static String getRegionName(final Player player) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final ProtectedRegion region = getRegionWithMaxpriority(player.getLocation());
        return (region == null) ? "__global__" : region.getId();
    }
    
    private static ProtectedRegion getRegionWithMaxpriority(final Location loc) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final Set<ProtectedRegion> regions;
        ProtectedRegion region;
        if (ActionBar.version.substring(3).compareTo("13") < 0 || ActionBar.version.startsWith("v1_8_")
                || ActionBar.version.startsWith("v1_9_"))
            regions = API_old.getRegionWithMaxpriority(loc);
        else
            regions = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery()
                    .getApplicableRegions(BukkitAdapter.adapt(loc)).getRegions();
        if (regions.size() == 0) {
            return null;
        }
        long highPriority = -1L;
        region = null;
        for (final ProtectedRegion region2 : regions) {
            if (region2.getPriority() > highPriority) {
                highPriority = region2.getPriority();
                region = region2;
            }
        }
        return region;
    }
}
