package com.wonkglorg.minecraft.input;

import com.wonkglorg.minecraft.input.event.ChatEventListener;
import com.wonkglorg.minecraft.input.event.EntityEventListener;
import com.wonkglorg.minecraft.input.event.ItemEventListener;
import com.wonkglorg.minecraft.input.request.RequestType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record InputManager(Plugin plugin){
	private static InputManager manager;
	private static final EnumMap<RequestType, Map<UUID, InputRequest<?, ?, ?>>> activeInputRequests = new EnumMap<>(RequestType.class);
	
	static {
		for(var request : RequestType.values()){
			activeInputRequests.put(request, new HashMap<>());
		}
	}
	
	public InputManager {
		Bukkit.getServer().getPluginManager().registerEvents(new ChatEventListener(this), plugin);
		Bukkit.getServer().getPluginManager().registerEvents(new ItemEventListener(this), plugin);
		Bukkit.getServer().getPluginManager().registerEvents(new EntityEventListener(this), plugin);
	}
	
	public static void createInstance(Plugin plugin) {
		if(manager == null){
			manager = new InputManager(plugin);
		}
	}
	
	public static InputManager getInstance() {
		if(manager == null){
			throw new IllegalStateException("Manager has not been registered! Use: InputManager#createInstance before using any input feature");
		}
		return manager;
	}
	
	public void registerInputRequest(InputRequest<?, ?, ?> request) {
		Map<UUID, InputRequest<?, ?, ?>> requests = activeInputRequests.get(request.getRequestType());
		
		if(requests.containsKey(request.getPlayerUuid())){
			throw new IllegalStateException("Player already has an active request of type " + request.getRequestType());
		}
		
		activeInputRequests.get(request.getRequestType()).put(request.getPlayerUuid(), request);
	}
	
	/**
	 * Unregisters an input request
	 *
	 * @param request
	 */
	public void unRegisterInputRequest(InputRequest<?, ?, ?> request) {
		request.cancelTimeoutTask();
		activeInputRequests.get(request.getRequestType()).remove(request.getPlayerUuid());
	}
	
	/**
	 * Unregisters an input request forcefully, should only be used if not done via internal InputRequests
	 */
	public void forceUnRegisterInputRequest(InputRequest<?, ?, ?> request) {
		request.submitForceCancel();
		request.cancelTimeoutTask();
		activeInputRequests.get(request.getRequestType()).remove(request.getPlayerUuid());
	}
	
	public void unRegisterInputRequestOnPlayerDisconnect(Player player) {
		for(var type : activeInputRequests.values()){
			var value = type.get(player.getUniqueId());
			if(value == null || value.isPersistOnDisconnect()){
				continue;
			}
			
			value.submitDisconnect(player);
		}
	}
	
	public boolean hasActiveRequests(RequestType type) {
		return !activeInputRequests.get(type).isEmpty();
	}
	
	public InputRequest<?, ?, ?> getRequest(RequestType type, UUID user) {
		return activeInputRequests.get(type).get(user);
	}
	
}
