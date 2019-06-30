package com.victorheaven.sctop.runnable;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.collect.Lists;
import com.victorheaven.sctop.SCTop;
import com.victorheaven.sctop.manager.LocationManager;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

public class SCTopRunnable implements Runnable {

	private SCTop sctop;
	private LocationManager locationManager;
	private SimpleClans simpleClans;

    public static List<NPC> NPC;
    public static List<Hologram> HOLOGRAM;

	public SCTopRunnable(SCTop sctop, LocationManager locationManager, SimpleClans simpleClans) {
		this.sctop = sctop;
		this.locationManager = locationManager;
		this.simpleClans = simpleClans;
	}

	@Override
	public void run() {
		Set<Clan> clans = simpleClans.getClanManager().getClans().stream()
				.sorted(Comparator.comparing(Clan::getTotalKDR)).collect(Collectors.toSet());

		if (clans.size() <= 0) {
			sctop.debug(
					"Infelizmente n�o atualizados o top cl�s pelo fato de n�o ter nenhum cl� registrado, sinto muito.");
			return;
		}
		
		for (NPC npc : NPC) {
			npc.destroy();
		}
		for (Hologram hologram : HOLOGRAM) {
			hologram.delete();
		}

		NPCRegistry npcRegistry = CitizensAPI.getNPCRegistry();

		int position = 1;
		for (Clan clan : clans) {
			if (!locationManager.getLocationMap().containsKey(position))
				break;

			Location location = locationManager.getLocation(position);
			ClanPlayer clanLeader= clan.getLeaders().get(0);

			if (sctop.getConfig().contains("hologram.lines")) {
				Location holoLocation = location.clone().add(0, sctop.getConfig().getInt("hologram.height"), 0);

				Hologram hologram = HologramsAPI.createHologram(sctop, holoLocation);
				for (String line : sctop.getConfig().getStringList("hologram.lines")) {
					line = line.replace("&", "�");
					line = line.replace("{pos}", String.valueOf(position));
					line = line.replace("{clanTag}", clan.getTag());
					line = line.replace("{clanName}", clan.getName());
					line = line.replace("{clanLeader}", clanLeader.getName());
					line = line.replace("{clanKdr}", String.valueOf(clan.getTotalKDR()));
					
					hologram.appendTextLine(line);
				}
	            HOLOGRAM.add(hologram);
			}
		
			NPC npc = npcRegistry.createNPC(EntityType.PLAYER, "");
            npc.data().set("player-skin-name", clanLeader.getName());
            npc.setProtected(true);
            npc.spawn(location);
            
            NPC.add(npc);
		}
	}
	
	static {
    	NPC = Lists.newArrayList();
    	HOLOGRAM = Lists.newArrayList();
    }

}
