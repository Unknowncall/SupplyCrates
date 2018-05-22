package me.unknowncall.supplycrate;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

	private CrateDriver crateDriver;

	public PlayerInteractListener(CrateDriver crateDriver) {
		this.crateDriver = crateDriver;
	}
	
	@EventHandler
	public void playerInteractHandler(PlayerInteractEvent e) {
		
	}

}
