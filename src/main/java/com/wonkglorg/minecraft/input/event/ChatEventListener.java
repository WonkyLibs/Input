package com.wonkglorg.minecraft.input.event;

import com.wonkglorg.minecraft.input.InputManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatEventListener implements Listener{
	
	private final InputManager inputManager;
	
	public ChatEventListener(InputManager inputManager) {
		this.inputManager = inputManager;
	}
	
	@EventHandler
	public void onChatMessage(AsyncChatEvent e) {
		if(!inputManager.hasOutstandingChatRequests()){
			return;
		}
		
		var request = inputManager.getOutstandingChatRequest(e.getPlayer().getUniqueId());
		
		if(request == null){
			return;
		}
		
		e.setCancelled(true);
		
		Component message = e.message();
		try{
			request.parse(message);
		} catch(Exception ex){
			request.incrementFailedAttempts();
			request.validate();
		}
	}
}
