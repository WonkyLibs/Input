package com.wonkglorg.minecraft.input.request.type;

import com.wonkglorg.minecraft.input.InputManager;
import com.wonkglorg.minecraft.input.request.InputRequest;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class DropItemInputRequest extends InputRequest<ItemStack, DropItemInputRequest>{
	public DropItemInputRequest(UUID playerUuid) {
		super(playerUuid);
	}
	
	public DropItemInputRequest test() {
	
	}
	
	@Override
	protected void registerRequest() {
		InputManager.getInstance(plugin).registerItemInputRequest(this);
	}
	
	@Override
	protected void unRegisterRequest() {
		InputManager.getInstance(plugin).consumeOutstandingItemRequest(playerUuid);
	}
}
