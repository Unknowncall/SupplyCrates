package me.unknowncall.supplycrate;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Reward {
	
	private ItemStack displayItem;
	private List<String> commands;
	private int chance;

	public Reward(ItemStack displayItem, List<String> commands, int chance) {
		this.displayItem = displayItem;
		this.commands = commands;
		this.chance = chance;
	}

	public ItemStack getDisplayItem() {
		return displayItem;
	}

	public void setDisplayItem(ItemStack displayItem) {
		this.displayItem = displayItem;
	}

	public List<String> getCommands() {
		return commands;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}

	public int getChance() {
		return chance;
	}

	public void setChance(int chance) {
		this.chance = chance;
	}
}
