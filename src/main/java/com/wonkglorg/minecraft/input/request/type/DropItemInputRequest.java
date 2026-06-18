package com.wonkglorg.minecraft.input.request.type;

import com.wonkglorg.minecraft.input.request.InputRequest;
import com.wonkglorg.minecraft.input.request.RequestType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class DropItemInputRequest extends InputRequest<ItemStack, PlayerDropItemEvent, DropItemInputRequest>{
	public DropItemInputRequest(UUID playerUuid) {
		super(playerUuid, RequestType.DROP_ITEM);
	}
	
	@Override
	public void handleEvent(PlayerDropItemEvent event) {
	
	}
}
