package me.unknowncall.supplycrate;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

public class SupplyCrate {

	private String name;
	private ItemStack itemStack;
	private ArrayList<Reward> rewards;
	private ArrayList<Reward> opRewards;

	public SupplyCrate(String name, ItemStack itemStack, ArrayList<Reward> rewards, ArrayList<Reward> opRewards) {
		this.name = name;
		this.itemStack = itemStack;
		this.rewards = rewards;
		this.opRewards = opRewards;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack is) {
		this.itemStack = is;
	}

	public ArrayList<Reward> getRewards() {
		return rewards;
	}

	public void setRewards(ArrayList<Reward> rewards) {
		this.rewards = rewards;
	}

	public ArrayList<Reward> getOpRewards() {
		return opRewards;
	}

	public void setOpRewards(ArrayList<Reward> opRewards) {
		this.opRewards = opRewards;
	}

}
