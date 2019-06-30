package com.victorheaven.sctop.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.victorheaven.sctop.SCTop;
import com.victorheaven.sctop.manager.LocationManager;
import com.victorheaven.sctop.util.LocationUtil;

public class SCTopCommand implements CommandExecutor {

	private SCTop sctop;
	private LocationManager locationManager;

	public SCTopCommand(SCTop sctop, LocationManager locationManager) {
		this.sctop = sctop;
		this.locationManager = locationManager;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if (commandSender instanceof ConsoleCommandSender) {
			commandSender.sendMessage("�cEste comando � restrito para jogadores.");
			return false;
		}

		if (!commandSender.hasPermission("sctop.command.use")) {
			commandSender.sendMessage("�cVoc� n�o possui permiss�o para executar este comando! =(");
			return false;
		}

		Player player = (Player) commandSender;
		if (args.length == 0) {
			player.sendMessage("�cUtilize \"/sctop add/del <posi��o (n�mero)>\" para definir/deletar uma posi��o.");
			return false;
		}

		if (args[0].equalsIgnoreCase("add")) {
			if (args.length != 2) {
				player.sendMessage("�cUtilize \"/sctop add <posi��o (n�mero)>.");
				return false;
			}
			
			int position = 0;
			try {
				position = Integer.valueOf(args[1]);
			} catch (Exception e) {
				player.sendMessage("�cInsira um n�mero para a posi��o v�lido.");
				return false;
			}

			if (position <= 0) {
				player.sendMessage("�cInsira uma posi��o acima de 0.");
				return false;
			}

			if (locationManager.getLocationMap().containsKey(position)) {
				player.sendMessage("�cEsta posi��o j� est� definida.");
				return false;
			}
			
			locationManager.getLocationMap().put(position, player.getLocation());
			
			List<String> locList = sctop.getConfig().contains("locations") ? sctop.getConfig().getStringList("locations") : Lists.newArrayList();
			locList.add(position + " " + LocationUtil.byLocationNoBlock(player.getLocation()));
			
			sctop.getConfig().set("locations", locList);
			sctop.saveConfig();

			player.sendMessage("�aPosi��o #" + position + " definida.");
			return false;
		}

		if (args[0].equalsIgnoreCase("del")) {
			if (args.length != 2) {
				player.sendMessage("�cUtilize \"/sctop del <posi��o (n�mero)>.");
				return false;
			}
			
			int position = 0;
			try {
				position = Integer.valueOf(args[1]);
			} catch (Exception e) {
				player.sendMessage("�cInsira um n�mero para a posi��o v�lido.");
				return false;
			}

			if (position <= 0) {
				player.sendMessage("�cInsira uma posi��o acima de 0.");
				return false;
			}

			if (!locationManager.getLocationMap().containsKey(position)) {
				player.sendMessage("�cEsta posi��o n�o est� definida.");
				return false;
			}

			List<String> locList = sctop.getConfig().contains("locations") ? sctop.getConfig().getStringList("locations") : Lists.newArrayList();
			locList.remove(position + " " + LocationUtil.byLocationNoBlock(locationManager.getLocation(position)));
			
			sctop.getConfig().set("locations", locList);
			sctop.saveConfig();
			
			locationManager.getLocationMap().remove(position);

			player.sendMessage("�aPosi��o #" + position + " deletada.");
			return false;
		}
		return false;
	}

}
