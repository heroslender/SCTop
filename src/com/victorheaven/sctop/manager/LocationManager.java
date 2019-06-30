package com.victorheaven.sctop.manager;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

public class LocationManager {
	
	private Map<Integer, Location> locationMap;
	
	public LocationManager() {
		locationMap = new HashMap<>();
	}
	
	public Map<Integer, Location> getLocationMap() {
		return locationMap;
	}
	
	public Location getLocation(int position) {
		return locationMap.get(position);
	}

}
