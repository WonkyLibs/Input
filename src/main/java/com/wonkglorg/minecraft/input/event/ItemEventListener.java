package com.wonkglorg.minecraft.input.event;

import com.wonkglorg.minecraft.input.InputManager;
import com.wonkglorg.minecraft.input.request.RequestType;
import com.wonkglorg.minecraft.input.request.type.DropItemInputRequest;
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
		if(!inputManager.hasActiveRequests(RequestType.DROP_ITEM)){
			return;
		}
		
		var request = (DropItemInputRequest) inputManager.getRequest(RequestType.DROP_ITEM, e.getPlayer().getUniqueId());
		if(request == null){
			return;
		}
		
		request.handleEvent(e);
	}
}
