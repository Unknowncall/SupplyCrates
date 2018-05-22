package me.unknowncall.supplycrate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class CrateDriver extends JavaPlugin {

	private HashMap<String, SupplyCrate> crates;

	public void onEnable() {
		this.saveDefaultConfig();
		loadDrops();
		this.getCommand("supplycrates").setExecutor(new SupplyCratesCommand(this));
		this.getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
		this.getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);
	}

	public void loadDrops() {
		this.crates = new HashMap<String, SupplyCrate>();
		this.getLogger().info("Loading supply drops.");
		if (!(new File(this.getDataFolder() + "/crates").exists())) {
			this.getLogger().info("Supply drops directory not found, creating it now.");
			new File(this.getDataFolder() + "/crates").mkdir();
		}
		File[] files = new File(this.getDataFolder() + "/crates").listFiles();
		for (File file : files) {
			FileConfiguration drop = new YamlConfiguration();
			try {
				drop.load(file);
			} catch (IOException | InvalidConfigurationException e) {
				this.getLogger().info("Error occured when loading supply drops.");
			}
			String name = file.getName().split(".")[0];
			ItemStack is = ItemStackSerializer.deserialize(drop.getString("item_stack"));
			ArrayList<Reward> rewards = new ArrayList<Reward>();
			int totalChanceCounter = 0;
			for (String s : drop.getConfigurationSection("rewards").getKeys(false)) {
				ItemStack rewardIs = ItemStackSerializer.deserialize("rewards." + s + ".display_item");
				List<String> commands = drop.getStringList("rewards." + s + ".commands");
				int chance = drop.getInt("rewards. " + s + ".chance");
				totalChanceCounter = totalChanceCounter + chance;
				rewards.add(new Reward(rewardIs, commands, chance));
			}
			ArrayList<Reward> oprewards = new ArrayList<Reward>();
			int totalOPChanceCounter = 0;
			for (String s : drop.getConfigurationSection("op_rewards").getKeys(false)) {
				ItemStack rewardIs = ItemStackSerializer.deserialize("op_rewards." + s + ".display_item");
				List<String> commands = drop.getStringList("op_rewards." + s + ".commands");
				int chance = drop.getInt("op_rewards. " + s + ".chance");
				totalOPChanceCounter = totalOPChanceCounter + chance;
				oprewards.add(new Reward(rewardIs, commands, chance));
			}
			this.crates.put(name,
					new SupplyCrate(name, is, rewards, oprewards, totalChanceCounter, totalOPChanceCounter));
		}
		this.getLogger().info("Loaded supply drops!");
	}

	public boolean giveCrate(String crateName, Player player, int amount) {
		if (crates.get(crateName) != null) {
			SupplyCrate crate = crates.get(crateName);
			ItemStack copy = crate.getItemStack();
			copy.setAmount(amount);
			player.getInventory().addItem(copy);
			return true;
		} else {
			return false;
		}
	}

	public void giveCrate(SupplyCrate crate, Player player, int amount) {
		ItemStack copy = crate.getItemStack();
		copy.setAmount(amount);
		player.getInventory().addItem(copy);
	}

	public void openCrate(SupplyCrate crate, Player player) {
		List<Integer> rewardSlots = new ArrayList<Integer>();
		List<Integer> opRewardSlots = new ArrayList<Integer>();
		for (String s : this.getConfig().getConfigurationSection("gui.reward_slots").getKeys(false)) {
			if (s.contains("*")) {
				opRewardSlots.add(Integer.parseInt(s.replace("*", "")));
			} else {
				rewardSlots.add(Integer.parseInt(s));
			}
		}
		String inventoryTitle = this.getConfig().getString("gui.title");
		int size = this.getConfig().getInt("gui.size");
		Inventory inv = Bukkit.createInventory(player, size, inventoryTitle);
		int counter = 1;
		for (int i : rewardSlots) {
			ItemStack rewardItemStack = ItemStackSerializer
					.deserialize(this.getConfig().getString("gui.default_reward_item"));
			ItemMeta im = rewardItemStack.getItemMeta();
			String displayName = im.getDisplayName();
			displayName = displayName.replace("%number%", "" + counter);
			counter++;
			im.setDisplayName(displayName);
			rewardItemStack.setItemMeta(im);
			inv.setItem(i, rewardItemStack);
		}
		counter = 1;
		for (int i : opRewardSlots) {
			ItemStack rewardItemStack = ItemStackSerializer
					.deserialize(this.getConfig().getString("gui.op_reward_item"));
			ItemMeta im = rewardItemStack.getItemMeta();
			String displayName = im.getDisplayName();
			displayName = displayName.replace("%number%", "" + counter);
			counter++;
			im.setDisplayName(displayName);
			rewardItemStack.setItemMeta(im);
			inv.setItem(i, rewardItemStack);
		}
	}

	public HashMap<String, SupplyCrate> getCrates() {
		return crates;
	}

	public void setCrates(HashMap<String, SupplyCrate> crates) {
		this.crates = crates;
	}

	public void onDisable() {
	}

}
