package me.unknowncall.supplycrate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class CrateDriver extends JavaPlugin {

	private ArrayList<SupplyCrate> drops;

	public void onEnable() {
		this.saveDefaultConfig();
		loadDrops();
		this.getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
	}

	private void loadDrops() {
		this.drops = new ArrayList<SupplyCrate>();
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
			for (String s : drop.getConfigurationSection("rewards").getKeys(false)) {
				ItemStack rewardIs = ItemStackSerializer.deserialize("rewards." + s + ".display_item");
				List<String> commands = drop.getStringList("rewards." + s + ".commands");
				int chance = drop.getInt("rewards. " + s + ".chance");
				rewards.add(new Reward(rewardIs, commands, chance));
			}
			ArrayList<Reward> oprewards = new ArrayList<Reward>();
			for (String s : drop.getConfigurationSection("op_rewards").getKeys(false)) {
				ItemStack rewardIs = ItemStackSerializer.deserialize("op_rewards." + s + ".display_item");
				List<String> commands = drop.getStringList("op_rewards." + s + ".commands");
				int chance = drop.getInt("op_rewards. " + s + ".chance");
				oprewards.add(new Reward(rewardIs, commands, chance));
			}
			this.drops.add(new SupplyCrate(name, is, rewards, oprewards));
		}
		this.getLogger().info("Loaded supply drops!");
	}
	
	public void giveCrate(String crateName, Player player, int amount) {
		for (SupplyCrate crate : drops) {
			if (crate.getName().equalsIgnoreCase(crateName)) {
				ItemStack copy = crate.getItemStack();
				copy.setAmount(amount);
				player.getInventory().addItem(copy);
			}
		}
	}
	
	public void openCrate(SupplyCrate crate, Player player) {
		
	}

	public void onDisable() {
	}

}
