package com.wonkglorg.minecraft.input.request.type;

import com.wonkglorg.minecraft.input.InputRequest;
import com.wonkglorg.minecraft.input.request.RequestType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.UUID;

public class DropItemInputRequest extends InputRequest<Item, PlayerDropItemEvent, DropItemInputRequest>{
	public DropItemInputRequest(UUID playerUuid) {
		super(playerUuid, RequestType.DROP_ITEM);
	}
	
	@Override
	public void handleEvent(PlayerDropItemEvent e) {
		var item = e.getItemDrop();
		Player player = e.getPlayer();
		handleInput(e, player, item);
	}
}
