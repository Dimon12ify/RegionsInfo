package ru.servbuy.regions;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class API_old {
    public static Set<ProtectedRegion> getRegionWithMaxpriority(final Location loc) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Class<?> c1 = com.sk89q.worldguard.protection.managers.RegionManager.class;
        final Method getApplicableRegions = c1.getDeclaredMethod("getApplicableRegions", Location.class);
        final Class<?> c2 = com.sk89q.worldguard.bukkit.WGBukkit.class;
        final Method getRegionManager = c2.getDeclaredMethod("getRegionManager", World.class);
        final Class<?> c3 = com.sk89q.worldguard.protection.ApplicableRegionSet.class;
        final Method getRegions = c3.getDeclaredMethod("getRegions");
        final Object obj2 = getApplicableRegions.invoke(getRegionManager.invoke(c2, loc.getWorld()), loc);
        Set<ProtectedRegion> regions = (Set<ProtectedRegion>) getRegions.invoke(obj2);
        //WGBukkit.getRegionManager(world).getApplicableRegions(loc).getRegions() <- code above make this;
        return regions;
    }
}
