package me.unknowncall.supplycrate;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

	private CrateDriver crateDriver;

	public InventoryClickListener(CrateDriver crateDriver) {
		this.crateDriver = crateDriver;
	}
	
	@EventHandler
	public void inventoryClickHandler(InventoryClickEvent e) {
		if (e.getClickedInventory() != null) {
			if (e.getAction() != null) {
				
			}
			
		}
	}

}
