package com.victorheaven.sctop.loader;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import com.victorheaven.sctop.SCTop;
import com.victorheaven.sctop.manager.LocationManager;
import com.victorheaven.sctop.util.LocationUtil;

public class LocationLoader {
	
	private SCTop sctop;
	private LocationManager locationManager;
	
	public LocationLoader(SCTop sctop, LocationManager locationManager) {
		this.sctop = sctop;
		this.locationManager = locationManager;
	}
	
	public void loadLocations() {
		FileConfiguration config = sctop.getConfig();
		if (!config.contains("locations") || config.getStringList("locations").isEmpty()) {
			sctop.debug("Ué... não encontramos nenhum localidade salva para carregar =(");
			return;
		}
		
		for (String line : config.getStringList("locations")) {
			int pos = Integer.valueOf(line.split(" ")[0]);
			Location location = LocationUtil.byStringNoBlock(line.split(" ")[1].split(","));
			
			locationManager.getLocationMap().put(pos, location);
		}
		sctop.debug("Eba! Carregamos " + locationManager.getLocationMap().size() + " posições! O.o");
	}

}
