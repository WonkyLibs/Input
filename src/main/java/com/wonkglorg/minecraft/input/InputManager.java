package com.wonkglorg.minecraft.input;

import com.wonkglorg.minecraft.input.event.ChatEventListener;
import com.wonkglorg.minecraft.input.event.ItemEventListener;
import com.wonkglorg.minecraft.input.request.InputRequest;
import com.wonkglorg.minecraft.input.request.RequestType;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InputManager{
	private static InputManager manager;
	
	private static final EnumMap<RequestType, Map<UUID, InputRequest<?, ?>>> activeInputRequests = new EnumMap<>(RequestType.class);
	
	static {
		for(var request : RequestType.values()){
			activeInputRequests.put(request, new HashMap<>());
		}
	}
	
	public InputManager(Plugin plugin) {
		Bukkit.getServer().getPluginManager().registerEvents(new ChatEventListener(this), plugin);
		Bukkit.getServer().getPluginManager().registerEvents(new ItemEventListener(this), plugin);
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
	
	public void registerInputRequest(InputRequest<?, ?> request) {
		activeInputRequests.get(request.getRequestType()).put(request.getPlayerUuid(), request);
	}
	
	public void unRegisterInputRequest(InputRequest<?, ?> request) {
		activeInputRequests.get(request.getRequestType()).remove(request.getPlayerUuid());
	}
	
	public boolean hasActiveRequests(RequestType type) {
		return !activeInputRequests.get(type).isEmpty();
	}
	
}
