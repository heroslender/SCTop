package com.victorheaven.sctop;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.victorheaven.sctop.command.SCTopCommand;
import com.victorheaven.sctop.loader.LocationLoader;
import com.victorheaven.sctop.manager.LocationManager;
import com.victorheaven.sctop.runnable.SCTopRunnable;

import net.citizensnpcs.api.npc.NPC;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

public class SCTop extends JavaPlugin {
	
	private LocationManager locationManager;
	
	@Override
	public void onEnable() {
		initialize();
	}
	
	@Override
	public void onDisable() {
		for (NPC npc : SCTopRunnable.NPC) {
			npc.destroy();
		}
		for (Hologram hologram : SCTopRunnable.HOLOGRAM) {
			hologram.delete();
		}
	}
	
	private void initialize() {
		saveDefaultConfig();
		
		locationManager = new LocationManager();
		
		new LocationLoader(this, locationManager).loadLocations();
		
		Bukkit.getScheduler().runTaskTimer(this, new SCTopRunnable(this, locationManager, SimpleClans.getInstance()), 20, 20 * 60 * getConfig().getInt("runnable_time"));
		
		command();
	}
	
	private void command() {
		getCommand("sctop").setExecutor(new SCTopCommand(this, locationManager));
	}
	
	public void debug(String message) {
		Bukkit.getConsoleSender().sendMessage("§e[SCTop] " + message);
	}

}
