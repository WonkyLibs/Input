package com.wonkglorg.minecraft.input.event;

import com.wonkglorg.minecraft.input.InputManager;
import com.wonkglorg.minecraft.input.request.RequestType;
import com.wonkglorg.minecraft.input.request.type.ChatInputRequest;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatEventListener implements Listener{
	
	private final InputManager inputManager;
	
	public ChatEventListener(InputManager inputManager) {
		this.inputManager = inputManager;
	}
	
	@EventHandler
	public void onChatMessage(AsyncChatEvent e) {
		if(!inputManager.hasActiveRequests(RequestType.CHAT)){
			return;
		}
		
		Player player = e.getPlayer();
		var request = (ChatInputRequest<?>) inputManager.getRequest(RequestType.CHAT, player.getUniqueId());
		
		if(request == null){
			return;
		}
		
		request.handleEvent(e);
	}
}
