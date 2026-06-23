package com.wonkglorg.minecraft.input.event;

import com.wonkglorg.minecraft.input.InputManager;
import com.wonkglorg.minecraft.input.request.RequestType;
import com.wonkglorg.minecraft.input.request.type.EntitySelectInputRequest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityEventListener implements Listener{
	
	private final InputManager inputManager;
	
	public EntityEventListener(InputManager inputManager) {
		this.inputManager = inputManager;
	}
	
	@EventHandler
	public void onItemDrop(EntityDamageByEntityEvent e) {
		if(!(e.getDamager() instanceof Player player)){
			return;
		}
		
		if(!inputManager.hasActiveRequests(RequestType.CLICK_ENTITY)){
			return;
		}
		
		var request = (EntitySelectInputRequest) inputManager.getRequest(RequestType.CLICK_ENTITY, player.getUniqueId());
		if(request == null){
			return;
		}
		
		request.handleEvent(e);
	}
}
