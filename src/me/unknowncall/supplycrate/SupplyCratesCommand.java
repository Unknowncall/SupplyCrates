package me.unknowncall.supplycrate;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class SupplyCratesCommand implements CommandExecutor {

	private CrateDriver crateDriver;

	public SupplyCratesCommand(CrateDriver crateDriver) {
		this.crateDriver = crateDriver;
	}

	/*
	 * Give player crate amount
	 * Reload
	 * List
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("reload")) {
				if (!sender.hasPermission("crates.reload")) {
					sender.sendMessage(ChatColor.RED + "I can't let you do this.");
					return true;
				}
				
				this.crateDriver.reloadConfig();
				this.crateDriver.saveConfig();
				this.crateDriver.loadDrops();
				return true;
			}
			if (args[0].equalsIgnoreCase("list")) {
				if (!sender.hasPermission("crates.list")) {
					sender.sendMessage(ChatColor.RED + "I can't let you do this.");
					return true;
				}
				sender.sendMessage("The current crates are as follows: " + this.crateDriver.getCrates().keySet());
				return true;
			}
		}
		if (args.length == 4) {
			if (args[0].equalsIgnoreCase("give")) {
				if (!sender.hasPermission("crates.give")) {
					sender.sendMessage(ChatColor.RED + "I can't let you do this.");
					return true;
				}
				if (Bukkit.getPlayer(args[1]) == null) {
					sender.sendMessage(ChatColor.RED + "That player is not online. /sc give [player] [crate] [amount]");
					return true;
				}
				Player player = Bukkit.getPlayer(args[1]);
				if (this.crateDriver.getCrates().get(args[2]) == null) {
					sender.sendMessage(ChatColor.RED + "That crate does not exsit. /sc give [player] [crate] [amount]");
					return true;
				}
				SupplyCrate crate = this.crateDriver.getCrates().get(args[2]);
				try {
					Integer.parseInt(args[3]);
				} catch (NumberFormatException e) {
					sender.sendMessage(ChatColor.RED + "You must specify an integer. /sc give [player] [crate] [amount]");
					return true;
				}
				int amount = Integer.parseInt(args[3]);
				this.crateDriver.giveCrate(crate, player, amount);
				sender.sendMessage(ChatColor.GREEN + "You have given a " + crate.getName() + " to " + player.getName());
				player.sendMessage(ChatColor.GREEN + "You have been given " + amount + "x " +  crate.getName() + "!");
				return true;
			}
		}
		
		sender.sendMessage(ChatColor.RED + "Wrong usage.");
		return true;
	}

}
