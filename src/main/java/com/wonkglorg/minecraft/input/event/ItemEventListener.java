package com.wonkglorg.minecraft.input.event;

import com.wonkglorg.minecraft.input.InputManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemEventListener implements Listener{
	
	private final InputManager inputManager;
	
	public ItemEventListener(InputManager inputManager) {
		this.inputManager = inputManager;
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		if(!inputManager.hasActiveRequests()){
			return;
		}
		
		var outstandingItemRequest = inputManager.consumeOutstandingItemRequest(e.getPlayer().getUniqueId());
		if(outstandingItemRequest == null){
			return;
		}
		
		e.setCancelled(true);
		
		outstandingItemRequest.complete(e.getItemDrop().getItemStack());
	}
}
