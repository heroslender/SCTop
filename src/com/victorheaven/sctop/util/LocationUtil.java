package com.victorheaven.sctop.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtil {

    public static Location byStringNoBlock(String... s) {
        return new Location(Bukkit.getWorld(s[0]), Double.valueOf(s[1]), Double.valueOf(s[2]), Double.valueOf(s[3]), Float.valueOf(s[4]), Float.valueOf(s[5]));
    }

    public static String byLocationNoBlock(Location l) {
        return l.getWorld().getName() + "," + l.getX() + "," + l.getY() + "," + l.getZ() + "," + l.getYaw() + "," + l.getPitch();
    }

    public static Location byStringBlock(String... s) {
        return new Location(Bukkit.getWorld(s[0]), Double.valueOf(s[1]), Double.valueOf(s[2]), Double.valueOf(s[3]));
    }

    public static String byLocationBlock(Location l) {
        return l.getWorld().getName() + "," + l.getX() + "," + l.getY() + "," + l.getZ();
    }

}
